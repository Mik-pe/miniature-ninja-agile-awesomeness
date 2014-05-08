package tson_utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.tson.HomeActivity;
import com.example.tson.R;
import com.example.tson.R.drawable;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
/**
 *  Class to handle notifications, parse data and make them show after a while
 * Will need inputs sent with Intent argument.
 * @author mikpe201 and malky217
 *
 */
public class NotificationHandler extends BroadcastReceiver
{
	/***********************
	  *  	VARIABLES		*/	
	 /***********************/
	String notificationTitle;
	String notificationText;
	int nrOfNots;
	long timeUntilNextDate;
	int calendarDefinition;
	int calendarValue;
	int nextWeekDay;
	List<Integer> repeatList;
	
	
	 /***********************
	  *  	OTHERS			*/	
	 /************************/
	/**
	 *  
	 * @param arg0 - context the notification is sent from
	 * @param arg1 - intent of the notification
	 */
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		notificationTitle = arg1.getStringExtra("title");
		notificationText = arg1.getStringExtra("text");
		nrOfNots = arg1.getIntExtra("nrOfNots", 0);
		timeUntilNextDate = arg1.getLongExtra("timeUntilNextDate", 0);
		calendarDefinition = arg1.getIntExtra("calendarDefinition", 0);
		
		//TODO
		calendarValue = arg1.getIntExtra("calendarValue", 0);
		repeatList = arg1.getIntegerArrayListExtra("repeatList");
		

			
		Log.d("logging", "HERE");
		/**
		 * if repeatList is empty, the notification should not be repeated
		 */
		if(!repeatList.isEmpty())
		{
			Calendar notificationCalendar = Calendar.getInstance();
			
			Intent mServiceIntent = new Intent(arg0, NotificationHandler.class);
			/**
			 * Values of the class are put in to keep them until the next call.
			 */
			mServiceIntent.putExtra("title", notificationTitle);
			if(notificationText != "")
				mServiceIntent.putExtra("text", notificationText);
			else
				mServiceIntent.putExtra("text", "Default Reminder");
			
			mServiceIntent.putExtra("nrOfNots", nrOfNots);
			mServiceIntent.putExtra("timeUntilNextDate", (timeUntilNextDate));
			mServiceIntent.putExtra("calendarDefinition", calendarDefinition);
			mServiceIntent.putExtra("calendarValue",calendarValue);
			mServiceIntent.putIntegerArrayListExtra("repeatList", (ArrayList<Integer>) repeatList);
			
			nextWeekDay = repeatList.get(0);
			for(int i=0;i<repeatList.size();i++)
			{
				if(notificationCalendar.get(Calendar.DAY_OF_WEEK)<(repeatList.get(i)+1))
				{
					nextWeekDay = repeatList.get(i);
					i = repeatList.size();
				}
				Log.d("logging", "HERE"+nextWeekDay+notificationCalendar.get(Calendar.DAY_OF_WEEK));
			}
			if(nextWeekDay != 7)
				nextWeekDay++;
			else
				nextWeekDay = 1;
			
			if(nextWeekDay < notificationCalendar.get(Calendar.DAY_OF_WEEK)){
				nextWeekDay = (7-notificationCalendar.get(Calendar.DAY_OF_WEEK))+nextWeekDay;
			}
			else
			{
				nextWeekDay = nextWeekDay-notificationCalendar.get(Calendar.DAY_OF_WEEK);
				if(nextWeekDay == 0)
				{
					nextWeekDay = 7;
				}
			}
			Log.d("logging", "HERE"+nextWeekDay);
			notificationCalendar.add(calendarDefinition, nextWeekDay);
			/**
			 * getBroadcast will queue the pendingIntent for this handler
			 * AlarmManager will be set inside the function for recursive calls.
			 */
			PendingIntent pendingIntent = PendingIntent.getBroadcast(arg0, nrOfNots, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager)arg0.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, notificationCalendar.getTimeInMillis(), pendingIntent);			
		}		
		
		showNotification(arg0);
	}
	
	/**
	 * Function to show the notification, will be called at certain times from the onReceive function.
	 * @param context - the activity context the notification was sent from
	 */
	protected void showNotification(Context context) {
		Intent intent = new Intent(context, HomeActivity.class);
		
		
		
		long[] vibraPattern = {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
		NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle(notificationTitle);
		mBuilder.setContentText(notificationText);
		mBuilder.setVibrate(vibraPattern);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setContentIntent(PendingIntent.getActivity(context, nrOfNots, intent, 0));
		mBuilder.setAutoCancel(true);
		
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(0, mBuilder.build());
		
	}



}

