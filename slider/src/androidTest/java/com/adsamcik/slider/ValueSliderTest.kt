package com.adsamcik.slider


import android.os.Build
import android.os.Looper
import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry
import android.widget.SeekBar
import android.widget.TextView
import com.adsamcik.slider.sliders.ObjectValueSlider
import junit.framework.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class ValueSliderTest {
    private val appContext = InstrumentationRegistry.getTargetContext()

    private val strings = arrayOf("a", "b", "c", "d", "e", "f")

    private val atomicInteger = AtomicInteger(0)

    @Test
    @Throws(Exception::class)
    fun baseTest() {
        val slider = ObjectValueSlider<String>(appContext)

        slider.setItems(strings)
        slider.value = "b"
        slider.removeTextView()

        assertEquals("b", slider.value)
        assertEquals((strings.size - 1).toLong(), slider.max.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun textViewTest() {
        val slider = ObjectValueSlider<String>(appContext)
        val textView = TextView(appContext)

        slider.setItems(strings)
        slider.setTextView(textView) { value -> "Test " + value }
        slider.value = "d"

        assertEquals("Test d", textView.text)

        slider.removeTextView()
        slider.value = "a"
        assertEquals("Test d", textView.text)
        assertEquals("a", slider.value)
    }

    @Test
    @Throws(Exception::class)
    fun progressTest() {
        if (Looper.myLooper() == null)
            Looper.prepare()

        val slider = ObjectValueSlider<String>(appContext)
        slider.setItems(strings)
        slider.value = "a"

        assertEquals("a", slider.value)
        assertEquals(0, slider.progress.toLong())

        if (Build.VERSION.SDK_INT >= 24)
            slider.setValue("f", false)
        else
            slider.value = "f"

        assertEquals(5, slider.progress.toLong())
        assertEquals("f", slider.value)

        if (Build.VERSION.SDK_INT >= 24)
            slider.setValue("b", true)
        else
            slider.value = "b"

        assertEquals(1, slider.progress.toLong())
        assertEquals("b", slider.value)
    }

    @Test
    @Throws(Exception::class)
    fun valueTest() {
        if (Looper.myLooper() == null)
            Looper.prepare()

        val slider = ObjectValueSlider<String>(appContext)

        slider.setItems(strings)
        slider.value = "a"

        slider.clearItems()

        EAssert.assertException({ slider.value }, RuntimeException::class.java)
    }


    @Test
    @Throws(Exception::class)
    fun invalidCodeInputTest() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val slider = ObjectValueSlider<String>(appContext)

        EAssert.assertException({ slider.setProgress(15) }, RuntimeException::class.java)

        slider.setItems(strings)

        EAssert.assertException({ slider.setProgress(15) }, RuntimeException::class.java)
        EAssert.assertException({ slider.setProgress(-1) }, RuntimeException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun exceptionTest() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val slider = ObjectValueSlider<String>(appContext)

        EAssert.assertException({ slider.value }, RuntimeException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun sharedPreferences() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
        val prefName = "TESTING PREFERENCE"
        val noPreference = "NO PREFERENCE"

        preferences.edit().clear().apply()

        val slider = ObjectValueSlider<String>(appContext)
        slider.setItems(strings)

        slider.setPreferences(preferences, prefName, "d") { value -> value }

        Assert.assertEquals("d", slider.value)

        slider.value = "c"

        val value = slider.value
        Assert.assertEquals(value, preferences.getString(prefName, noPreference))

        slider.removePreferences()

        slider.value = "b"

        Assert.assertEquals(value, preferences.getString(prefName, noPreference))
        Assert.assertEquals("b", slider.value)

        //cleanup
        preferences.edit().remove(prefName).apply()
    }

    @Test
    @Throws(Exception::class)
    fun callbackTests() {
        val slider = ObjectValueSlider<String>(appContext)
        slider.setItems(strings)

        val valueChangeListener: OnValueChange<String> = { _, _ -> atomicInteger.incrementAndGet() }

        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                atomicInteger.incrementAndGet()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        slider.setOnValueChangeListener(valueChangeListener)
        slider.value = "d"
        assertEquals(2, atomicInteger.get().toLong())

        slider.setOnValueChangeListener(null)
        slider.setOnSeekBarChangeListener(null)
        slider.value = "c"
        assertEquals(2, atomicInteger.get().toLong())
    }
}
