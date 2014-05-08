package tson_utilities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import com.example.tson.HomeActivity;

import android.util.Log;
import tson.sqlite.helper.DatabaseHelper;

/**
 * User-class for person using the application
 * @author Sofie & John
 *
 */
public class User
{
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	private static User	instance = null;
	
	 private String email = "";
	 private String name = ""; 
	 private String picURL = "";
	 private long id;
	 private List<Project> projectList = new ArrayList<Project>();
	 DatabaseHelper db;
	 //public HomeActivity homeActivity = new HomeActivity();
	 
	 /***********************
	  *  	CONSTRUCTORS 	*/	
	 /***********************/
	 
	 /**
	  * Constructor for a User, used to defeat instantiation.
	  */ 
	 protected User()
	 {
		 // Exists only to defeat instantiation
	 }
	 
	 /***********************
	  *  	GETTERS  		*/	
	 /**********************/
	
	 public static User getInstance() {
		 if(instance == null)
			 instance = new User();
		 
		 return instance;
	 }
	 
	  /** 
	  * Return list of projects
	  * @return
	  **/
	 public List<Project> getProjects()
	 {	
		 return projectList;
	 }
	 /**
	  * Return user's ID
	  * @return
	  */
	 public long getID()
	 {
		 return id;
	 }
	 /**
	  * Return user's name
	  * @return
	  */
	 public String getName()
	 {
		 return name;
	 }
	 /**
	  * Return user's email
	  * @return
	  */
	 public String getEmail()
	 {
		 return email;
	 }
	 /**
	  * 
	  * @return user's picture url
	  */
	 public String getPicURL()
	 {
		 return picURL;
	 }
	 
	 /**
	  * Send in cal object and get number of reported minutes for that date
	  * @param cal
	  * @return
	  */
	 public int getTimeByDate(Calendar cal)
	 {
		 int totalTime = 0;
		 TimeBlock t;
		 
		 for(int i=0; i< projectList.size(); i++)
		 {
			 
			 t = projectList.get(i).getTimeByDate(cal);
			 if(t != null){
				 totalTime += t.getTimeInMinutes();
			 }
		 }
		 return totalTime;
	 }
	 /***********************
	  *  	SETTERS  		*/	
	 /**********************/
	 
	 /**
	  * Set id of user
	  * @param id_
	  */
	 public void setId(long id_)
	 {
		 this.id = id_;
	 }
	 
	 /**
	  * Set email of user
	  * @param email_
	  */
	 public void setEmail(String email_)
	 {
		 this.email = email_;
	 }
	 
	 /**
	  * set name of user
	  * @param name_
	  */
	 public void setName(String name_)
	 {
		 this.name = name_;
	 }
	 /**
	  * Set url of profile picture
	  * @param picURL_
	  */
	 public void setPictureURL(String picURL_)
	 {
		 this.picURL = picURL_;
	 }
	 
	 /***********************
	  *  	OTEHRS  		*/	
	 /************************/
	 
	 /**
	  * Check if a date is confirmed.
	  * @param cal - Calendar object for the date to check
	  * @return - Returns 1 IF CONFIRMED, 0 if timeblock EXISTS, -1 if no timeblock exists.
	  */
	 public int isDateConfirmed(Calendar cal){
		 if(projectList.size()==0)
			 return -1;
		 TimeBlock t;
		 int confirmed=-1;
		 int temp = 0;
		 for(int i=0;i<projectList.size();i++){
			 t=projectList.get(i).getTimeByDate(cal);
			 if(t != null){
				 confirmed=t.getConfirmed();
				 if(confirmed == 0)
					 return confirmed;
			 }
		 }
		 temp/=projectList.size();
		 if(temp==1){
			 return temp;
		 }
		return confirmed;
	 }
	 /**
	  * Adds an existing project to user's project list
	  * @param p - Name of project
	  */
	 public void addProject(Project p)
	 {
		 if(!projectList.contains(p))
		 {
			 projectList.add(p);
		 }
	 }
	 
	 /**
	  * Function creating a new project and adding it to the users project list
	  * @param name - Name of new project
	  */
	 public void createProject(String name){
		 
		 Project p = new Project(name);
		 addProject(p);
	 }
}
