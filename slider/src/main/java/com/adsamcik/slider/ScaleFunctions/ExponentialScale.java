package com.adsamcik.slider.ScaleFunctions;

import com.adsamcik.slider.Slider;

public class ExponentialScale implements Slider.IScale {
	@Override
	public float scale(float value, float min, float max) {
		return min + (value * value) * (max - min);
	}
}
