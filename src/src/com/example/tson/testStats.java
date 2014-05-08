package com.example.tson;

import java.util.Calendar;
import java.util.List;


import tson_utilities.Project;
import tson_utilities.User;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class testStats extends Activity {
	ViewPager Tab;
    TabAdapter TabAdapter;
	ActionBar actionBar;
	ArrayAdapter<Project> statsAdapter;
	ListView projectListView;
	private View test;
	public static User user = User.getInstance();
	Button loginBtn;
	
	List<Project> projeWctListStats = user.getProjects();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		final Intent intent = new Intent(this, HomeActivity.class);
		
		loginBtn = (Button) findViewById(R.id.login_button);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(intent);
				finish();
				// TODO Auto-generated method stub
				
			}
		});
    	
    	
	}
    
    
}
