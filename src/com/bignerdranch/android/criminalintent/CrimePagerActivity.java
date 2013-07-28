package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	private ArrayList<Crime> mCrimes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * We only have one view so we will define the 
		 * layout here (in code) instead of using an XML
		 * file
		 */
		mViewPager = new ViewPager(this);
		
		/** 
		 * ViewPager requires a resource ID.  Also the Fragment
		 * Manager requires that any view used as a fragment
		 * container must have a resource ID. We define this
		 * one in /res/values/ids.xml
		 */	
		mViewPager.setId(R.id.viewPager);
		
		/* set the content view */
		setContentView(mViewPager);
		
		mCrimes = CrimeLab.get(this).getCrimes();
		
		/**
		 * ViewPager requires a PagerAdapter to provide views.
		 * We can use FragmentStatePagerAdapter (a subclass
		 * of PagerAdapter) to manage the interactions between
		 * the ViewPager and the PagerAdapter. 
		 * 
		 * FragmentPagerAdapter could also be used. It does'nt
		 * destroy the fragment instance when it is no longer 
		 * needed (uses detach()) instead) so the fragment instance
		 * stays alive in the fragment manager.  OK to use when 
		 * when you have a small fixed number of fragments.
		 */
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter( new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
				return mCrimes.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Crime c = mCrimes.get(pos);
				return CrimeFragment.newInstance(c.getId());
			}
		});
		
		/**
		 * By default ViewPager shows the first item in its PagerAdapter.
		 * If we want to show another item you use the SetCurrentItem
		 * method it to the index you want shown
		 */
		UUID crimeId = (UUID)getIntent()
				.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		
		for (int i = 0; i < mCrimes.size(); i++ )
		{
			if ( mCrimes.get(i).getId().equals(crimeId) ) {
				mViewPager.setCurrentItem(i);
				
				/* Set Title of activity to current crime */
				if (mCrimes.get(i).getTitle() != null )
				{
					setTitle(mCrimes.get(i).getTitle());
				}
				break;
			}
		}
		

		/**
		 * Replace the activity's title with the current crime title
		 */
		mViewPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				Crime c = mCrimes.get(pos);
				if ( c.getTitle() != null )
				{
					setTitle(c.getTitle());
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }
			
			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});
	}

}
