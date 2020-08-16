package com.adsamcik.slider.implementations

import android.content.Context
import android.util.AttributeSet
import com.adsamcik.slider.abstracts.ValueSlider

/**
 * Implementation of [ValueSlider] for [Float]
 */
class FloatValueSlider : ValueSlider<Float> {
	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}
