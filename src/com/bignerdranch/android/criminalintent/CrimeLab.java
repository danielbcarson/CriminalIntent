package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

/**
 * Singleton to contain all Crimes
 * @author User
 *
 */
public class CrimeLab {
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	private ArrayList<Crime> mCrimes;
	
	private CrimeLab( Context appContext)
	{
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
		
		/* text vector */
		for (int i=0; i<100; i++ )
		{
			Crime c = new Crime();
			c.setTitle("Crime #" + i);
			c.setSolved(i % 2 == 0);
			mCrimes.add( c );
		}
	}
	
	public static CrimeLab get( Context c)
	{
		if (sCrimeLab == null )
		{
			/** 
			 * Notice that we do not directly pass in the Context
			 * parameter supplied in get(). This context could be
			 * an Activity or another Context object( Ex, Service).
			 * We want the Context that exists for the life of the
			 * application. The "application context" is a global
			 * Context that exists for the life of the application. 
			 * Whenever you have a application-wide singleton, it 
			 * should always use the application context.
			 */
			sCrimeLab = new CrimeLab( c.getApplicationContext());
		}
		
		return sCrimeLab;
	}

	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}
	
	public Crime getCrime( UUID uuid)
	{
		for ( Crime c : mCrimes) {
			/* note use of .equals method */
			if ( c.getId().equals(uuid) )
				return c;
		}
		return null;
	}

}
