package com.adsamcik.slider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EAssert {
	interface ICallback {
		void run();
	}

	/**
	 * Asserts that given methods throws exception.
	 *
	 * @param callback       callback
	 * @param exceptionClass expected class of the exception
	 */
	static void assertException(ICallback callback, Class exceptionClass) {
		try {
			callback.run();
			throw new AssertionError("Exception of type " + exceptionClass.getName() + " expected.");
		} catch (Exception e) {
			//Tests for assertion exceptions
			assertNotEquals(AssertionError.class, e.getClass());
			assertEquals(exceptionClass, e.getClass());
		}
	}
}
