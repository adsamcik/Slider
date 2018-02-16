package com.adsamcik.slider.sliders

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet

import com.adsamcik.slider.EMath
import com.adsamcik.slider.R
import com.adsamcik.slider.scaleFunctions.LinearScale

import java.security.InvalidParameterException

class IntSlider : NumberSlider<Int> {
    private var mMin = 0
    private var mMax = 10

    override var minValue: Int
        get() = mMin
        set(min) {
            if (min >= mMax)
                throw InvalidParameterException("Min must be smaller than max")

            mMin = min
            updateSeekBarMax()
            updateText()
        }

    override var maxValue: Int
        get() = mMax
        set(max) {
            if (max <= mMin)
                throw InvalidParameterException("Max must be larger than min")

            mMax = max
            updateSeekBarMax()
            updateText()
        }

    override var step: Int
        get() = sliderStep
        set(step) {
            if (step <= 0)
                throw InvalidParameterException("Step must be larger than 0")

            sliderStep = step
            value = EMath.step(value, step)
        }

    override var value: Int
        get() = scale!!.invoke(progress, max, mMin, mMax)
        set(progress) = setProgress(toSliderProgress(progress))

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        setAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        setAttrs(context, attrs)
    }

    private fun init() {
        scale = LinearScale.integerScale
    }

    private fun setAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.IntSlider)
        mMin = ta.getInteger(R.styleable.IntSlider_minInt, if (Build.VERSION.SDK_INT >= 26) min else 0)
        mMax = ta.getInteger(R.styleable.IntSlider_maxInt, max)
        step = ta.getInteger(R.styleable.IntSlider_stepInt, 1)
        ta.recycle()

        updateSeekBarMax()
        updateText()
    }

    @RequiresApi(24)
    override fun setValue(progress: Int, animate: Boolean) {
        setProgress(toSliderProgress(EMath.step(progress, sliderStep)), animate)
    }

    override fun loadProgress(sharedPreferences: SharedPreferences, preferenceString: String, defaultValue: Int) {
        value = sharedPreferences.getInt(preferenceString, defaultValue)
    }

    public override fun updatePreferences(sharedPreferences: SharedPreferences, preferenceString: String, value: Int) {
        sharedPreferences.edit().putInt(preferenceString, value).apply()
    }

    private fun updateSeekBarMax() {
        max = mMax - mMin
    }

    private fun toSliderProgress(progress: Int): Int {
        return progress - mMin
    }
}
