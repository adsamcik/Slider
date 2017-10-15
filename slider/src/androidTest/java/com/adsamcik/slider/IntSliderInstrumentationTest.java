package com.adsamcik.slider;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class IntSliderInstrumentationTest {
	@Test
	public void baseTest() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.adsamcik.slider.test", appContext.getPackageName());

		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(1);
		slider.setMaxValue(9);
		slider.setStep(2);
		slider.setProgressValue(5);
		assertEquals(1, (long) slider.getMinValue());
		assertEquals(9, (long) slider.getMaxValue());
		assertEquals(8, (long) slider.getMax());
		assertEquals(4, (long) slider.getProgress());
		assertEquals(5, (long) slider.getValue());
	}

	@Test
	public void moveTest() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.adsamcik.slider.test", appContext.getPackageName());

		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(0);
		slider.setMaxValue(12);
		slider.setStep(3);
		for (int i = 0; i < 12; i++) {
			slider.setProgressValue(i);
			assertEquals(Math.round(i / 3.0) * 3, (long) slider.getValue());
		}
	}

	@Test
	public void negativeNumberTest() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.adsamcik.slider.test", appContext.getPackageName());

		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(-15);
		slider.setMaxValue(15);
		slider.setStep(3);
		for (int i = -15; i <= 15; i++) {
			slider.setProgressValue(i);
			assertEquals(Math.round(i / 3.0) * 3, (long) slider.getValue());
		}
	}
}
