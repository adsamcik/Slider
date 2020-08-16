package com.adsamcik.slider.implementations

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.adsamcik.slider.R
import com.adsamcik.slider.Scale
import com.adsamcik.slider.SliderUtility
import com.adsamcik.slider.SliderUtility.decimalPlaces
import com.adsamcik.slider.SliderUtility.round
import com.adsamcik.slider.abstracts.NumberSlider
import com.adsamcik.slider.scaleFunctions.LinearScale
import java.security.InvalidParameterException

/**
 * Implementation of [NumberSlider] for single precision floating point numbers ([Float])
 *
 * There is no default implementation of DoubleSlider because Android does not properly support [Double]. (Attrs can only have float and so can [SharedPreferences])
 */
class FloatSlider : NumberSlider<Float> {
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
			invalidatePosition()
		}

	override var maxValue: Float
		get() = mMax
		set(max) {
			mMax = max
			fluidStep = mStep / mRange
			updateDecimalPlaces()
			invalidatePosition()
		}

	override var step: Float
		get() = mStep
		set(step) {
			mStep = step
			fluidStep = step / mRange
			updateDecimalPlaces()
			invalidatePosition()
		}

	override var value: Float
		get() = round(scale.invoke(fluidPosition, mMin, mMax), mDecimalPlaces)
		set(progress) {
			require(progress <= mMax) { "Value must be smaller than maximum." }
			require(progress >= mMin) { "Value must be larger than minimum" }

			invalidatePosition(progress)
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
		invalidatePosition()
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

	override fun invalidatePosition() {
		invalidatePosition(value)
		invalidateText()
	}

	protected fun invalidatePosition(value: Float) {
		fluidPosition = (value - mMin) / mRange
	}

}
