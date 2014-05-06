package com.example.tson;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class LoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final Intent intent = new Intent(this, HomeActivity.class);
		
		ImageButton loginBtn = (ImageButton) findViewById(R.id.login_button);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(intent);
				finish();
				// TODO Auto-generated method stub
				
			}
		});
    	
    	
	}

}
