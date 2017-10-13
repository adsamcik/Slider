package com.adsamcik.slidertest;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.adsamcik.slider.Slider;

import java.security.InvalidParameterException;

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
				try {
					slider.setMin(parseFloat(s));
				} catch (InvalidParameterException e) {
					//no need to throw exception. This is proper behavior.
				}
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
				try {
					slider.setMax(parseFloat(s));
				} catch (InvalidParameterException e) {
					//no need to throw exception. This is proper behavior.
				}
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
				try {
					slider.setStep(parseFloat(s));
				} catch (InvalidParameterException e) {
					//no need to throw exception. This is proper behavior.
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private float parseFloat(CharSequence charSequence) {
		try {
			return Float.parseFloat(charSequence.toString());
		} catch (Exception e) {
			return 1;
		}
	}
}
