package com.adsamcik.slider

import android.content.Context
import android.os.Looper
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import com.adsamcik.slider.abstracts.Slider
import com.adsamcik.slider.abstracts.SliderExtension
import com.adsamcik.slider.extensions.IntSliderSharedPreferencesExtension
import com.adsamcik.slider.implementations.IntSlider
import com.adsamcik.slider.scaleFunctions.LinearScale
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToLong

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class IntSliderInstrumentationTest {
	companion object {
		private const val FLUID_DELTA = 0.0001f
	}

	private val appContext = ApplicationProvider.getApplicationContext<Context>()

	private val atomicInteger = AtomicInteger(0)


	@Test
	@Throws(Exception::class)
	fun baseTest() {
		val slider = IntSlider(appContext)
		slider.minValue = 1
		slider.maxValue = 9
		slider.step = 2
		slider.value = 5
		slider.setLabelFormatter(null)

		assertEquals(1, slider.minValue.toLong())
		assertEquals(9, slider.maxValue.toLong())
		assertEquals(0.5f, slider.fluidPosition, FLUID_DELTA)
		assertEquals(5, slider.value.toLong())

		val scale = LinearScale.integerScale
		slider.scale = scale
		assertEquals(scale, slider.scale)
	}

	@Test
			/**
			 * This tests check if description is set properly.
			 * This also ensures updating description does not crash the slider.
			 */
	fun descriptionTest() {
		val slider = IntSlider(appContext)
		slider.minValue = 1
		slider.maxValue = 9
		slider.step = 2
		slider.value = 5
		val description = "TestDescription"
		slider.description = description

		assertEquals(description, slider.description)

		val scale = LinearScale.integerScale
		slider.scale = scale
		assertEquals(scale, slider.scale)
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

		assertEquals(0.5f, slider.fluidPosition, FLUID_DELTA)

		slider.value = 8

		assertEquals(0.8f, slider.fluidPosition, FLUID_DELTA)


		slider.value = 3

		assertEquals(0.3f, slider.fluidPosition, FLUID_DELTA)
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
			assertEquals((i / 3.0).roundToLong() * 3, slider.value.toLong())
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
			assertEquals((i / 3.0).roundToLong() * 3, slider.value.toLong())
		}
	}

	@Test
	@Throws(Exception::class)
	fun stepTest() {
		val slider = IntSlider(appContext)
		slider.minValue = 0
		slider.maxValue = 10

		slider.value = 5

		assertEquals(5, slider.value)

		slider.step = 2

		val value = slider.value
		Assert.assertTrue(value == 4 || value == 6)

		slider.step = 3
		slider.value = 5
		assertEquals(6, slider.value)
	}

	@Test
	@Throws(Exception::class)
	fun sharedPreferences() {
		val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
		val prefName = "TESTING PREFERENCE"

		preferences.edit().clear().apply()

		val slider = IntSlider(appContext)
		slider.minValue = -5
		slider.maxValue = 5
		slider.step = 2

		val extension = IntSliderSharedPreferencesExtension(preferences, prefName, 1)
		slider.addExtension(extension)

		assertEquals(1, slider.value.toLong())

		slider.value = 4

		val value = slider.value
		assertEquals(value, preferences.getInt(prefName, Integer.MIN_VALUE))

		slider.removeExtension(extension)

		slider.value = 1

		assertEquals(value, preferences.getInt(prefName, Integer.MIN_VALUE))
		assertEquals(1, slider.value)

		//cleanup
		preferences.edit().remove(prefName).apply()
	}

	@Test
	@Throws(Exception::class)
	fun callbackTests() {
		val slider = IntSlider(appContext)

		val lastValue = 5

		val extension = object : SliderExtension<Int> {
			override fun onValueChanged(
					slider: Slider<Int>,
					value: Int,
					position: Float,
					isFromUser: Boolean
			) {
				assertEquals(lastValue, value)
				assertEquals(false, isFromUser)
				atomicInteger.incrementAndGet()
			}
		}

		slider.addExtension(extension)
		slider.value = lastValue
		assertEquals(1, atomicInteger.get().toLong())

		slider.removeExtension(extension)
		slider.value = 3
		assertEquals(1, atomicInteger.get().toLong())
	}

	@Test
	fun exceptionTest() {
		val slider = IntSlider(appContext)
		AssertUtility.assertException(
				{ slider.step = -5 },
				IllegalArgumentException::class.java
		)

		AssertUtility.assertException(
				{ slider.minValue = slider.maxValue },
				IllegalArgumentException::class.java
		)
		AssertUtility.assertException(
				{ slider.maxValue = slider.minValue },
				IllegalArgumentException::class.java
		)
	}
}
