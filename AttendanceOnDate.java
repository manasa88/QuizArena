package com.example.sendmessage;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AttendanceOnDate extends ListActivity {

		ArrayList<String> myStringArray1 =  new ArrayList<String>();
		QuizApp mApp = null;
		private static final String TAG = "PTP_AttendanceReportDate";
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mApp = (QuizApp)getApplication();
			URL url; 
			HttpURLConnection urlConnection; 
			TextView resp=(TextView) findViewById(R.id.txtattenreportdate);
			try 
			{ 
	   
    
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				String qsnday = AppPreferences.getStringFromPref(mApp, AppPreferences.PREF_NAME, AppPreferences.DateSelected);
    	
				String urlstr="https://mongolab.com/api/1/databases/operating/collections/attendance?q={\"day\":\""+qsnday+"\"}&apiKey=Q0X5qdU97fBBpUlEA-YpnQGl1BXL6a7U";
				url= new URL(urlstr);
				urlConnection= (HttpURLConnection) url.openConnection();  
				urlConnection.setConnectTimeout(3000);
	    		Log.d(TAG, "Connection response code"+urlConnection.getResponseCode());
	    		if(urlConnection.getResponseCode()==200)
	    		{
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				String s=convertStreamToString(in);
				String temp="empty";
      
				//lvqsns=(ListView) findViewById(R.id.lv_questions);
				try {
        	
					JSONArray jsonarray = new JSONArray(s);
					for (int i = 0; i < jsonarray.length(); i++) {

						JSONObject jsonobj = jsonarray.getJSONObject(i);				
						temp = jsonobj.getString("psuid");
						if(!temp.isEmpty() && !myStringArray1.contains(temp))
							//resp.setText(temp);
						{
							myStringArray1.add(temp);
						}
			
					}
					setListAdapter(new ArrayAdapter<String>(this, R.layout.attendance_report,myStringArray1));

					ListView listView = getListView();
					listView.setTextFilterEnabled(true);

			

					//lvqsns.setAdapter(adapter);
			
		      
				} catch (JSONException e) {
			
					resp.setText(e.getLocalizedMessage());
			
				}
        
				urlConnection.disconnect();   
			} 
	    		else
	    		{
	    			myStringArray1.add("Check internet connection");
					setListAdapter(new ArrayAdapter<String>(this, R.layout.attendance_report,myStringArray1));
	    		}
			}catch (MalformedURLException e) {
		
				myStringArray1.add("Malformed URL Exception");
				setListAdapter(new ArrayAdapter<String>(this, R.layout.attendance_report,myStringArray1));
			} catch (IOException e) {
		
				myStringArray1.add("Check internet connection");
				setListAdapter(new ArrayAdapter<String>(this, R.layout.attendance_report,myStringArray1));
				 Log.d(TAG, "IOException");
			}
		}
		private String convertStreamToString(InputStream is) {
			ByteArrayOutputStream oas = new ByteArrayOutputStream();
			copyStream(is, oas);
			String t = oas.toString();
			try {
				oas.close();
				oas = null;
			} catch (IOException e) {
       
				e.printStackTrace();
			}
			return t;
		}

		private void copyStream(InputStream is, OutputStream os)
		{
			final int buffer_size = 1024;
			try
			{
				byte[] bytes=new byte[buffer_size];
				for(;;)
				{
					int count=is.read(bytes, 0, buffer_size);
					if(count==-1)
						break;
					os.write(bytes, 0, count);
				}
			}
			catch(Exception ex){}
		}
	}
