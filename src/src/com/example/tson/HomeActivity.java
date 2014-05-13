package com.example.tson;

//IMPORT OUR OWN CLASSES
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

import model.NavDrawerItem;
import tson.sqlite.helper.DatabaseHelper;
import tson_utilities.MyNotification;
import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;
import adapter.NavDrawerListAdapter;
import android.app.ActionBar;
import android.content.SharedPreferences;

import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


//IMPORT ANDROID
//IMPORT ANDROID
//IMPORT OTHER

public class HomeActivity extends FragmentActivity
{
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	/**SHARED PREFERENCES*/
	public static final String PREFS_NAME = "MyPrefsFile";
	
	int hour,min, newHour, newMin;
	int holder = 0;
	static final int TIME_DIALOG_ID=0;
	String[] hourmin;
	View currentPage;
	ListView projectListView;
	ActionBar ab;
	private static Calendar c;
	//Typeface tf = Typeface.createFromAsset(getAssets(), "assets/fontawesome-webfont.ttf");
	
	//DATABASE
	public static DatabaseHelper db;
	List<Project> projectList;
	public User user = null;

	
	 //Fetch Google+ data for input
	 /*SharedPreferences pref =  getApplicationContext().getSharedPreferences("MyPref", 0);
	 String personName = pref.getString("personName", null); // getting String
	 String email = pref.getString("email", null);*/

	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	//nav drawer title
	private CharSequence mDrawerTitle;

	//used to store app title
	private CharSequence mTitle;

	//slide menu items
	private String[] navMenuTitles;
	//private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	

	
	

	 /***********************
	  *  	OTHERS			*/	
	 /************************/

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Calendar.getInstance().setFirstDayOfWeek(Calendar.MONDAY);
		c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
	    db = new DatabaseHelper(getApplicationContext());
	    
	    if(user == null){
	    	
			 //Fetch Google+ data for input
			 SharedPreferences pref =  getApplicationContext().getSharedPreferences("MyPref", 0);
			 String personName = pref.getString("personName", null); // getting String
			 String personPhotoUrl = pref.getString("personPhotoUrl", null);
			 String email = pref.getString("email", null);
	    	
	    	Log.d("User insertion", "USER IS NULL CREATE NEW");
	    	user = db.createUser(email, personName, personPhotoUrl);
	    }
	    db.getAllProjects(user);
	    db.getAllTimeBlocks();
	    db.logTimeblocks();
	    projectList = db.getAllProjects(user);
	    user.getProjects().clear();
	    
	    List<MyNotification> notificationList = db.getNotifications();
	    user.setNotificationList(notificationList);
		
        for (int i = 0; i < projectList.size(); i++)
        {
	        user.addProject(projectList.get(i));
	        user.getProjects().get(i).setSubmissionList(db.getTimeBlocksByProject(user.getProjects().get(i)));	       	       
        }

	        
       
		
	

	
    mTitle = mDrawerTitle = getTitle();

	//load slide menu items
	navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

	mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

	navDrawerItems = new ArrayList<NavDrawerItem>();
	Log.d("navMenu", Integer.toString(navDrawerItems.size()));
	//adding nav drawer items to array
	//Home
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
	//Find People
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
	//Photos
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
	//Communities, Will add a counter here
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], true, "22"));
	//Pages
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
	Log.d("navMenu", Integer.toString(navDrawerItems.size()));


	mDrawerList.setOnItemClickListener(new SlideMenuClickListener());	

	//setting the nav drawer list adapter
	adapter = new NavDrawerListAdapter(getApplicationContext(),
			navDrawerItems);
	mDrawerList.setAdapter(adapter);

	//enabling action bar app icon and behaving it as toggle button
	getActionBar().setDisplayHomeAsUpEnabled(true);
	getActionBar().setHomeButtonEnabled(true);

	mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			R.drawable.ic_drawer, //nav menu toggle icon
			R.string.app_name, // nav drawer open - description for accessibility
			R.string.app_name // nav drawer close - description for accessibility
			) {
		public void onDrawerClosed(View view) {
			getActionBar().setTitle(mTitle);
			//calling onPrepareOptionsMenu() to show action bar icons
			invalidateOptionsMenu();
		}

		public void onDrawerOpened(View drawerView) {
			getActionBar().setTitle(mDrawerTitle);
			//calling onPrepareOptionsMenu() to hide action bar icons
			invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	
		if (savedInstanceState == null) {
			//on first time display view for first nav item
			displayView(0);
		}

	
	}//End onCreate-function
	
	/**
	 * Getter of Calendar from the Homeactivity
	 * @return the date of HomeActivity
	 */
	public static Calendar getCal(){
		return c;	
	}
	
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements

	ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//display view for selected nav drawer item
			displayView(position);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;}
			else 
				return false;
		}

	

	

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
		private void displayView(final int position) {
		//update the main content by replacing fragments
		//PreviousFragment is nollstalld because otherwise skulle inte navigationen med backbutton work.
		HomeFragment.previousFragment = "";
		Fragment fragment = null;
		
    	ab = getActionBar();
		ab.removeAllTabs();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new SubmissionFragment();
			break;
		case 2:
			fragment = new StatisticsFragment();
			break;
		case 3:
			fragment = new ExportFragment();
			break;
		case 4:
			fragment = new SettingsFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();

			//update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			//error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}		
	

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		//Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed()
	{
		if(HomeFragment.previousFragment == "Submission")
		{
			Fragment fragment = new SubmissionFragment();
			
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();
			
			HomeFragment.previousFragment = "";
			
		}
		else
			super.onBackPressed();
	}
	
	 
}//End HomeActivity
