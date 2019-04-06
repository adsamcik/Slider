package com.adsamcik.slider.implementations

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import com.adsamcik.slider.abstracts.ValueSlider

/**
 * Implementation of [ValueSlider] for [Float]
 */
class FloatValueSlider : ValueSlider<Float> {
	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	override fun loadProgress(sharedPreferences: SharedPreferences, preferenceString: String, defaultValue: Float) {
		super.setProgress(getValueIndex(sharedPreferences.getFloat(preferenceString, defaultValue)))
	}

	override fun updatePreferences(sharedPreferences: SharedPreferences, preferenceString: String, value: Float) {
		sharedPreferences.edit().putFloat(preferenceString, value).apply()
	}
}