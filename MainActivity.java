package com.example.sendmessage;
import static com.example.sendmessage.Constants.MSG_PUSHOUT_DATA;
import static com.example.sendmessage.Constants.MSG_REGISTER_ACTIVITY;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class MainActivity extends Activity {
	public static final String TAG = "QuizAct";
	QuizApp mApp = null;
	QuestionFragment mChatFrag = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		Intent i = getIntent();
		String initMsg = i.getStringExtra("FIRST_MSG");
		
		mApp = (QuizApp)getApplication(); 
		initFragment(initMsg);
		
	}
	public void initFragment(String initMsg) {
    	// to add fragments to your activity layout, just specify which viewgroup to place the fragment.
    	final FragmentTransaction ft = getFragmentManager().beginTransaction();
    	if( mChatFrag == null ){
    		//mChatFrag = QuestionFragment.newInstance(this, ConnectionService.getInstance().mConnMan.mServerAddr);
    		mChatFrag = QuestionFragment.newInstance(this, null, initMsg);
    	}
    	
    	Log.d(TAG, "initFragment : show chat fragment..." + initMsg);
    	// chat fragment on top, do not do replace, as frag_detail already hard coded in layout.
    	ft.add(R.id.frag_chat, mChatFrag, "chat_frag");
    	ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    	ft.commit();
    }
	@Override
	public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: chat activity resume, register activity to connection service !");
        registerActivityToService(true);
    }
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause: chat activity closed, de-register activity from connection service !");
		registerActivityToService(false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, " onDestroy: nothing... ");
	}
	 /**
     * register this activity to service process 
     */
    protected void registerActivityToService(boolean register){
    	if( ConnectionService.getInstance() != null ){
	    	Message msg = ConnectionService.getInstance().getHandler().obtainMessage();
	    	msg.what = MSG_REGISTER_ACTIVITY;
	    	msg.obj = this;
	    	msg.arg1 = register ? 1 : 0;
	    	ConnectionService.getInstance().getHandler().sendMessage(msg);
    	}
    }
	
	 /**
     * post send msg to service to handle it in background.
     */
    public void pushOutMessage(String jsonstring) {
    	Log.d(TAG, "pushOutMessage : " + jsonstring);
    	Message msg = ConnectionService.getInstance().getHandler().obtainMessage();
    	msg.what = MSG_PUSHOUT_DATA;
    	msg.obj = jsonstring;
    	ConnectionService.getInstance().getHandler().sendMessage(msg);
    }
    
    /**
     * show the msg in chat fragment, update view must be from ui thread.
     */
    public void showMessage(final MessageRow row){
    	runOnUiThread(new Runnable() {
    		@Override public void run() {
    			Log.d(TAG, "showMessage : " + row.mMsg);
    			if( mChatFrag != null ){
    				mChatFrag.appendChatMessage(row);
    			}
    		}
    	});
    }

}
