/**
 * @author Pär Eriksson
 * A database helper class that takes care of all interaction with the SQLite database
 */
package tson.sqlite.helper;

import java.util.ArrayList;
import java.util.List;


import tson_utilities.Project;
import tson_utilities.TimeBlock;

import java.util.Calendar;
import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

	//Logcat tag
	private static final String LOG = "DatabaseHelper";
	
	//Database Version
	private static final int DATABASE_VERSION = 31;
		
	//Database Name
	private static final String DATABASE_NAME = "timeManager.db";
	
	//Table Names
	private static final String TABLE_USER = "users";
	private static final String TABLE_PROJECT = "projects";
	private static final String TABLE_TIME_BLOCK = "time_blocks";
	private static final String TABLE_NOTIFICATION = "notifications";
	
	// Common column names
	private static final String KEY_ID = "id";	
	
	//USERS Table
	private static final String KEY_USER_EMAIL = "email";
	private static final String KEY_USER_NAME = "name";
	private static final String KEY_USER_PICTURE = "picture";
	
	//PROJECTS Table
	private static final String KEY_PROJECT_NAME = "project_name";
	private static final String KEY_PROJECT_USER_ID = "user_id";

	//TIME BLOCKS TABLE
	private static final String KEY_TIME_BLOCK_PROJECT_ID = "project_id";
	private static final String KEY_TIME_BLOCK_YEAR = "year";
	private static final String KEY_TIME_BLOCK_MONTH = "month";
	private static final String KEY_TIME_BLOCK_DAY = "day";
	private static final String KEY_TIME_BLOCK_MINUTES = "minutes";
	private static final String KEY_TIME_BLOCK_CONFIRMED = "confirmed";
	
	// NOTIFICATIONS TABLE
	private static final String KEY_NOTIFICATION_USER_ID = "user_id";
	private static final String KEY_NOTIFICATION_TITLE = "title";
	private static final String KEY_NOTIFICATION_TEXT = "text";
	private static final String KEY_NOTIFICATION_HOUR = "hour";
	private static final String KEY_NOTIFICATION_MINUTE = "minute";
	//flags for the day of the week (ugly! should be normalized)
	private static final String KEY_NOTIFICATION_WEEK_DAYS = "weekdays";
	
	
	

	
	// Table Create Statements
	
	private static final String CREATE_TABLE_USER = "CREATE TABLE " 
			+ TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_EMAIL
			+ " TEXT," + KEY_USER_NAME + " TEXT," + KEY_USER_PICTURE + " TEXT" + ")";
	
	// Project table create statement
	private static final String CREATE_TABLE_PROJECT = "CREATE TABLE "
			+ TABLE_PROJECT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PROJECT_USER_ID +" INTEGER,"
			+KEY_PROJECT_NAME+ " TEXT" + ")";

	private static final String CREATE_TABLE_TIME_BLOCK = "CREATE TABLE "
			+ TABLE_TIME_BLOCK + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME_BLOCK_PROJECT_ID + " INTEGER," + KEY_TIME_BLOCK_YEAR + " INTEGER,"
			+ KEY_TIME_BLOCK_MONTH + " INTEGER," + KEY_TIME_BLOCK_DAY + " INTEGER," + KEY_TIME_BLOCK_MINUTES + " INTEGER,"  + KEY_TIME_BLOCK_CONFIRMED + " INTEGER"  +")";
	
	private static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE "
			+ TABLE_NOTIFICATION + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOTIFICATION_USER_ID + " INTEGER,"
			+ KEY_NOTIFICATION_TITLE + " TEXT," + KEY_NOTIFICATION_TEXT + " TEXT," + KEY_NOTIFICATION_HOUR + " INTEGER,"
			+ KEY_NOTIFICATION_MINUTE + " INTEGER," + KEY_NOTIFICATION_WEEK_DAYS + " INTEGER" + ")";
	
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_PROJECT);
		db.execSQL(CREATE_TABLE_TIME_BLOCK);
		db.execSQL(CREATE_TABLE_NOTIFICATION);
		
		//IF MORE TABLES: OTHER TABLES WILL ALSO BE EXECUTED		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.d("UPGRADE DATABASE", "!!!!");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME_BLOCK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
		// IF MORE TABLES: OTHER TABLES WILL ALSO BE EXECUTED		
	
		//Create new tables
		onCreate(db);
	}
	
	
	//========================================================
	// USER ==================================================
	//========================================================
	
	public User createUser(String email, String name, String picURL){
		Log.d("User insertion", "INNEEEEE");
		
		
		User user = getUserByEmail(email);
		long user_id;
		if(user == null) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_USER_EMAIL, email);
			values.put(KEY_USER_NAME, name);
			values.put(KEY_USER_PICTURE, picURL);
			//insert row
			
			user_id = db.insert(TABLE_USER, null, values);
			user = User.getInstance();
			Log.d("User insertion", "found no user by email :" + user_id);
			user.setId(user_id);
			user.setEmail(email);
		}
		//assigning tags to project	

		user.setName(name);
		user.setPictureURL(picURL);
		return user;		
	}
	
	public User getUserByEmail(String email){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE "
				+ KEY_USER_EMAIL + " = " + "'" + email + "'";
		//Log.e(LOG, selectQuery);
		Log.e("User insertion", "getUserByEmail:   " + selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);
			
		if(c != null)
			c.moveToFirst();
		
		if(c.getCount()==0){
			Log.d("User insertion", "getUserByEmail ---> user not found, CREATE!");
			return null;
		}
		
		Log.d("User insertion", "cursor " + c.getCount());	
		
		//Set all attributes before returning it
		// Create a project out of the row name
		User user = User.getInstance();
		user.setEmail(c.getString(c.getColumnIndex(KEY_USER_EMAIL)));
		user.setId(c.getLong(c.getColumnIndex(KEY_ID)));
		
		Log.d("User insertion", "getUserByEmail   ->" +User.getInstance().getEmail() + "   with id = " + User.getInstance().getID());
		
		return user;	
	}
	
	
	
	
	//========================================================
	// PROJECT ===============================================
	//========================================================

	/**
	 * Creating a project.
	 * @param project - the project object.
	 * @param tag_ids - the tags, e.g. name.
	 * @return - returns the id of the row.
	 */
	public long createProject(Project project, User user)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		
		
		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_NAME, project.getName());
		values.put(KEY_PROJECT_USER_ID, user.getID());
		
		//insert row
		long project_id = db.insert(TABLE_PROJECT, null, values);
		Log.d("User insertion create" ,"createProject-> userID: " + user.getID()+" projectId: " + project_id);
		//Set project id (THIS IS IMPORTANT)
		project.setId(project_id);
		//assigning tags to project	
		return project_id;		
	}
	
	
	/**
	 * Updating the name of a project.
	 * @param p - the project object.
	 * @param newName - the specified new name.
	 * @return - returns an indicator if the row was updated.
	 */
	public long updateProjectName(String newName, Project p)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_NAME, newName);
		
		// This will be send as a parameter to db.update
		Log.d("Kommer vi hit", "TJENARE!");
		
		String[] args = new String[]{String.valueOf(p.getId())};
		//Update row
		return db.update(TABLE_PROJECT, values, KEY_ID + " = ?",
				args);
	}
	
	/**
	 * Get a single project by an ID.
	 * @param project_id.
	 * @return A new project with the key name.
	 */
	public Project getProject(long project_id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_PROJECT + " WHERE "
				+ KEY_ID + " = " + project_id;
		//Log.e(LOG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);
				
		if(c != null)
			c.moveToFirst();
		
		//Set all attributes before returning it
		// Create a project out of the row name
		Project p = new Project(c.getString(c.getColumnIndex(KEY_PROJECT_NAME)));
		
		return p;
	}
	
	/**
	 * Select all projects.
	 * @param user - a User instance
	 * @return List<Project> 
	 */
	public List<Project> getAllProjects(User user)
	{
		List<Project> projects = new ArrayList<Project>();
		String selectQuery = "SELECT * FROM " + TABLE_PROJECT + " WHERE "
				 + KEY_PROJECT_USER_ID + " = " + user.getID();
		
		Log.e("User insertion", "getAllProjects:   " + selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		//Loop through all rows in the table and add to the list
		if(c.moveToFirst())
		{
			do
			{
				Project p = getProject(c.getInt(c.getColumnIndex(KEY_ID)));
				p.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				Log.d("User insertion", "Getting project " + p.getName() + " for mail " + user.getEmail() + " rowcount: " + p.getId());
				// Add to the projects list that will be returned
				projects.add(p);
			}while (c.moveToNext());
		}
		return projects;
	}
	/**
	 * Get the id of a project by it's name (which should be unique).
	 * @param project_name - String.
	 * @param user - User, get project by name need to be dependent on the user as well.
	 * @return auto incremented id from the database.
	 */
	public long getProjectId(String project_name, User user){
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT "+ "*" + " FROM " + TABLE_PROJECT + " WHERE " + KEY_PROJECT_NAME + " = " + "'" + project_name + "'" + " AND " + KEY_PROJECT_USER_ID + " = " + user.getID();
		Cursor c = db.rawQuery(selectQuery, null);
		Log.e("User insertion", "getProjectId:   " + selectQuery);
		//Log.e(LOG, c.toString());
		if(c != null)
			c.moveToFirst();
		return c.getLong(0);
	}
	
	/**
	 * Delete a project by an id, usually called after getProjectId.
	 * @param project_id
	 */
	public void deleteProject(long project_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROJECT, KEY_ID + " = ?",
				new String[] {String.valueOf(project_id) });
	}
	
	
	
	
	
	
	
	
	//========================================================
	// TIME BLOCK ============================================
	//========================================================
	/**	
	 * Create a timeblock instance in the database.
	 * @param timeblock - TimeBlock object.
	 * @param project	- Project object.
	 * @return			- (long) id of the inserted row.
	 */
	public long createTimeBlock(TimeBlock timeblock, Project project)
	{
		//Log.d("createTB", "ok");
		SQLiteDatabase db = this.getWritableDatabase();

		Calendar d = timeblock.getDate();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TIME_BLOCK_PROJECT_ID, project.getId());
		values.put(KEY_TIME_BLOCK_MINUTES, timeblock.getTimeInMinutes());
		values.put(KEY_TIME_BLOCK_YEAR, d.get(Calendar.YEAR));
		values.put(KEY_TIME_BLOCK_MONTH, d.get(Calendar.MONTH));
		values.put(KEY_TIME_BLOCK_DAY, d.get(Calendar.DAY_OF_MONTH));
		values.put(KEY_TIME_BLOCK_CONFIRMED, timeblock.getConfirmed());
				
		//insert row
		long timeblock_id = db.insert(TABLE_TIME_BLOCK, null, values);
		timeblock.setID(timeblock_id);
		Log.d("User insertion", "createTimeBlock ->  timeblock_id" + timeblock_id + "  projectid " + project.getId());
		logTimeblocks();
		return timeblock_id;
		
	}
	/**
	 * Set a confirmation tag for a TimeBlock object, this indicates that the user has sumbitted a timeblock for a specific date.
	 * @param TimeBlock object.
	 * @return an int indicator if the row was updated.
	 */
	public int setConfirmed(TimeBlock timeblock)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		  
		    ContentValues values = new ContentValues();
		    values.put(KEY_TIME_BLOCK_CONFIRMED, timeblock.getConfirmed());
		    //String strFilter = "year=" + timeblock.getYear() 
		   
		    //updating row
		    Log.d("Set confirmed", "" + timeblock.getID() + " confirmed -> " + timeblock.getConfirmed());
		    return db.update(TABLE_TIME_BLOCK, values, KEY_ID + " = ?",
		            new String[] { String.valueOf(timeblock.getID()) });
	
	}
	/**
	 * Update a time block, only updates the time column for now.
	 * @param timeblock.
	 * @return indicator if the row was updated.
	 */
	public int updateTimeBlock(TimeBlock timeblock, Project p)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TIME_BLOCK_MINUTES, timeblock.getTimeInMinutes());
		
		// This will be send as a parameter to db.update
		String[] args = new String[]{String.valueOf(p.getId()), String.valueOf(timeblock.getYear()), String.valueOf(timeblock.getMonth()), String.valueOf(timeblock.getDay())};
		//Update row
		return db.update(TABLE_TIME_BLOCK, values, KEY_TIME_BLOCK_PROJECT_ID + " = ?" + " AND " +KEY_TIME_BLOCK_YEAR + " = ?" + " AND " + KEY_TIME_BLOCK_MONTH + " = ?" + " AND " + KEY_TIME_BLOCK_DAY + " = ?",
				args);
	}
	
	/**
	 * Get all the time blocks of all projects.
	 * @return List<TimeBlock> of all time blocks.
	 */
	public List<TimeBlock> getAllTimeBlocks()
	{
		List<TimeBlock> timeblocks = new ArrayList<TimeBlock>();
		String selectQuery = "SELECT * FROM " + TABLE_TIME_BLOCK;
		
		//Log.e(LOG, selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		//Loop through all rows in the table and add to the list
		if(c != null)
			if(c.moveToFirst())
			{
				do
				{
					int year = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_YEAR));
					int month = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_MONTH));
					int day = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_DAY));
					int minutes = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_MINUTES));
					int confirmed = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_CONFIRMED));
					int hours = (int)minutes/60;
					long id = c.getLong(c.getColumnIndex(KEY_ID));
					minutes = minutes-hours*60;
					TimeBlock t = new TimeBlock(year, month, day, hours, minutes);
					t.setID(id);
					t.setConfirmed(confirmed);
					timeblocks.add(t);
				}while (c.moveToNext());
			}
			return timeblocks;
	}
	
	/**
	 * Get all time blocks connected to a specific project.
	 * @param Project object.
	 * @return List<TimeBlock> of all time blocks for a specific project.
	 */
	public List<TimeBlock> getTimeBlocksByProject(Project p)
	{
		long pid = p.getId();
		String selectQuery = "SELECT * FROM " + TABLE_TIME_BLOCK + " WHERE "+ KEY_TIME_BLOCK_PROJECT_ID + " = " + pid;
		Log.e("User insertion", selectQuery);
		List<TimeBlock> timeblocks = new ArrayList<TimeBlock>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if(c != null)
			if(c.moveToFirst())
			{
				do
				{
					int year = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_YEAR));
					int month = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_MONTH));
					int day = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_DAY));
					int minutes = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_MINUTES));
					int hours = minutes/60;
					int confirmed = c.getInt(c.getColumnIndex(KEY_TIME_BLOCK_CONFIRMED));
					long id = c.getLong(c.getColumnIndex(KEY_ID));
					minutes = minutes-hours*60;
					TimeBlock t = new TimeBlock(year, month, day, hours, minutes);
					t.setConfirmed(confirmed);
					t.setID(id);
					timeblocks.add(t);
					Log.d("User insertion", "getTimeBlocksByProject ->  timeblock_id " + id + "  projectid " + p.getId());
				}while (c.moveToNext());
			}
			return timeblocks;
	}
	
	//========================================================
	// NOTIFICATOIN ==========================================
	//========================================================
	
	
	//title
	//text
	//timme
	//minut
	// days of week (bool)
	
	
	
	
	
	//TODOOOOOOO
	
	/*
	 * 
	 * 	private static final String KEY_NOTIFICATION_USER_ID = "user_id";
	private static final String KEY_NOTIFICATION_TITLE = "title";
	private static final String KEY_NOTIFICATION_TEXT = "text";
	private static final String KEY_NOTIFICATION_HOUR = "hour";
	private static final String KEY_NOTIFICATION_MINUTE = "minute";
	//flags for the day of the week (ugly! should be normalized)
	private static final String KEY_NOTIFICATION_WEEK_DAYS = "weekdays";
	 */
	public long createNotification(String title, String text, int hour, int minute, List<Integer> selectedDays)
	{
		
		SQLiteDatabase db = this.getWritableDatabase();
		//String temp = Arrays.toString(int[])
		int temp=0;
		for(int i = 0; i< selectedDays.size(); i++)
		{
			temp += selectedDays.get(i)*10^(selectedDays.size()-i-1);
		}
		
		Log.d("CreateNotification", " "+temp);
		
		
		ContentValues values = new ContentValues();

				
		//insert row

		return 0;
	}
	
	
	
	/**
	 * A log time function, not fully implemented.
	 */
	public void logTimeblocks()
	{	
		getAllTimeBlocks();
	}
	
	
	//Close database
	/**
	 * Close the database.
	 */
	public void closeDB()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		if(db!= null && db.isOpen())
			db.close();
	}

}
