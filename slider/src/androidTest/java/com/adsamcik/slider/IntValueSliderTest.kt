package com.adsamcik.slider


import android.content.Context
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import com.adsamcik.slider.extensions.IntSliderSharedPreferencesExtension
import com.adsamcik.slider.implementations.IntValueSlider
import org.junit.Assert
import org.junit.Test

class IntValueSliderTest {
	private val appContext = ApplicationProvider.getApplicationContext<Context>()

	private val ints = arrayOf(0, 1, 2, 4, 7, 12)

	@Test
	@Throws(Exception::class)
	fun sharedPreferences() {
		val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
		val prefName = "TESTING PREFERENCE"
		val noPreference = -1

		preferences.edit().clear().apply()

		val slider = IntValueSlider(appContext)
		slider.setItems(ints)

		val extension = IntSliderSharedPreferencesExtension(preferences, prefName, 4)
		slider.addExtension(extension)

		Assert.assertEquals(4L, slider.value.toLong())

		slider.value = 7

		val value = slider.value
		Assert.assertEquals(value, preferences.getInt(prefName, noPreference))

		slider.removeExtension(extension)

		slider.value = 1

		Assert.assertEquals(value, preferences.getInt(prefName, noPreference))
		Assert.assertEquals(1.toLong(), slider.value.toLong())

		//cleanup
		preferences.edit().remove(prefName).apply()
	}
}
