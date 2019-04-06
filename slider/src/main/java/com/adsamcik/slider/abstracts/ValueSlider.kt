package com.adsamcik.slider.abstracts

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.RequiresApi

abstract class ValueSlider<T> : Slider<T> {
	protected var mItems: Array<T>? = null

	override var value: T
		get() {
			val items = mItems
			return if (items == null)
				throw RuntimeException("You must first set items before requesting value")
			else
				items[progress]
		}
		set(item) {
			progress = getValueIndex(item)
		}

	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	init {
		max = 0
	}

	/**
	 * Sets items.
	 * This method overrides any previous min, max and step settings
	 *
	 * @param items Items
	 */
	fun setItems(items: Array<T>) {
		if (items.size < 2)
			throw RuntimeException("Value slider requires 2 or more values")

		progress = progress.coerceIn(0, items.size)
		max = items.size - 1
		this.mItems = items
	}

	/**
	 * Removes items
	 */
	fun removeItems() {
		this.mItems = null
	}


	@RequiresApi(24)
	override fun setValue(value: T, animate: Boolean) {
		val index = getValueIndex(value)
		setProgress(index, animate)
	}

	@Synchronized
	override fun setProgress(progress: Int) {
		setProgressValueCheck(progress)
		super.setProgress(progress)
	}

	override fun setProgress(progress: Int, animate: Boolean) {
		setProgressValueCheck(progress)
		super.setProgress(progress, animate)
	}

	private fun setProgressValueCheck(progress: Int) {
		if (progress < 0 || progress > max)
			throw IllegalArgumentException("Progress must be larger than 0 and not larger than $max. Was $progress")
	}

	protected fun getValueIndex(item: T): Int = mItems?.indexOf(item) ?: -1
}
