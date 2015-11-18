package com.doantotnghiep.motiondetector;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.regex.Pattern;

import com.doantotnghiep.motiondetector.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WowzaSettingActivity extends ActionBarActivity {
	public static int REQUEST_CODE=1024;
	public static int RESULT_OK=1025;
	public static int RESULT_CANCELED=1026;
	private EditText ed_wowza_server_ip;
	private EditText ed_wowza_server_port;
	private EditText ed_wowza_server_app_name;
	private EditText ed_wowza_server_app_username;
	private EditText ed_wowza_server_app_password;
	private Button btnOK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wowza_setting);
		ed_wowza_server_ip=(EditText)findViewById(R.id.ed_ip);
		ed_wowza_server_ip.setText(ed_wowza_server_ip.getText()+"*");
		ed_wowza_server_port=(EditText)findViewById(R.id.ed_port);
		ed_wowza_server_port.setText(ed_wowza_server_port.getText()+"*");
		ed_wowza_server_app_name=(EditText)findViewById(R.id.ed_wowza_app_name);
		ed_wowza_server_app_name.setText(ed_wowza_server_app_name.getText()+"*");
		ed_wowza_server_app_username=(EditText)findViewById(R.id.ed_wowza_app_username);
		ed_wowza_server_app_password=(EditText)findViewById(R.id.ed_wowza_app_pass);
		btnOK=(Button)findViewById(R.id.button1);
		SharedPreferences sp=getSharedPreferences("motion_detect_setting", MODE_PRIVATE);
		ed_wowza_server_ip.setText(sp.getString("wowza_server_add", ""));
		ed_wowza_server_port.setText(sp.getString("wowza_server_port",""));
		ed_wowza_server_app_name.setText(sp.getString("wowza_server_app",""));
		ed_wowza_server_app_username.setText(sp.getString("wowza_server_app_username",""));
		ed_wowza_server_app_password.setText(sp.getString("wowza_server_app_pass",""));
//		View.OnKeyListener onKeyListener=new View.OnKeyListener() {
//			
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				enableOKButton();
//				return false;
//			}
//		};
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ed_wowza_server_ip.getText().toString().matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.\\d{1,3}$")&&
						!ed_wowza_server_port.getText().toString().equals("")&&
						!ed_wowza_server_app_name.getText().toString().equals(""))
				{
					SharedPreferences.Editor editor=WowzaSettingActivity.this.getSharedPreferences("motion_detect_setting", MODE_PRIVATE).edit();
					editor.putString("wowza_server_add", ed_wowza_server_ip.getText().toString());
					editor.putString("wowza_server_port", ed_wowza_server_port.getText().toString());
					editor.putString("wowza_server_app", ed_wowza_server_app_name.getText().toString());
					editor.putString("wowza_server_app_username", ed_wowza_server_app_username.getText().toString());
					editor.putString("wowza_server_app_pass",ed_wowza_server_app_password.getText().toString());
					editor.apply();
					WowzaSettingActivity.this.setResult(RESULT_OK,new Intent());
					finish();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			SharedPreferences.Editor editor=WowzaSettingActivity.this.getSharedPreferences("motion_detect_setting", MODE_PRIVATE).edit();
			editor.putBoolean("wowza_stream_enable", false);
			editor.apply();
			setResult(RESULT_CANCELED,new Intent());
		}
		return super.onKeyDown(keyCode, event);
	}
}
