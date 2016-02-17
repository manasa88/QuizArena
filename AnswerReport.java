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

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

public class AnswerReport extends Activity {
	private static final String TAG = "AnswerReport";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ansreport);
		//Activity activity = null;
		//MainActivity mActivity=(MainActivity)activity;
		Log.d(TAG, "Enterig into answerreport");
		QuizApp mApp = (QuizApp)getApplication();
		Log.d(TAG, "Enterig into answerreport step 2");
		//final String qsn="what is d android version?";
		 final String qsn = AppPreferences.getStringFromPref(mApp, AppPreferences.PREF_NAME, AppPreferences.QuestionSelectedReport);
		 if(qsn!=null)
		 {
		 TextView txtqsn=(TextView) findViewById(R.id.txtqsnSel);
		 TextView tnew=(TextView) findViewById(R.id.txtrepliescount);
		 txtqsn.setText(qsn);
		 Log.d(TAG, "Enterig into answerreport step 3"+qsn);
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    	StrictMode.setThreadPolicy(policy);
	    	
	    	 
	        URL url;    
	        HttpURLConnection urlConnection;
	        URL urlnew;    
	        HttpURLConnection urlConnectionnew;
	    	String qsnnew="";
	    	String s="";
	    	ArrayList<String> myStringArray1 =  new ArrayList<String>();
	    	
	    	
	    	 qsnnew=qsn.replace(" ","%20");
	    	try{	    	
	    		urlnew= new URL("https://mongolab.com/api/1/databases/operating/collections/answers?q={%22qsn%22:%22"+qsnnew+"%22}&apiKey=Q0X5qdU97fBBpUlEA-YpnQGl1BXL6a7U");
	    		urlConnectionnew= (HttpURLConnection) urlnew.openConnection();
	    		urlConnectionnew.setConnectTimeout(6000);
	    		Log.d(TAG, "Connection response code"+urlConnectionnew.getResponseCode());
	    		if(urlConnectionnew.getResponseCode()==200)
	    		{
	    		InputStream in = new BufferedInputStream(urlConnectionnew.getInputStream());
	    		s=convertStreamToString(in);
	    		
	    		TextView op1=(TextView) findViewById(R.id.txtcount1);
	    		TextView op1Ans=(TextView) findViewById(R.id.txtChoice1Ans);
	    		TextView op2Ans=(TextView) findViewById(R.id.txtChoice2Ans);
	    		TextView op2=(TextView) findViewById(R.id.txtcount2);
	    		String[] countArr=new String[3];
	    		String temp;
	    				   try{     	
					        	JSONArray jsonarray = new JSONArray(s);
					        	
					        	int rep=jsonarray.length();
					        	if(rep>0)
					        	{
					        	
					        	tnew.setText(" "+rep);
					        	
								for (int i = 0; i < jsonarray.length(); i++) {
									
								JSONObject jsonobj = jsonarray.getJSONObject(i);				
								temp = jsonobj.getString("ans");
								
								if(!temp.isEmpty() && !myStringArray1.contains(temp))					
								{
								myStringArray1.add(temp);
								}
								
					
								}
								
					
					op1Ans.setText(myStringArray1.get(0));
					if(myStringArray1.size() > 1)
					{
					op2Ans.setText(myStringArray1.get(1));
					}
					for (int k = 0; k < myStringArray1.size(); k++) {
						
						url= new URL("https://mongolab.com/api/1/databases/operating/collections/answers?c=true&q={%22qsn%22:%22"+qsnnew+"%22,%22ans%22:%22"+myStringArray1.get(k)+"%22}&apiKey=Q0X5qdU97fBBpUlEA-YpnQGl1BXL6a7U");
			    		urlConnection= (HttpURLConnection) url.openConnection();                                      
			    		InputStream innew = new BufferedInputStream(urlConnection.getInputStream());
			    		s=convertStreamToString(innew);
			    		
			    		countArr[k]=s;
			    		
					}
					
					
					op1.setText(countArr[0]);
					
					op2.setText(countArr[1]);
		        	}
	    	} catch (JSONException e) {
				
	    		Log.d(TAG, "JSON exception"+e.getMessage());
				
			}
	    		
	    		}
	    		else
	    		{
	    			tnew.setText("Check internet connection");
	    			
	    			
	    		}
	    			
	    	
	    	} catch (MalformedURLException e) {
	    		tnew.setText("Check internet connection");
	    	  Log.d(TAG, "MalformedURLException"+e.getMessage());
	      	
	    	} catch (IOException e) {
	    		tnew.setText("Check internet connection");
	    		Log.d(TAG, "IOException"+e.getMessage());
	    		
	    	}
	    	
	    	
	    	}
		 else
		 {
			 Log.d(TAG, "question is null");
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
		        // TODO Auto-generated catch block
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
