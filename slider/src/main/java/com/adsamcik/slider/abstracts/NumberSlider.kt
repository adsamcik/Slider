package com.adsamcik.slider.abstracts

import android.content.Context
import android.util.AttributeSet
import com.adsamcik.slider.Scale

/**
 * Abstract implementation of [Slider] for numbers
 */
abstract class NumberSlider<N : Number> : Slider<N> {
	/**
	 *  Slider's min value
	 *
	 */
	abstract var minValue: N

	/**
	 * Slider's max value
	 *
	 */
	abstract var maxValue: N

	/**
	 * Slider step value
	 *
	 * @return Step value
	 */
	abstract var step: N


	protected abstract var mScale: Scale<N>

	/**
	 * Returns current scale function
	 *
	 * @return Scale function
	 */
	var scale: Scale<N>
		get() = mScale
		set(value) {
			mScale = value
			invalidateSliderPosition()
			invalidateText()
		}

	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
			context,
			attrs,
			defStyleAttr
	)

	override fun onTextInvalidated() {
		endText = labelFormatter(maxValue)
		startText = labelFormatter(minValue)
		bubbleText = labelFormatter(scale(fluidPosition, minValue, maxValue))
	}

	protected abstract fun invalidateSliderPosition()
}
