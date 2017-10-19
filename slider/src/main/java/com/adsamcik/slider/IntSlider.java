package com.adsamcik.slider;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.adsamcik.slider.ScaleFunctions.LinearScale;

import java.security.InvalidParameterException;
import java.util.Arrays;

import static com.adsamcik.slider.EMath.step;

public class IntSlider extends Slider<Integer> {
	private int mMin = 0;
	private int mMax = 10;

	public IntSlider(Context context) {
		super(context);
		init();
	}

	public IntSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		setAttrs(context, attrs);
	}

	public IntSlider(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		setAttrs(context, attrs);
	}

	public IntSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
		setAttrs(context, attrs);
	}

	private void init() {
		setScale(LinearScale.getIntegerScale());
	}

	private void setAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IntSlider);
		mMin = ta.getInteger(R.styleable.IntSlider_minInt, Build.VERSION.SDK_INT >= 26 ? getMin() : 0);
		mMax = ta.getInteger(R.styleable.IntSlider_maxInt, getMax());
		setStep(ta.getInteger(R.styleable.IntSlider_stepInt, 1));
		ta.recycle();

		updateSeekBarMax();
		updateText();
	}

	@Override
	public void setStep(Integer step) {
		if (step <= 0)
			throw new InvalidParameterException("Step must be larger than 0");

		setSliderStep(step);
	}

	@Override
	@RequiresApi(24)
	public void setProgressValue(Integer progress, boolean animate) {
		setProgress(toSliderProgress(step(progress, getSliderStep())), animate);
	}

	@Override
	public void setProgressValue(Integer progress) {
		setProgress(toSliderProgress(progress));
	}

	@Override
	public void setMinValue(Integer min) {
		if (min >= mMax)
			throw new InvalidParameterException("Min must be smaller than max");

		mMin = min;
		updateSeekBarMax();
		updateText();
	}

	@Override
	public void setMaxValue(Integer max) {
		if (max <= mMin)
			throw new InvalidParameterException("Max must be larger than min");

		mMax = max;
		updateSeekBarMax();
		updateText();
	}

	private void updateSeekBarMax() {
		setMax(mMax - mMin);
	}

	@Override
	public Integer getMinValue() {
		return mMin;
	}

	@Override
	public Integer getMaxValue() {
		return mMax;
	}

	@Override
	public Integer getStep() {
		return getSliderStep();
	}

	@Override
	public Integer getValue() {
		return items != null ? items[getProgress()] : mScale.scale(getProgress(), getMax(), mMin, mMax);
	}

	@Override
	public void setItems(@Nullable Integer[] items) {
		this.items = items;
		if (items != null) {
			setMinValue(0);
			setMaxValue(items.length - 1);
			setStep(1);
		}
	}

	private int toSliderProgress(int progress) {
		Integer itemIndex = getItemIndex(progress);
		return itemIndex != null ? itemIndex : progress - mMin;
	}

	private int fromSliderProgress(int progress) {
		return items != null ? items[progress] : progress + mMin;
	}
}
