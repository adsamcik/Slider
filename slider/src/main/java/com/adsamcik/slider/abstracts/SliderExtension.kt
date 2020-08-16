package com.adsamcik.slider.abstracts

/**
 * Interface defining extensions that extend [Slider] functionality.
 */
interface SliderExtension<N> {
	/**
	 * On slider value changed.
	 *
	 * @param slider Slider instance
	 * @param value New value
	 * @param position New position (in percent)
	 * @param isFromUser True, if new position was set by user
	 *
	 */
	fun onValueChanged(slider: Slider<N>, value: N, position: Float, isFromUser: Boolean) = Unit

	/**
	 * On slider start tracking touch.
	 *
	 * @param slider Slider instance
	 */
	fun onStartTrackingTouch(slider: Slider<N>) = Unit

	/**
	 * On slider stop tracking touch.
	 *
	 * @param slider Slider instance
	 */
	fun onEndTrackingTouch(slider: Slider<N>) = Unit

	/**
	 * Called when attached to slider.
	 */
	fun onAttach(slider: Slider<N>) = Unit
}
