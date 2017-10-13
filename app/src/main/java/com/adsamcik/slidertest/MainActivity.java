package com.adsamcik.slidertest;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.adsamcik.slider.Slider;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Slider slider = findViewById(R.id.slider);
		slider.setTextView(findViewById(R.id.slider_title), String::valueOf);

		EditText minEditText = findViewById(R.id.minEditText);
		minEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				slider.setMin(Float.parseFloat(s.toString()));
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		EditText maxEditText = findViewById(R.id.maxEditText);
		maxEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				slider.setMax(Float.parseFloat(s.toString()));
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		EditText stepEditText = findViewById(R.id.stepEditText);
		stepEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				slider.setStep(Float.parseFloat(s.toString()));
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
}
