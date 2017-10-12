package com.adsamcik.slidertest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.adsamcik.slider.Slider;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Slider slider = findViewById(R.id.slider);
		slider.setTextView(findViewById(R.id.slider_title), String::valueOf);
	}
}
