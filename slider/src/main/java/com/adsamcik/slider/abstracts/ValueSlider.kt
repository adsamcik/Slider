package com.adsamcik.slider.abstracts

import android.content.Context
import android.util.AttributeSet
import kotlin.math.round

/**
 * Abstract implementation of [Slider] that allows the use of custom value lists
 */
abstract class ValueSlider<T> : Slider<T> {
	protected var mItems: Array<T>? = null

	/**
	 * Number of items in value slider
	 */
	val size: Int get() = mItems?.size ?: 0

	/**
	 * Selected index
	 */
	var index: Int
		get() {
			val size = size
			if(size == 0) return -1

			return round(fluidPosition / fluidStep).toInt()
		}
		set(value) {
			fluidPosition = value * fluidStep
		}

	override var value: T
		get() {
			val items = mItems
					?: throw NullPointerException("You must first set items before requesting value")
			return items[index]
		}
		set(item) {
			val index = getValueIndex(item)
			fluidPosition = index * fluidStep
			this.index = index
		}

	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
			context,
			attrs,
			defStyleAttr
	)

	/**
	 * Sets items.
	 * This method overrides any previous min, max and step settings
	 *
	 * @param items Items
	 */
	fun setItems(items: Array<T>) {
		require(items.size >= 2) { "Value slider requires 2 or more values" }

		this.mItems = items

		fluidStep = 1f / (items.size - 1).toFloat()

		invalidateText()
	}

	override fun onTextInvalidated() {
		val items = mItems
		if (items != null) {
			endText = labelFormatter.invoke(items.last())
			startText = labelFormatter.invoke(items.first())
			bubbleText = labelFormatter.invoke(value)
		}
	}

	/**
	 * Removes items
	 */
	fun removeItems() {
		this.mItems = null
	}


	protected fun getValueIndex(item: T): Int = mItems?.indexOf(item) ?: -1
}
