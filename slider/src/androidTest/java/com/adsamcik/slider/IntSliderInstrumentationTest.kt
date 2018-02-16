package com.adsamcik.slider

import android.os.Build
import android.os.Looper
import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.widget.SeekBar
import android.widget.TextView
import com.adsamcik.slider.scaleFunctions.LinearScale
import com.adsamcik.slider.sliders.IntSlider
import junit.framework.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.security.InvalidParameterException
import java.util.concurrent.atomic.AtomicInteger

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class IntSliderInstrumentationTest {
    private val appContext = InstrumentationRegistry.getTargetContext()

    private val atomicInteger = AtomicInteger(0)

    @Test
    @Throws(Exception::class)
    fun baseTest() {
        val slider = IntSlider(appContext)
        slider.minValue = 1
        slider.maxValue = 9
        slider.step = 2
        slider.value = 5
        slider.setTextView(null, null)

        assertEquals(1, slider.minValue.toLong())
        assertEquals(9, slider.maxValue.toLong())
        assertEquals(8, slider.max.toLong())
        assertEquals(4, slider.progress.toLong())
        assertEquals(5, slider.value.toLong())

        val scale = LinearScale.integerScale
        slider.scale = scale
        assertEquals(scale, slider.scale)
    }

    @Test
    @Throws(Exception::class)
    fun textViewTest() {
        val slider = IntSlider(appContext)
        val textView = TextView(appContext)

        slider.setTextView(textView) { value -> "Test " + value }
        slider.minValue = 0
        slider.maxValue = 10
        slider.value = 5

        assertEquals("Test 5", textView.text)

        slider.setTextView(null) { value -> "Test " + value }
        slider.value = 4
        assertEquals("Test 5", textView.text)
        assertEquals(4, slider.value.toLong())

        slider.setTextView(textView) { value -> "Test " + value }
        slider.setTextView(textView, null)
        slider.value = 6
        assertEquals("Test 4", textView.text)
        assertEquals(6, slider.value.toLong())

        slider.setTextView(null, null)
        slider.value = 8
        assertEquals("Test 4", textView.text)
        assertEquals(8, slider.value.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun progressTest() {
        if (Looper.myLooper() == null)
            Looper.prepare()

        val slider = IntSlider(appContext)
        slider.minValue = 0
        slider.maxValue = 10
        slider.value = 5

        assertEquals(5, slider.progress.toLong())

        if (Build.VERSION.SDK_INT >= 24)
            slider.setValue(8, false)
        else
            slider.value = 8

        assertEquals(8, slider.progress.toLong())

        if (Build.VERSION.SDK_INT >= 24)
            slider.setValue(3, true)
        else
            slider.value = 3

        assertEquals(3, slider.progress.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun moveTest() {
        val slider = IntSlider(appContext)
        slider.minValue = 0
        slider.maxValue = 12
        slider.step = 3
        for (i in 0..11) {
            slider.value = i
            assertEquals(Math.round(i / 3.0) * 3, slider.value.toLong())
        }
    }

    @Test
    @Throws(Exception::class)
    fun negativeNumberTest() {
        val slider = IntSlider(appContext)
        slider.minValue = -15
        slider.maxValue = 15
        slider.step = 3
        for (i in -15..15) {
            slider.value = i
            assertEquals(Math.round(i / 3.0) * 3, slider.value.toLong())
        }
    }

    @Test
    @Throws(Exception::class)
    fun stepTest() {
        val slider = IntSlider(appContext)
        slider.minValue = 0
        slider.maxValue = 10

        slider.value = 5

        Assert.assertEquals(5, slider.value)

        slider.step = 2

        val value = slider.value
        Assert.assertTrue(value == 4 || value == 6)

        slider.step = 3
        slider.value = 5
        Assert.assertEquals(6, slider.value)
    }

    @Test
    @Throws(Exception::class)
    fun sharedPreferences() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
        val prefName = "TESTING PREFERENCE"

        preferences.edit().remove(prefName).apply()

        val slider = IntSlider(appContext)
        slider.minValue = -5
        slider.maxValue = 5
        slider.step = 2

        slider.setPreferences(preferences, prefName, 1)

        Assert.assertEquals(1, slider.value.toLong())

        slider.value = 4

        val value = slider.value
        Assert.assertEquals(value, preferences.getInt(prefName, Integer.MIN_VALUE))

        slider.removePreferences()

        slider.value = 1

        Assert.assertEquals(value, preferences.getInt(prefName, Integer.MIN_VALUE))
        Assert.assertEquals(1, slider.value)

        //cleanup
        preferences.edit().remove(prefName).apply()
    }

    @Test
    @Throws(Exception::class)
    fun callbackTests() {
        val slider = IntSlider(appContext)

        val valueChangeListener: OnValueChange<Int> = { _, _ -> atomicInteger.incrementAndGet() }

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
        slider.value = 5
        assertEquals(2, atomicInteger.get().toLong())

        slider.setOnValueChangeListener(null)
        slider.setOnSeekBarChangeListener(null)
        slider.value = 3
        assertEquals(2, atomicInteger.get().toLong())
    }

    @Test
    fun exceptionTest() {
        val slider = IntSlider(appContext)
        EAssert.assertException({ slider.sliderStep = -5 }, RuntimeException::class.java)

        EAssert.assertException({ slider.minValue = slider.maxValue }, InvalidParameterException::class.java)
        EAssert.assertException({ slider.maxValue = slider.minValue }, InvalidParameterException::class.java)
    }
}
