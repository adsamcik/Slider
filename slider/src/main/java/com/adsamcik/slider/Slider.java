package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;

public abstract class Slider<T> extends SeekBar implements SeekBar.OnSeekBarChangeListener {
	protected TextView mTextView = null;
	protected IStringify<T> mStringify = null;

	protected SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = null;
	protected OnValueChangeListener<T> mOnValueChangeListener = null;

	private SharedPreferences mPreferences = null;
	private String mPreferenceString = null;

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

	/**
	 * Initialization function
	 */
	private void init() {
		super.setOnSeekBarChangeListener(this);
	}

	/**
	 * Set slider's TextView and String function. This TextView will be automatically updated when slider's progress changes and formatted using String function.
	 *
	 * @param textView  TextView
	 * @param stringify String function
	 */
	public void setTextView(@Nullable TextView textView, @Nullable IStringify<T> stringify) {
		if (textView == null || stringify == null) {
			mTextView = null;
			mStringify = null;
		} else {
			mTextView = textView;
			mStringify = stringify;
			updateText();
		}
	}

	/**
	 * Set slider's current progress value. This value needs to be between Min value and Max value not min and max from SeekBar.
	 *
	 * @param progress Desired progress
	 */
	public abstract void setValue(T progress);

	/**
	 * Set slider's current progress value. This value needs to be between Min value and Max value not min and max from SeekBar.
	 *
	 * @param progress Desired progress
	 * @param animate  Animate
	 */
	@RequiresApi(24)
	public abstract void setValue(T progress, boolean animate);

	/**
	 * Set slider's preferences for automatic saving inside passed instance of {@link SharedPreferences}.
	 * Objects are either saved as strings using toString() method or more efficient way if Slider implementation implemented it
	 *
	 * @param sharedPreferences Instance of shared preferences
	 * @param preferenceString  String name of desired preference
	 * @param defaultValue      Default value if no value is saved in shared preferences
	 */
	public void setPreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull T defaultValue) {
		this.mPreferences = sharedPreferences;
		this.mPreferenceString = preferenceString;
		loadProgress(sharedPreferences, preferenceString, defaultValue);
	}

	public void removePreferences() {
		this.mPreferences = null;
		this.mPreferenceString = null;
	}

	/**
	 * Set slider's preferences for automatic saving inside passed instance of {@link SharedPreferences} and loads currently saved values.
	 * Saves only value not bounds or step.
	 * It is important NOT to call this function before setting min, max and step.
	 *
	 * @param sharedPreferences Instance of shared preferences
	 * @param preferenceString  String name of desired preference
	 * @param defaultValue      Default value to load if there are none saved
	 */
	public abstract void loadProgress(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull T defaultValue);

	/**
	 * Function called when updating preferences is need.
	 * Function is guranteed to be called only when SharedPreferences are not null
	 *
	 * @param sharedPreferences Shared Preferences instance
	 * @param preferenceString  Preference String
	 * @param value             Value
	 */
	protected abstract void updatePreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull T value);

	/**
	 * Get slider's current value after scaling.
	 *
	 * @return Current value
	 */
	public abstract T getValue();

	/**
	 * Sets on seek bar changes listener
	 *
	 * @param l On SeekBar changes listener
	 */
	public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener l) {
		mOnSeekBarChangeListener = l;
	}

	/**
	 * Sets on value changed listener
	 *
	 * @param l On value changed listener
	 */
	public void setOnValueChangeListener(OnValueChangeListener<T> l) {
		mOnValueChangeListener = l;
	}


	@Override
	@CallSuper
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		updateText();

		if (mOnSeekBarChangeListener != null)
			mOnSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);

		if (mOnValueChangeListener != null)
			mOnValueChangeListener.onValueChanged(getValue(), fromUser);

		if (mPreferences != null)
			updatePreferences(mPreferences, mPreferenceString, getValue());
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
	}

	/**
	 * Updates TextView if it is not null using stringify function
	 */
	protected void updateText() {
		if (mTextView != null)
			mTextView.setText(mStringify.toString(getValue()));
	}


	public interface OnValueChangeListener<N> {
		/**
		 * On value changed listener
		 *
		 * @param value    value
		 * @param fromUser whether it is from a user
		 */
		void onValueChanged(N value, boolean fromUser);
	}

	public interface IStringify<N> {
		/**
		 * Convert desired number to string
		 *
		 * @param value number
		 * @return desired output
		 */
		String toString(N value);
	}

	public interface IScale<N extends Number> {
		/**
		 * Scale SeekBar progress to desired value.
		 *
		 * @param progress    SeekBar progress
		 * @param maxProgress SeekBar max
		 * @param min         desired minimum
		 * @param max         desired maximum
		 * @return scaled value
		 */
		N scale(int progress, int maxProgress, N min, N max);
	}
}
