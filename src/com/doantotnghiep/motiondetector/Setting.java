package com.doantotnghiep.motiondetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CheckedInputStream;

import com.doantotnghiep.motiondetector.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Setting extends ActionBarActivity{
	private File haarCascadeFilesDir;
	MyPreferenceFragment myPreferenceFragment;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.pref_with_actionbar);
		myPreferenceFragment=new MyPreferenceFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, myPreferenceFragment).commit();
        haarCascadeFilesDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MotionDetector/haarcascades");
        if(!haarCascadeFilesDir.exists())
        {
        	haarCascadeFilesDir.mkdirs();
        	copyAssets();
        }
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		myPreferenceFragment.hardUpdateAllPreference();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void copyAssets() {
	    AssetManager assetManager = getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	        Log.e("tag", "Failed to get asset file list.", e);
	    }
	    if (files != null) for (String filename : files) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(filename);
	          File outFile = new File(haarCascadeFilesDir, filename);
	          out = new FileOutputStream(outFile);
	          copyFile(in, out);
	        } catch(IOException e) {
	            Log.e("tag", "Failed to copy asset file: " + filename, e);
	        }     
	        finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                }
	            }
	            if (out != null) {
	                try {
	                    out.close();
	                } catch (IOException e) {
	                }
	            }
	        }  
	    }
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException
	{
		byte[] buffer=new byte[1024];
		int read;
		while((read=in.read(buffer))!=-1)
		{
			out.write(buffer,0,read);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.settings_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.goto_camera)
		{
			startActivity(new Intent(this, BGSActivity.class));
		}
		if(item.getItemId()==R.id.reload_setting)
		{
			myPreferenceFragment.hardUpdateAllPreference();
		}
		return super.onOptionsItemSelected(item);
	}

	public class MyPreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            PreferenceManager prefMgr = getPreferenceManager();
            prefMgr.setSharedPreferencesName("motion_detect_setting");
            prefMgr.setSharedPreferencesMode(MODE_PRIVATE);
            addPreferencesFromResource(R.xml.preferences);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
          super.onResume();
          updateAllPreference();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        	SharedPreferences sp=getSharedPreferences("motion_detect_setting", MODE_PRIVATE);
        	if(key.equals("wowza_stream_enable"))
        	{	
        		if(sp.getBoolean("wowza_stream_enable", true))
        		{
        			//if((sp.getString("wowza_server_add", null)==null)&&(sp.getString("wowza_server_port",null)== null)&&(sp.getString("wowza_server_app",null)== null))
        			Setting.this.startActivityForResult(new Intent(Setting.this,WowzaSettingActivity.class),WowzaSettingActivity.REQUEST_CODE);
        		}
        	}
        	if(key.equals("motiondetect_alarm_method"))
        	{
        		if(sp.getString("motiondetect_alarm_method", "2").equals("1")&&!sp.getString("user_phone_number", "").matches("^\\d{10,15}$"))
        		{
        			Setting.this.startActivityForResult(new Intent(Setting.this,SetPhoneNumberActivity.class),SetPhoneNumberActivity.REQUEST_CODE);
        		}
        	}
        	if(key.equals("haarcascade_detect_alarm_method"))
        	{
        		if(sp.getString("haarcascade_detect_alarm_method", "2").equals("1")&&!sp.getString("user_phone_number", "").matches("^\\d{10,15}$"))
        		{
        			Setting.this.startActivityForResult(new Intent(Setting.this,SetPhoneNumberActivity.class),WowzaSettingActivity.REQUEST_CODE);
        		}
        	}
        	if(key.equals("user_phone_number"))
        	{
        		String tmp=sp.getString("user_phone_number", "");
        		if(tmp.equals("")||tmp.length()<10)
        		{
        			ListPreference p=(ListPreference)findPreference("motiondetect_alarm_method");
        			if(p.getValue().equals("1"))
        			{
        				p.setValue("2");
        				updatePreference(p);
        			}
        			p=(ListPreference)findPreference("haarcascade_detect_alarm_method");
        			if(p.getValue().equals("1"))
        			{
        				p.setValue("2");
        				updatePreference(p);
        			}
        		}
        	}
        	if(key.equals("wowza_server_add"))
        	{
        		if(!sp.getString("wowza_server_add", "").matches("^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}$"))
        		{
        			CheckBoxPreference cb=(CheckBoxPreference)findPreference("wowza_stream_enable");
        			cb.setChecked(false);
        			updatePreference(cb);
        		}
        	}
        	if(key.equals("wowza_server_port"))
        	{
        		if(!sp.getString("wowza_server_port", "").matches("^\\d{1,5}$"))
        		{
        			CheckBoxPreference cb=(CheckBoxPreference)findPreference("wowza_stream_enable");
        			cb.setChecked(false);
        			updatePreference(cb);
        		}
        	}
        	updatePreference(findPreference(key));
        }
		
		private void updatePreference(Preference preference) {
          if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
          }
          else if(preference instanceof EditTextPreference)
          {
        	  EditTextPreference editTextPreference=(EditTextPreference)preference;
        	  editTextPreference.setSummary(editTextPreference.getText());
          }
        }
		private void hardUpdatePreference(String key)
		{	
			SharedPreferences sp=getSharedPreferences("motion_detect_setting", MODE_PRIVATE);
			Preference preference=findPreference(key);
			if (preference instanceof ListPreference) {
	            ListPreference listPreference = (ListPreference) preference;
	            //listPreference.setValue(sp.getString(key, ""));
	            listPreference.setSummary(listPreference.getEntries()[Integer.valueOf(sp.getString(key, "2"))]);
	          }
	          else if(preference instanceof EditTextPreference)
	          {
	        	  EditTextPreference editTextPreference=(EditTextPreference)preference;
	        	  editTextPreference.setText(sp.getString(key, null));
	        	  editTextPreference.setSummary(sp.getString(key, null));
	          }
		}
		public void updateAllPreference() {
			for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
	            Preference preference = getPreferenceScreen().getPreference(i);
	            if (preference instanceof PreferenceGroup) {
	              PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
	              for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
	                updatePreference(preferenceGroup.getPreference(j));
	              }
	            } else {
	              updatePreference(preference);
	            }
	          }
		}
		public void hardUpdateAllPreference() {
			for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
	            Preference preference = getPreferenceScreen().getPreference(i);
	            if (preference instanceof PreferenceGroup) {
	              PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
	              for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
	                hardUpdatePreference(preferenceGroup.getPreference(j).getKey());
	              }
	            } else {
	              hardUpdatePreference(preference.getKey());
	            }
	          }
		}
      }
}
