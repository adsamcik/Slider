package com.adsamcik.slider;

class EMath {
	/**
	 * Returns number of decimal places in float.
	 * Whole numbers can return 1 instead of 0, however there was no reason to change it for current use case.
	 *
	 * @param value Number
	 * @return Number of decimal places
	 */
	static int decimalPlaces(float value) {
		String text = Float.toString(value);
		return text.length() - text.indexOf('.') - 1;
	}

	/**
	 * Round float numbers to number of decimal places.
	 *
	 * @param value     Number
	 * @param precision Decimal places
	 * @return Rounded number
	 */
	static float round(float value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return Math.round(value * scale) / ((float) scale);
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
	static int step(final int value, final int direction, final int step) {
		int target = value + direction;

		if (target % step != 0 && sign(direction) == sign(target))
			target = target / step + sign(direction);
		else
			target /= step;
		return target * step;
	}

	/**
	 * Rounds value to step.
	 *
	 * @param value Value
	 * @param step  Step value
	 * @return Value after rounding
	 */
	static int step(final int value, final int step) {
		int left = value % step;
		int roundDown = value - left;
		return Math.abs(left) >= Math.abs(step) * 0.5f ? roundDown + step : roundDown;
	}

	/**
	 * Returns sign for integer numbers
	 *
	 * @param value Value
	 * @return Signum of value
	 */
	static int sign(int value) {
		if (value > 0)
			return 1;
		else if (value < 0)
			return -1;
		else
			return 0;
	}
}
