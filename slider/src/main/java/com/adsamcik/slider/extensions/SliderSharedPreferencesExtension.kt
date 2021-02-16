package com.adsamcik.slider.extensions

import android.content.SharedPreferences
import com.adsamcik.slider.abstracts.Slider
import com.adsamcik.slider.abstracts.SliderExtension

abstract class BaseSliderSharedPreferencesExtension<T>(
		protected val sharedPreferences: SharedPreferences,
		protected val key: String,
		protected val defaultValue: T
) : SliderExtension<T> {
	/**
	 * Set slider's preferences for automatic saving inside passed instance of [SharedPreferences] and loads currently saved values.
	 * Saves only value not bounds or step.
	 * It is important NOT to call this function before setting min, max and step.
	 *
	 */
	abstract fun loadProgress(): T

	/**
	 * Function called when updating preferences is need.
	 * Function is guaranteed to be called only when SharedPreferences are not null
	 *
	 * @param value             Value
	 */
	protected abstract fun updatePreferences(
			value: T
	)

	override fun onAttach(slider: Slider<T>) {
		slider.value = loadProgress()
	}

	override fun onValueChanged(
			slider: Slider<T>,
			value: T,
			position: Float,
			isFromUser: Boolean
	) {
		updatePreferences(value)
	}
}

/**
 * Slider extension for float shared preferences support
 */
class FloatSliderSharedPreferencesExtension(
		preferences: SharedPreferences,
		key: String,
		defaultValue: Float
) : BaseSliderSharedPreferencesExtension<Float>(preferences, key, defaultValue) {
	override fun loadProgress(): Float {
		return sharedPreferences.getFloat(key, defaultValue)
	}

	override fun updatePreferences(value: Float) {
		sharedPreferences.edit().putFloat(key, value).apply()
	}
}

/**
 * Slider extension for integer shared preferences support
 */
class IntSliderSharedPreferencesExtension(
		preferences: SharedPreferences,
		key: String,
		defaultValue: Int
) : BaseSliderSharedPreferencesExtension<Int>(preferences, key, defaultValue) {
	override fun loadProgress(): Int {
		return sharedPreferences.getInt(key, defaultValue)
	}

	override fun updatePreferences(value: Int) {
		sharedPreferences.edit().putInt(key, value).apply()
	}
}

/**
 * Slider extension for float shared preferences support
 */
class StringSliderSharedPreferencesExtension(
		preferences: SharedPreferences,
		key: String,
		defaultValue: String
) : BaseSliderSharedPreferencesExtension<String>(preferences, key, defaultValue) {
	override fun loadProgress(): String {
		return requireNotNull(sharedPreferences.getString(key, defaultValue))
	}

	override fun updatePreferences(value: String) {
		sharedPreferences.edit().putString(key, value).apply()
	}
}
