package com.example.sendmessage;

import static com.example.sendmessage.Constants.ATTEN;

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
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AttendanceReport extends ListActivity {

		ArrayList<String> myStringArray1 =  new ArrayList<String>();
		QuizApp mApp = null;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mApp = (QuizApp)getApplication();
			URL url; 
			HttpURLConnection urlConnection; 
			TextView resp=(TextView) findViewById(R.id.txtattenreport);
			try 
			{ 
	   
    
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				url= new URL(ATTEN);
				urlConnection= (HttpURLConnection) url.openConnection();                                      
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				String s=convertStreamToString(in);
				String temp="empty";
      
        
				try {
        	
					JSONArray jsonarray = new JSONArray(s);
					for (int i = 0; i < jsonarray.length(); i++) {

						JSONObject jsonobj = jsonarray.getJSONObject(i);				
						temp = jsonobj.getString("day");
						if(!temp.isEmpty() && !myStringArray1.contains(temp))			
						{
							myStringArray1.add(temp);
						}
			
					}
					setListAdapter(new ArrayAdapter<String>(this, R.layout.attendance_report,myStringArray1));

					ListView listView = getListView();
					listView.setTextFilterEnabled(true);

					listView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							String str=((TextView) view).getText().toString();
							AppPreferences.setStringToPref(mApp, AppPreferences.PREF_NAME, AppPreferences.DateSelected, str);
							Intent i= new Intent(AttendanceReport.this,AttendanceOnDate.class);
							startActivity(i);             
				
						}
					});
		      
				} 
				catch (JSONException e) {
			
					resp.setText(e.getLocalizedMessage());
			
				}        
				urlConnection.disconnect();   
			} 
			catch (MalformedURLException e) {
		
				resp.setText(e.getLocalizedMessage());
			} catch (IOException e) {
		
				resp.setText(e.getLocalizedMessage());
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
