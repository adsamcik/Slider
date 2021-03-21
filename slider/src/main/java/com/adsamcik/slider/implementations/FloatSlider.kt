package com.adsamcik.slider.implementations

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import com.adsamcik.slider.R
import com.adsamcik.slider.Scale
import com.adsamcik.slider.SliderUtility.decimalPlaces
import com.adsamcik.slider.SliderUtility.round
import com.adsamcik.slider.abstracts.NumberSlider
import com.adsamcik.slider.scaleFunctions.LinearScale

/**
 * Implementation of [NumberSlider] for single precision floating point numbers ([Float])
 *
 * There is no default implementation of DoubleSlider because Android does not properly support [Double]. (Attrs can only have float and so can [SharedPreferences])
 */
open class FloatSlider : NumberSlider<Float> {
	private var mStep = 1f
	private var mMin = 0f
	private var mMax = 10f
	private val mRange
		get() = mMax - mMin

	private var mDecimalPlaces: Int = 0

	override var minValue: Float
		get() = mMin
		set(min) {
			mMin = min
			updateDecimalPlaces()
			invalidateSliderPosition()
		}

	override var maxValue: Float
		get() = mMax
		set(max) {
			mMax = max
			fluidStep = mStep / mRange
			updateDecimalPlaces()
			invalidateSliderPosition()
		}

	override var step: Float
		get() = mStep
		set(step) {
			mStep = step
			fluidStep = step / mRange
			updateDecimalPlaces()
			invalidateSliderPosition()
		}

	override var value: Float
		get() = round(scale.invoke(fluidPosition, mMin, mMax), mDecimalPlaces)
		set(value) {
			updateSliderPosition(value.coerceIn(mMin, mMax))
		}

	override var mScale: Scale<Float> = LinearScale.floatScale


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
		val ta = context.obtainStyledAttributes(attrs, R.styleable.FloatSlider)
		mMin = ta.getFloat(
				R.styleable.FloatSlider_minFloat,
				minValue
		)
		mMax = ta.getFloat(R.styleable.FloatSlider_maxFloat, maxValue)
		fluidStep = ta.getFloat(R.styleable.FloatSlider_stepFloat, 1f)
		ta.recycle()
		invalidateSliderPosition()
	}

	private fun updateDecimalPlaces() {
		mDecimalPlaces = decimalPlaces(mMax)
		var temp = decimalPlaces(mMin)
		if (temp > mDecimalPlaces)
			mDecimalPlaces = temp

		temp = decimalPlaces(mStep)
		if (temp > mDecimalPlaces)
			mDecimalPlaces = temp

	}

	override fun invalidateSliderPosition() {
		updateSliderPosition(value)
		invalidateText()
	}

	private fun updateSliderPosition(value: Float) {
		fluidPosition = (value - mMin) / mRange
	}

}
