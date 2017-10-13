package com.adsamcik.slider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adsamcik.slider.ScaleFunctions.LinearScale;

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
		m_DecimalPlaces = ta.getInteger(R.styleable.Slider_decimalPlaces, 0);
		m_Step = ta.getFloat(R.styleable.Slider_step, 1);
		ta.recycle();

		setMax();
	}

	public void setStep(float step) {
		this.m_Step = step;
		setMax();
		updateText();
	}

	public void setScale(IScale scale) {
		this.m_Scale = scale;
		updateText();
	}

	public void setTextView(TextView textView, IStringify stringify) {
		this.m_TextView = textView;
		this.m_Stringify = stringify;
		updateText();
	}

	public void setMax(int max) {
		m_Max = max;
		setMax();
		updateText();
	}

	private void setMax() {
		int max = (int) ((m_Max - m_Min) * getPercentPower());
		super.setMax(max);
		m_ScaledStep = Math.round(m_Step / (m_Max - m_Min) * max);
	}

	public void setDecimalPlaces(int decimalPlaces) {
		this.m_DecimalPlaces = decimalPlaces;
		setMax();
		updateText();
	}

	public float getValue() {
		return m_Scale.scale(getPercentProgress(), m_Min, m_Max);
	}

	private float getPercentProgress() {
		return getProgress() / (float) getMax();
	}

	private int getPercentPower() {
		return 100 * (int) Math.pow(10, m_DecimalPlaces);
	}

	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
		onSeekBarChangeListener = l;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		//if (fromUser)
		//	lastRounded = limit(m_Min, m_Max, step(progress, progress - m_PrevProg, m_ScaledStep));
		//title.setText(textGenerationFuncton.m_Stringify(minValue + lastRounded));

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
		seekBar.setProgress(step(getProgress(), m_ScaledStep));
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

	//Static math functions
	private static float limit(final float min, final float max, final float value) {
		if (value < min)
			return min;
		else if (value > max)
			return max;
		else
			return value;
	}

	private static int step(final int value, final int direction, final int step) {
		int target = value + direction;

		if (target % step != 0 && sign(direction) == sign(target))
			target = target / step + sign(direction);
		else
			target /= step;
		return target * step;
	}

	private static int step(final int value, final int step) {
		int left = value % step;
		int roundDown = value - left;
		return left >= step * 0.5 ? roundDown + step : roundDown;
	}

	private static int sign(float value) {
		if (value > 0)
			return 1;
		else if (value < 0)
			return -1;
		else
			return 0;

	}
}
