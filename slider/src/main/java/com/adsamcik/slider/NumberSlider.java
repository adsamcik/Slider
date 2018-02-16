package com.adsamcik.slider;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

import static com.adsamcik.slider.EMath.step;

public abstract class NumberSlider<N extends Number> extends Slider<N> {
	private int mSliderStep = 1;

	protected IScale<N> mScale = null;

	public NumberSlider(Context context) {
		super(context);
	}

	public NumberSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NumberSlider(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * Round desired value to step. Primarily used for SeekBar progress.
	 *
	 * @param value Value
	 * @return Value after rounding to nearest step
	 */
	private int roundToStep(int value) {
		return step(value, mSliderStep);
	}

	/**
	 * Set slider's scale.
	 *
	 * @param scale Scale
	 */
	public void setScale(IScale<N> scale) {
		mScale = scale;
		updateText();
	}

	/**
	 * Set slider's step value.
	 *
	 * @param step Step value
	 */
	public abstract void setStep(N step);

	/**
	 * Sets step used by SeekBar
	 *
	 * @param sliderStep slider step
	 */
	protected void setSliderStep(int sliderStep) {
		if (sliderStep <= 0)
			throw new RuntimeException("Slider step must be larger than 0");

		this.mSliderStep = sliderStep;
	}

	/**
	 * Set slider's min value.
	 *
	 * @param min Min value
	 */
	public abstract void setMinValue(N min);

	/**
	 * Set slider's max value.
	 *
	 * @param max Max value
	 */
	public abstract void setMaxValue(N max);

	/**
	 * Get slider's min value.
	 *
	 * @return Min value
	 */
	public abstract N getMinValue();

	/**
	 * Get slider's max value.
	 *
	 * @return Max value
	 */
	public abstract N getMaxValue();

	/**
	 * Get slider's step value.
	 *
	 * @return Step value
	 */
	public abstract N getStep();

	/**
	 * Returns current scale function
	 *
	 * @return Scale function
	 */
	public IScale getScale() {
		return mScale;
	}

	/**
	 * Returns step used by SeekBar
	 *
	 * @return Slider step
	 */
	public int getSliderStep() {
		return mSliderStep;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		int round = roundToStep(progress);
		if (round != progress)
			setProgress(round);
		else {
			super.onProgressChanged(seekBar, progress, fromUser);
		}
	}

}
