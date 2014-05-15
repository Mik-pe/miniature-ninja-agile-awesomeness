package com.example.tson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import tson_utilities.MyNotification;
import tson_utilities.NotificationHandler;
import tson_utilities.Project;
import tson_utilities.User;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;


public class CreateNotificationActivity extends Activity {
	
	/***********************
	  *  	VARIABLES		*/	
	 /***********************/	
	
	TextView timeTextView;
	TextView repeatTextView;
	TextView notificationTitle;
	TextView notificationText;
	TimePickerDialog picker;
	Button backButton;
	Calendar c;
	Button createButton;
	int hour, minute, ID;
	String title;
	String text;
	MyNotification thisNotification;
	List<Integer> repeatList;
	boolean isEdit = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_notification);
		c = Calendar.getInstance();
		repeatList = new ArrayList<Integer>();
		Bundle extras = getIntent().getExtras();
		repeatTextView = (TextView) this.findViewById(R.id.notification_repeat_days);

		/**
		 * If extras isn't null, the activity should start in edit-mode
		 */
		if(extras != null)
		{
			isEdit	= true;
			title 	= extras.getString("notificationTitle");
			text 	= extras.getString("notificationText");
			hour 	=  extras.getInt("notificationHour");
			minute 	= extras.getInt("notificationMinute");
			ID 		= (int) extras.getLong("notificationID");
			
			if(extras.getIntegerArrayList("notificationRepeat") != null)
				repeatList =  extras.getIntegerArrayList("notificationRepeat");
			
			notificationTitle = (TextView) findViewById(R.id.notification_title_editText);
			notificationText = (TextView) findViewById(R.id.notification_text_editText);
			
			notificationTitle.setText(title);
			notificationText.setText(text);
			
			thisNotification = new MyNotification(title, text, ID, hour, minute);
			setOldTimeOnView();
			if(!repeatList.isEmpty())
			{
				String days = "";
				Calendar Weekdays = Calendar.getInstance();
				for(int i=0;i<repeatList.size();i++)
				{
					Weekdays.set(Calendar.DAY_OF_WEEK, (repeatList.get(i)+1)%7);
					days += Weekdays.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH)+", ";
				}
				repeatTextView.setText(days);
			}
			else
			{
				repeatTextView.setText("Never");
			}
		}
		else{
			thisNotification = new MyNotification(title, text, ID, hour, minute);
			setCurrentTimeOnView();
		}

		
		backButton = (Button) findViewById(R.id.cancel_project_button);
		backButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
		    	finish(); //finishes the activity and closes it
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_notification, menu);
		return true;
	}
	
	public void setCurrentTimeOnView() {
		 
		timeTextView = (TextView) this.findViewById(R.id.notification_time_set);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		
		picker = new TimePickerDialog(this, timePickerListener, hour, minute, true);
		// set current time into textview
		timeTextView.setText(hour+ ":" +minute);
		thisNotification.setNotificationHour(hour);
		thisNotification.setNotificationMinute(minute);
	}
	
	public void setOldTimeOnView()
	{
		timeTextView = (TextView) this.findViewById(R.id.notification_time_set);
		
		picker = new TimePickerDialog(this, timePickerListener, hour, minute, true);
		timeTextView.setText(hour+ ":" +minute);
	}
	

	
	public void setNotificationTime(View v)
	{
		picker.show();
	}
	
	public void repeatNotification(View v)
	{
		showRepeatDialog();
	}
	
	public void createNotification(View v)
	{
		notificationTitle = (TextView) findViewById(R.id.notification_title_editText);
		notificationText = (TextView) findViewById(R.id.notification_text_editText);
		int nextWeekDay = 1;
		
		thisNotification.setNotificationTitle(notificationTitle.getText().toString());
		thisNotification.setNotificationText(notificationText.getText().toString());
		c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, thisNotification.getNotificationHour());
		c.set(Calendar.MINUTE, thisNotification.getNotificationMinute());
		c.set(Calendar.SECOND, 0);
		
		/**
		 * Sort the repeatList and add the notification to the database
		 */
		Collections.sort(repeatList);
		thisNotification.setNotificationRepeat(repeatList);
		
		if(!isEdit)
			thisNotification.addNotification();
		else
			thisNotification.updateNotification();
		
		/**
		 * If repeatList has values, the notification should repeat.
		 * Else, it should only notify today.
		 */
		if(!repeatList.isEmpty())
		{
			
			nextWeekDay = repeatList.get(0);
			for(int i=0;i<repeatList.size();i++)
			{
				if(c.get(Calendar.DAY_OF_WEEK)<=(repeatList.get(i)+1))
				{
					nextWeekDay = repeatList.get(i);
					i = repeatList.size();
				}
			}
			if(nextWeekDay != 7)
				nextWeekDay++;
			else
				nextWeekDay = 1;
		}
		else{
			nextWeekDay = c.get(Calendar.DAY_OF_WEEK);
		}
		
		/**
		 * Calculate how many days it is until next notification goes off
		 */
		if(nextWeekDay < c.get(Calendar.DAY_OF_WEEK)){
			nextWeekDay = (7-c.get(Calendar.DAY_OF_WEEK))+nextWeekDay;
		}
		else
		{
			nextWeekDay = nextWeekDay-c.get(Calendar.DAY_OF_WEEK);
		}
		
		/**
		 * Add the remaining days until next notification
		 */
		c.add(Calendar.DAY_OF_WEEK, nextWeekDay);
		Log.d("nextDay", "is: "+c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH));
		
		Intent mServiceIntent = new Intent(this, NotificationHandler.class);
		mServiceIntent.putExtra("title", thisNotification.getNotificationTitle());
		if(thisNotification.getNotificationText() != "")
			mServiceIntent.putExtra("text", thisNotification.getNotificationText());
		else
			mServiceIntent.putExtra("text", "Default Reminder");
		
		mServiceIntent.putExtra("nrOfNots", thisNotification.getNotificationID());
		mServiceIntent.putExtra("calendarDefinition", Calendar.DAY_OF_WEEK);
		mServiceIntent.putExtra("calendarValue", 5);
		mServiceIntent.putIntegerArrayListExtra("repeatList", (ArrayList<Integer>) repeatList);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) thisNotification.getNotificationID(), mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		//ADD SOME ID OR SOMETHING!!!
		
		AlarmManager alarmManager = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
		
		User.getInstance().updateNotificationList();
		Intent intent = new Intent(this, HomeActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //set flag on intent to clear history stack of all activities
    	
    	startActivity(intent);
		finish();
	}
	
	/**
	 * Callbackfunction of the timepicker
	 * will set hour and minute accordingly.
	 */
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			
			// set current time into textview
			timeTextView.setText(hour+" h:"+minute+" m");
			
			thisNotification.setNotificationHour(hour);
			thisNotification.setNotificationMinute(minute);
		}
	};
	
	/**
	 * Shows a dialog to repeat days, will show Monday-Sunday in a list and add those checked into the
	 * List repeatList.
	 */
	public void showRepeatDialog()
	{
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.notification_repeat_list);
		dialog.setTitle("Set repeat");
		
		LinearLayout repeatLayout = (LinearLayout) dialog.findViewById(R.id.repeat_layout_id);
		
		final Calendar weekdays = Calendar.getInstance();
		weekdays.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		for(int i=1; i<8;i++)
		{
			CheckBox cb = new CheckBox(this);
            cb.setText(weekdays.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
            cb.setId(i);
            if(repeatList.contains(i))
            	cb.setChecked(true);
            repeatLayout.addView(cb);
            weekdays.add(Calendar.DAY_OF_WEEK, 1);
		}
		
		Button repeatButton = (Button) dialog.findViewById(R.id.repeatButton);
		repeatButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String Days = "";
				weekdays.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				for(int i=1;i<8;i++)
				{
					
					CheckBox cb = (CheckBox) dialog.findViewById(i);
					if(cb.isChecked()){
						Days += weekdays.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH)+" ";
						if(!repeatList.contains(i))
							repeatList.add(i);
					}
					else
					{
						if(repeatList.contains(i))
							repeatList.remove(repeatList.indexOf(i));
					}
					weekdays.add(Calendar.DAY_OF_WEEK, 1);
				}
				if(Days != "")
					repeatTextView.setText(Days);
				else
					repeatTextView.setText("Never");
				
				dialog.dismiss();
			}
		});
		
		dialog.show();		
	}
}
