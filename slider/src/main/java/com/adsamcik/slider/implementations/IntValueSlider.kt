package com.adsamcik.slider.implementations

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import com.adsamcik.slider.abstracts.ValueSlider

class IntValueSlider : ValueSlider<Int> {
	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	override fun loadProgress(sharedPreferences: SharedPreferences, preferenceString: String, defaultValue: Int) {
		super.setProgress(getValueIndex(sharedPreferences.getInt(preferenceString, defaultValue)))
	}

	override fun updatePreferences(sharedPreferences: SharedPreferences, preferenceString: String, value: Int) {
		sharedPreferences.edit().putInt(preferenceString, value).apply()
	}

}
