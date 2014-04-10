package com.example.tson;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabAdapter extends FragmentStatePagerAdapter 
{
    public TabAdapter(FragmentManager fm) 
    {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) 
	{
		switch (i) 
		{
        case 0:
            return new SubmissionListFragment();
        case 1:
            return new SubmissionCalFragment();
        }
		return null;
		
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return 2; //No of Tabs
	}


}