package com.adsamcik.slider


import android.content.Context
import android.os.Build
import android.os.Looper
import android.preference.PreferenceManager
import android.widget.SeekBar
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.adsamcik.slider.implementations.ObjectValueSlider
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class ValueSliderTest {
	private val appContext = ApplicationProvider.getApplicationContext<Context>()

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
		slider.setTextView(textView) { value -> "Test $value" }
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

		slider.removeItems()

		AssertUtility.assertException({ slider.value }, NullPointerException::class.java)
	}


	@Test
	@Throws(Exception::class)
	fun invalidCodeInputTest() {
		val appContext = ApplicationProvider.getApplicationContext<Context>()

		val slider = ObjectValueSlider<String>(appContext)

		AssertUtility.assertException({ slider.setProgress(15) }, IllegalArgumentException::class.java)

		slider.setItems(strings)

		AssertUtility.assertException({ slider.setProgress(15) }, IllegalArgumentException::class.java)
		AssertUtility.assertException({ slider.setProgress(-1) }, IllegalArgumentException::class.java)
	}

	@Test
	@Throws(Exception::class)
	fun exceptionTest() {
		val appContext = ApplicationProvider.getApplicationContext<Context>()

		val slider = ObjectValueSlider<String>(appContext)

		AssertUtility.assertException({ slider.value }, NullPointerException::class.java)
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

		assertEquals("d", slider.value)

		slider.value = "c"

		val value = slider.value
		assertEquals(value, preferences.getString(prefName, noPreference))

		slider.removePreferences()

		slider.value = "b"

		assertEquals(value, preferences.getString(prefName, noPreference))
		assertEquals("b", slider.value)

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
