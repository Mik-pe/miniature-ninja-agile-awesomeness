package tson_utilities;

import java.util.List;
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
	 private List<Project> projectList = null;
	 
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
	  * Adds an exicsting project to user's project list
	  * @param p - Name of project
	  */
	 public void addProject(Project p)
	 {
		 if(!projectList.contains(p))
			 projectList.add(p);
	 }
	 
	 /**
	  * Function creating a new project and adding it to the users project list
	  * @param name - Name of new project
	  */
	 public void createProject(String name){
		 
		 Project p = new Project(name);
		 addProject(p);
	 }
	 
	 /* Getters */	
	 /**
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
}
