package com.adsamcik.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adsamcik.slider.ScaleFunctions.LinearScale;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

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
		slider.setValue(5);
		slider.setTextView(null, null);

		assertEquals(1, (long) slider.getMinValue());
		assertEquals(9, (long) slider.getMaxValue());
		assertEquals(8, (long) slider.getMax());
		assertEquals(4, (long) slider.getProgress());
		assertEquals(5, (long) slider.getValue());

		Slider.IScale<Integer> scale = LinearScale.getIntegerScale();
		slider.setScale(scale);
		assertEquals(scale, slider.getScale());
	}

	@Test
	public void textViewTest() throws Exception {
		IntSlider slider = new IntSlider(appContext);
		TextView textView = new TextView(appContext);

		slider.setTextView(textView, value -> "Test " + value);
		slider.setMinValue(0);
		slider.setMaxValue(10);
		slider.setValue(5);

		assertEquals("Test 5", textView.getText());

		slider.setTextView(null, value -> "Test " + value);
		slider.setValue(4);
		assertEquals("Test 5", textView.getText());
		assertEquals(4, (long) slider.getValue());

		slider.setTextView(textView, value -> "Test " + value);
		slider.setTextView(textView, null);
		slider.setValue(6);
		assertEquals("Test 4", textView.getText());
		assertEquals(6, (long) slider.getValue());

		slider.setTextView(null, null);
		slider.setValue(8);
		assertEquals("Test 4", textView.getText());
		assertEquals(8, (long) slider.getValue());
	}

	@Test
	public void progressTest() throws Exception {
		if (Looper.myLooper() == null)
			Looper.prepare();

		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(0);
		slider.setMaxValue(10);
		slider.setValue(5);

		assertEquals(5, slider.getProgress());

		if (Build.VERSION.SDK_INT >= 24)
			slider.setValue(8, false);
		else
			slider.setProgress(8);

		assertEquals(8, slider.getProgress());

		if (Build.VERSION.SDK_INT >= 24)
			slider.setValue(3, true);
		else
			slider.setProgress(3);

		assertEquals(3, slider.getProgress());
	}

	@Test
	public void moveTest() throws Exception {
		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(0);
		slider.setMaxValue(12);
		slider.setStep(3);
		for (int i = 0; i < 12; i++) {
			slider.setValue(i);
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
			slider.setValue(i);
			assertEquals(Math.round(i / 3.0) * 3, (long) slider.getValue());
		}
	}

	@Test
	public void stepTest() throws Exception {
		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(0);
		slider.setMaxValue(10);

		slider.setValue(5);

		Assert.assertEquals(5, (int) slider.getValue());

		slider.setStep(2);

		int value = slider.getValue();
		Assert.assertTrue(value == 4 || value == 6);

		slider.setStep(3);
		slider.setValue(5);
		Assert.assertEquals(6, (int) slider.getValue());
	}

	@Test
	public void sharedPreferences() throws Exception {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
		final String prefName = "TESTING PREFERENCE";

		preferences.edit().remove(prefName).apply();

		IntSlider slider = new IntSlider(appContext);
		slider.setMinValue(-5);
		slider.setMaxValue(5);
		slider.setStep(2);

		slider.setPreferencesAndLoad(preferences, prefName, 1);

		Assert.assertEquals(1, (long) slider.getValue());

		slider.setValue(4);

		int value = slider.getValue();
		Assert.assertEquals(value, preferences.getInt(prefName, Integer.MIN_VALUE));

		slider.setPreferences(null, null);

		slider.setValue(1);

		Assert.assertEquals(value, preferences.getInt(prefName, Integer.MIN_VALUE));
		Assert.assertEquals(1, (int) slider.getValue());

		//cleanup
		preferences.edit().remove(prefName).apply();
	}

	private AtomicInteger atomicInteger = new AtomicInteger(0);

	@Test
	public void callbackTests() throws Exception {
		IntSlider slider = new IntSlider(appContext);

		Slider.OnValueChangeListener<Integer> valueChangeListener = (value, fromUser) -> {
			atomicInteger.incrementAndGet();
		};

		slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				atomicInteger.incrementAndGet();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		slider.setOnValueChangeListener(valueChangeListener);
		slider.setValue(5);
		assertEquals(2, atomicInteger.get());

		slider.setOnValueChangeListener(null);
		slider.setOnSeekBarChangeListener(null);
		slider.setValue(3);
		assertEquals(2, atomicInteger.get());
	}

	@Test
	public void exceptionTest() {
		IntSlider slider = new IntSlider(appContext);
		EAssert.assertException(() -> slider.setSliderStep(-5), RuntimeException.class);

		EAssert.assertException(() -> slider.setMinValue(slider.getMaxValue()), InvalidParameterException.class);
		EAssert.assertException(() -> slider.setMaxValue(slider.getMinValue()), InvalidParameterException.class);
	}
}
