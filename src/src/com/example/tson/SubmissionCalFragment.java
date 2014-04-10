package com.example.tson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SubmissionCalFragment extends Fragment {

	List<Calendar> calList = new ArrayList<Calendar>();
	ListView submissionCalendarView;
	List<Project> projectList = HomeActivity.user.getProjects();
	Calendar today = Calendar.getInstance();
	CalendarView cal;
		
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View subCalView = inflater.inflate(R.layout.submission_cal_fragment, container, false);
        
        cal = (CalendarView) subCalView.findViewById(R.id.calendarView);
		
		//cal.getDate().setSelectedDateVerticalBar(R.drawable.ic_launcher);
		
		cal.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				
				Toast.makeText(getActivity().getBaseContext(),"Selected Date is\n\n"
					+dayOfMonth+" : "+month+" : "+year , 
					Toast.LENGTH_SHORT).show();
			}
		});
        
        ((TextView)subCalView.findViewById(R.id.textView)).setText("Calendar");
        return subCalView;
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.sub_list, menu);
		return true;
	}

}
