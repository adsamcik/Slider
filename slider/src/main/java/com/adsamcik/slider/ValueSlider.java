package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.security.InvalidParameterException;
import java.util.Arrays;

public class ValueSlider<T> extends Slider<T> {
	private T[] mItems = null;
	private IStringify<T> mPreferenceToString = null;

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

	/**
	 * Set slider's preferences for automatic saving inside passed instance of {@link SharedPreferences}.
	 * Objects are saved as strings using passed function {@link IStringify}
	 *
	 * @param sharedPreferences Instance of shared preferences
	 * @param preferenceString  String name of desired preference
	 * @param defaultValue      Default value if no value is saved in shared preferences
	 * @param itemsToString     function to convert object to string so they can be saved to shared preferences
	 */
	public void setPreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull T defaultValue, @NonNull IStringify<T> itemsToString) {
		this.mPreferenceToString = itemsToString;
		super.setPreferences(sharedPreferences, preferenceString, defaultValue);
		loadPreferences(sharedPreferences, preferenceString, defaultValue);
	}

	@Override
	public void removePreferences() {
		super.removePreferences();
		this.mPreferenceToString = null;
	}

	@Override
	public void setValue(T item) {
		setProgress(getItemIndex(item));
	}

	@Override
	@RequiresApi(24)
	public void setValue(T item, boolean animate) {
		Integer index = getItemIndex(item);
		setProgress(index, animate);
	}

	@Override
	public void loadPreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull T defaultValue) {
		sharedPreferences.getString(preferenceString, mPreferenceToString == null ? defaultValue.toString() : mPreferenceToString.toString(defaultValue));
	}

	@Override
	public void updatePreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull T value) {
		sharedPreferences.edit().putString(preferenceString, value.toString()).apply();
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
	public void setItems(@NonNull T[] items) {
		if (items.length < 2)
			throw new RuntimeException("Value slider requires 2 or more values");

		setProgress(0);
		setMax(items.length - 1);
		this.mItems = items;
	}

	public void clearItems() {
		this.mItems = null;
	}

	/**
	 * Returns items
	 *
	 * @return Returns items or null if no items were set
	 */
	public T[] getItems() {
		return mItems;
	}

	@Override
	public synchronized void setProgress(int progress) {
		setProgressValueCheck(progress);
		super.setProgress(progress);
	}

	@Override
	public void setProgress(int progress, boolean animate) {
		setProgressValueCheck(progress);
		super.setProgress(progress, animate);
	}

	private void setProgressValueCheck(int progress) {
		if (progress < 0 || progress > getMax())
			throw new RuntimeException("Progress must be larger than 0 and not larger than " + getMax() + ". Was " + progress);
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
		if (itemIndex == null)
			throw new InvalidParameterException("Item not found in item list");
		else
			return itemIndex;
	}
}
