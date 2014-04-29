package com.example.tson;

import java.util.Calendar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsFragment extends Fragment{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	 /***********************
	  *  	OTHERS			*/	
	 /************************/
	
	public SettingsFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) 
	{
		 
		 super.onCreate(savedInstanceState);
		 View settings = inflater.inflate(R.layout.settings_fragment, container, false);
		 TextView meName = (TextView) settings.findViewById(R.id.meName);
		 
		 meName.setText(HomeActivity.user.getName());
		 
		 return settings;
		 
	}

}
