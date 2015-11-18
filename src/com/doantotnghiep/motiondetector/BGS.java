package com.doantotnghiep.motiondetector;

import org.opencv.core.Mat;
import android.util.Log;
public class BGS {
	private final String TAG="BGS Module";
	private long nativeObject=0;
	private boolean isEnabled;
	private int nextMethod=0;
	private boolean isEnabledThreshold;
	private boolean isCreated=false;
	public BGS(BGSMethod method)
	{
		nativeObject=createObject(method.ordinal());
	}
	public void disable()
	{
		isEnabled=false;
	}
	public void enable()
	{
		isEnabled=true;
	}
	public void destroy()
	{
		if(nativeObject!=0)
		{
			destroyObject(nativeObject);
		}
	}
	public synchronized void process(Mat img_input,Mat img_mask, Mat img_model)
	{
		if(isEnabled && nativeObject!=0)
		{
			Log.i(TAG, "process");
			process(nativeObject, img_input.getNativeObjAddr(), img_mask.getNativeObjAddr(), img_model.getNativeObjAddr());
		}
	}
	public void resetBackground()
	{
		reset(nativeObject);
	}
	public void enableThreshold(boolean arg0)
	{
		enableThreshold(nativeObject,arg0);
	}
	public void setThreshold(int arg0)
	{
		setThreshold(nativeObject, arg0);
	}
	public int getThreshold()
	{
		return getThreshold(nativeObject);
	}
	public boolean isEnabledThreshold()
	{
		return isEnabledThreshold(nativeObject);
	}
	private native void process(long nativeObject,long img_input, long img_mask, long img_model);
	private native long createObject(int method);
	private native void destroyObject(long nativeObj);
	private native void reset(long obj);
	private native void enableThreshold(long object, boolean ena);
	private native void setThreshold(long object, int value);
	private native boolean isEnabledThreshold(long object);
	private native int getThreshold(long object);
}