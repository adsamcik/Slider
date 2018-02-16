package com.adsamcik.slider.sliders

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import com.adsamcik.slider.EMath
import com.adsamcik.slider.Scale

abstract class NumberSlider<N : Number> : Slider<N> {
    /**
     * Returns step used by SeekBar
     *
     * @return Slider step
     */
    /**
     * Sets step used by SeekBar
     *
     */
    var sliderStep = 1
        set(sliderStep) {
            if (sliderStep <= 0)
                throw RuntimeException("Slider step must be larger than 0")

            field = sliderStep
        }

    /**
     * Get slider's min value.
     *
     * @return Min value
     */
    /**
     * Set slider's min value.
     *
     * @param min Min value
     */
    abstract var minValue: N

    /**
     * Get slider's max value.
     *
     * @return Max value
     */
    /**
     * Set slider's max value.
     *
     * @param max Max value
     */
    abstract var maxValue: N

    /**
     * Get slider's step value.
     *
     * @return Step value
     */
    /**
     * Set slider's step value.
     *
     * @param step Step value
     */
    abstract var step: N

    /**
     * Returns current scale function
     *
     * @return Scale function
     */
    var scale: Scale<N>? = null
        set(value) {
            field = value
            updateText()
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     * Round desired value to step. Primarily used for SeekBar progress.
     *
     * @param value Value
     * @return Value after rounding to nearest step
     */
    private fun roundToStep(value: Int): Int {
        return EMath.step(value, sliderStep)
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
