package com.adsamcik.slider.implementations

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import com.adsamcik.slider.Stringify
import com.adsamcik.slider.abstracts.ValueSlider

/**
 * Implementation of [ValueSlider] for custom objects
 */
open class ObjectValueSlider<T> : ValueSlider<T> {
	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
			context,
			attrs,
			defStyleAttr
	)
}
