package com.adsamcik.slider

/**
 * Convert desired number to string
 *
 * @param value number
 * @return desired output
 */
typealias LabelFormatter<N> = (value: N) -> String

/**
 * Scale SeekBar progress to desired value.
 *
 * @param position    SeekBar progress
 * @param min         desired minimum
 * @param max         desired maximum
 * @return scaled value
 */
typealias Scale<N> = (position: Float, min: N, max: N) -> N
