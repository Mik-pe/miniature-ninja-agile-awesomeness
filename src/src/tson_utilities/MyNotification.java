package tson_utilities;

import java.util.ArrayList;
import java.util.List;
import com.example.tson.HomeActivity;

/**
 * 
 * @author mikpe201 & malky217
 *Class to handle Notification information
 *Has setters and getters
 *wallabaja
 */
public class MyNotification {
	
	/***********************
	  *  	VARIABLES		*/	
	 /***********************/
	private long notificationID; // id that correspond to the id row in database, this is set upon database save
	private String notificationTitle;
	private String notificationText;
	private int notificationHour;
	private int notificationMinute;
	private List<Integer> notificationRepeat;

	
	
	/***********************
	  *  	CONSTRUCTOR		*/	
	 /***********************/
	public MyNotification(String title, String text, long id, int hour, int min) 
	{
		setNotificationID(id);
		setNotificationText(text);
		setNotificationTitle(title);
		setNotificationHour(hour);
		setNotificationMinute(min);
	}
	/***********************
	  *  	OTHER		*/	
	 /***********************/
	
	public void addNotification()
	{
		HomeActivity.db.createNotification(this);
	}
	
	/***********************
	  *  	SETTERS		*/	
	 /***********************/
	
	public void setNotificationID(long notificationID) 
	{
		this.notificationID = notificationID;
	}
	
	public void setNotificationText(String notificationText) 
	{
		this.notificationText = notificationText;
	}
	
	public void setNotificationTitle(String notificationTitle) 
	{
		this.notificationTitle = notificationTitle;
	}
	
	public void setNotificationRepeat(List<Integer> notificationRepeat) {
		this.notificationRepeat = notificationRepeat;
	}
	
	public void setNotificationMinute(int notificationMinute) {
		this.notificationMinute = notificationMinute;
	}

	public void setNotificationHour(int notificationHour) {
		this.notificationHour = notificationHour;
	}
	/***********************
	  *  	GETTERS		*/	
	 /***********************/
	
	
	public long getNotificationID() 
	{
		return notificationID;
	}

	public String getNotificationText() 
	{
		return notificationText;
	}
	
	public String getNotificationTitle() 
	{
		return notificationTitle;
	}


	public List<Integer> getNotificationRepeat() {
		return notificationRepeat;
	}

	public int getNotificationMinute() {
		return notificationMinute;
	}

	public int getNotificationHour() {
		return notificationHour;
	}

}
