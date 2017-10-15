package com.adsamcik.slider.ScaleFunctions;

import com.adsamcik.slider.Slider;

public class LinearScale {
	private LinearScale() {}

	public static Slider.IScale<Float> getFloatScale() {
		return (int progress, int maxProgress, Float min, Float max) -> min + (progress / maxProgress) * (max - min);
	}

	public static Slider.IScale<Integer> getIntegerScale() {
		return (int progress, int maxProgress, Integer min, Integer max) -> progress + min;
	}
}
