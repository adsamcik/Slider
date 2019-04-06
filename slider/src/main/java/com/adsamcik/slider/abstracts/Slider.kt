package com.adsamcik.slider.abstracts

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatSeekBar
import com.adsamcik.slider.OnValueChange
import com.adsamcik.slider.Stringify

abstract class Slider<T> : AppCompatSeekBar, SeekBar.OnSeekBarChangeListener {
	protected var mTextView: TextView? = null
	protected var mStringify: Stringify<T>? = null

	protected var mOnSeekBarChangeListener: SeekBar.OnSeekBarChangeListener? = null
	protected var mOnValueChangeListener: OnValueChange<T>? = null

	private var mPreferences: SharedPreferences? = null
	private var mPreferenceString: String? = null

	/**
	 * Get slider's current value after scaling.
	 *
	 * @return Current value
	 */
	/**
	 * Set slider's current progress value. This value needs to be between Min value and Max value not min and max from SeekBar.
	 *
	 */
	abstract var value: T

	constructor(context: Context) : super(context, null)
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs, android.R.attr.seekBarStyle)
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	init {
		super.setOnSeekBarChangeListener(this)
	}

	/**
	 * Set slider's TextView and String function. This TextView will be automatically updated when slider's progress changes and formatted using String function.
	 *
	 * @param textView  TextView
	 * @param stringify String function
	 */
	fun setTextView(textView: TextView, stringify: Stringify<T>) {
		mTextView = textView
		mStringify = stringify
		updateText()
	}

	fun removeTextView() {
		mTextView = null
		mStringify = null
	}

	/**
	 * Set slider's current progress value. This value needs to be between Min value and Max value not min and max from SeekBar.
	 *
	 * @param value Desired progress
	 * @param animate  Animate
	 */
	@RequiresApi(24)
	abstract fun setValue(value: T, animate: Boolean)

	/**
	 * Set slider's preferences for automatic saving inside passed instance of [SharedPreferences].
	 * Objects are either saved as strings using toString() method or more efficient way if Slider implementation implemented it
	 *
	 * @param sharedPreferences Instance of shared preferences
	 * @param preferenceString  String name of desired preference
	 * @param defaultValue      Default value if no value is saved in shared preferences
	 */
	fun setPreferences(sharedPreferences: SharedPreferences, preferenceString: String, defaultValue: T) {
		this.mPreferences = sharedPreferences
		this.mPreferenceString = preferenceString
		loadProgress(sharedPreferences, preferenceString, defaultValue)
	}

	open fun removePreferences() {
		this.mPreferences = null
		this.mPreferenceString = null
	}

	/**
	 * Set slider's preferences for automatic saving inside passed instance of [SharedPreferences] and loads currently saved values.
	 * Saves only value not bounds or step.
	 * It is important NOT to call this function before setting min, max and step.
	 *
	 * @param sharedPreferences Instance of shared preferences
	 * @param preferenceString  String name of desired preference
	 * @param defaultValue      Default value to load if there are none saved
	 */
	abstract fun loadProgress(sharedPreferences: SharedPreferences, preferenceString: String, defaultValue: T)

	/**
	 * Function called when updating preferences is need.
	 * Function is guaranteed to be called only when SharedPreferences are not null
	 *
	 * @param sharedPreferences Shared Preferences instance
	 * @param preferenceString  Preference String
	 * @param value             Value
	 */
	protected abstract fun updatePreferences(sharedPreferences: SharedPreferences, preferenceString: String, value: T)

	/**
	 * Sets on seek bar changes listener
	 *
	 * @param l On SeekBar changes listener
	 */
	override fun setOnSeekBarChangeListener(l: SeekBar.OnSeekBarChangeListener?) {
		mOnSeekBarChangeListener = l
	}

	/**
	 * Sets on value changed listener
	 *
	 * @param l On value changed listener
	 */
	fun setOnValueChangeListener(l: OnValueChange<T>?) {
		mOnValueChangeListener = l
	}


	@CallSuper
	override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
		updateText()

		mOnSeekBarChangeListener?.onProgressChanged(seekBar, progress, fromUser)

		mOnValueChangeListener?.invoke(value, fromUser)

		if (mPreferences != null)
			updatePreferences(mPreferences!!, mPreferenceString!!, value)
	}

	override fun onStartTrackingTouch(seekBar: SeekBar) {
		mOnSeekBarChangeListener?.onStartTrackingTouch(seekBar)
	}

	override fun onStopTrackingTouch(seekBar: SeekBar) {
		updateText()
		mOnSeekBarChangeListener?.onStopTrackingTouch(seekBar)
	}

	/**
	 * Updates TextView if it is not null using stringify function
	 */
	protected fun updateText() {
		mTextView?.text = mStringify!!.invoke(value)
	}
}
