package de.dst.xposed.usbdebugginglistenercontrol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class USBDebuggingListenerControlSettingsActivity extends Activity {

	@SuppressLint("WorldReadableFiles")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usbdebugging_listener_control_settings);

		final SharedPreferences prefs = getSharedPreferences(USBDebuggingListenerControl.PREFS, Context.MODE_WORLD_READABLE);
		
		boolean listenerEnabled = prefs.getBoolean(USBDebuggingListenerControl.PREF_ListenerEnabled, true);
		final CheckBox cb = (CheckBox) findViewById(R.id.checkBox_enable_listener);
		cb.setChecked(listenerEnabled);
			

		cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Editor prefsEditor = prefs.edit();
		    	prefsEditor.putBoolean(USBDebuggingListenerControl.PREF_ListenerEnabled, isChecked);
		    	prefsEditor.commit();
			}
		});
	}
    
}
