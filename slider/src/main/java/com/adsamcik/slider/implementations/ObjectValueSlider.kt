package com.adsamcik.slider.implementations

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import com.adsamcik.slider.Stringify
import com.adsamcik.slider.abstracts.ValueSlider

class ObjectValueSlider<T> : ValueSlider<T> {
    private var mPreferenceToString: Stringify<T>? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun loadProgress(sharedPreferences: SharedPreferences, preferenceString: String, defaultValue: T) {
        val defaultString = mPreferenceToString?.invoke(defaultValue) ?: defaultValue.toString()
        val loadedValue = sharedPreferences.getString(preferenceString, defaultString)
        var index = getItemIndex(loadedValue!!)
        if (index >= 0)
            super.setProgress(index)
        else {
            if (loadedValue != defaultString) {
                index = getItemIndex(defaultString)
                if (index >= 0)
                    super.setProgress(index)
                else
                    throw RuntimeException("Neither loaded value ($loadedValue) nor default value ($defaultString) were found")
            }

            throw RuntimeException("Default value $defaultString was not found")
        }
    }

    /**
     * Set slider's preferences for automatic saving inside passed instance of [SharedPreferences].
     * Objects are saved as strings using passed function [Stringify]
     *
     * @param sharedPreferences Instance of shared preferences
     * @param preferenceString  String name of desired preference
     * @param defaultValue      Default value if no value is saved in shared preferences
     * @param itemsToString     function to convert object to string so they can be saved to shared preferences
     */
    fun setPreferences(sharedPreferences: SharedPreferences, preferenceString: String, defaultValue: T, itemsToString: Stringify<T>) {
        this.mPreferenceToString = itemsToString
        super.setPreferences(sharedPreferences, preferenceString, defaultValue)
        loadProgress(sharedPreferences, preferenceString, defaultValue)
    }

    override fun removePreferences() {
        super.removePreferences()
        this.mPreferenceToString = null
    }

    public override fun updatePreferences(sharedPreferences: SharedPreferences, preferenceString: String, value: T) {
        sharedPreferences.edit().putString(preferenceString, value.toString()).apply()
    }


    protected fun getItemIndex(item: String): Int {
        if (mItems != null) {
            val items = mItems!!
            for (i in items.indices) {
                val sItem = mPreferenceToString?.invoke(items[i]) ?: items[i].toString()
                if (item == sItem)
                    return i
            }
        }
        return -1
    }
}
