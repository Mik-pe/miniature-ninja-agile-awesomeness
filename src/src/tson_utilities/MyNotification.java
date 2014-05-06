package tson_utilities;
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
	private String notificationTitle;
	private String notificationText;
	private int notificationID;
	
	/***********************
	  *  	CONSTRUCTOR		*/	
	 /***********************/
	public MyNotification(String title, String text, int id) 
	{
		setNotificationID(id);
		setNotificationText(text);
		setNotificationTitle(title);
	}
	/***********************
	  *  	OTHER		*/	
	 /***********************/
	
	
	/***********************
	  *  	SETTERS		*/	
	 /***********************/
	
	public void setNotificationID(int notificationID) 
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
	
	/***********************
	  *  	GETTERS		*/	
	 /***********************/
	
	public int getNotificationID() 
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

}
