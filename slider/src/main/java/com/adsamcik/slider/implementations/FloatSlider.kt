package com.adsamcik.slider.implementations

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import com.adsamcik.slider.EMath
import com.adsamcik.slider.EMath.decimalPlaces
import com.adsamcik.slider.EMath.round
import com.adsamcik.slider.R
import com.adsamcik.slider.abstracts.NumberSlider
import com.adsamcik.slider.scaleFunctions.LinearScale
import java.security.InvalidParameterException

class FloatSlider : NumberSlider<Float> {
    private var mStep = 1f
    private var mMin = 0f
    private var mMax = 10f

    private var mDecimalPlaces = 0

    private val percentPower: Int
        get() = 100 * Math.pow(10.0, mDecimalPlaces.toDouble()).toInt()

    override var minValue: Float
        get() = mMin
        set(min) {
            if (min >= mMax)
                throw InvalidParameterException("Min must be smaller than max")

            mMin = min
            updateDecimalPlaces()
        }

    override var maxValue: Float
        get() = mMax
        set(max) {
            if (max <= mMin)
                throw InvalidParameterException("Max must be larger than min")

            mMax = max
            val diff = round(mMax - mMin, 5)
            val m = (diff * percentPower).toInt()
            setMax(m)
            sliderStep = Math.round(mStep / diff * max)
            updateDecimalPlaces()
        }

    override var step: Float
        get() = mStep
        set(step) {
            mStep = step
            sliderStep = (step * percentPower).toInt()
            updateDecimalPlaces()
        }

    override var value: Float
        get() = round(scale!!.invoke(stepProgress, max, mMin, mMax), mDecimalPlaces)
        set(progress) {
            if (progress > mMax || progress < mMin)
                throw IllegalArgumentException("Progress must be larger than $mMin and smaller than $mMax was $progress")

            setProgress(((progress - mMin) * percentPower).toInt())
        }

    private val stepProgress: Int
        get() = EMath.step(progress, sliderStep)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setAttrs(context, attrs)
    }

    init {
        scale = LinearScale.floatScale
    }

    private fun setAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.FloatSlider)
        mMin = ta.getFloat(R.styleable.FloatSlider_minFloat, (if (Build.VERSION.SDK_INT >= 26) min else 0).toFloat())
        mMax = ta.getFloat(R.styleable.FloatSlider_maxFloat, max.toFloat())
        step = ta.getFloat(R.styleable.FloatSlider_stepFloat, 1f)
        ta.recycle()
        updateText()
    }

    @RequiresApi(24)
    override fun setValue(progress: Float, animate: Boolean) {
        if (progress > mMax || progress < mMin)
            throw IllegalArgumentException("Progress must be larger than $mMin and smaller than $mMax was $progress")
        setProgress(((progress - mMin) * percentPower).toInt(), animate)
    }

    override fun loadProgress(sharedPreferences: SharedPreferences, preferenceString: String, defaultValue: Float) {
        value = sharedPreferences.getFloat(preferenceString, defaultValue)
    }

    public override fun updatePreferences(sharedPreferences: SharedPreferences, preferenceString: String, value: Float) {
        sharedPreferences.edit().putFloat(preferenceString, value).apply()
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

}
