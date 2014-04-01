package tson_utilities;

import java.util.ArrayList;
import java.util.List;

public class User 
{
	 private String email = "";
	 private String name = ""; 
	 private String id = "";
	 private List<Project> projectList = new ArrayList<Project>();
	 
	 public User(String email, String name, String id)
	 {
		 this.email = email;
		 this.name = name;
		 this.id = id;
		
		 Project p1 = new Project("SafariResa");
		 Project p2 = new Project("IndienResa");
		 Project p3 = new Project("Resa till USA 'MURRICA");
		 
		 this.addProject(p1);
		 this.addProject(p2);
		 this.addProject(p3);
		
		 
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
