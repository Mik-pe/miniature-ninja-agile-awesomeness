package com.example.tson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.MyNotification;
import tson_utilities.NotificationHandler;
import tson_utilities.Project;

import android.support.v4.app.Fragment;

import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class SettingsFragment extends Fragment{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	View settings;
	TextView meName;
	TextView notificationEditText;
	TextView timeTextView;
	Calendar notificationCal;
	Button manageProjectsButton;
	int nrOfNotifications;
	List<MyNotification> notificationList;
	notificationAdapter notiAdapter;
	int holder = 0;
	int hour, min, newHour, newMin;
	ListView notificationListView;
	/**SHARED PREFERENCES*/
	public static final String PREFS_NAME = "MyPrefsFile";
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
		 meName = (TextView) settings.findViewById(R.id.meName);
		 notificationCal = Calendar.getInstance();
		 meName.setText(HomeActivity.user.getName());
		 
		 notificationListView = (ListView) settings.findViewById(R.id.notificationListView);
		 /**
		  * Static defaultnotifications, not saved internally
		  */
		 notificationList = new ArrayList<MyNotification>();
		 notificationList.add(new MyNotification("TsonSays", "Hej", nrOfNotifications, 1, 1));
		 nrOfNotifications++;
		 notificationList.add(new MyNotification("HEHSEHSDG", "REMSEFINDF", nrOfNotifications, 1, 1));
		 
		 
		 manageProjectsButton = (Button) settings.findViewById(R.id.manage_projects_button);
		 manageProjectsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ManageProjectsActivity.class);
				startActivity(intent);				
			}
		});


		 notiAdapter = new notificationAdapter();
		 notificationListView.setAdapter(notiAdapter);
		 return settings;
	}
	
	public void showInputDialog(final TextView e)
	{
		final EditText newNameInput = new EditText(getActivity());
		if(e.getText().toString() == "")
			newNameInput.setHint(e.getHint().toString());
		else
			newNameInput.setHint(e.getText().toString());
		
		new AlertDialog.Builder(getActivity())
		.setTitle("Set new reminder!")
		.setMessage("Set a remindertext!")
		.setView(newNameInput)
		.setPositiveButton("Set name!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				e.setText( newNameInput.getText().toString());
				
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		})
		.show();
	}
	boolean mIgnoreTimeSet = false;
	public void showTimeDialog(View v)
    {		
		//Calculates what page and position we are at
		holder = notificationListView.getPositionForView(v);
    	

    	/**
    	 * Show the TimePickerDialog
    	 */
    	TimePickerDialog picker = new TimePickerDialog(getActivity(), timeSetListener,  newHour, newMin, true);
    	picker.setTitle("Enter hours and minutes spent on this project:");
    	picker.setButton(TimePickerDialog.BUTTON_POSITIVE, "Set", picker);
    	picker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() 
	 	   {
    			@Override
	            public void onClick(DialogInterface dialog, int id) 
	            {
	            	//dialog.dismiss();
	            	mIgnoreTimeSet = true;
	            	Log.d("Picker", "Cancelled!");

	          
	            }
	     });
    	
    	picker.show();
    }
	
	
    private TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			//If Cancel button is clicked we do not want to save any data from time picker
			if (mIgnoreTimeSet) 
			{
				mIgnoreTimeSet = false; 
				return;
				}
			//If Set button is clicked we want to save data from time picker
			else 
			{
				mIgnoreTimeSet = false;
				hour=hourOfDay;
				min=minute;
				
				timeTextView.setText(hour+" h : "+min+" m");
			}
			
			
		}//End onTimeSet	
	};//End timeSetListener
	
    private class notificationAdapter extends ArrayAdapter<MyNotification> {
    	
    	public notificationAdapter() {
    		super(getActivity(), R.layout.settings_notification_item, notificationList);
		}

    	@Override
    	public View getView(int position, View view, ViewGroup parent)
    	{
			if(view == null)
				view = getActivity().getLayoutInflater().inflate(R.layout.settings_notification_item, parent, false);
			
			 notificationEditText = (TextView) view.findViewById(R.id.notificationTitle);
			 notificationEditText.setText(notificationList.get(position).getNotificationTitle());
			 notificationEditText.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showInputDialog( notificationEditText);
				}
			 });
			 
			 Button addNotification = (Button) view.findViewById(R.id.addNotification);
			 
			 if(position != notificationList.size()-1)
			 {
				 addNotification.setVisibility(Button.GONE);
			 }
			 else{
				 addNotification.setVisibility(Button.VISIBLE);
				 addNotification.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(), CreateNotificationActivity.class);
							startActivity(intent);
						}
					 });	 
			 }
	        return view;
    	}

    }
    
    
}
