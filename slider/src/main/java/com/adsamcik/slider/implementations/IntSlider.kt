package com.adsamcik.slider.implementations

import android.content.Context
import android.util.AttributeSet
import com.adsamcik.slider.R
import com.adsamcik.slider.Scale
import com.adsamcik.slider.abstracts.NumberSlider
import com.adsamcik.slider.scaleFunctions.LinearScale

/**
 * Implementation of [NumberSlider] for [Int]
 */
class IntSlider : NumberSlider<Int> {
	private var mMin = 0
	private var mMax = 10

	override var minValue: Int
		get() = mMin
		set(min) {
			mMin = min
			invalidateSliderPosition()
		}

	override var maxValue: Int
		get() = mMax
		set(max) {
			mMax = max
			invalidateSliderPosition()
		}

	override var step: Int = 1
		set(step) {
			require(step > 0) { "Step must be larger than 0" }

			fluidStep = step.toFloat() / (mMax - mMin).toFloat()
			field = step

			invalidateSliderPosition()
		}

	override var value: Int
		get() = scale.invoke(fluidPosition, mMin, mMax)
		set(value) {
			invalidatePosition(value)
		}

	override var mScale: Scale<Int> = LinearScale.integerScale

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		setAttrs(context, attrs)
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
			context,
			attrs,
			defStyleAttr
	) {
		setAttrs(context, attrs)
	}

	private fun setAttrs(context: Context, attrs: AttributeSet) {
		val ta = context.obtainStyledAttributes(attrs, R.styleable.IntSlider)
		mMin = ta.getInteger(
				R.styleable.IntSlider_minInt,
				mMin
		)
		mMax = ta.getInteger(R.styleable.IntSlider_maxInt, mMax)
		step = ta.getInteger(R.styleable.IntSlider_stepInt, 1)
		ta.recycle()

		invalidateSliderPosition()
	}

	override fun invalidateSliderPosition() {
		invalidatePosition(value)
		invalidateText()
	}

	protected fun invalidatePosition(value: Int) {
		fluidPosition = (value - mMin).toFloat() / (mMax - mMin).toFloat()
	}
}
