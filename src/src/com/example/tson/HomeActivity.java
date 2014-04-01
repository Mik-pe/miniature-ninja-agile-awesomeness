package com.example.tson;

//IMPORT OUR OWN CLASSES
import java.util.Calendar;
import java.util.Locale;
import tson_utilities.*;

//IMPORT ANDROID
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;

//IMPORT OTHER

public class HomeActivity extends Activity 
{

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        User user = new User("sdf@sdf.com", "Bosse", "b1337");
        setContentView(R.layout.activity_home);
        user.getProjects().get(0).addTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 1, 30);
        final RelativeLayout rl=(RelativeLayout) findViewById(R.id.rl);
        final TextView[] tv=new TextView[10];
        
        for(int i=0; i<user.getProjects().size();i++)
        {
            tv[i]=new TextView(this);   
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
                    ((int)LayoutParams.WRAP_CONTENT,(int)LayoutParams.WRAP_CONTENT);
            params.leftMargin=50;
                params.topMargin=i*50;
            tv[i].setText(user.getProjects().get(i).getName()+user.getProjects().get(i).getTimeByDate(c));
            tv[i].setTextSize((float) 20);
            tv[i].setPadding(20, 50, 20, 50);
            tv[i].setLayoutParams(params);
            rl.addView(tv[i]);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
