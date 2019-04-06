package com.adsamcik.slider.abstracts

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import com.adsamcik.slider.Scale
import com.adsamcik.slider.SliderUtility

/**
 * Abstract implementation of [Slider] for numbers
 */
abstract class NumberSlider<N : Number> : Slider<N> {
	/**
	 * Step used by SeekBar
	 *
	 */
	var sliderStep = 1
		set(sliderStep) {
			if (sliderStep <= 0)
				throw IllegalArgumentException("Slider step must be larger than 0")

			field = sliderStep
		}

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
			updateText()
		}

	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	private fun roundToStep(value: Int): Int {
		return SliderUtility.step(value, sliderStep)
	}

	override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
		val round = roundToStep(progress)
		if (round != progress)
			setProgress(round)
		else {
			super.onProgressChanged(seekBar, progress, fromUser)
		}
	}

}
