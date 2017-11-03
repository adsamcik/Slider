package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FloatSliderInstrumentationTest {
	private final float DELTA = 0.00001f;

	@Test
	public void baseTest() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.adsamcik.slider.test", appContext.getPackageName());

		final float MAX = 9;
		final float MIN = 1;
		final int PLACES = 1;
		final int POW = (int) (100 * Math.pow(10, PLACES));
		final float MAX_SLIDER = (MAX - MIN) * POW;

		FloatSlider slider = new FloatSlider(appContext);
		slider.setMinValue(MIN);
		slider.setMaxValue(MAX);
		slider.setStep(2f);
		slider.setValue(5f);
		assertEquals(MIN, slider.getMinValue(), DELTA);
		assertEquals(MAX, slider.getMaxValue(), DELTA);
		assertEquals(MAX_SLIDER, slider.getMax(), DELTA);
		assertEquals(4 * POW, slider.getProgress(), DELTA);
		assertEquals(5, slider.getValue(), DELTA);
	}

	@Test
	public void moveTest() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.adsamcik.slider.test", appContext.getPackageName());

		FloatSlider slider = new FloatSlider(appContext);
		slider.setMinValue(0f);
		slider.setMaxValue(12f);
		slider.setStep(3f);
		for (int i = 0; i < 12; i++) {
			slider.setValue((float) i);
			assertEquals(Math.round(i / 3.0) * 3, slider.getValue(), DELTA);
		}
	}

	@Test
	public void negativeNumberTest() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.adsamcik.slider.test", appContext.getPackageName());

		FloatSlider slider = new FloatSlider(appContext);
		slider.setMinValue(-15f);
		slider.setMaxValue(15f);
		slider.setStep(3f);
		for (int i = -15; i <= 15; i++) {
			slider.setValue((float) i);
			assertEquals(Math.round(i / 3f) * 3f, slider.getValue(), DELTA);
		}
	}

	@Test
	public void floatNumberTest() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.adsamcik.slider.test", appContext.getPackageName());

		FloatSlider slider = new FloatSlider(appContext);
		slider.setMinValue(-1.5f);
		slider.setMaxValue(1.5f);
		slider.setStep(0.3f);
		for (float i = -1.5f; i <= 1.5f; i += 0.3f) {
			slider.setValue(i);
			assertEquals(Math.round(i / 0.3f) * 0.3f, slider.getValue(), DELTA);
		}
	}

	@Test
	public void valueTest() throws Exception {
		Context appContext = InstrumentationRegistry.getTargetContext();

		Slider<Float> slider = new FloatSlider(appContext);
		slider.setMinValue(-15f);
		slider.setMaxValue(15f);
		slider.setStep(3f);
		Float[] floats = new Float[]{0f, 3f, 4f, 20f, 35f};
		slider.setItems(floats);
		for (int i = 0; i < floats.length; i++) {
			slider.setValue(floats[i]);
			assertEquals(floats[i], slider.getValue());
		}

		for (int i = 0; i < floats.length; i++) {
			slider.setProgress(i);
			assertEquals(floats[i], slider.getValue());
		}

		EAssert.assertException(() -> slider.setMinValue(-15f), RuntimeException.class);
		EAssert.assertException(() -> slider.setMaxValue(15f), RuntimeException.class);
		EAssert.assertException(() -> slider.setStep(3f), RuntimeException.class);
	}

	@Test
	public void sharedPreferences() throws Exception {
		final Context appContext = InstrumentationRegistry.getTargetContext();
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
		final String prefName = "TESTING PREFERENCE";

		FloatSlider slider = new FloatSlider(appContext);
		slider.setMinValue(-1.5f);
		slider.setMaxValue(1.5f);
		slider.setStep(0.3f);
		slider.setPreferences(preferences, prefName);
		slider.setValue(0.1f);

		Assert.assertEquals(slider.getValue(), preferences.getFloat(prefName, Float.MIN_VALUE));

		preferences.edit().remove(prefName).apply();
	}
}
