package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.adsamcik.slider.ScaleFunctions.LinearScale;

import java.security.InvalidParameterException;

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
		else if(mItems != null)
			throw new RuntimeException("Step cannot be set while custom slider values are set");

		setSliderStep(step);
		setValue(step(getValue(), step));
	}

	@Override
	@RequiresApi(24)
	public void setValue(Integer progress, boolean animate) {
		setProgress(toSliderProgress(step(progress, getSliderStep())), animate);
	}

	@Override
	public void setValue(Integer progress) {
		setProgress(toSliderProgress(progress));
	}

	@Override
	public void setMinValue(Integer min) {
		if (min >= mMax)
			throw new InvalidParameterException("Min must be smaller than max");
		else if(mItems != null)
			throw new RuntimeException("Min cannot be set while custom slider values are set");

		mMin = min;
		updateSeekBarMax();
		updateText();
	}

	@Override
	public void setMaxValue(Integer max) {
		if (max <= mMin)
			throw new InvalidParameterException("Max must be larger than min");
		else if(mItems != null)
			throw new RuntimeException("Max cannot be set while custom slider values are set");

		mMax = max;
		updateSeekBarMax();
		updateText();
	}

	@Override
	public void setPreferencesAndLoad(@Nullable SharedPreferences sharedPreferences, @Nullable String preferenceString, Integer defaultValue) {
		if (sharedPreferences == null || preferenceString == null)
			setPreferences(null, null);
		else {
			setValue(sharedPreferences.getInt(preferenceString, defaultValue));
			setPreferences(sharedPreferences, preferenceString);
		}
	}

	@Override
	public void updatePreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull Integer value) {
		sharedPreferences.edit().putInt(preferenceString, value).apply();
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
		return mItems != null ? mItems[getProgress()] : mScale.scale(getProgress(), getMax(), mMin, mMax);
	}

	@Override
	public void setItems(@Nullable Integer[] items) {
		this.mItems = items;
		if (items != null) {
			mMin = 0;
			mMax = items.length - 1;
			setSliderStep(1);
		}
	}

	private int toSliderProgress(int progress) {
		Integer itemIndex = getItemIndex(progress);
		return itemIndex != null ? itemIndex : progress - mMin;
	}

	private int fromSliderProgress(int progress) {
		return mItems != null ? mItems[progress] : progress + mMin;
	}
}
