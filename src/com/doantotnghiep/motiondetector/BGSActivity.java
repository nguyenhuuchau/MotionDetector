package com.doantotnghiep.motiondetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.opencv.android.BaseLoaderCallback;
import com.doantotnghiep.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import com.doantotnghiep.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import com.doantotnghiep.motiondetector.R;

public class BGSActivity extends FragmentActivity implements CvCameraViewListener2 {
	private final String TAG = "MotionDetector";

	private enum AlarmCase {
		MOTION_DETECT, FACE_DETECT
	};

	private String cameraName;
	private SharedPreferences sp;
	// camera
	private final int FRAME_WIDTH = 320;
	private final int FRAME_HEIGHT = 240;
	// bgs
	private BGS mBGS = null;
	private Mat mask;
	private Mat model;
	private Mat input;
	private Mat output;
	// face detect
	private boolean useFaceDetect;
	private CascadeClassifier faceDetect;
	private MatOfRect faces;
	private Rect[] facesArray = null;
	// object filter
	private float mRelativeFaceSize;
	private int minimumFaceSize;
	private float mRelativeObjectSize;
	private int minimumObjectSize;
	//
	private int frameElapsed = 0;
	private int objectsCount = 0;
	//private int facesCount = 0;
	private List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	private boolean isMotionDetected = false;
	private boolean isFaceDetected = false;
	// alarm
	private String motiondetectAlarmMethod = null;
	private String facedetectAlarmMethod = null;
	private boolean isSoundAlarming = false;
	private int alarmSound = 0;
	private int timeDelayBeforeSoundAlarm = 1;
	private String phoneNumber="";
	private int smsTimeInterval;
	// private boolean emailAllowed=true;
	// private String email="";
	// private String emailContent="TEST CAM";
	// private boolean emailAlarmAllowed=false;
	// alarm thread
	private Thread motionDetectAlarmThread;
	private Thread faceDetectAlarmThread;
	//image save;
	File mediaStorageDir = null;
	private long saveImageTimeInterval;
	private long saveImageTimeBegin = 0;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				//Log.i(TAG, "OpenCV loaded successfully");
				init();
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
			super.onManagerConnected(status);
		}
	};
	private com.doantotnghiep.opencv.android.CameraBridgeViewBase mOpenCvCameraView;

	private void init() {
		System.loadLibrary("motion_detection");
		sp = getSharedPreferences("motion_detect_setting", Context.MODE_PRIVATE);
		mRelativeObjectSize = (float) sp.getInt("motiondetect_threshold", 10) / 100;
		minimumObjectSize = (int) (mRelativeObjectSize * FRAME_HEIGHT);

		cameraName = sp.getString("camera_name", "MotionDetectCamera");
		phoneNumber=sp.getString("user_phone_number", "");
		if(!phoneNumber.matches("^\\d{10,15}$"))
		{
			phoneNumber="";
			//Toast.makeText(BGSActivity.this,"Invalid Phone Number", Toast.LENGTH_SHORT).show();
		}
		// init BGS
		int bgsmethod = Integer.valueOf(sp.getString("bgs_method", "0"));
		Log.i("BGS INIT", String.valueOf(bgsmethod));
		switch (bgsmethod) {
		case 0:
			mBGS = new BGS(BGSMethod.STATIC_FRAME_DIFFERENCE);
			break;
		case 1:
			mBGS = new BGS(BGSMethod.STATIC_FRAME_DIFFERENCE);
			break;
		case 2:
			mBGS = new BGS(BGSMethod.MIXTURE_OF_GAUSSIAN_1);
			break;
		case 3:
			mBGS = new BGS(BGSMethod.MIXTURE_OF_GAUSSIAN_2);
			break;
		case 4:
			mBGS = new BGS(BGSMethod.FRAME_DIFFERENCE);
			break;
		case 5:
			mBGS = new BGS(BGSMethod.WEIGHTED_MOVING_VARIANCE);
			break;
		default:
			mBGS = new BGS(BGSMethod.BGS_DEFAULT);
			break;
		}
		mBGS.enableThreshold(sp.getBoolean("enable_threshold", true));
		mBGS.setThreshold(sp.getInt("bgs_threshold", 15));
		mBGS.enableThreshold(true);
		mBGS.setThreshold(30);
		mBGS.enable();
		// alarm
		alarmSound = Integer.valueOf(sp.getString("alarm_sound", "0"));
		smsTimeInterval = Integer.valueOf(sp.getString("sms_time_interval", "5"));
		//smsTimeInterval=1;
		// init face_detect
		useFaceDetect = sp.getBoolean("enable_haarcascade_detect", false);
		if (useFaceDetect) {
			mRelativeFaceSize = (float) sp.getInt("haarcascade_detect_threshold", 20) / 100;
			minimumFaceSize = (int) (mRelativeFaceSize * FRAME_HEIGHT);
			int objectDetectType = Integer.valueOf(sp.getString("haarcascade_set", "0"));
			if (objectDetectType == 1) {
				faceDetect = new CascadeClassifier(Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/MotionDetector/haarcascades/haarcascade_fullbody.xml");
			} else {
				faceDetect = new CascadeClassifier(Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/MotionDetector/haarcascades/haarcascade_frontalface_default.xml");
			}
			faces = new MatOfRect();
			facedetectAlarmMethod = sp.getString("haarcascade_detect_alarm_method", "2");
			if (facedetectAlarmMethod.equals("0")&&!sp.getString("motiondetect_alarm_method", "2").equals("0")) {
				faceDetectAlarmThread = new Thread(new SoundAlarmRunable(AlarmCase.FACE_DETECT));
				faceDetectAlarmThread.start();
			} else if (facedetectAlarmMethod.equals("1")&&!phoneNumber.equals("")) {
				faceDetectAlarmThread = new Thread(new SmsAlarmRunable(AlarmCase.FACE_DETECT));
				faceDetectAlarmThread.start();
			}
		}
		//motion detect alarm
		motiondetectAlarmMethod = sp.getString("motiondetect_alarm_method", "2");
		if (motiondetectAlarmMethod.equals("0")) {
			motionDetectAlarmThread = new Thread(new SoundAlarmRunable(AlarmCase.MOTION_DETECT));
			motionDetectAlarmThread.start();
		} else if (motiondetectAlarmMethod.equals("1")&&!phoneNumber.equals("")) {
			motionDetectAlarmThread = new Thread(new SmsAlarmRunable(AlarmCase.MOTION_DETECT));
			motionDetectAlarmThread.start();
		} 
		// Save Image
		saveImageTimeInterval = Integer.valueOf(sp.getString("image_storage_time_interval", "1")) * 60000;
		// init camera
		int cameraIndex = Integer.valueOf(sp.getString("camera_id", String.valueOf(org.opencv.android.CameraBridgeViewBase.CAMERA_ID_BACK)));
		mOpenCvCameraView.disableView();
		if (cameraIndex == 0) {
			mOpenCvCameraView.setCameraIndex(org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT);
		} else {
			mOpenCvCameraView.setCameraIndex(org.opencv.android.CameraBridgeViewBase.CAMERA_ID_BACK);
		}
		mOpenCvCameraView.setMaxFrameSize(FRAME_WIDTH, FRAME_HEIGHT);
		//init ffmpeg recorder
		mOpenCvCameraView.initRecorder();
		mOpenCvCameraView.enableView();
		mOpenCvCameraView.setFocusable(false);
		mOpenCvCameraView.startRecording();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mOpenCvCameraView = (com.doantotnghiep.opencv.android.CameraBridgeViewBase) findViewById(
				R.id.md_activity_surface_view);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);
		mOpenCvCameraView.enableFpsMeter();
		mediaStorageDir = new File(
				Environment.getExternalStorageDirectory().getAbsolutePath() + "/MotionDetector/Pictures");
		if (!mediaStorageDir.exists())
			mediaStorageDir.mkdirs();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, 103, 1, "Reset Background");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == 103) {
			mBGS.resetBackground();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);
		if (isMotionDetected && mp != null) {
			mp.start();
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null)
		{
			mOpenCvCameraView.stopRecording();
			mOpenCvCameraView.disableView();
		}
		if (mp != null && mp.isPlaying()) {
			mp.pause();
		}
	}

	public void onDestroy() {
		isMotionDetected=false;
		isFaceDetected=false;
		if (mOpenCvCameraView != null)
		{
			mOpenCvCameraView.realeaseFFMPEGRecord();
			mOpenCvCameraView.disableView();
		}
		processThread.interrupt();
		processThread = null;
		if(motionDetectAlarmThread!=null)
		{
			motionDetectAlarmThread.interrupt();
			motionDetectAlarmThread = null;
		}
		if (faceDetectAlarmThread != null) {
			faceDetectAlarmThread.interrupt();
			faceDetectAlarmThread = null;
		}
		if (mp != null) {
			mp.stop();
			mp.release();
			mp=null;
		}
		super.onDestroy();
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Auto-generated method stub
		if (output != null) {
			output.release();
		}
		output = inputFrame.rgba().clone();
		if (!processThread.isAlive() && frameElapsed >= 7) {
			frameElapsed = 0;
			if (input != null) {
				input.release();
			}
			if (mask != null) {
				mask.release();
			}
			if (model != null) {
				model.release();
			}
			input = inputFrame.rgba().clone();
			mask = inputFrame.gray().clone();
			model = inputFrame.gray().clone();
			processThread.run();
		}
		frameElapsed += 1;
		synchronized (contours) {
			for (int i = 0; i < contours.size(); i++) {
				Rect rect = Imgproc.boundingRect(contours.get(i));
				if (rect.width >= minimumObjectSize && rect.height >= minimumObjectSize) {
					objectsCount++;
					Core.rectangle(output, rect.tl(), rect.br(), new Scalar(255, 255, 255));
				}
			}
		}
		if (objectsCount > 0) {
			if (!isMotionDetected) {
				isMotionDetected = true;
			}
			if ((System.currentTimeMillis() - saveImageTimeBegin) >= saveImageTimeInterval) {
				ImageSave();
				saveImageTimeBegin = System.currentTimeMillis();
				//Log.i("Save Image", "Image Saved at " + String.valueOf(saveImageTimeBegin));
			}
			//Log.i("Save Image", String.valueOf(saveImageTimeBegin) + ", "
			//		+ String.valueOf(System.currentTimeMillis() - saveImageTimeBegin));
		} else if (objectsCount == 0) {
			if (isMotionDetected) {
				isMotionDetected = false;
			}
			saveImageTimeBegin = 0;
		}
		objectsCount = 0;
		// face detect
		if (useFaceDetect) {
			if (facesArray != null) {
				synchronized (facesArray) {
					if(facesArray.length>0)
					{
						isFaceDetected=true;
						for (int i = 0; i < facesArray.length; i++) {
							Core.rectangle(output, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 0, 255));
						}
					}
					else isFaceDetected=false;
				}
			}
		}
		return output;
	}
	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub

	}

	public void ImageSave() {
		String timeStamp = new SimpleDateFormat("HHmmss_ddMMyyyy").format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		Highgui.imwrite(mediaFile.toString(), output);
	}

	private Runnable processRun = new Runnable() {

		@Override
		public void run() {
			try {
				// BGS process start
				mBGS.process(input, mask, model);
				// BGS process end
				// Noise reduce start
				Mat mask_blur = new Mat();
				Imgproc.GaussianBlur(mask, mask_blur, new Size(5, 5), 0);
				// Noise reduce end
				// find moving object start
				contours.clear();
				Imgproc.findContours(mask_blur, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
				//Log.i("Motion Detect", String.valueOf(contours.size()) + " object");
				// find moving object end
				// find face in moving object start
				if (useFaceDetect) {
					faceDetect.detectMultiScale(input, faces, 1.1, 2, 2, new Size(minimumFaceSize, minimumFaceSize),
							new Size());
					facesArray = faces.toArray();
					//Log.i("Face Detect", String.valueOf(facesArray.length)+" object");
				}
				// find face in moving object end
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	MediaPlayer mp = new MediaPlayer();
	private Thread processThread = new Thread(processRun);

	private class SoundAlarmRunable implements Runnable {
		private AlarmCase alarmCase;
		private AssetFileDescriptor descriptor = null;

		public SoundAlarmRunable(AlarmCase arg0) {
			alarmCase = arg0;
			try {
				if (alarmSound == 1) {
					descriptor = getAssets().openFd("sounds/dog_bark.mp3");
				} else {
					descriptor = getAssets().openFd("sounds/default.mp3");
				}
				mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
				mp.prepare();
				mp.setVolume(1.0f, 1.0f);
				mp.setLooping(true);
				//Log.i(TAG, "init SoudAlarm");
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (true) {
				boolean check;
				if (alarmCase == AlarmCase.MOTION_DETECT) {
					check = isMotionDetected;
				} else if (alarmCase == AlarmCase.FACE_DETECT)
				{
					check = isFaceDetected;
				}else check=false;
				if (check) {
					try {
						if (!isSoundAlarming) {
							isSoundAlarming = true;
							Thread.sleep(timeDelayBeforeSoundAlarm * 1000);
						}
						if (alarmCase == AlarmCase.MOTION_DETECT) {
							check = isMotionDetected;
						} else if (alarmCase == AlarmCase.FACE_DETECT)
						{
							check = isFaceDetected;
						}else check=false;
						if (check) {
							soundAlarm.makeAlarm();
							//Log.i(TAG,"Sound Alarm");
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					if (isSoundAlarming) {
						isSoundAlarming = false;
						soundAlarm.stop();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}

		private Alarm soundAlarm = new Alarm(new IMakeAlarm() {
			public void alarm() {
				try {
					if(!mp.isPlaying())
					{
						mp.start();
					}
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void stop() {
				if (mp.isPlaying()) {
					mp.pause();
				}

			}
		});
	};

	private class SmsAlarmRunable implements Runnable {
		private int loopCount = 0;
		private SmsManager smsManager = null;
		private AlarmCase alarmCase;
		private int maxloop = smsTimeInterval * 60;

		public SmsAlarmRunable(AlarmCase arg0) {
			alarmCase = arg0;
			smsManager = SmsManager.getDefault();
		}

		public void run() {
			//Log.i("SmsAlarm", "sms Start");
			while (true) {
				boolean check;
				if (alarmCase == AlarmCase.MOTION_DETECT) {
					check = isMotionDetected;
				} else if (alarmCase == AlarmCase.FACE_DETECT)
				{
					check = isFaceDetected;
				}else check=false;
				if (check) {
					try {
						Thread.sleep(1000);
						if (alarmCase == AlarmCase.MOTION_DETECT) {
							check = isMotionDetected;
						} else if (alarmCase == AlarmCase.FACE_DETECT)
						{
							check = isFaceDetected;
						}else check=false;
						if (check) {
							if (loopCount == 0) {
								smsAlarm.makeAlarm();
								//Log.i("SmsAlarm", "sms sended");
							}
							loopCount += 1;
							if (loopCount == maxloop) {
								loopCount = 0;
							}
						} else {
							loopCount = 0;
						}
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					try {
						Thread.sleep(10 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		private Alarm smsAlarm = new Alarm(new IMakeAlarm() {

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}

			@Override
			public void alarm() {
				// TODO Auto-generated method stub
				if (smsManager != null) {
					if(alarmCase==AlarmCase.MOTION_DETECT)
					{
						smsManager.sendTextMessage(phoneNumber, null, cameraName + ": Motion detected", null, null);
					}
					if(alarmCase==AlarmCase.FACE_DETECT)
					{
						smsManager.sendTextMessage(phoneNumber, null, cameraName + ": Face detected", null, null);
					}
				}
			}
		});
	};
}