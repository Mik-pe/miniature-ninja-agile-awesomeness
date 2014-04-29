package com.example.tson;

import java.util.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsFragment extends Fragment{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	View settings;
	TextView meName;
	EditText notificationEditText;
	Calendar notificationCal;
	Intent mServiceIntent;
	PendingIntent pendingIntent;
	 /***********************
	  *  	OTHERS			*/	
	 /************************/
	
	public SettingsFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) 
	{
		 
		 super.onCreate(savedInstanceState);
		 settings = inflater.inflate(R.layout.settings_fragment, container, false);
		 getActivity();
		 notificationCal = Calendar.getInstance();
		 
		 meName = (TextView) settings.findViewById(R.id.meName);		 
		 meName.setText(HomeActivity.user.getName());
		 
		 Button addNotification = (Button) settings.findViewById(R.id.addNotification);
		 mServiceIntent = new Intent(getActivity(), NotificationHandler.class);
		 
		 
		 addNotification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				notificationEditText = (EditText) settings.findViewById(R.id.editText3);
				Editable notificationText = notificationEditText.getText();
				notificationCal.add(Calendar.SECOND, 5);
				mServiceIntent.putExtra("title", "Tson says:");
				mServiceIntent.putExtra("text", notificationText);
				pendingIntent = PendingIntent.getService(getActivity(), 0, mServiceIntent, 0);
				AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationCal.getTimeInMillis(), 24*60*60*5*1000 , pendingIntent);
			}
		 });
		 
		 return settings;
	}

}
