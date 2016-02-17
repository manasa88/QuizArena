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
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import static com.example.sendmessage.Constants.QUESTIONS;

import com.example.sendmessage.QuizApp.PTPLog;

public class Questions extends ListActivity {

		ArrayList<String> myStringArray1 =  new ArrayList<String>();
		QuizApp mApp = null;
		private static final String TAG = "PTP_Questions";
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mApp = (QuizApp)getApplication();
			URL url; 
			HttpURLConnection urlConnection; 
			TextView resp=(TextView) findViewById(R.id.txtQuestions);
			try 
			{ 
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				url= new URL(QUESTIONS);
				urlConnection= (HttpURLConnection) url.openConnection();
				urlConnection.setConnectTimeout(6000);
	    		Log.d(TAG, "Connection response code"+urlConnection.getResponseCode());
	    		if(urlConnection.getResponseCode()==200)
	    		{
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				String s=convertStreamToString(in);
				String temp="empty";    
				try {
        	
					JSONArray jsonarray = new JSONArray(s);
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject jsonobj = jsonarray.getJSONObject(i);				
						temp = jsonobj.getString("qsn");
						if(!temp.isEmpty())			
						{
							myStringArray1.add(temp);
						}			
					}
					setListAdapter(new ArrayAdapter<String>(this, R.layout.questions,myStringArray1));

					ListView listView = getListView();
					listView.setTextFilterEnabled(true);

					listView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		   
							String str=((TextView) view).getText().toString();
							PTPLog.d(TAG, "question selected"+str);
							AppPreferences.setStringToPref(mApp, AppPreferences.PREF_NAME, AppPreferences.Questions, "FromDbScreen");
							AppPreferences.setStringToPref(mApp, AppPreferences.PREF_NAME, AppPreferences.QuestionSelected, str);
							Intent i = mApp.getLauchActivityIntent(MainActivity.class, str);
							startActivity(i);				
						}
					}); 
				} catch (JSONException e) {
					resp.setText(e.getLocalizedMessage());
				}        
				urlConnection.disconnect();   
			} 
			}catch (MalformedURLException e) {
		
				myStringArray1.add("Malformed URL Exception");
				setListAdapter(new ArrayAdapter<String>(this, R.layout.questions,myStringArray1));
			} catch (IOException e) {	
				myStringArray1.add("Check internet connection");
				setListAdapter(new ArrayAdapter<String>(this, R.layout.questions,myStringArray1));
			}
			catch (Exception e) {
				myStringArray1.add("Exception establishing database connection");
				setListAdapter(new ArrayAdapter<String>(this, R.layout.questions,myStringArray1));
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


