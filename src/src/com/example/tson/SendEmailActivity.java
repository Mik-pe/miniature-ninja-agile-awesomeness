package com.example.tson;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendEmailActivity extends Activity {
	EditText editTextTo, editTextSubject, editTextBody;
	Button btnSend, btnAttachment;
	String to, subject, body, attachmentFile;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_email);
		
		editTextTo = (EditText) findViewById(R.id.email_to_editText);
		editTextSubject = (EditText) findViewById(R.id.email_subject_editText);
		editTextBody = (EditText) findViewById(R.id.email_content_editText);
		btnSend = (Button) findViewById(R.id.send_email_btn);
		// btnAttachment = ...
		
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Send email				
			}
		});
		
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	public void sendEmail(){
		try{
			to = editTextTo.getText().toString();
			subject = editTextSubject.getText().toString();
			body = editTextBody.getText().toString();
			
			final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, 
								 new String[] {to});
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
					 new String[] {subject});
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
					 new String[] {body});
			
			this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
			
			
			
		} catch (Throwable t) {
			Toast.makeText(this, "Request failed try again: " + t.toString(), 
							Toast.LENGTH_LONG).show();
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_email, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_send_email,
					container, false);
			return rootView;
		}
	}

}
