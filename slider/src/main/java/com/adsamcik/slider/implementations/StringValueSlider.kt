package com.adsamcik.slider.implementations

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import com.adsamcik.slider.abstracts.ValueSlider

/**
 * Implementation of [ValueSlider] for [String]
 */
@Suppress("UNUSED")
class StringValueSlider : ValueSlider<String> {
	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	override fun loadProgress(sharedPreferences: SharedPreferences, preferenceString: String, defaultValue: String) {
		val preferenceValue = sharedPreferences.getString(preferenceString, defaultValue)
		if (preferenceValue != null)
			value = preferenceValue
	}

	override fun updatePreferences(sharedPreferences: SharedPreferences, preferenceString: String, value: String) {
		sharedPreferences.edit().putString(preferenceString, value).apply()
	}
}