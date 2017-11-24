package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.io.InvalidObjectException;
import java.util.Arrays;

public class ValueSlider<T> extends Slider<T> {
	protected T[] mItems = null;

	public ValueSlider(Context context) {
		super(context);
	}

	public ValueSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ValueSlider(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ValueSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void setStep(T step) {
		throw new UnsupportedOperationException("You cannot set step in value slider");
	}

	@Override
	public void setValue(T progress) {
		//todo implement
	}

	@Override
	public void setValue(T progress, boolean animate) {
//todo implement
	}

	@Override
	public void setMinValue(T min) {
		throw new UnsupportedOperationException("You cannot set min value in Value Slider");
	}

	@Override
	public void setMaxValue(T max) {
		throw new UnsupportedOperationException("You cannot set max value in Value Slider");
	}

	@Override
	public void setPreferencesAndLoad(@Nullable SharedPreferences sharedPreferences, @Nullable String preferenceString, T defaultValue) {

	}

	@Override
	public void updatePreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull Integer value) {

	}

	@Override
	public T getMinValue() {
		throw new UnsupportedOperationException("There is no min value in value slider");
	}

	@Override
	public T getMaxValue() {
		throw new UnsupportedOperationException("There is no max value in value slider");
	}

	@Override
	public T getStep() {
		throw new UnsupportedOperationException("There is no step value in value slider");
	}

	@Override
	public T getValue() {
		if (mItems == null)
			throw new RuntimeException("You must first set items before requesting value");
		else
			return mItems[getProgress()];
	}


	/**
	 * Sets items.
	 * This method overrides any previous min, max and step settings
	 *
	 * @param items Items
	 */
	public void setItems(@Nullable T[] items) {
		this.mItems = items;
		if (items != null) {
			setSliderStep(1);
			setProgress(0);
		}
	}

	/**
	 * Returns items
	 *
	 * @return Returns items or null if no items were set
	 */
	public T[] getItems() {
		return mItems;
	}

	protected Integer getItemIndex(T item) {
		if (mItems != null) {
			int index = Arrays.asList(mItems).indexOf(item);
			if (index == -1)
				return null;
			else
				return index;
		}
		return null;
	}

	private int toSliderProgress(int progress) {
		Integer itemIndex = getItemIndex(progress);
		return itemIndex != null ? itemIndex : progress - mMin;
	}
}
