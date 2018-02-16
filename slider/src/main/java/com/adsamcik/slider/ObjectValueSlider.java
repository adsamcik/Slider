package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

public class ObjectValueSlider<T> extends ValueSlider<T> {
	private IStringify<T> mPreferenceToString = null;

	public ObjectValueSlider(Context context) {
		super(context);
	}

	public ObjectValueSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ObjectValueSlider(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void loadProgress(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull T defaultValue) {
		String defaultString = mPreferenceToString == null ? defaultValue.toString() : mPreferenceToString.toString(defaultValue);
		String loadedValue = sharedPreferences.getString(preferenceString, defaultString);
		int index = getItemIndex(loadedValue);
		if (index >= 0)
			super.setProgress(index);
		else {
			if (!loadedValue.equals(defaultString)) {
				index = getItemIndex(defaultString);
				if (index >= 0)
					super.setProgress(index);
				else
					throw new RuntimeException("Neither loaded value (" + loadedValue + ") nor default value (" + defaultString + ") were found");
			}

			throw new RuntimeException("Default value " + defaultString + " was not found");
		}
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
		loadProgress(sharedPreferences, preferenceString, defaultValue);
	}

	@Override
	public void removePreferences() {
		super.removePreferences();
		this.mPreferenceToString = null;
	}

	@Override
	public void updatePreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull T value) {
		sharedPreferences.edit().putString(preferenceString, value.toString()).apply();
	}


	protected int getItemIndex(@NonNull String item) {
		if (mItems != null) {
			for (int i = 0; i < mItems.length; i++) {
				String sItem = mPreferenceToString != null ? mPreferenceToString.toString(mItems[i]) : mItems[i].toString();
				if (item.equals(sItem))
					return i;
			}
		}
		return -1;
	}
}
