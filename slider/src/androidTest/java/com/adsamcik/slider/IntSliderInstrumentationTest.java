package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

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
public class IntSliderInstrumentationTest {
	private Context appContext = InstrumentationRegistry.getTargetContext();

	@Test
	public void baseTest() throws Exception {
		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(1);
		slider.setMaxValue(9);
		slider.setStep(2);
		slider.setProgressValue(5);
		slider.setTextView(null, null);
		assertEquals(1, (long) slider.getMinValue());
		assertEquals(9, (long) slider.getMaxValue());
		assertEquals(8, (long) slider.getMax());
		assertEquals(4, (long) slider.getProgress());
		assertEquals(5, (long) slider.getValue());
	}

	@Test
	public void textViewTest() throws Exception {
		IntSlider slider = new IntSlider(appContext);
		TextView textView = new TextView(appContext);

		slider.setTextView(textView, value -> "Test " + value);
		slider.setMinValue(0);
		slider.setMaxValue(10);
		slider.setProgressValue(5);

		assertEquals("Test 5", textView.getText());

		slider.setTextView(null, value -> "Test " + value);
		slider.setProgressValue(4);
		assertEquals("Test 5", textView.getText());
		assertEquals(4, (long) slider.getValue());

		slider.setTextView(textView, value -> "Test " + value);
		slider.setTextView(textView, null);
		slider.setProgressValue(6);
		assertEquals("Test 5", textView.getText());
		assertEquals(6, (long) slider.getValue());

		slider.setTextView(null, null);
		slider.setProgressValue(8);
		assertEquals("Test 5", textView.getText());
		assertEquals(8, (long) slider.getValue());
	}

	@Test
	public void progressTest() throws Exception {
		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(0);
		slider.setMaxValue(10);
		slider.setProgressValue(5);

		assertEquals(5, slider.getProgress());

		slider.setProgressValue(8, false);

		assertEquals(8, slider.getProgress());

		slider.setProgressValue(3, true);

		assertEquals(3, slider.getProgress());
	}

	@Test
	public void moveTest() throws Exception {
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
		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(-15);
		slider.setMaxValue(15);
		slider.setStep(3);
		for (int i = -15; i <= 15; i++) {
			slider.setProgressValue(i);
			assertEquals(Math.round(i / 3.0) * 3, (long) slider.getValue());
		}
	}

	@Test
	public void valueTest() throws Exception {
		Context appContext = InstrumentationRegistry.getTargetContext();

		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(-15);
		slider.setMaxValue(15);
		slider.setStep(3);
		Integer[] integers = new Integer[]{0, 3, 4, 20, 35};
		slider.setItems(integers);
		for (int i = 0; i < integers.length; i++) {
			slider.setProgressValue(integers[i]);
			assertEquals((long) integers[i], (long) slider.getValue());
		}

		for (int i = 0; i < integers.length; i++) {
			slider.setProgress(i);
			assertEquals((long) integers[i], (long) slider.getValue());
		}

		EAssert.assertException(() -> slider.setMinValue(-15), RuntimeException.class);
		EAssert.assertException(() -> slider.setMaxValue(15), RuntimeException.class);
		EAssert.assertException(() -> slider.setStep(3), RuntimeException.class);
	}

	@Test
	public void sharedPreferences() throws Exception {
		final Context appContext = InstrumentationRegistry.getTargetContext();
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
		final String prefName = "TESTING PREFERENCE";

		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(-5);
		slider.setMaxValue(5);
		slider.setStep(2);
		slider.setPreferences(preferences, prefName);
		slider.setProgressValue(4);

		Assert.assertEquals((long) slider.getValue(), (long) preferences.getInt(prefName, Integer.MIN_VALUE));

		preferences.edit().remove(prefName).apply();
	}
}
