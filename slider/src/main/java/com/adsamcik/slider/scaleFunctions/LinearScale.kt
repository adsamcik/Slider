package com.adsamcik.slider.scaleFunctions

import com.adsamcik.slider.Scale
import kotlin.math.roundToInt

internal object LinearScale {

	val floatScale: Scale<Float>
		get() = { position, min, max -> min + position * (max - min) }

	val integerScale: Scale<Int>
		get() = { position, min, max -> (position * (max - min)).roundToInt() + min }
}
