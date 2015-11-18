package com.doantotnghiep.motiondetector;

public class Alarm {
	private IMakeAlarm alarmMethod;
	public Alarm(IMakeAlarm arg0)
	{
		alarmMethod=arg0;
	}
	public void setAlarmMethod(IMakeAlarm arg0)
	{
		alarmMethod=arg0;
	}
	public void makeAlarm()
	{
		alarmMethod.alarm();
	}
	public void stop()
	{
		alarmMethod.stop();
	}
}
