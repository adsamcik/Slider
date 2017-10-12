package com.adsamcik.slider.ScaleFunctions;

import com.adsamcik.slider.Slider;

public class LinearScale implements Slider.IScale {
	@Override
	public float scale(float progress, float min, float max) {
		return min + progress * (max - min);
	}
}
