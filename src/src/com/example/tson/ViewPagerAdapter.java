package com.example.tson;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
 
    final int PAGE_COUNT = 2;
    // Tab Titles
    private String tabtitles[] = new String[] { "List", "Calendar"};
    Context context;
    Fragment submissionList;
    Fragment submissionCal;
 
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
 
    @Override
    public Fragment getItem(int position) {
        switch (position) {
 
            // Open FragmentTab1.java
        case 0:
            submissionList = new SubmissionListFragment();
            return submissionList;
 
            // Open FragmentTab2.java
        case 1:
            submissionCal = new SubmissionCalFragment();
            return submissionCal;
        
        }
        return null;
    }
 
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
