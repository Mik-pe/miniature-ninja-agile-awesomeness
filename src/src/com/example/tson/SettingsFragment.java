package com.example.tson;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.MyNotification;
import tson_utilities.NotificationHandler;
import tson_utilities.Project;
import tson_utilities.TimeBlock;

import tson_utilities.User;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

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
import android.view.MotionEvent;
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
import android.widget.ListAdapter;
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
	Button revokeButton;

	int nrOfNotifications;
	List<MyNotification> notificationList;
	notificationAdapter notiAdapter;
	int holder = 0;
	int hour, min, newHour, newMin;
	ListView notificationListView;
	/**SHARED PREFERENCES*/
	public static final String PREFS_NAME = "MyPrefsFile";

	User user = User.getInstance();
	

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
		//getActivity();
		notificationCal = Calendar.getInstance();
		
		client = LoginActivity.getmGoogleApiClient();
		if (!client.isConnected()) {
			LoginActivity.firstTime = false;
			client.connect();
	       }


		
		//Set account info in Settings
		ImageView imgProfilePic = (ImageView) settings.findViewById(R.id.imageView1);
		new LoadProfileImage(imgProfilePic).execute(user.getPicURL());	
		meName = (TextView) settings.findViewById(R.id.meName);	
		meName.setText(user.getName());
		meEmail = (TextView) settings.findViewById(R.id.meEmail);	
		meEmail.setText(user.getEmail());
		notificationListView = (ListView) settings.findViewById(R.id.notificationListView);

		notificationList = User.getInstance().getNotificationList();
		notificationListView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dpToPx(65)*notificationList.size()));
	
		manageProjectsButton = (Button) settings.findViewById(R.id.manage_projects_button);
		addNotificationButton = (Button) settings.findViewById(R.id.addNotification);
		logoutButton = (Button) settings.findViewById(R.id.log_out_button);
		revokeButton = (Button) settings.findViewById(R.id.revoke_access_button);
		
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
				showLogoutDialog(v);
			}
		});
		
		revokeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showRevokeDialog(v);
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
       if (client.isConnected()) {
           Plus.AccountApi.clearDefaultAccount(client);
           client.disconnect();
           client.connect();
       }
   }

   /**
    * Revoking access from google
    * */
   private void revokeGplusAccess() {
       if (client.isConnected()) {
           Plus.AccountApi.clearDefaultAccount(client);
           Plus.AccountApi.revokeAccessAndDisconnect(client)
                   .setResultCallback(new ResultCallback<Status>() {
                       @Override
                       public void onResult(Status arg0) {
                           Log.e("try", "User access revoked!");
                           client.connect();
                       }

                   });
       }
   }
   
   public void showLogoutDialog(View v)
   {
  		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
  		//Add title
  		builder.setTitle(R.string.title_logout);
  		
  		
  		//Add the buttons 		
  		builder.setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() 
	   	{	   	
		    // User clicked OK button - Go to submission page       
		   	public void onClick(DialogInterface dialog, int id) 
		   	{   	        	   
				signOutFromGplus();
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
		   	} 	
      });
	   //Cancel button close the dialog and go back to settings screen
	   builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() 
	   {
          public void onClick(DialogInterface dialog, int id) 
          {
              
          }
	   });
	   
		
	   	// Create the AlertDialog
	   	AlertDialog dialog = builder.create();
	   	dialog.show();
	   	
   }//End Dialog confirm reported time
   

   public void showRevokeDialog(View v)
   {
  		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
  		//Add title
  		builder.setTitle(R.string.title_revoke);
  		
  		
  		//Add the buttons 		
  		builder.setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() 
	   	{	   	
		    // User clicked OK button - Go to submission page       
		   	public void onClick(DialogInterface dialog, int id) 
		   	{   	        	   
				revokeGplusAccess();
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
		   	} 	
      });
	   //Cancel button close the dialog and go back to settings screen
	   builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() 
	   {
          public void onClick(DialogInterface dialog, int id) 
          {
              
          }
	   });
	   
		
	   	// Create the AlertDialog
	   	AlertDialog dialog = builder.create();
	   	dialog.show();
	   	
   }//End Dialog confirm reported time
   

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
	final int posi = position;
	notificationEditText.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), CreateNotificationActivity.class);
			intent.putExtra("notificationTitle"	, notificationList.get(posi).getNotificationTitle());
			intent.putExtra("notificationText"	, notificationList.get(posi).getNotificationText());
			intent.putExtra("notificationHour"	, notificationList.get(posi).getNotificationHour());
			intent.putExtra("notificationMinute", notificationList.get(posi).getNotificationMinute());
			intent.putExtra("notificationID"	, notificationList.get(posi).getNotificationID());
			intent.putIntegerArrayListExtra("notificationRepeat", (ArrayList<Integer>) notificationList.get(posi).getNotificationRepeat());
			
			startActivity(intent);
		}
	});

	return view;
    }

   }   
		
}
