package com.doantotnghiep.motiondetector;

import com.doantotnghiep.motiondetector.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class SeekBarPreference extends Preference{

	private int mCurrentValue=0;
	private int maxValue=100;
	private String unit=null;
	private SeekBar mSeekBar=null;
	private int defaultValue=0;
	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SeekBarPreference);
	    maxValue = a.getInt(R.styleable.SeekBarPreference_setMax,100);
	    unit=a.getString(R.styleable.SeekBarPreference_unit);
	    defaultValue=a.getInt(R.styleable.SeekBarPreference_defaultValue, 0);
	    a.recycle();
	}
	public SeekBarPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected View onCreateView(ViewGroup parent) {
		LayoutInflater li = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View v=li.inflate( R.layout.seekbar_preference, parent, false);
		mSeekBar=(SeekBar)v.findViewById(R.id.seekbar);
		mSeekBar.setMax(maxValue);
		mSeekBar.setProgress(mCurrentValue);
		SeekBarPreference.this.setSummary(String.valueOf(mCurrentValue)+ (unit==null?"":" "+unit));
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				SeekBarPreference.this.setSummary(String.valueOf(mCurrentValue)+ (unit==null?"":" "+unit));
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				SeekBarPreference.this.persistInt(progress);
				mCurrentValue=progress;
				
			}
		});
		return v;
	}
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
	    if (restorePersistedValue) {
	        // Restore existing state
	        mCurrentValue= this.getPersistedInt(this.defaultValue);
	    } else {
	        // Set default state from the XML attribute
	        mCurrentValue = this.defaultValue;
	        persistInt(this.defaultValue);
	    }
	}
}
