package com.junyangwang.monkeyrescue;

import com.junyangwang.monkeyrescue.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class MonkeyRescue extends Activity implements OnClickListener{
	
	Button start;
	Facebook facebook = new Facebook("110210579106610");
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.main);
		 
	     start = (Button)findViewById(R.id.bStart);
	     start.setOnClickListener(this);
	     
	   
	     facebook.authorize(this, new String[] { "email", "publish_checkins", "user_location" },
	    		new DialogListener() {
	    	 
	            public void onComplete(Bundle values) {}

	            public void onFacebookError(FacebookError error) {}

	            public void onError(DialogError e) {}

	            public void onCancel() {}
	            
	        });
	        
	 }
	 

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.bStart:
			Intent i = new Intent("android.intent.action.GAME");
			startActivity(i);
			break;
		}
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
	
}
