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
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.sendmessage.QuizApp.PTPLog;

public class QuestionFragment extends ListFragment {
	private static final String TAG = "PTP_QuestionFrag";
	
	QuizApp mApp = null; 
	private static MainActivity mActivity = null;
	
	private ArrayList<MessageRow> mMessageList = null;   // a list of chat msgs.
	private ArrayList<MessageRow> mQuestionList = null;
	private ArrayList<MessageRow> mQuestionListnew = null;
	
    private ArrayAdapter<MessageRow> mAdapter= null;
 
    URL url;    
    HttpURLConnection urlConnection;
    // private String mMyAddr;
    
	/**
     * Static factory to create a fragment object from tab click.
     */
    public static QuestionFragment newInstance(Activity activity, String groupOwnerAddr, String msg) {
    	QuestionFragment f = new QuestionFragment();
    	mActivity = (MainActivity)activity;
    	
        Bundle args = new Bundle();
        args.putString("groupOwnerAddr", groupOwnerAddr);
        args.putString("initMsg", msg);
        f.setArguments(args);
        Log.d(TAG, "newInstance :" + groupOwnerAddr + " : " + msg);
        return f;
    }
    
    @Override
    	public void onCreate(Bundle savedInstanceState) {  // this callback invoked after newInstance done.  
        	super.onCreate(savedInstanceState);
        	mApp = (QuizApp)mActivity.getApplication();
        
        	setRetainInstance(true);   // Tell the framework to try to keep this fragment around during a configuration change.
    	}
    
    /**
     * the data you place in the Bundle here will be available in the Bundle given to onCreate(Bundle)
     
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putParcelableArrayList("MSG_LIST", mMessageList);
    	Log.d(TAG, "onSaveInstanceState. " + mMessageList.get(0).mMsg);
    }
    
    /**
     * no matter your fragment is declared in main activity layout, or dynamically added thru fragment transaction
     * You need to inflate fragment view inside this function. 
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// inflate the fragment's res layout. 
    	
        final View contentView = inflater.inflate(R.layout.chat_frag, container, false);  // no care whatever container is.
        final EditText inputEditText = (EditText)contentView.findViewById(R.id.edit_input);    
        
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,countries);
		//setListAdapter(adapter);
        final String qsn = AppPreferences.getStringFromPref(mApp, AppPreferences.PREF_NAME, AppPreferences.QuestionSelected);
        Log.d(TAG, "question Selected is. " + qsn);
        inputEditText.setText(qsn);
        //final TextView inputMsgText = (TextView)contentView.findViewById(R.id.edit_input);      
        
        final RadioGroup rgrp=(RadioGroup)contentView.findViewById(R.id.radioGroup1);  	  
        URL urlconstr; 
        HttpURLConnection urlConnectionstr; 	
        try 
        { 
		 	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    	StrictMode.setThreadPolicy(policy);
	    	String qsnnew;
	    	String tempqsn;
	    		if(qsn!=null)
	    		{
	    			qsnnew=qsn.replace(" ","%20");
	    		}
	    		else
	    		{
	    			mQuestionListnew = new ArrayList<MessageRow>();
	    			jsonArrayToList(mApp.mMessageArray, mQuestionListnew); 
		            int n=mQuestionListnew.size();
			        if(n>0)
			        	{
			        	  	tempqsn=mQuestionListnew.get(n-1).mMsg;
			        	  	qsnnew=tempqsn.replace(" ","%20");
			        	}
			          	else
			          	{
			          		tempqsn=mQuestionListnew.get(0).mMsg;
			          		qsnnew=tempqsn.replace(" ","%20");
			          	}
	    		}
	    	
	    		urlconstr= new URL("https://mongolab.com/api/1/databases/operating/collections/qsnoptions?q={\"qsn\":\""+qsnnew+"\"}&apiKey=Q0X5qdU97fBBpUlEA-YpnQGl1BXL6a7U");
	    		urlConnectionstr= (HttpURLConnection) urlconstr.openConnection();  
	    		Log.d(TAG, "line 1 url connection"+urlconstr);
	    		InputStream instr = new BufferedInputStream(urlConnectionstr.getInputStream());
	    		Log.d(TAG, "line 2 inputstream");
	    		String sres=convertStreamToString(instr);
	    		Log.d(TAG, "string conversion");
	    		String temp="empty";
	    		Log.d(TAG, "call to db"+temp);	      
	        	       try{
	        	    	   JSONArray jsonarray = new JSONArray(sres);
	        	    	   Log.d(TAG, "response"+jsonarray.length());
	        	    	   if(jsonarray.length()>0)
	        	    	   {
	        	    		   rgrp.setVisibility(View.VISIBLE);
	        	    	   }
	        	    	   for (int j = 0; j < jsonarray.length(); j++) {

	        	    		   JSONObject jsonobj = jsonarray.getJSONObject(j);				
	        	    		   temp = jsonobj.getString("option");
	        	    		   Log.d(TAG, "option from db"+temp);
	        	    		   if(!temp.isEmpty() && j<2)					
	        	    		   {
					
	        	    			   ((RadioButton) rgrp.getChildAt(j)).setText(temp);
	        	    		   }
							}			
	 
	        	       }
	        	       catch (JSONException e) {			
	        	    	   Log.d(TAG, "exception1"+e.getMessage());
	        	       }      
	        	       urlConnectionstr.disconnect();  
	        	       AppPreferences.setStringToPref(mApp, AppPreferences.PREF_NAME, AppPreferences.QuestionSelected, null);
        } 
        catch (MalformedURLException e) {		
        	Log.d(TAG, "exception2"+e.getMessage());
  	
        } 
        catch (IOException e) {		
        	Log.d(TAG, "exception3"+e.getMessage());
		
        }
	    
        final Button sendBtn = (Button)contentView.findViewById(R.id.btn_send);
       
        //send button click        
        sendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// send the chat text in current line to the server
				//String inputMsg = inputEditText.getText().toString();
					String inputMsg;
					String screen = AppPreferences.getStringFromPref(mApp, AppPreferences.PREF_NAME, AppPreferences.Questions);
					if(screen!= null)
					 	{
						 
						 	inputMsg = inputEditText.getText().toString();
						 	AppPreferences.setStringToPref(mApp, AppPreferences.PREF_NAME, AppPreferences.Questions, null);
					 	}
					 	else
					 	{
						 	inputMsg = inputEditText.getText().toString();						 	
						 	Log.d(TAG, "Array size"+mApp.mMessageArray.length());
						 		if(mApp.mMessageArray.length()>0)
						 		{						 		
						 			mQuestionList = new ArrayList<MessageRow>();
						            jsonArrayToList(mApp.mMessageArray, mQuestionList); 
						            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				         			StrictMode.setThreadPolicy(policy);
				         			
				         			try{
					         				
					         				
					               		    StrictMode.ThreadPolicy policynew = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					               		    StrictMode.setThreadPolicy(policynew);
					               		    HttpClient httpclient1 = new DefaultHttpClient();
					               		    HttpPost httppost = new HttpPost("https://mongolab.com/api/1/databases/operating/collections/answers?apiKey=Q0X5qdU97fBBpUlEA-YpnQGl1BXL6a7U");
					               		    httppost.setHeader("Content-Type", "application/json");
					               		   
		               		
					               		    if(!inputMsg.equalsIgnoreCase("") )
					               		    {
					               		    	Log.d(TAG, "Input text not empty"+inputMsg);
					               		    	JSONObject j = new JSONObject();
					               		    	StringEntity snew;   
					               		    	int n=mQuestionList.size();
		          			           
					               		    	if(n>0)
					               		    	{
					               		    		j.put("qsn", mQuestionList.get(n-1).mMsg);
					               		    	}
					               		    	else
					               		    	{
					               		    		j.put("qsn", mQuestionList.get(0).mMsg);
					               		    	}
		          			          
					               		    	j.put("ans", inputMsg);
					               		    	j.put("device",mApp.mDeviceName);
		          						
					               		    	snew = new StringEntity(j.toString());
					               		    	httppost.setEntity(snew);
		          			           
					               		    	HttpResponse response = httpclient1.execute(httppost);
					               		     if(response != null)
	        	         			            {
					               		    	rgrp.setVisibility(View.GONE);  
	        	         			            }
					               		    }
					               		    else
					               		    {
					               		    	Log.d(TAG, "Input empty");
					               		    	JSONObject j = new JSONObject();
					               		    	StringEntity snew;   
					               		    	int n=mQuestionList.size();
					               		    	if(n>0)
					               		    	{
					               		    		j.put("qsn", mQuestionList.get(n-1).mMsg);
					               		    	}
					               		    	else
					               		    	{
					               		    		j.put("qsn", mQuestionList.get(0).mMsg);
					               		    	}
					               		    	RadioButton rbtn;
					               		    	int selected = rgrp.getCheckedRadioButtonId();
					               		    	rbtn = (RadioButton) contentView.findViewById(selected);
					               		    	inputMsg = rbtn.getText().toString();
					               		    	j.put("ans",inputMsg	);
					               		    	j.put("device",mApp.mDeviceName);
		          						
					               		    	snew = new StringEntity(j.toString());
					               		    	httppost.setEntity(snew);
					               		    	HttpResponse response = httpclient1.execute(httppost);
					               		    	
					               		    	//hiding responses after saving
					               		    	if(response != null)
					               		    	{
					               		    		rgrp.setVisibility(View.GONE);      	         			            }
					               		    }
		               		 		               		
				         			}
						 
				         			catch (MalformedURLException e1) {
		   						
				         				Log.d(TAG, "MalformedURLException. " + e1.getLocalizedMessage());
		         			    		}
				         			catch (JSONException e) {
				         				Log.d(TAG, "JSONException. " + e.getLocalizedMessage());
		         			    		}
				         			catch (UnsupportedEncodingException e) {
		       					
				         				Log.d(TAG, "UnsupportedEncodingException. " + e.getLocalizedMessage());
				         			}
				         			catch (ClientProtocolException e){
		       	            
				         				Log.d(TAG, "ClientProtocolException. " + e.getLocalizedMessage());
				         			}
				         			catch (IOException e) {
				         				Log.d(TAG, "IOException. " + e.getLocalizedMessage());
				         			}
						 		} 
					 	}
					 
					inputEditText.setText("");
					AppPreferences.setStringToPref(mApp, AppPreferences.PREF_NAME, AppPreferences.QuestionSelected, null);
					InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(inputEditText.getWindowToken(), 0);
					MessageRow row = new MessageRow(mApp.mDeviceName, inputMsg, null);
					appendChatMessage(row);
					String jsonMsg = mApp.shiftInsertMessage(row);
					PTPLog.d(TAG, "sendButton clicked: sendOut data : " + jsonMsg);
					mActivity.pushOutMessage(jsonMsg);
				}
        	});
        
        //String groupOwnerAddr = getArguments().getString("groupOwnerAddr");
        
        	String msg = getArguments().getString("initMsg");
        	PTPLog.d(TAG, "onCreateView : fragment view created: msg :" + msg);
        
        	if( savedInstanceState != null ){
        		mMessageList = savedInstanceState.getParcelableArrayList("MSG_LIST");
        		Log.d(TAG, "onCreate : savedInstanceState: " + mMessageList.get(0).mMsg);
        	}else if( mMessageList == null ){
        	// no need to setContentView, just setListAdapter, but listview must be android:id="@android:id/list"
        		mMessageList = new ArrayList<MessageRow>();
        		jsonArrayToList(mApp.mMessageArray, mMessageList);
        		Log.d(TAG, "onCreate : jsonArrayToList : " + mMessageList.size() );
        	}else {
        		Log.d(TAG, "onCreate : setRetainInstance good : ");
        	}
        
        	mAdapter = new QuizMessageAdapter(mActivity, mMessageList);
        
        	setListAdapter(mAdapter);  // list fragment data adapter 
        
        	PTPLog.d(TAG, "onCreate chat msg fragment: devicename : " + mApp.mDeviceName + " : " + getArguments().getString("initMsg"));
        	return contentView;	
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
    	@Override 
    	public void onDestroyView(){ 
    		super.onDestroyView(); 
    		Log.d(TAG, "onDestroyView: ");
    	}

    	@Override
    	public void onActivityCreated(Bundle savedInstanceState) {  // invoked after fragment view created.
    		super.onActivityCreated(savedInstanceState);
        
    		if( mMessageList.size() > 0){
    			getListView().smoothScrollToPosition(mMessageList.size()-1);
    		}
        
    		setHasOptionsMenu(true);
    		Log.d(TAG, "onActivityCreated: chat fragment displayed ");
    	}
    
    /**
     * add a chat message to the list, return the format the message as " sender_addr : msg "
     */
    public void appendChatMessage(MessageRow row) {
    		Log.d(TAG, "appendChatMessage: chat fragment append msg: " + row.mSender + " ; " + row.mMsg);
    		mMessageList.add(row);
    		getListView().smoothScrollToPosition(mMessageList.size()-1);
    		mAdapter.notifyDataSetChanged();  // notify the attached observer and views to refresh.
    		return;
    }
    
    private void jsonArrayToList(JSONArray jsonarray, List<MessageRow> list) {
    		try{
    			for(int i=0;i<jsonarray.length();i++){
    				MessageRow row = MessageRow.parseMesssageRow(jsonarray.getJSONObject(i));
    				PTPLog.d(TAG, "jsonArrayToList: row : " + row.mMsg);
    				list.add(row);
    			}
    		}catch(JSONException e){
    		PTPLog.e(TAG, "jsonArrayToList: " + e.toString());
    		}
    }
    
    /**
     * chat message adapter from list adapter.
     * Responsible for how to show data to list fragment list view.
     */
    final class QuizMessageAdapter extends ArrayAdapter<MessageRow> {

    	public static final int VIEW_TYPE_MYMSG = 0;
    	public static final int VIEW_TYPE_INMSG = 1;
    	public static final int VIEW_TYPE_COUNT = 2;    // msg sent by me, or all incoming msgs
    	private LayoutInflater mInflater;
    	
		public QuizMessageAdapter(Context context, List<MessageRow> objects){
			super(context, 0, objects);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
		
		@Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }
		
		@Override
        public int getItemViewType(int position) {
			MessageRow item = this.getItem(position);
			if ( item.mSender.equals(mApp.mDeviceName )){
				return VIEW_TYPE_MYMSG;
			}
			return VIEW_TYPE_INMSG;			
		}
		
		/**
		 * assemble each row view in the list view.
		 * http://dl.google.com/googleio/2010/android-world-of-listview-android.pdf
		 */
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;  // old view to re-use if possible. Useful for Heterogeneous list with diff item view type.
			MessageRow item = this.getItem(position);
			boolean mymsg = false;
			
			if ( getItemViewType(position) == VIEW_TYPE_MYMSG){
				if( view == null ){
	            	view = mInflater.inflate(R.layout.chat_row_mymsg, null);  // inflate chat row as list view row.
	            }
				mymsg = true;
				// view.setBackgroundResource(R.color.my_msg_background);
			} else {
				if( view == null ){
	            	view = mInflater.inflate(R.layout.chat_row_inmsg, null);  // inflate chat row as list view row.
	            }
				// view.setBackgroundResource(R.color.in_msg_background);
			}
			
            TextView sender = (TextView)view.findViewById(R.id.sender);
            sender.setText(item.mSender);
            
            TextView msgRow = (TextView)view.findViewById(R.id.msg_row);
            msgRow.setText(item.mMsg);
            if( mymsg ){
            	msgRow.setBackgroundResource(R.color.my_msg_background);	
            }else{
            	msgRow.setBackgroundResource(R.color.in_msg_background);
            }
            
            TextView time = (TextView)view.findViewById(R.id.time);
            time.setText(item.mTime);
            
            Log.d(TAG, "getView : " + item.mSender + " " + item.mMsg + " " + item.mTime);
            return view;
		}
    }
}
