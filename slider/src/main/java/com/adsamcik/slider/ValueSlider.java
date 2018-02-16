package com.adsamcik.slider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.util.Arrays;

public abstract class ValueSlider<T> extends Slider<T> {
	protected T[] mItems = null;

	public ValueSlider(Context context) {
		super(context);
		setMax(0);
	}

	public ValueSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		setMax(0);
	}

	public ValueSlider(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setMax(0);
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

	protected int getItemIndex(@NonNull T item) {
		if (mItems != null)
			return Arrays.asList(mItems).indexOf(item);
		return -1;
	}
}
