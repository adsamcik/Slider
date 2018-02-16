package com.adsamcik.slider.sliders

import android.content.Context
import android.support.annotation.RequiresApi
import android.util.AttributeSet


abstract class ValueSlider<T> : Slider<T> {
    protected var mItems: Array<T>? = null

    override var value: T
        get() {
            return if (mItems == null)
                throw RuntimeException("You must first set items before requesting value")
            else
                mItems!![progress]
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

        progress = 0
        max = items.size - 1
        this.mItems = items
    }

    /**
     * Removes items
     */
    fun clearItems() {
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
            throw RuntimeException("Progress must be larger than 0 and not larger than $max. Was $progress")
    }

    protected fun getValueIndex(item: T): Int = mItems?.indexOf(item) ?: -1
}
