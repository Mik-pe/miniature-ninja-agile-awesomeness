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
	 private String email = "";
	 private String name = ""; 
	 private String id = "";
	 private List<Project> projectList = new ArrayList<Project>();
	 DatabaseHelper db;
	 public HomeActivity homeActivity = new HomeActivity();
	 
	 /**
	  * Constructor for a User, information to be fetched from Google account in the future
	  * @param email
	  * @param name
	  * @param id
	  */ 
	 public User(String email, String name, String id)
	 {
		 this.email = email;
		 this.name = name;
		 this.id = id;
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
	 
	 /***********************
	  *  	GETTTERS  		*/	
	 /***********************
	  * 
	  * Return list of projects
	  * @return
	  */
	 public List<Project> getProjects()
	 {	
		 return projectList;
	 }
	 /**
	  * Return user's ID
	  * @return
	  */
	 public String getID()
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
}
