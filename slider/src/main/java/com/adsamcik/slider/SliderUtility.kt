package com.adsamcik.slider

import kotlin.math.abs
import kotlin.math.pow

internal object SliderUtility {
	/**
	 * Returns number of decimal places in float.
	 * Whole numbers can return 1 instead of 0, however there was no reason to change it for current use case.
	 *
	 * @param value Number
	 * @return Number of decimal places
	 */
	fun decimalPlaces(value: Float): Int {
		val text = value.toString()
		return text.length - text.indexOf('.') - 1
	}

	/**
	 * Round [Float] to number of decimal places.
	 *
	 * @param value     Number
	 * @param precision Decimal places
	 * @return Rounded number
	 */
	fun round(value: Float, precision: Int): Float {
		return SliderUtility.round(value.toDouble(), precision).toFloat()
	}

	/**
	 * Round [Double] to number of decimal places.
	 *
	 * @param value     Number
	 * @param precision Decimal places
	 * @return Rounded number
	 */
	fun round(value: Double, precision: Int): Double {
		val scale = 10.0.pow(precision.toDouble())
		return kotlin.math.round(value * scale) / scale
	}

	/**
	 * Rounds value to step in desired direction.
	 * This direction is than added to target value.
	 *
	 * @param value     Value
	 * @param direction Direction
	 * @param step      Step value
	 * @return Value after rounding
	 */
	fun step(value: Int, direction: Int, step: Int): Int {
		var target = value + direction

		if (target % step != 0 && sign(direction) == sign(target))
			target = target / step + sign(direction)
		else
			target /= step
		return target * step
	}

	/**
	 * Rounds value to step.
	 *
	 * @param value Value
	 * @param step  Step value
	 * @return Value after rounding
	 */
	fun step(value: Int, step: Int): Int {
		val left = value % step
		val roundDown = value - left
		return if (abs(left) >= abs(step) / 2f) roundDown + step else roundDown
	}

	private fun sign(value: Int): Int {
		return when {
			value > 0 -> 1
			value < 0 -> -1
			else -> 0
		}
	}
}
