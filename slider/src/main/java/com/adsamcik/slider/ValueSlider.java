package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.io.InvalidObjectException;
import java.security.InvalidParameterException;
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
	public void setValue(T item) {
		Integer index = getItemIndex(item);
		setProgress(index);
	}

	@Override
	@RequiresApi(24)
	public void setValue(T item, boolean animate) {
		Integer index = getItemIndex(item);
		setProgress(index, animate);
	}

	@Override
	public void setPreferencesAndLoad(@Nullable SharedPreferences sharedPreferences, @Nullable String preferenceString, T defaultValue) {

	}

	@Override
	public void updatePreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull T value) {

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

	private int toSliderProgress(T item) {
		Integer itemIndex = getItemIndex(item);
		if(itemIndex == null)
			throw new InvalidParameterException("Item not found in item list");
		else
			return itemIndex;
	}
}
