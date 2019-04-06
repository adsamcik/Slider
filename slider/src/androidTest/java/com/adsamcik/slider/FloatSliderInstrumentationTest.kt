package com.adsamcik.slider

import android.content.Context
import android.preference.PreferenceManager
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.adsamcik.slider.implementations.FloatSlider
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FloatSliderInstrumentationTest {
	private val DELTA = 0.00001f

	private val appContext = ApplicationProvider.getApplicationContext<Context>()!!

	@Test
	@Throws(Exception::class)
	fun baseTest() {
		val MAX = 9f
		val MIN = 1f
		val PLACES = 1
		val POW = (100 * Math.pow(10.0, PLACES.toDouble())).toInt()
		val MAX_SLIDER = (MAX - MIN) * POW

		val slider = FloatSlider(appContext)
		slider.minValue = MIN
		slider.maxValue = MAX
		slider.step = 2f
		slider.value = 5f
		assertEquals(MIN, slider.minValue, DELTA)
		assertEquals(MAX, slider.maxValue, DELTA)
		assertEquals(MAX_SLIDER, slider.max.toFloat(), DELTA)
		assertEquals((4 * POW).toFloat(), slider.progress.toFloat(), DELTA)
		assertEquals(5f, slider.value, DELTA)
	}

	@Test
	@Throws(Exception::class)
	fun moveTest() {
		val slider = FloatSlider(appContext)
		slider.minValue = 0f
		slider.maxValue = 12f
		slider.step = 3f
		for (i in 0..11) {
			slider.value = i.toFloat()
			assertEquals((Math.round(i / 3.0) * 3).toFloat(), slider.value, DELTA)
		}
	}

	@Test
	@Throws(Exception::class)
	fun negativeNumberTest() {
		val slider = FloatSlider(appContext)
		slider.minValue = -15f
		slider.maxValue = 15f
		slider.step = 3f
		for (i in -15..15) {
			slider.value = i.toFloat()
			assertEquals(Math.round(i / 3f) * 3f, slider.value, DELTA)
		}
	}

	@Test
	@Throws(Exception::class)
	fun floatNumberTest() {
		val slider = FloatSlider(appContext)
		slider.minValue = -1.5f
		slider.maxValue = 1.5f
		slider.step = 0.3f
		var i = -1.5f
		while (i <= 1.5f) {
			slider.value = i
			assertEquals(Math.round(i / 0.3f) * 0.3f, slider.value, DELTA)
			i += 0.3f
		}
	}

	@Test
	@Throws(Exception::class)
	fun sharedPreferences() {
		val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
		preferences.edit().clear().apply()

		val prefName = "TESTING PREFERENCE"

		val slider = FloatSlider(appContext)
		slider.minValue = -1.5f
		slider.maxValue = 1.5f
		slider.step = 0.3f
		slider.setPreferences(preferences, prefName, 1f)
		slider.value = 0.1f

		assertEquals(slider.value, preferences.getFloat(prefName, java.lang.Float.MIN_VALUE))

		preferences.edit().remove(prefName).apply()
	}

	@Test
	@Throws(Exception::class)
	fun textViewTest() {
		val slider = FloatSlider(appContext)
		slider.minValue = -15f
		slider.maxValue = 15f
		slider.step = 3f
		slider.value = 15f

		slider.setTextView(TextView(appContext)) { `val` -> java.lang.Float.toString(`val`) }

		slider.onProgressChanged(slider, 14, true)
	}
}
