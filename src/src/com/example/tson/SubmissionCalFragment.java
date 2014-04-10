package com.example.tson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;

import com.learn2crack.tab.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SubmissionCalFragment extends Fragment {

	List<Calendar> calList = new ArrayList<Calendar>();
	ListView submissionCalendarView;
	List<Project> projectList = HomeActivity.user.getProjects();
	Calendar today = Calendar.getInstance();
		
	
	@Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState) {
		 
		        View subCalView = inflater.inflate(R.layout.submission_cal_fragment, container, false);
		        ((TextView)subCalView.findViewById(R.id.textView)).setText("Calendar");
		        return subCalView;
	}


public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getActivity().getMenuInflater().inflate(R.menu.sub_list, menu);
	return true;
}

}
