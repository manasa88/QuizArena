package com.example.sendmessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainQuiz extends Activity {
	Button onfly;
	Button qsns;
	Button atten;
	Button ansReport;
	QuizApp mApp = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_quiz);
		mApp = (QuizApp)getApplication();
		 if(mApp.mMyAddr.equals("Master"))
		 {
		qsns = (Button) findViewById(R.id.btn_qsnsDb);
		 qsns.setVisibility(View.VISIBLE);
		qsns.setOnClickListener(new View.OnClickListener() {

			   @Override
			   public void onClick(View v) {
			   
				   Intent intent = new Intent(MainQuiz.this,Questions.class);
		    		  startActivity(intent);	  
		    		  }
			   });
		 }
		onfly = (Button) findViewById(R.id.btn_qsnsOf);
		 
		onfly.setOnClickListener(new View.OnClickListener() {

			   @Override
			   public void onClick(View v) {
			   
				   Intent intent = new Intent(MainQuiz.this,Onthefly.class);
		    		  startActivity(intent);	 
		    		  }
			   });
		atten = (Button) findViewById(R.id.btn_atten);
		 
		atten.setOnClickListener(new View.OnClickListener() {

			   @Override
			   public void onClick(View v) {
			   
				   Intent intent = new Intent(MainQuiz.this,Attendance.class);
		    		  startActivity(intent);	 
		    		  }
			   });
		ansReport = (Button) findViewById(R.id.btn_ansReport);
		 
		ansReport.setOnClickListener(new View.OnClickListener() {

			   @Override
			   public void onClick(View v) {
			   
				   Intent intent = new Intent(MainQuiz.this,QsnReport.class);
		    		  startActivity(intent);	 
		    		  }
			   });
	}	

}

