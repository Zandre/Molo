package com.example.molo;


import java.io.IOException;
import java.util.ArrayList;

import tutorial_stuff.User;
import database_stuff.DatabaseCreater;
import database_stuff.UserProfileHelper;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends ActionBarActivity 
{
	String user_email;
	String user_password;
	String user_name;
	int back_presses = 0;
	
	ArrayList<User> users = new ArrayList<User>();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		if( getIntent().getBooleanExtra("Exit me", false))
		{
		    finish();
		}
		
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		DatabaseCreater database = new DatabaseCreater(null);
		database = new DatabaseCreater(this);
		try 
		{
			database.create();
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			throw new Error("Unable to create database");
		}
		
		try
		{
			database.open();
		}
		catch(SQLException sqle)
		{
			throw sqle;
		}
		database.close();
		
		
		//CANCEL BUTTON
		Button cancel = (Button)findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		
		//LOGIN BUTTON
		Button btn_login = (Button)findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new View.OnClickListener() 
		{			
	        boolean email_found = false;
			
	        @Override
			public void onClick(View view) 
			{
				String email = getLoginEmail();
				String password = getLoginPassword();
				
				System.out.println("Email: "+email+"\nPw: "+password);
				
			    DatabaseCreater database = new DatabaseCreater(getApplicationContext());
			    database.open();
			    
			    UserProfileHelper db_users = new UserProfileHelper(getApplicationContext());
		        users = db_users.getUsers();

		        for(int x = 0; x < users.size(); x++)
		        {
		        	user_email = users.get(x).getEmail();
		        	user_password = users.get(x).getPassword();
		        	
		        	System.out.println("DB email: "+user_email+"\nDB PW: "+user_password);
		        	
		        	if(user_email.equals(email))
		        	{
		        		email_found = true;
		        		
		        		System.out.println("Email found!");
		        		
		        		if(user_password.equals(password))
		        		{
		        			System.out.println("Correct PW!");
		        			
		        			Dashboard(view, user_email, users.get(x).getNativeLanguage().toString());
		        		}
		        		else
		        		{
		        			System.out.println("Incorrect PW!");
		        			
		        			InvalidPasswordToast();
		        		}
		        	}
		        }
		        
		        
		        if(email_found == false)
		        {
		        	System.out.println("Email NOT found!");
		        	
		        	EmailNotFoundToast();
		        } 
			}
			});
	
		//SHOW PASSWORD checkbox
		final EditText password = (EditText)findViewById(R.id.eT_L_password);
		CheckBox checkbox =  (CheckBox)findViewById(R.id.login_checkbox);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				else
				{
					password.setInputType(129);
				}
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		back_presses++;
		
		if (back_presses == 1)
		{
			Toast.makeText(this, "Press BACK again to leave", Toast.LENGTH_SHORT).show();
		}
		else if (back_presses <= 2)
		{
		//EXit the app when back is pressed twice
		  Intent intent = new Intent(Intent.ACTION_MAIN);
		  intent.addCategory(Intent.CATEGORY_HOME);
		  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  startActivity(intent);
		}
	}

	public void RegisterUser (View view)
	{
		Intent intent = new Intent(this, RegisterUser.class);
		startActivity(intent);
	}

	public void Dashboard (View view, String email, String native_language)
	{
		Intent intent = new Intent(this, Dashboard.class);
		intent.putExtra("email", email);
		intent.putExtra("native_language", native_language);
		startActivity(intent);
	}
	
	public void InvalidPasswordToast()
	{
		Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show();
	}

	public void EmailNotFoundToast()
	{
		Toast.makeText(this, "Email is not registered", Toast.LENGTH_LONG).show();
	}

	public String getLoginEmail()
	{
		final EditText login_email = (EditText)findViewById(R.id.eT_L_email);
		final String Login_email = login_email.getText().toString();
		
		return Login_email;
	}
	
	public String getLoginPassword()
	{
		final EditText login_password = (EditText)findViewById(R.id.eT_L_password);
		final String Login_password = login_password.getText().toString();
		
		return Login_password;
	}
}
