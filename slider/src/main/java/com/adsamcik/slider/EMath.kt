package com.adsamcik.slider

internal object EMath {
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
     * Round float numbers to number of decimal places.
     *
     * @param value     Number
     * @param precision Decimal places
     * @return Rounded number
     */
    fun round(value: Float, precision: Int): Float {
        val scale = Math.pow(10.0, precision.toDouble()).toInt()
        return Math.round(value * scale) / scale.toFloat()
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
        return if (Math.abs(left) >= Math.abs(step) / 2f) roundDown + step else roundDown
    }

    /**
     * Returns sign for integer numbers
     *
     * @param value Value
     * @return Signum of value
     */
    private fun sign(value: Int): Int {
        return when {
            value > 0 -> 1
            value < 0 -> -1
            else -> 0
        }
    }
}
