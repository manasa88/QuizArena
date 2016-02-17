package com.example.sendmessage;

import android.content.Context;
import android.content.SharedPreferences;



public class AppPreferences {

	
	    
	    public static final String PREF_NAME = Constants.PACKAGE_NAME;
	    
	    public static final String P2P_ENABLED = "p2pEnabled";
	    
	    public static final String QuestionSelected="QuestionSelected";
	    public static final String QuestionSelectedReport="QuestionSelectedReport";
	    public static final String Questions="Questions";
	    public static final String DateSelected="DateSelected";

	    private QuizApp mApp;
	    private SharedPreferences mPref;

	    public AppPreferences(QuizApp app) {
	    	mApp = app;
	        mPref = mApp.getSharedPreferences(Constants.PACKAGE_NAME, 0);
	    }

	    /**
	     * Get the value of a key
	     * @param key
	     * @return
	     */
	    public String getString(String key) {
	        return mPref.getString(key, null);
	    }

	    /**
	     * Set the value of a key
	     * @param key
	     * @return
	     */
	    public void setString(String key, String value) {
	        SharedPreferences.Editor editor = mPref.edit();
	        editor.putString(key, value);
	        editor.commit();
	    }
	    
	    
	        
	    /**
	     * Get the value of a key from a different preference in the same application    
	     */
	    public static String getStringFromPref(Context ctx, String preferenceFileName, String key) {
	    	String value = null;
	    	SharedPreferences pref = ctx.getSharedPreferences(preferenceFileName, 0);
	    	if( pref != null){
	    		value = pref.getString(key, null);
	    	}
	        return value;
	    }
	    
	    public static void setStringToPref(Context ctx, String preferenceFileName, String key, String value) {
	    	SharedPreferences pref = ctx.getSharedPreferences(preferenceFileName, 0);
	    	if( pref != null){
	    		SharedPreferences.Editor editor = pref.edit();
	            editor.putString(key, value);
	            editor.commit();
	    	}
	    }
	    
	    /**
	     * Get the value of a key from a different preference in the same application
	    		 
	     */
	    public static boolean getBooleanFromPref(Context ctx, String preferenceFileName, String key) {
	    	boolean value = false;
	    	SharedPreferences pref = ctx.getSharedPreferences(preferenceFileName, 0);
	    	if( pref != null){
	    		value = pref.getBoolean(key, false);
	    	}
	        return value;
	    }
}
