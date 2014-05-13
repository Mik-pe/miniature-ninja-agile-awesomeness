package com.example.tson;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

/**
 * Class for the SubmissionFragment as a whole
 * Contains the tabs for subfragments:
 * {@link SubmissionListFragment}
 * {@link SubmissionCalFragment}
 * @author 
 *
 */

public class SubmissionFragment extends Fragment {
	ViewPager viewPager;
    TabAdapter TabAdapter;
	ActionBar actionBar;
	/**
	 * Constructor for 
	 * {@link SubmissionFragment}
	 */
	public SubmissionFragment(){}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        View submission = inflater.inflate(R.layout.submission_fragment, container, false);
        
        // Locate the viewpager in activity_main.xml
        viewPager = (ViewPager) submission.findViewById(R.id.pager);
 
        // Set the ViewPagerAdapter into ViewPager
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        
        setRetainInstance(true);
			
        return submission;
    }   
    
    
}
