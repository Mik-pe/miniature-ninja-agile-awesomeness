package com.example.tson;

import tson_utilities.User;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.model.people.Person;

public class LoginActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
				
        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, null)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		ImageButton btnSignIn = (ImageButton) findViewById(R.id.login_button);
		btnSignIn.setOnClickListener(new View.OnClickListener() {

	   @Override
	   public void onClick(View v) {
	 
	       // Sign in button clicked
		   if(!mGoogleApiClient.isConnected()){
	           	signInWithGplus();
	   			}
	       }
		});  	
	}//End onCreate
	
	/*******************************************************************************************
	  *  Google+ Log in		
	  * Tutuorial: http://www.androidhive.info/2014/02/android-login-with-google-plus-account-1/
	  * Authors: Anton, Sofie, Per
	 /********************************************************************************************/
	
	private static final int RC_SIGN_IN = 0;
   // Profile pic image size in pixels
   private static final int PROFILE_PIC_SIZE = 400;
   // Google client to interact with Google API
   private GoogleApiClient mGoogleApiClient;

   /**
    * A flag indicating that a PendingIntent is in progress and prevents us
    * from starting further intents.
    */
   private boolean mIntentInProgress; 
   private boolean mSignInClicked;
   private ConnectionResult mConnectionResult;

   protected void onStart() {
       super.onStart();
       mGoogleApiClient.connect();
   }

   protected void onStop() {
       super.onStop();
       if (mGoogleApiClient.isConnected()) {
           mGoogleApiClient.disconnect();
       }
   }
   
   @Override
   public void onConnectionFailed(ConnectionResult result) {
       if (!result.hasResolution()) {
           GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                   0).show();
           return;
       }
    
       if (!mIntentInProgress) {
           // Store the ConnectionResult for later usage
           mConnectionResult = result;
    
           if (mSignInClicked) {
               // The user has already clicked 'sign-in' so we attempt to
               // resolve all
               // errors until the user is signed in, or they cancel.
               resolveSignInError();
           }
       }    
   }
    
   @Override
   protected void onActivityResult(int requestCode, int responseCode,
           Intent intent) {
       if (requestCode == RC_SIGN_IN) {
           if (responseCode != RESULT_OK) {
               mSignInClicked = false;
           }
    
           mIntentInProgress = false;
    
           if (!mGoogleApiClient.isConnecting()) {
               mGoogleApiClient.connect();
           }
       }
   }
    
   @Override
   public void onConnected(Bundle arg0) {
       mSignInClicked = false;
 
       //Get account info
       if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
           Person currentPerson = Plus.PeopleApi
                   .getCurrentPerson(mGoogleApiClient);

           String personName = currentPerson.getDisplayName();
           String personPhotoUrl = currentPerson.getImage().getUrl();
           String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
           
           personPhotoUrl = personPhotoUrl.substring(0,
                   personPhotoUrl.length() - 2)
                   + PROFILE_PIC_SIZE;
           
           //Store account information in db
           //User user = new User(email, personName, "");
           
           SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
           Editor editor = pref.edit();
           editor.putString("personName", personName); // Storing string
           editor.putString("personPhotoUrl", personPhotoUrl); 
           editor.putString("email", email); 
           editor.commit(); // commit changes
           
           //Print to HomeActivity
           personName = personName + " is connected!";
           Toast.makeText(this, personName, Toast.LENGTH_LONG).show();
	   }
	   else{
		   Log.d("myname", "is null");
	   }
   
       final Intent intent = new Intent(this, HomeActivity.class);
       startActivity(intent);
	   finish();    
   }
    
   @Override
   public void onConnectionSuspended(int arg0) {
       mGoogleApiClient.connect();
   }
   
   /**
    * Sign-in into google
    * */
   private void signInWithGplus() {
       if (!mGoogleApiClient.isConnecting()) {
           mSignInClicked = true;
           resolveSignInError();          
       }
      
   }
    
   /**
    * Method to resolve any sign in errors
    * */
   private void resolveSignInError() {
       if (mConnectionResult.hasResolution()) {
           try {
               mIntentInProgress = true;
               mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
           } catch (SendIntentException e) {
               mIntentInProgress = false;
               mGoogleApiClient.connect();
           }
       }
   }
}//End LoginActivity
