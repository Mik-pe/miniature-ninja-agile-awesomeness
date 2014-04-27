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
	ViewPager Tab;
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
        
        
        TabAdapter = new TabAdapter(getFragmentManager());
        
        Tab = (ViewPager)submission.findViewById(R.id.pager);
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                       
                    	//actionBar = getActivity().getActionBar();
                    	actionBar.setSelectedNavigationItem(position);                    }
                });
        Tab.setAdapter(TabAdapter);
        
        actionBar = getActivity().getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){

			@Override
			public void onTabReselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

			@Override
			 public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	          
	            Tab.setCurrentItem(tab.getPosition());
	        }

			@Override
			public void onTabUnselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}};
			//Add New Tab
			
			
		/*	Display display = getWindowManager().getDefaultDisplay();
	        int width = display.getWidth();

	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	       TabHost mTabHost = getTabHost();

	       mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
	               .setIndicator((""),getResources().getDrawable(R.drawable.mzl_05))
	         .setContent(new Intent(this, NearBy.class)));
	       mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
	               .setIndicator((""),getResources().getDrawable(R.drawable.mzl_08))
	         .setContent(new Intent(this, SearchBy.class)));
	               mTabHost.setCurrentTab(0);
	               mTabHost.getTabWidget().getChildAt(0).setLayoutParams(new
	                 LinearLayout.LayoutParams((width/2)-2,50));
	          mTabHost.getTabWidget().getChildAt(1).setLayoutParams(new
	                     LinearLayout.LayoutParams((width/2)-2,50));*/
			if(actionBar.getTabCount() <2)
			{
				actionBar.addTab(actionBar.newTab().setText("List").setTabListener(tabListener));
				actionBar.addTab(actionBar.newTab().setText("Calendar").setTabListener(tabListener));
			}
			
			final View myview = actionBar.getTabAt(0).getCustomView();
			
			return submission;
    }   
    
    
}
