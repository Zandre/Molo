package com.example.molo;

import java.util.ArrayList;

import com.example.molo.Dashboard.GetFromServer;

import tutorial_stuff.User;
import database_stuff.RecordHelper;
import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends ActionBarActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		FillScreen();
	}
	
	@Override
	public void onBackPressed()
	{	
		String email = getIntent().getStringExtra("email");
		String native_language = getIntent().getStringExtra("native_language");
		
		Intent intent = new Intent(this, Dashboard.class);
		intent.putExtra("email", email);
		intent.putExtra("native_language", native_language);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.profile_save:
			Popup();
			return true;

		default:
			return super.onOptionsItemSelected(item);	
		}
	}
	
	public void FillScreen()
	{
		//TEXTVIEW & EDIT_TEXTS
		String email = getIntent().getStringExtra("email");
		
		UserProfileHelper db_user = new UserProfileHelper(getApplicationContext());
		User user = db_user.getUser(email);
		
		TextView email_address = (TextView)findViewById(R.id.tv_profile_email);
		EditText user_name = (EditText)findViewById(R.id.et_profile_username);
		EditText password = (EditText)findViewById(R.id.et_profile_password);
		
		email_address.setText(user.getEmail().toString());
		user_name.setText(user.getName().toString(), TextView.BufferType.EDITABLE);
		password.setText(user.getPassword().toString(),	TextView.BufferType.EDITABLE);
		
		
		//RADIO BUTTONS
		RadioButton english = (RadioButton)findViewById(R.id.rb_profile_english);
		RadioButton isixhosa = (RadioButton)findViewById(R.id.rb_profile_isixhosa);
		
		if(user.getNativeLanguage().equals("english"))
		{
			english.setChecked(true);
			isixhosa.setChecked(false);
		}
		else
		{
			english.setChecked(false);
			isixhosa.setChecked(true);
		}
		
		
		//LISTVIEW
		TutorialHelper db_tutorial = new TutorialHelper(getApplicationContext());
		ArrayList<String> tutorials = db_tutorial.getTutorials();
		
		RecordHelper db_records = new RecordHelper(getApplicationContext());
		
		ArrayList<String> myCompletedTutorials = new ArrayList<String>();
		
		for(int x = 0; x < tutorials.size(); x++)
		{
			String key = email+tutorials.get(x).toString();
			if(db_records.SeeIfUserHasCompletedCertainTutorial(key) == true)
			{
				myCompletedTutorials.add(tutorials.get(x));
			}
		}
		
		ListView tutorials_completed = (ListView)findViewById(R.id.lv_profile_tutorials);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myCompletedTutorials);
		tutorials_completed.setAdapter(arrayAdapter);
	}

	public boolean check_name()
	{
		EditText user_name = (EditText)findViewById(R.id.et_profile_username);
		String name = user_name.getText().toString();
		
		int length = name.length();

		if (length >= 5)
		{
			return true;
		}
		else
		{
			Toast.makeText(this, "INVALID: \nuser name must be at least 5 characters", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	public boolean check_password()
	{
		EditText et_pw = (EditText)findViewById(R.id.et_profile_password);
		String password = et_pw.getText().toString();
		int length = password.length();
		
		//check that password is long enough, and contains digits and letters
		if (length >= 8 && password.matches(".*\\d.*") && password.matches(".*\\s*."))
		{
			return true;
		}
		else
		{		
			Toast.makeText(this, "INVALID PASSWORD: must contain digits and letters, and be a minimum of 8 characters", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	public void Popup()
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(Profile.this);
		popup.setTitle("ARE YOU SURE?");
		popup.setMessage("You are about to change your profile information. "
						+ "You will then have to log in to Molo by using your new information");
		popup.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				UpdateUser();
				LogOut();
				dialog.cancel();
			}
		}); 
		popup.show();
	}
	
	public void UpdateUser()
	{
		
		
		EditText user_name = (EditText)findViewById(R.id.et_profile_username);
		EditText password = (EditText)findViewById(R.id.et_profile_password);
		RadioButton english = (RadioButton)findViewById(R.id.rb_profile_english);
		String native_language;
		if(english.isChecked()==true)
		{
			native_language = "enlgish";
		}
		else
		{
			native_language = "isixhosa";
		}
		String email = getIntent().getStringExtra("email");
		String Name = user_name.getText().toString();
		String Password = password.getText().toString();
		
		UserProfileHelper db_user = new UserProfileHelper(getApplicationContext());
		db_user.UpdateUserProfile(email, Name, Password, native_language);
	}
	
	public void LogOut()
	{
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
	}
}
