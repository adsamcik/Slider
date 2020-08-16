package com.adsamcik.slider.abstracts

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.widget.TextView
import com.adsamcik.slider.Stringify

/**
 * Base Slider class
 */
abstract class Slider<T> : FluidSlider {
	@Suppress("PRIVATE")
	protected var mTextView: TextView? = null

	private var mStringify: Stringify<T>? = null

	protected val stringify: Stringify<T>
		get() = mStringify ?: { it.toString() }

	private val extensions: MutableList<SliderExtension<T>> = mutableListOf()

	/**
	 * Get slider's current value after scaling.
	 *
	 * @return Current value
	 */
	/**
	 * Set slider's current progress value.
	 * This value needs to be between Min value and Max value not min and max from SeekBar.
	 *
	 */
	abstract var value: T

	private var lastValue: T? = null

	constructor(context: Context) : super(context, null)
	constructor(context: Context, attrs: AttributeSet) : super(
			context,
			attrs,
			android.R.attr.seekBarStyle
	)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
			context,
			attrs,
			defStyleAttr
	)


	/**
	 * Set sliders String function. This TextView will be automatically
	 * updated when slider's progress changes and formatted using String function.
	 *
	 * @param stringify String function
	 */
	fun setStringify(stringify: Stringify<T>?) {
		mStringify = stringify
		invalidateText()
	}

	/**
	 * Adds new extension.
	 */
	fun addExtension(extension: SliderExtension<T>) {
		extensions.add(extension)
		extension.onAttach(this)
	}

	/**
	 * Removes existing extension.
	 *
	 * @return True if extension was found and removed.
	 */
	fun removeExtension(extension: SliderExtension<T>): Boolean {
		return extensions.remove(extension)
	}

	override fun onStartTrackingTouch() {
		extensions.forEach { it.onStartTrackingTouch(this) }
	}

	override fun onEndTrackingTouch() {
		extensions.forEach { it.onEndTrackingTouch(this) }
	}

	override fun onPositionChanged(position: Float, isFromUser: Boolean) {
		bubbleText = stringify.invoke(value)
		val newValue = value

		if (lastValue != newValue) {
			lastValue = newValue
			extensions.forEach { it.onValueChanged(this, value, position, isFromUser) }
		}
	}

	protected fun invalidateText() {
		onTextInvalidated()
	}

	protected abstract fun onTextInvalidated()
}
