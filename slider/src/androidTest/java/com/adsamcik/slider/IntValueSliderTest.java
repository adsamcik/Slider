package com.adsamcik.slider;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Test;

public class IntValueSliderTest {
	private final Context appContext = InstrumentationRegistry.getTargetContext();

	private final Integer[] ints = new Integer[]{0, 1, 2, 4, 7, 12};

	@Test
	public void sharedPreferences() throws Exception {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
		final String prefName = "TESTING PREFERENCE";
		final int noPreference = -1;

		preferences.edit().remove(prefName).commit();

		IntValueSlider slider = new IntValueSlider(appContext);
		slider.setItems(ints);

		slider.setPreferences(preferences, prefName, 4);

		Assert.assertEquals(4L, (long) slider.getValue());

		slider.setValue(7);

		int value = slider.getValue();
		Assert.assertEquals(value, preferences.getInt(prefName, noPreference));

		slider.removePreferences();

		slider.setValue(1);

		Assert.assertEquals(value, preferences.getInt(prefName, noPreference));
		Assert.assertEquals((long) 1, (long) slider.getValue());

		//cleanup
		preferences.edit().remove(prefName).apply();
	}
}
