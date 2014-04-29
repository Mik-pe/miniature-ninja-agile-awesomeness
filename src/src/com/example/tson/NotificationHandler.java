package com.example.tson;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationHandler extends IntentService
{
	
	public NotificationHandler() {
		super("NotificationHandler");
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		long[] vibraPattern = {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
		NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(this);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle(arg0.getStringExtra("title"));
		mBuilder.setContentText(arg0.getStringExtra("text"));
		mBuilder.setVibrate(vibraPattern);
		Log.d("logged", ""+arg0.getStringExtra("title"));
		Intent notificationIntent = new Intent(this, HomeActivity.class);
		mBuilder.setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0));
		mBuilder.setAutoCancel(true);
		
		
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(0, mBuilder.build());
		
	}

}

