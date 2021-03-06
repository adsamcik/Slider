package com.adsamcik.slidertest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.adsamcik.slider.implementations.IntSlider;

import java.security.InvalidParameterException;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		IntSlider slider = findViewById(R.id.slider);
		slider.setLabelFormatter(String::valueOf);

		EditText minEditText = findViewById(R.id.minEditText);
		minEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					slider.setMinValue(parseInt(s));
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
					slider.setMaxValue(parseInt(s));
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
					slider.setStep(parseInt(s));
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

	private int parseInt(CharSequence charSequence) {
		try {
			return Integer.parseInt(charSequence.toString());
		} catch (Exception e) {
			return 1;
		}
	}
}
