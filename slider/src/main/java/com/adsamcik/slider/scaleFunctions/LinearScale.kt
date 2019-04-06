package com.adsamcik.slider.scaleFunctions

import com.adsamcik.slider.Scale

object LinearScale {

	val floatScale: Scale<Float>
		get() = { progress, maxProgress, min, max -> min + progress / maxProgress.toFloat() * (max - min) }

	val integerScale: Scale<Int>
		get() = { progress, _, min, _ -> progress + min }
}
