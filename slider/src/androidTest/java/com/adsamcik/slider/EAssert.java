package com.adsamcik.slider;

import static org.junit.Assert.assertEquals;

public class EAssert {
	interface ICallback {
		void run();
	}

	static void assertException(ICallback callback, Class exceptionClass) {
		try {
			callback.run();
			throw new Exception("Exception of type " + exceptionClass.getName() + " expected.");
		} catch (Exception e) {
			assertEquals(exceptionClass, e.getClass());
		}
	}
}
