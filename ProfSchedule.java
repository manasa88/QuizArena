package com.example.sendmessage;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class ProfSchedule extends Activity {
	
	QuizApp mApp = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.schedule);
		mApp = (QuizApp)getApplication();
		
		 if(mApp.mMyAddr.equals("Master"))
		 {
			 EditText et=(EditText) findViewById(R.id.txtOfficehrs);
			 et.setEnabled(true);
		 }
	}
}
