package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

public class IntValueSlider extends ValueSlider<Integer> {
	public IntValueSlider(Context context) {
		super(context);
	}

	public IntValueSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IntValueSlider(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public IntValueSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void loadProgress(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull Integer defaultValue) {
		super.setProgress(getItemIndex(sharedPreferences.getInt(preferenceString, defaultValue)));
	}

	@Override
	protected void updatePreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull Integer value) {
		sharedPreferences.edit().putInt(preferenceString, value).apply();
	}

}
