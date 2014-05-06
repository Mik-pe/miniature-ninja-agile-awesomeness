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
	ExpandableListView notificationListView;
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
		 
		 notificationListView = (ExpandableListView) settings.findViewById(R.id.expandableListView1);
		 /**
		  * Static defaultnotifications, not saved internally
		  */
		 notificationList = new ArrayList<MyNotification>();
		 notificationList.add(new MyNotification("TsonSays", "Hej", nrOfNotifications));
		 nrOfNotifications++;
		 notificationList.add(new MyNotification("HEHSEHSDG", "REMSEFINDF", nrOfNotifications));
		 
		 
		 manageProjectsButton = (Button) settings.findViewById(R.id.manage_projects_button);
		 manageProjectsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ManageProjectsActivity.class);
				startActivity(intent);				
			}
		});


		 notiAdapter = new notificationAdapter(getActivity(), notificationList);
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
	
    private class notificationAdapter extends BaseExpandableListAdapter {
    	private Context context;
    	private List<MyNotification> notificationList;
    	private List<String> myHeader = new ArrayList<String>();
    	
		public notificationAdapter(Context c, List<MyNotification> nL) {
			context = c;
			notificationList = nL;
			if(!notificationList.isEmpty())
				for(int i=0;i<notificationList.size(); i++){
					myHeader.add(notificationList.get(i).getNotificationTitle());
				}
		}


		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return this.notificationList.get(groupPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null)
				convertView = getActivity().getLayoutInflater().inflate(R.layout.settings_notification_item, parent, false);
			
			 notificationEditText = (TextView) convertView.findViewById(R.id.editText3);
			 notificationEditText.setHint(notificationList.get(groupPosition).getNotificationText());
			 notificationEditText.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showInputDialog( notificationEditText);
				}
			 });
			 
			 timeTextView = (TextView) convertView.findViewById(R.id.TimeSetTextView);
			 LinearLayout timeLayout = (LinearLayout) convertView.findViewById(R.id.TextLinear);
			 timeLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showTimeDialog(v);
				}
			});
			 Button addNotification = (Button) convertView.findViewById(R.id.addNotification);
			 addNotification.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String notificationText = notificationEditText.getText().toString();
						
						notificationCal = Calendar.getInstance();
						notificationCal.add(Calendar.SECOND, 5);
						
						Intent mServiceIntent = new Intent(getActivity(), NotificationHandler.class);
						mServiceIntent.putExtra("title", "Tson says:");
						if(notificationText != "")
							mServiceIntent.putExtra("text", notificationText);
						else
							mServiceIntent.putExtra("text", "Default Reminder");
						
						mServiceIntent.putExtra("nrOfNots", nrOfNotifications);
						mServiceIntent.putExtra("timeUntilNextDate", (notificationCal.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()));
						mServiceIntent.putExtra("calendarDefinition", Calendar.SECOND);
						mServiceIntent.putExtra("calendarValue", 5);

						PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), nrOfNotifications, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						nrOfNotifications++;
						
						AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
						alarmManager.set(AlarmManager.RTC_WAKEUP, notificationCal.getTimeInMillis(), pendingIntent);
							
						
					}
				 });
				 
	        return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return this.myHeader.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return myHeader.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String headerTitle = (String) getGroup(groupPosition);
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.settings_notification_group, null);
	        }
	        TextView text = (TextView) convertView.findViewById(R.id.notificationTitle);
	        text.setText(myHeader.get(groupPosition));
	        isExpanded = false;
	        return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
    }
    
    
}
