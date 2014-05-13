package com.example.tson;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.MyNotification;
import tson_utilities.NotificationHandler;
import tson_utilities.Project;
import tson_utilities.User;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import android.widget.ImageView;
import android.widget.ExpandableListView;


import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.LinearLayout.LayoutParams;

public class SettingsFragment extends Fragment{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	View settings;
	TextView meName;

	TextView notificationEditText;
	TextView timeTextView;

	TextView meEmail;

	Calendar notificationCal;
	Button manageProjectsButton;
	Button addNotificationButton;
	Button logoutButton;

	int nrOfNotifications;
	List<MyNotification> notificationList;
	notificationAdapter notiAdapter;
	int holder = 0;
	int hour, min, newHour, newMin;
	ListView notificationListView;
	/**SHARED PREFERENCES*/
	public static final String PREFS_NAME = "MyPrefsFile";

	User user = User.getInstance();
	
	
	/*
	 * Googleshit
	 */

	private GoogleApiClient client;
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
		
		client = LoginActivity.getmGoogleApiClient();
		if (client.isConnected()) {
	           Log.d("try", "connected1");
	       }

		
		//Set account info in Settings
		ImageView imgProfilePic = (ImageView) settings.findViewById(R.id.imageView1);
		new LoadProfileImage(imgProfilePic).execute(user.getPicURL());	
		meName = (TextView) settings.findViewById(R.id.meName);	
		meName.setText(user.getName());
		meEmail = (TextView) settings.findViewById(R.id.meEmail);	
		meEmail.setText(user.getEmail());
		notificationListView = (ListView) settings.findViewById(R.id.notificationListView);
		
		notificationListView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dpToPx(55)*notificationList.size()));
		/**
		* Static defaultnotifications, not saved internally
		*/
		notificationList = new ArrayList<MyNotification>();	
	
		manageProjectsButton = (Button) settings.findViewById(R.id.manage_projects_button);
		addNotificationButton = (Button) settings.findViewById(R.id.addNotification);
		logoutButton = (Button) settings.findViewById(R.id.log_out_button);
		manageProjectsButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ManageProjectsActivity.class);
				startActivity(intent);	
			}
		});
		
		addNotificationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CreateNotificationActivity.class);
				startActivity(intent);
			}
		});
		
		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("try", "logout pressed");
				signOutFromGplus();
				
			}
		});
		
	notiAdapter = new notificationAdapter();
	notificationListView.setAdapter(notiAdapter);
	return settings;
	}//End OnCreate
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
   
   /**
	 * Calculates the dp value to pixels
	 * @author Ramin Assadi
	 * @param dp The value of dp
	 * @return Returns the converted pixel value
	 */
	public static int dpToPx(int dp)
	{
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}
   
   /**
    * Sign-out from google
    * */
   private void signOutFromGplus() {
	   Log.d("try", "insignout1");
       if (client.isConnected()) {
    	   Log.d("try", "insignout2");
           Plus.AccountApi.clearDefaultAccount(client);
           client.disconnect();
           client.connect();
       }
   }

   /**
    * Revoking access from google
    * */
 /*  private void revokeGplusAccess() {
       if (client.isConnected()) {
           Plus.AccountApi.clearDefaultAccount(client);
           Plus.AccountApi.revokeAccessAndDisconnect(client)
                   .setResultCallback(new ResultCallback<Status>() {
                       @Override
                       public void onResult(Status arg0) {
                           Log.e(TAG, "User access revoked!");
                           client.connect();
                           updateUI(false);
                       }

                   });
       }
   }*/
   
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
    TimePickerDialog picker = new TimePickerDialog(getActivity(), timeSetListener, newHour, newMin, true);
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
			//TODO Auto-generated method stub
			showInputDialog( notificationEditText);
		}
	});
	return view;
    }

   }   
		
}
