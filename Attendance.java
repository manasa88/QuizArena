package com.example.sendmessage;

import static com.example.sendmessage.Constants.ATTEN;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Attendance extends Activity {
		URL url;    
		HttpURLConnection urlConnection;
		TextView resp;
		EditText attenString;
		Button postatten;
		QuizApp mApp = null;   
		
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mApp = (QuizApp)getApplication();
			if(mApp.mMyAddr.equals("Master"))
			{
					Intent i= new Intent(Attendance.this, AttendanceReport.class);
					startActivity(i);
			 
			}
			else{
				setContentView(R.layout.activity_attendance);		
		
				postatten = (Button) findViewById(R.id.btn_Mark);
		
				postatten.setOnClickListener(new View.OnClickListener() {

        			   @Override
        			  public void onClick(View v) {
        				   
        				   
        				   
        				   	try{
         			    	   
               				    StrictMode.ThreadPolicy policynew = new StrictMode.ThreadPolicy.Builder().permitAll().build();
               				    StrictMode.setThreadPolicy(policynew);
               				    HttpClient httpclient1 = new DefaultHttpClient();
               				    HttpPost httppost = new HttpPost(ATTEN);
               				    httppost.setHeader("Content-Type", "application/json");
               				    resp=(TextView) findViewById(R.id.tvServResAtten);
               				    attenString=(EditText) findViewById(R.id.et_atten);
                  
               				    String qsnstr=attenString.getText().toString();
               				    if(qsnstr.length()>0)
               				    {
               				    	JSONObject j = new JSONObject();
	          			            StringEntity snew;          						
	          						j.put("psuid", attenString.getText().toString());
	          						j.put("device",mApp.mDeviceName);
	          						String daytime=getCurrentTimeStamp();
	          						String[] day=daytime.split(" ");
	          						j.put("day", day[0]);
	          						j.put("time", day[1]);
	          						snew = new StringEntity(j.toString());
	          						httppost.setEntity(snew);
          			           
	          						HttpResponse response = httpclient1.execute(httppost);
          			            
	          			            /* Checking response */
	          			            if(response != null)
	          			            {
	          			                //responseBody = EntityUtils.toString(response.getEntity());
	          			                resp.setText("Posted");
	          			            	
	
	          			            }
	          			            else
	          			            {
	          			            	resp.setText("null response");
	          			            }
	          		            	}
        				   	}
        				   	
	         			    	catch (MalformedURLException e1) {   						
	         			    	 e1.printStackTrace();
	         			     	}
	         			    	catch (JSONException e) {       					
	         			    	 resp.setText(e.getLocalizedMessage());
	         			     	} 
	         			    	catch (UnsupportedEncodingException e) {
	       					
	         			    		resp.setText(e.getLocalizedMessage());
	         			    	}
	         			    	catch (ClientProtocolException e){
	       	            
	         			    		resp.setText(e.getLocalizedMessage());
	         			    	}
	         			    	catch (IOException e) {
	         			    		resp.setText("Check internet connection");
	         			    	}	   
        				   
        			   	}
                	});
                	}
    	}
		
		public static String getCurrentTimeStamp(){
			try {

				SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
				String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

				return currentTimeStamp;
			} catch (Exception e) {
            e.printStackTrace();

            return null;
			}
		}
}
