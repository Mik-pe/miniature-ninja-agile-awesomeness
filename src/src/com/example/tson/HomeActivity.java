package com.example.tson;

//IMPORT OUR OWN CLASSES
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tson.sqlite.helper.DatabaseHelper;
import tson_utilities.*;
//IMPORT ANDROID
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;

//IMPORT OTHER

public class HomeActivity extends Activity 
{
	//Database Helper
	DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        User user = new User("sdf@sdf.com", "Bosse", "b1337");
        setContentView(R.layout.activity_home);
        user.getProjects().get(0).addTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 1, 30);
        final LinearLayout rl=(LinearLayout) findViewById(R.id.rl);
        final TextView[] tv=new TextView[10];
        
       /* for(int i=0; i<user.getProjects().size();i++)
        {
            tv[i]=new TextView(this);   
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                    ((int)LayoutParams.WRAP_CONTENT,(int)LayoutParams.WRAP_CONTENT);
            params.leftMargin=50;
                params.topMargin=i*50;
            tv[i].setText(user.getProjects().get(i).getName()+user.getProjects().get(i).getTimeByDate(c));
            tv[i].setTextSize((float) 20);
            tv[i].setPadding(20, 50, 20, 50);
            tv[i].setLayoutParams(params);
            rl.addView(tv[i]);
        }*/
        
        //DB TEST
        
        db = new DatabaseHelper(getApplicationContext());
        
        Project p1 = new Project("Tester project");
        Log.d("Project Count", "Project Count BEFORE INITILIZATION: " + db.getAllProjects().size());
        
        long p1_id = db.createProject(p1);
        
        
        Log.d("Project Count", "Project Count: " + db.getAllProjects().size());
        
        // get all projects
        Log.d("Get projects", "get all projects");
        List<Project> projs = db.getAllProjects();
        for(Project proj: projs){
        	Log.d("PROJECT", proj.getName());
        }
        
        //delete project
        Log.d("Project Count", "Project Count before delete: " + db.getAllProjects().size());
        //db.deleteProject(p1_id);
        
        Log.d("Project Count", "Project Count after delete: " + db.getAllProjects().size());
        
        db.closeDB();
        
        
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    /**
     * Opens the Create Project View when the "Create Project" button is clicked
     * @param view - View for Create Project Screen
     */
    public void openCreateProjectActivity(View view)
    {
    	
    	Intent intent = new Intent(this, CreateProjectActivity.class);
    	startActivity(intent);
    	
    }
   
    
}
