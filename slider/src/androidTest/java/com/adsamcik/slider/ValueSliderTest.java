package com.adsamcik.slider;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adsamcik.slider.sliders.ObjectValueSlider;
import com.adsamcik.slider.sliders.Slider;

import junit.framework.Assert;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class ValueSliderTest {
	private final Context appContext = InstrumentationRegistry.getTargetContext();

	private final String[] strings = new String[]{"a", "b", "c", "d", "e", "f"};

	@Test
	public void baseTest() throws Exception {
		ObjectValueSlider<String> slider = new ObjectValueSlider<>(appContext);

		slider.setItems(strings);
		slider.setValue("b");
		slider.setTextView(null, null);

		assertEquals("b", slider.getValue());
		assertEquals(strings.length - 1, slider.getMax());
	}

	@Test
	public void textViewTest() throws Exception {
		ObjectValueSlider<String> slider = new ObjectValueSlider<>(appContext);
		TextView textView = new TextView(appContext);

		slider.setItems(strings);
		slider.setTextView(textView, value -> "Test " + value);
		slider.setValue("d");

		assertEquals("Test d", textView.getText());

		slider.setTextView(null, value -> "Test " + value);
		slider.setValue("b");
		assertEquals("Test d", textView.getText());
		assertEquals("b", slider.getValue());

		slider.setTextView(textView, value -> "Test " + value);
		slider.setTextView(textView, null);
		slider.setValue("e");
		assertEquals("Test b", textView.getText());
		assertEquals("e", slider.getValue());

		slider.setTextView(null, null);
		slider.setValue("a");
		assertEquals("Test b", textView.getText());
		assertEquals("a", slider.getValue());
	}

	@Test
	public void progressTest() throws Exception {
		if (Looper.myLooper() == null)
			Looper.prepare();

		ObjectValueSlider<String> slider = new ObjectValueSlider<>(appContext);
		slider.setItems(strings);
		slider.setValue("a");

		assertEquals("a", slider.getValue());
		assertEquals(0, slider.getProgress());

		if (Build.VERSION.SDK_INT >= 24)
			slider.setValue("f", false);
		else
			slider.setValue("f");

		assertEquals(5, slider.getProgress());
		assertEquals("f", slider.getValue());

		if (Build.VERSION.SDK_INT >= 24)
			slider.setValue("b", true);
		else
			slider.setValue("b");

		assertEquals(1, slider.getProgress());
		assertEquals("b", slider.getValue());
	}


	@Test
	public void invalidCodeInputTest() throws Exception {
		Context appContext = InstrumentationRegistry.getTargetContext();

		ObjectValueSlider<String> slider = new ObjectValueSlider<>(appContext);
		EAssert.assertException(() -> slider.setItems(new String[0]), RuntimeException.class);

		EAssert.assertException(() -> slider.setProgress(15), RuntimeException.class);

		slider.setItems(strings);

		EAssert.assertException(() -> slider.setProgress(15), RuntimeException.class);
		EAssert.assertException(() -> slider.setProgress(-1), RuntimeException.class);
	}

	@Test
	public void sharedPreferences() throws Exception {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
		final String prefName = "TESTING PREFERENCE";
		final String noPreference = "NO PREFERENCE";

		preferences.edit().remove(prefName).commit();

		ObjectValueSlider<String> slider = new ObjectValueSlider<>(appContext);
		slider.setItems(strings);

		slider.setPreferences(preferences, prefName, "d", value -> value);

		Assert.assertEquals("d", slider.getValue());

		slider.setValue("c");

		String value = slider.getValue();
		Assert.assertEquals(value, preferences.getString(prefName, noPreference));

		slider.removePreferences();

		slider.setValue("b");

		Assert.assertEquals(value, preferences.getString(prefName, noPreference));
		Assert.assertEquals("b", slider.getValue());

		//cleanup
		preferences.edit().remove(prefName).apply();
	}

	private AtomicInteger atomicInteger = new AtomicInteger(0);

	@Test
	public void callbackTests() throws Exception {
		ObjectValueSlider<String> slider = new ObjectValueSlider<>(appContext);
		slider.setItems(strings);

		Slider.OnValueChangeListener<String> valueChangeListener = (value, fromUser) -> atomicInteger.incrementAndGet();

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
		slider.setValue("d");
		assertEquals(2, atomicInteger.get());

		slider.setOnValueChangeListener(null);
		slider.setOnSeekBarChangeListener(null);
		slider.setValue("c");
		assertEquals(2, atomicInteger.get());
	}
}
