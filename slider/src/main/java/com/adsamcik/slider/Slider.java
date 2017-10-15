package com.adsamcik.slider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adsamcik.slider.ScaleFunctions.LinearScale;

import java.security.InvalidParameterException;

import static com.adsamcik.slider.EMath.decimalPlaces;
import static com.adsamcik.slider.EMath.round;
import static com.adsamcik.slider.EMath.step;

@SuppressLint("AppCompatCustomView")
//AppCompatSeekBar does not show up in app
public class Slider extends SeekBar implements SeekBar.OnSeekBarChangeListener {
	private IScale m_Scale = new LinearScale();

	private int m_DecimalPlaces = 0;

	private float m_Step = 1;
	private int m_ScaledStep;

	private float m_Min = 0;
	private float m_Max = getMax();

	private TextView m_TextView;

	private IStringify m_Stringify;

	private OnSeekBarChangeListener onSeekBarChangeListener = null;
	private OnValueChangeListener onValueChangeListener = null;

	public Slider(Context context) {
		super(context);
		init();
		updateText();
	}

	public Slider(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAttrs(context, attrs);
		init();
		updateText();
	}

	public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setAttrs(context, attrs);
		init();
		updateText();
	}

	private void init() {
		super.setOnSeekBarChangeListener(this);
	}

	private void setAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Slider);
		m_Min = ta.getFloat(R.styleable.Slider_min, Build.VERSION.SDK_INT >= 26 ? getMin() : 0);
		m_Max = ta.getFloat(R.styleable.Slider_max, getMax());
		setStep(ta.getFloat(R.styleable.Slider_step, 1));
		ta.recycle();

		setMax();
	}

	public void setStep(float step) {
		if (step <= 0)
			throw new InvalidParameterException("Step must be larger than 0");

		this.m_Step = step;
		this.m_DecimalPlaces = decimalPlaces(step);
		setMax();
		refreshProgress();
		updateText();
	}

	public void setScale(IScale scale) {
		this.m_Scale = scale;
		refreshProgress();
		updateText();
	}

	public void setTextView(TextView textView, IStringify stringify) {
		this.m_TextView = textView;
		this.m_Stringify = stringify;
		updateText();
	}

	@Override
	public void setProgress(int progress) {
		setProgress((float) progress);
	}

	public void setProgress(float progress) {
		float progressValue = progress / (m_Max - m_Min);

		if (progressValue > 1 || progressValue < 0)
			throw new IllegalArgumentException("Progress must be larger than " + m_Min + " and smaller than " + m_Max + " was " + progress);

		super.setProgress(Math.round(progressValue * super.getMax()));
	}

	@Override
	public void setMin(int min) {
		setMin((float) min);
	}

	public void setMin(float min) {
		if (min >= m_Max)
			throw new InvalidParameterException("Min must be smaller than max");

		this.m_Min = min;
		setMax();
		refreshProgress();
		updateText();
	}

	@Override
	public void setMax(int max) {
		setMax((float) max);
	}

	public void setMax(float max) {
		if (max <= m_Min)
			throw new InvalidParameterException("Max must be larger than min");

		m_Max = max;
		setMax();
		refreshProgress();
		updateText();
	}

	private void setMax() {
		float diff = round(m_Max - m_Min, 5);
		int max = (int) (diff * getPercentPower());
		super.setMax(max);
		m_ScaledStep = Math.round(m_Step / diff * max);
	}

	public float getValue() {
		return round(m_Scale.scale(getPercentProgress(), m_Min, m_Max), m_DecimalPlaces);
	}

	private float getPercentProgress() {
		return getStepProgress() / (float) getMax();
	}

	private int getPercentPower() {
		return 100 * (int) Math.pow(10, m_DecimalPlaces);
	}

	private int getStepProgress() {
		return step(getProgress(), m_ScaledStep);
	}

	private void refreshProgress() {
		setProgress(getStepProgress());
	}

	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
		onSeekBarChangeListener = l;
	}

	public void setOnValueChangeListener(OnValueChangeListener l) {
		onValueChangeListener = l;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser)
			updateText();

		if (onSeekBarChangeListener != null)
			onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if (onSeekBarChangeListener != null)
			onSeekBarChangeListener.onStartTrackingTouch(seekBar);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		refreshProgress();
		updateText();

		if (onSeekBarChangeListener != null)
			onSeekBarChangeListener.onStopTrackingTouch(seekBar);

		if (onValueChangeListener != null)
			onValueChangeListener.onValueChanged(getValue(), true);
	}

	private void updateText() {
		if (m_TextView != null)
			m_TextView.setText(m_Stringify.toString(getValue()));
	}

	public interface OnValueChangeListener {
		void onValueChanged(float value, boolean fromUser);
	}

	public interface IStringify {
		String toString(float value);
	}

	public interface IScale {
		float scale(float value, float min, float max);
	}
}
