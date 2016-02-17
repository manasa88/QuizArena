package com.example.sendmessage;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class Onthefly extends Activity {
	URL url;    
    HttpURLConnection urlConnection;
    URL urloptions;    
    HttpURLConnection urlConnectionoptions;
    TextView resp;
    EditText qsnString;
    EditText options;
    Button postqsn;
    QuizApp mApp = null;
    private static final String TAG="Onthefly";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_onthefly);		
				mApp = (QuizApp)getApplication();
		      	postqsn = (Button) findViewById(R.id.btn_askqsnOf);
       		 
                postqsn.setOnClickListener(new View.OnClickListener() {

        			   @Override
        			   public void onClick(View v) {
        				   
        				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       			    	StrictMode.setThreadPolicy(policy);
       			     String s;
       			    	try{
       			    		
       			    		url= new URL("https://mongolab.com/api/1/databases/operating/collections/questions?c=true&apiKey=Q0X5qdU97fBBpUlEA-YpnQGl1BXL6a7U");
       			    		urlConnection= (HttpURLConnection) url.openConnection();                                      
       			    		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
       			    		s=convertStreamToString(in);
       				
       			    		urlConnection.disconnect(); 
       			    		StrictMode.ThreadPolicy policynew = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       			    		StrictMode.setThreadPolicy(policynew);
       			    		HttpClient httpclient1 = new DefaultHttpClient();
       			    		HttpPost httppost = new HttpPost("https://mongolab.com/api/1/databases/operating/collections/questions?apiKey=Q0X5qdU97fBBpUlEA-YpnQGl1BXL6a7U");
       			    		httppost.setHeader("Content-Type", "application/json");
       			    		resp=(TextView) findViewById(R.id.tvServRes);
       			    		qsnString=(EditText) findViewById(R.id.et_qsn);
       			    		int myNum;               
       			    		myNum = Integer.parseInt(s);
       			    		String qsnstr=qsnString.getText().toString();
       			    		if(qsnstr.length()>0)
       			    		{
       			    			JSONObject j = new JSONObject();
        			            StringEntity snew;
        						j.put("qno",myNum+1);
        						j.put("qsn", qsnString.getText().toString());
        						snew = new StringEntity(j.toString());
        						httppost.setEntity(snew);
        			           
        			            HttpResponse response = httpclient1.execute(httppost);
        			           
        			            /* Checking response */
        			            if(response != null)
        			            {
        			            	 options=(EditText) findViewById(R.id.et_options);
        	        				   String optionstr=options.getText().toString();
        	        				   Log.d(TAG, "On the fly options" + optionstr);
        	        				   if(optionstr.length()>0)
        	        				   {
        	        					   Log.d(TAG, "inside if block" + optionstr);
        	        					  
        	              					
        	              				   	StrictMode.ThreadPolicy policynewoptions = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        	              				   	StrictMode.setThreadPolicy(policynewoptions);
        	              		    	
        	              				   	HttpPost httppostoptions = new HttpPost("https://mongolab.com/api/1/databases/operating/collections/qsnoptions?apiKey=Q0X5qdU97fBBpUlEA-YpnQGl1BXL6a7U");
        	              				   	httppostoptions.setHeader("Content-Type", "application/json");
        	              				  HttpClient httpclientoptions = new DefaultHttpClient();

        	              				   
        	              				   	String ansChoices[]=optionstr.split(" "); 
        	              				  Log.d(TAG, "answer choices after split" + ansChoices.length);
        	              				   	for(int k=0;k<ansChoices.length;k++)
        	              				   	{
        	              				   		JSONObject joptions = new JSONObject();
        	               			            StringEntity snewoptions;
        	               			            joptions.put("qsn",qsnString.getText().toString());
        	               			            joptions.put("option",ansChoices[k]);
        	               			            snewoptions = new StringEntity(joptions.toString());
        	               						httppostoptions.setEntity(snewoptions);
        	               					 HttpResponse optionsResponse = httpclientoptions.execute(httppostoptions);
        	         			            
        	         			            /* Checking response */
        	         			            if(optionsResponse != null)
        	         			            {
        	         			            	 Log.d(TAG, "options posted");
        	         			            }
        	              				   	}
        	               			       
        	        				   }
        			                //responseBody = EntityUtils.toString(response.getEntity());
        			                //resp.setText("Posted");
        			            	AppPreferences.setStringToPref(mApp, AppPreferences.PREF_NAME, AppPreferences.QuestionSelected, qsnString.getText().toString());
        			            	AppPreferences.setStringToPref(mApp, AppPreferences.PREF_NAME, AppPreferences.Questions, "Ontheflyscreen");
        							Intent i = mApp.getLauchActivityIntent(MainActivity.class, qsnString.getText().toString());
        			    	    	startActivity(i);

        			            }
        			            else
        			            {
        			            	resp.setText("null response");
        			            }
        		            }
        				   
        				   
        				   //to save options
        				  
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
     	        	resp.setText(e.getLocalizedMessage());
     	        }
       			    	}
                });
        			
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
