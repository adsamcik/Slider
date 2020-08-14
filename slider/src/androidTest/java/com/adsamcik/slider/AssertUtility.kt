package com.adsamcik.slider

import org.junit.Assert.assertEquals

typealias Method = () -> Unit

internal object AssertUtility {
	/**
	 * Asserts that given methods throws exception.
	 *
	 * @param callback       callback
	 * @param exceptionClass expected class of the exception
	 */
	internal fun assertException(callback: Method, exceptionClass: Class<*>) {
		try {
			callback.invoke()
			throw AssertionError("Exception of type " + exceptionClass.name + " expected.")
		} catch (e: Exception) {
			if (e is AssertionError || exceptionClass != e.javaClass) {
				throw e
			}
		}

	}
}
