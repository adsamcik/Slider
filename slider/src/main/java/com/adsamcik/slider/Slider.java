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

import java.util.Arrays;

import static com.adsamcik.slider.EMath.step;

public abstract class Slider<N extends Number> extends SeekBar implements SeekBar.OnSeekBarChangeListener {
	private int mSliderStep = 1;

	protected IScale<N> mScale = null;
	protected TextView mTextView = null;
	protected IStringify<N> mStringify = null;

	protected SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = null;
	protected OnValueChangeListener<N> mOnValueChangeListener = null;

	protected N[] mItems = null;

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
	 * Round desired value to step. Primarily used for SeekBar progress.
	 *
	 * @param value Value
	 * @return Value after rounding to nearest step
	 */
	private int roundToStep(int value) {
		return step(value, mSliderStep);
	}

	/**
	 * Set slider's scale.
	 *
	 * @param scale Scale
	 */
	public void setScale(IScale<N> scale) {
		mScale = scale;
		updateText();
	}

	/**
	 * Set slider's TextView and String function. This TextView will be automatically updated when slider's progress changes and formatted using String function.
	 *
	 * @param textView  TextView
	 * @param stringify String function
	 */
	public void setTextView(TextView textView, IStringify<N> stringify) {
		mTextView = textView;
		mStringify = stringify;
		updateText();
	}

	/**
	 * Set slider's step value.
	 *
	 * @param step Step value
	 */
	public abstract void setStep(N step);

	/**
	 * Set slider's current progress value. This value needs to be between Min value and Max value not min and max from SeekBar.
	 *
	 * @param progress Desired progress
	 */
	public abstract void setProgressValue(N progress);

	/**
	 * Set slider's current progress value. This value needs to be between Min value and Max value not min and max from SeekBar.
	 *
	 * @param progress Desired progress
	 * @param animate  Animate
	 */
	@RequiresApi(24)
	public abstract void setProgressValue(N progress, boolean animate);


	/**
	 * Method used to set progress manually.
	 * This method should only when you want to set {@link #mItems} by index
	 * or by Slider implementation. Calling it in other cases might result in issues.
	 *
	 * @param progress progress
	 */
	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
	}

	/**
	 * Method used to set progress manually.
	 * This method should only when you want to set {@link #mItems} by index
	 * or by Slider implementation. Calling it in other cases might result in issues.
	 *
	 * @param progress progress
	 */
	@Override
	public synchronized void setProgress(int progress, boolean animate) {
		super.setProgress(progress, animate);
	}

	/**
	 * Set slider's min value.
	 *
	 * @param min Min value
	 */
	public abstract void setMinValue(N min);

	/**
	 * Set slider's max value.
	 *
	 * @param max Max value
	 */
	public abstract void setMaxValue(N max);

	/**
	 * Set slider's preferences for automatic saving inside passed instance of {@link SharedPreferences}.
	 * Saves only value not bounds or step.
	 *
	 * @param sharedPreferences Instance of shared preferences
	 * @param preferenceString  String name of desired preference
	 */
	public void setPreferences(@Nullable SharedPreferences sharedPreferences, @Nullable String preferenceString) {
		if (sharedPreferences == null || preferenceString == null) {
			this.mPreferences = null;
			this.mPreferenceString = null;
		} else {
			this.mPreferences = sharedPreferences;
			this.mPreferenceString = preferenceString;
		}
	}

	/**
	 * Set slider's preferences for automatic saving inside passed instance of {@link SharedPreferences} and loads currently saved values.
	 * Saves only value not bounds or step.
	 * It is important NOT to call this function before setting min, max and step.
	 *
	 * @param sharedPreferences Instance of shared preferences
	 * @param preferenceString  String name of desired preference
	 */
	public abstract void setPreferencesAndLoad(@Nullable SharedPreferences sharedPreferences, @Nullable String preferenceString, N defaultValue);

	/**
	 * Function called when updating preferences is need.
	 * Function is guranteed to be called only when SharedPreferences are not null
	 *
	 * @param sharedPreferences Shared Preferences instance
	 * @param preferenceString  Preference String
	 * @param value             Value
	 */
	public abstract void updatePreferences(@NonNull SharedPreferences sharedPreferences, @NonNull String preferenceString, @NonNull N value);

	/**
	 * Get slider's min value.
	 *
	 * @return Min value
	 */
	public abstract N getMinValue();

	/**
	 * Get slider's max value.
	 *
	 * @return Max value
	 */
	public abstract N getMaxValue();

	/**
	 * Get slider's step value.
	 *
	 * @return Step value
	 */
	public abstract N getStep();

	/**
	 * Get slider's current value after scaling.
	 *
	 * @return Current value
	 */
	public abstract N getValue();

	/**
	 * Returns current scale function
	 *
	 * @return Scale function
	 */
	public IScale getScale() {
		return mScale;
	}

	/**
	 * Returns step used by SeekBar
	 *
	 * @return Slider step
	 */
	public int getSliderStep() {
		return mSliderStep;
	}

	/**
	 * Returns items
	 *
	 * @return Returns items or null if no items were set
	 */
	public N[] getItems() {
		return mItems;
	}

	/**
	 * Sets step used by SeekBar
	 *
	 * @param sliderStep slider step
	 */
	protected void setSliderStep(int sliderStep) {
		if (sliderStep <= 0)
			throw new RuntimeException("Slider step must be larger than 0");

		this.mSliderStep = sliderStep;
	}

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
	public void setOnValueChangeListener(OnValueChangeListener<N> l) {
		mOnValueChangeListener = l;
	}

	/**
	 * Sets items.
	 * This method overrides any previous min, max and step settings
	 *
	 * @param items Items
	 */
	public abstract void setItems(@Nullable N[] items);

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		setProgress(roundToStep(getProgress()));
		if (fromUser)
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

	protected Integer getItemIndex(N item) {
		if (mItems != null) {
			int index = Arrays.asList(mItems).indexOf(item);
			if (index == -1)
				return null;
			else
				return index;
		}
		return null;
	}


	public interface OnValueChangeListener<N extends Number> {
		/**
		 * On value changed listener
		 *
		 * @param value    value
		 * @param fromUser whether it is from a user
		 */
		void onValueChanged(N value, boolean fromUser);
	}

	public interface IStringify<N extends Number> {
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
