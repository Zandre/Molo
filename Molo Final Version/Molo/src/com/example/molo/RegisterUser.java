package com.example.molo;

import database_stuff.DatabaseCreater;
import database_stuff.UserProfileHelper;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.Toast;

public class RegisterUser extends ActionBarActivity 
{
	DatabaseCreater database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		
		database = new DatabaseCreater(getApplicationContext());
		database.open();
		
		Button btn_register = (Button)findViewById(R.id.btn_R_register);
		
		final EditText user_name = (EditText)findViewById(R.id.eT_R_userName);
		final EditText email = (EditText)findViewById(R.id.eT_R_email);
		final EditText password = (EditText)findViewById(R.id.eT_R_password);
		final RadioButton rb_english = (RadioButton)findViewById(R.id.rb_english);
		final RadioButton rb_isixhosa = (RadioButton)findViewById(R.id.rb_isixhosa);
				
		//REGISTER
		btn_register.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				final String Name = user_name.getText().toString();
				final String Email = email.getText().toString();
				final String Password = password.getText().toString();
				
				String native_language = "";
				if (rb_english.isChecked())
				{
					native_language = "english";
				}
				else if (rb_isixhosa.isChecked())
				{
					native_language = "isixhosa";
				}
				
				
				
				if (check_name(Name) == true && check_email(Email) == true && check_password(Password) == true)
				{
					RegisterNewUser(Name, Email, Password, native_language);
				}
			}
		});
		
		//SHOW PASSWORD checkbox
		CheckBox checkbox = (CheckBox)findViewById(R.id.register_checkbox);
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

	//>>>>>METHODS & STUFF
	
	@Override
	public void onBackPressed()
	{
		//When back is pressed ---> go to Login
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
	}
	
	public void RegisterNewUser(String Name, String Email, String Password, String native_language)
	{
		UserProfileHelper user = new UserProfileHelper(getApplicationContext());
		user.open();
		user.insertUser(Name, Email, Password, native_language);
		user.close();
		
		Login(null);
	}
	
	public void Login (View view)
	{
		//onClick	-> back to login screen
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
	}

	public boolean check_name(String name)
	{
		int length = name.length();

		//check if name is at least 5 characters long
		if (length >= 5)
		{
			//Debug
			System.out.println(">>>LENGTH OF NAME IS GOOD:"+ length);
			
			return true;
		}
		else
		{
			//Debug
			System.out.println(">>>LENGTH OF NAME TOO SHORT:"+ length);
			
			Toast.makeText(this, "INVALID: \nuser name must be at least 5 characters", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	public boolean check_email(String email)
	{
		int length = email.length();
		
		//check that email is long enough (i.e  real email)
		if (length >= 10 && email.contains("@"))
		{
			//Debug
			System.out.println(">>>EMIAL IS VALID");

			return true;
		}
		else
		{
			System.out.println(">>>INVALID EMAIL");
			
			Toast.makeText(this, "INVALID email adress", Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	public boolean check_password(String password)
	{
		int length = password.length();
		
		//check that password is long enough, and contains digits and letters
		if (length >= 8 && password.matches(".*\\d.*") && password.matches(".*\\s*."))
		{
			//Debug
			System.out.println(">>>PASSWORD IS VALID");

			return true;
		}
		else
		{
			System.out.println(">>>INVALID PASSWORD");
			
			Toast.makeText(this, "INVALID PASSWORD: must contain digits and letters, and be a minimum of 8 characters", Toast.LENGTH_LONG).show();
			return false;
		}
	}
}
