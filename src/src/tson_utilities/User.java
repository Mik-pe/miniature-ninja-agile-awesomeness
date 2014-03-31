package tson_utilities;

import java.util.List;

public class User 
{
	 private String email = "";
	 private String name = ""; 
	 private String id = "";
	 private List<Project> projectList = null;
	 
	 public User(String email, String name, String id)
	 {
		 this.email = email;
		 this.name = name;
		 this.id = id;
	 }
	 
	 public void addProject(Project p)
	 {
		 if(!projectList.contains(p))
			 projectList.add(p);
	 }
	 
	 /* Getters */	 
	 public List<Project> getProjects()
	 {
		 return projectList;
	 }
	 
	 public String getID()
	 {
		 return id;
	 }
	 
	 public String getName()
	 {
		 return name;
	 }
	 
	 public String getEmail()
	 {
		 return email;
	 }
}
