package com.adsamcik.slider;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.AbsSeekBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adsamcik.slider.ScaleFunctions.LinearScale;

import static com.adsamcik.slider.EMath.step;

public abstract class Slider<N extends Number> extends SeekBar implements SeekBar.OnSeekBarChangeListener {
	private int mSliderStep = 1;

	protected IScale<N> mScale = null;
	protected TextView mTextView = null;
	protected IStringify<N> mStringify = null;

	protected SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = null;
	protected OnValueChangeListener<N> mOnValueChangeListener = null;

	public Slider(Context context) {
		super(context, null);
		init();
	}

	public Slider(Context context, AttributeSet attrs) {
		super(context, attrs, android.R.attr.seekBarStyle);
		init();
	}

	public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr, 0);
		init();
	}

	public Slider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		super.setOnSeekBarChangeListener(this);
	}

	private int roundToStep(int value) {
		return step(value, mSliderStep);
	}


	public void setScale(IScale scale) {
		mScale = scale;
		updateText();
	}

	public void setTextView(TextView textView, IStringify<N> stringify) {
		mTextView = textView;
		mStringify = stringify;
		updateText();
	}

	public abstract void setStep(N step);

	public abstract void setProgressValue(N progress);

	@RequiresApi(24)
	public abstract void setProgressValue(N progress, boolean animate);

	public abstract void setMinValue(N min);

	public abstract void setMaxValue(N max);

	public abstract N getMinValue();

	public abstract N getMaxValue();

	public abstract N getStep();

	public abstract N getValue();

	public IScale getScale() {
		return mScale;
	}

	public int getSliderStep() {
		return mSliderStep;
	}

	protected void setSliderStep(int sliderStep) {
		if (sliderStep <= 0)
			throw new RuntimeException("Slider step must be larger than 0");

		this.mSliderStep = sliderStep;
	}

	public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener l) {
		mOnSeekBarChangeListener = l;
	}

	public void setOnValueChangeListener(OnValueChangeListener<N> l) {
		mOnValueChangeListener = l;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		setProgress(roundToStep(getProgress()));
		if (fromUser)
			updateText();

		if (mOnSeekBarChangeListener != null)
			mOnSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if (mOnSeekBarChangeListener != null)
			mOnSeekBarChangeListener.onStartTrackingTouch(seekBar);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		updateText();

		if (mOnSeekBarChangeListener != null)
			mOnSeekBarChangeListener.onStopTrackingTouch(seekBar);

		if (mOnValueChangeListener != null)
			mOnValueChangeListener.onValueChanged(getValue(), true);
	}

	protected void updateText() {
		if (mTextView != null)
			mTextView.setText(mStringify.toString(getValue()));
	}


	public interface OnValueChangeListener<N extends Number> {
		void onValueChanged(N value, boolean fromUser);
	}

	public interface IStringify<N extends Number> {
		String toString(N value);
	}

	public interface IScale<N extends Number> {
		N scale(int progress, int maxProgress, N min, N max);
	}
}
