package com.adsamcik.slider


import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry
import com.adsamcik.slider.sliders.IntValueSlider
import junit.framework.Assert
import org.junit.Test

class IntValueSliderTest {
    private val appContext = InstrumentationRegistry.getTargetContext()

    private val ints = arrayOf(0, 1, 2, 4, 7, 12)

    @Test
    @Throws(Exception::class)
    fun sharedPreferences() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
        val prefName = "TESTING PREFERENCE"
        val noPreference = -1

        preferences.edit().remove(prefName).commit()

        val slider = IntValueSlider(appContext)
        slider.setItems(ints)

        slider.setPreferences(preferences, prefName, 4)

        Assert.assertEquals(4L, slider.value.toLong())

        slider.value = 7

        val value = slider.value
        Assert.assertEquals(value, preferences.getInt(prefName, noPreference))

        slider.removePreferences()

        slider.value = 1

        Assert.assertEquals(value, preferences.getInt(prefName, noPreference))
        Assert.assertEquals(1.toLong(), slider.value.toLong())

        //cleanup
        preferences.edit().remove(prefName).apply()
    }
}
