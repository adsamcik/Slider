package com.adsamcik.slider


import android.content.Context
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.adsamcik.slider.abstracts.Slider
import com.adsamcik.slider.abstracts.SliderExtension
import com.adsamcik.slider.extensions.IntSliderSharedPreferencesExtension
import com.adsamcik.slider.extensions.StringSliderSharedPreferencesExtension
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

		assertEquals("b", slider.value)

		assertEquals(1, slider.index)
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
		assertEquals(0f, slider.fluidPosition)

		slider.value = "f"

		assertEquals(1f, slider.fluidPosition)
		assertEquals("f", slider.value)

		slider.value = "b"

		assertEquals(0.2f, slider.fluidPosition)
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

		AssertUtility.assertException(
				{ slider.index = 15 },
				NullPointerException::class.java
		)

		slider.setItems(strings)

		//checks whether it crashes here, it shouldn't
		slider.index = 15
		slider.index = -1
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
	fun stringSharedPreferences() {
		val preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(appContext)
		val prefName = "TESTING PREFERENCE"
		val noPreference = "NO PREFERENCE"

		preferences.edit().clear().apply()

		val slider = ObjectValueSlider<String>(appContext)
		slider.setItems(strings)

		val extension = StringSliderSharedPreferencesExtension(preferences, prefName,  "d")
		slider.addExtension(extension)

		assertEquals("d", slider.value)

		slider.value = "c"

		val value = slider.value
		assertEquals(value, preferences.getString(prefName, noPreference))

		slider.removeExtension(extension)

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

		val lastValue = "d"
		val extension = object : SliderExtension<String> {
			override fun onValueChanged(
					slider: Slider<String>,
					value: String,
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
		slider.value = "c"
		assertEquals(1, atomicInteger.get().toLong())
	}
}
