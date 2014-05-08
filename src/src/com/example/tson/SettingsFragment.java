package com.example.tson;


import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;

import tson_utilities.User;

import com.google.android.gms.common.api.GoogleApiClient;

import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;

import android.widget.ExpandableListView;

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
	Button manageProjectsButton;
	
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
		 
		 //Fetch Google+ data for input
		 SharedPreferences pref =  getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
		 String personName = pref.getString("personName", null); // getting String
		 String personPhotoUrl = pref.getString("personPhotoUrl", null);
		 String email = pref.getString("email", null);
		 
		 ImageView imgProfilePic = (ImageView) settings.findViewById(R.id.imageView1);
		 new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
		 Log.d("Settings", personName);
		 Log.d("email", email);
		 Log.d("personPhotoUrl", personPhotoUrl);
		 
		 notificationCal = Calendar.getInstance();
		 meName = (TextView) settings.findViewById(R.id.meName);		 
		 meName.setText(User.getInstance().getName());
		 
		 Button addNotification = (Button) settings.findViewById(R.id.addNotification);
		 mServiceIntent = new Intent(getActivity(), NotificationHandler.class);
		 

		 ExpandableListView notificationsExpand = (ExpandableListView) settings.findViewById(R.id.expandableListView1);


		 manageProjectsButton = (Button) settings.findViewById(R.id.manage_projects_button);
		 manageProjectsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ManageProjectsActivity.class);
				startActivity(intent);				
			}
		});
		 
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
	
	   /**
	    * Background Async task to load user profile picture from url
	    * */
	   private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
	       ImageView bmImage;
	    
	       public LoadProfileImage(ImageView bmImage) {
	           this.bmImage = bmImage;
	       }
	    
	       protected Bitmap doInBackground(String... urls) {
	           String urldisplay = urls[0];
	           Bitmap mIcon11 = null;
	           try {
	               InputStream in = new java.net.URL(urldisplay).openStream();
	               mIcon11 = BitmapFactory.decodeStream(in);
	           } catch (Exception e) {
	               Log.e("Error", e.getMessage());
	               e.printStackTrace();
	           }
	           return mIcon11;
	       }
	    
	       protected void onPostExecute(Bitmap result) {
	           bmImage.setImageBitmap(result);
	       }
	   }
	
	

}
