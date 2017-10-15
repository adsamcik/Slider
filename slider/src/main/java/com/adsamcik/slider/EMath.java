package com.adsamcik.slider;

class EMath {
	static int decimalPlaces(float value) {
		String text = Float.toString(value);
		int integerPlaces = text.indexOf('.');
		return text.length() - integerPlaces - 1;
	}

	static float round(float value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return Math.round(value * scale) / ((float) scale);
	}

	static float limit(final float value, final float min, final float max) {
		if (value < min)
			return min;
		else if (value > max)
			return max;
		else
			return value;
	}

	static float limitMax(final float value, final float max) {
		return value > max ? max : value;
	}

	static int step(final int value, final int direction, final int step) {
		int target = value + direction;

		if (target % step != 0 && sign(direction) == sign(target))
			target = target / step + sign(direction);
		else
			target /= step;
		return target * step;
	}

	static int step(final int value, final int step) {
		int left = value % step;
		int roundDown = value - left;
		return Math.abs(left) >= Math.abs(step) * 0.5 ? roundDown + step : roundDown;
	}

	static int sign(int value) {
		if (value > 0)
			return 1;
		else if (value < 0)
			return -1;
		else
			return 0;
	}

	static int sign(float value) {
		if (value > 0)
			return 1;
		else if (value < 0)
			return -1;
		else
			return 0;
	}
}
