package com.adsamcik.slider;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.adsamcik.slider.ScaleFunctions.LinearScale;

import java.security.InvalidParameterException;

import static com.adsamcik.slider.EMath.decimalPlaces;
import static com.adsamcik.slider.EMath.round;
import static com.adsamcik.slider.EMath.step;

public class FloatSlider extends Slider<Float> {
	private float mStep = 1;
	private float mMin = 0;
	private float mMax = 10;

	private int mDecimalPlaces = 0;

	public FloatSlider(Context context) {
		super(context);
		init();
	}

	public FloatSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		setAttrs(context, attrs);
	}

	public FloatSlider(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		setAttrs(context, attrs);
	}

	public FloatSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
		setAttrs(context, attrs);
	}

	private void init() {
		setScale(LinearScale.getFloatScale());
	}

	private void setAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FloatSlider);
		mMin = ta.getFloat(R.styleable.FloatSlider_minFloat, Build.VERSION.SDK_INT >= 26 ? getMin() : 0);
		mMax = ta.getFloat(R.styleable.FloatSlider_maxFloat, getMax());
		setStep(ta.getFloat(R.styleable.FloatSlider_stepFloat, 1));
		ta.recycle();
		updateText();
	}

	@Override
	public void setStep(Float step) {
		if (items != null)
			throw new RuntimeException("Step cannot be set while custom slider values are set");

		mStep = step;
		setSliderStep((int) (step * getPercentPower()));
		updateDecimalPlaces();
	}

	@Override
	public void setProgressValue(Float progress) {
		if (items != null) {
			setProgress(getItemIndex(progress));
		} else {
			if (progress > mMax || progress < mMin)
				throw new IllegalArgumentException("Progress must be larger than " + mMin + " and smaller than " + mMax + " was " + progress);

			setProgress((int) ((progress - mMin) * getPercentPower()));
		}
	}

	@Override
	@RequiresApi(24)
	public void setProgressValue(Float progress, boolean animate) {
		if (items != null) {
			setProgress(getItemIndex(progress));
		} else {
			if (progress > mMax || progress < mMin)
				throw new IllegalArgumentException("Progress must be larger than " + mMin + " and smaller than " + mMax + " was " + progress);
			setProgress((int) ((progress - mMin) * getPercentPower()), animate);
		}
	}

	private int getPercentPower() {
		return 100 * (int) Math.pow(10, mDecimalPlaces);
	}

	@Override
	public void setMinValue(Float min) {
		if (min >= mMax)
			throw new InvalidParameterException("Min must be smaller than max");
		else if (items != null)
			throw new RuntimeException("Min cannot be set while custom slider values are set");

		mMin = min;
		updateDecimalPlaces();
	}


	@Override
	public void setMaxValue(Float max) {
		if (max <= mMin)
			throw new InvalidParameterException("Max must be larger than min");
		else if (items != null)
			throw new RuntimeException("Max cannot be set while custom slider values are set");

		mMax = max;
		float diff = round(mMax - mMin, 5);
		int m = (int) (diff * getPercentPower());
		setMax(m);
		setSliderStep(Math.round(mStep / diff * max));
		updateDecimalPlaces();
	}

	@Override
	public Float getMinValue() {
		return mMin;
	}

	@Override
	public Float getMaxValue() {
		return mMax;
	}

	@Override
	public Float getStep() {
		return mStep;
	}

	@Override
	public Float getValue() {
		return items != null ? items[getProgress()] : round(mScale.scale(getStepProgress(), getMax(), mMin, mMax), mDecimalPlaces);
	}

	@Override
	public void setItems(@Nullable Float[] items) {
		this.items = items;
		if (items != null) {
			mMin = 0f;
			mDecimalPlaces = 0;
			mMax = (float) (items.length - 1);
			setMax(items.length - 1);
			mStep = 1f;
			setSliderStep(1);
		}
	}

	private int getStepProgress() {
		return step(getProgress(), getSliderStep());
	}

	private void updateDecimalPlaces() {
		mDecimalPlaces = decimalPlaces(mMax);
		int temp = decimalPlaces(mMin);
		if (temp > mDecimalPlaces)
			mDecimalPlaces = temp;

		temp = decimalPlaces(mStep);
		if (temp > mDecimalPlaces)
			mDecimalPlaces = temp;

	}

}
