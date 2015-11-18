package com.doantotnghiep.motiondetector;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetPhoneNumberActivity extends ActionBarActivity {
	public static int REQUEST_CODE=1021;
	public static int RESULT_OK=1022;
	public static int RESULT_CANCELED=1023;
	private Button btnOK;
	private EditText edt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_phone_number);
		edt=(EditText)findViewById(R.id.editText1);
		btnOK=(Button)findViewById(R.id.button1);
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor=SetPhoneNumberActivity.this.getSharedPreferences("motion_detect_setting", MODE_PRIVATE).edit();
				editor.putString("user_phone_number", edt.getText().toString());
				editor.apply();
				SetPhoneNumberActivity.this.setResult(RESULT_OK,new Intent());
				finish();
			}
		});
		edt.setOnKeyListener(new View.OnKeyListener()
		{

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(edt.getText().length()>=10)
				{
					btnOK.setEnabled(true);
				}
				else
				{
					btnOK.setEnabled(false);
				}
				return false;
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_phone_number, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			SharedPreferences.Editor editor=SetPhoneNumberActivity.this.getSharedPreferences("motion_detect_setting", MODE_PRIVATE).edit();
			editor.putString("motiondetect_alarm_method", "2");
			editor.putString("haarcascade_detect_alarm_method", "2");
			editor.apply();
			setResult(RESULT_CANCELED,new Intent());
		}
		return super.onKeyDown(keyCode, event);
	}
}
