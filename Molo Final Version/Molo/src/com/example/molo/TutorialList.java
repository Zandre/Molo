package com.example.molo;

import java.util.ArrayList;

import tutorial_stuff.TutorialCompletedInformation;
import database_stuff.RecordHelper;
import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import adapter_stuff.TutorialListAdapter;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class TutorialList extends ListActivity 
{
	private static Context mycontext;
	
	@Override
	protected void onCreate(Bundle icicle) 
	{
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		super.onCreate(icicle);
			
	    ArrayList<TutorialCompletedInformation> tutorials;
	    
	    RecordHelper db_records = new RecordHelper(getApplicationContext());
	    db_records.open();
	    tutorials = db_records.getTutorials_AND_Info();
	    db_records.close();
	    
		String email = getIntent().getStringExtra("email");
	    
	    TutorialListAdapter adapter = new TutorialListAdapter(this, tutorials, email);
	    setListAdapter(adapter);
	}
	
	@Override
	public void onBackPressed()
	{

			mycontext = getApplicationContext();
			String native_language = getIntent().getStringExtra("native_language");
			String email = getIntent().getStringExtra("email");
			
			ChangeExercise go = new ChangeExercise(mycontext);
			go.GoToDashboard(native_language, email);
	}
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id)
	{
		TextView tut = (TextView)view.findViewById(R.id.tv_tutorialName);
		String tutorial_name =	tut.getText().toString();
		String email = getIntent().getStringExtra("email");
		
		
		TutorialHelper tutorial = new TutorialHelper(getApplicationContext());
		tutorial.open();
		int tutorial_level = tutorial.getTutorialLevel(tutorial_name);
		tutorial.close();
		
		UserProfileHelper user = new UserProfileHelper(getApplicationContext());
		user.open();
		int user_level = user.getUserLevel(email);
		user.close();
		
		if(user_level >= tutorial_level)
		{
			GoToExercise(tutorial_name, email);
		}
		else
		{
			AlertDialog.Builder popup = new AlertDialog.Builder(TutorialList.this);
			popup.setTitle("NOT SO FAST");
			popup.setMessage("You must first complete the preceding tutorials to gain access to "+tutorial_name);
			popup.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.cancel();
				}
			}); 
			popup.show();
		}
	}

	public void GoToExercise(String tutorial_name, String email)
	{
		int layout_pos = 0;
		long time = 0;
		int stars = 3;
		boolean sound_effects = true;

		String native_language = getIntent().getStringExtra("native_language");
		
		String tutorial_language = "";
		if(native_language.equals("english"))
		{
			tutorial_language = "isixhosa";
		}
		else if(native_language.equals("isixhosa"))
		{
			tutorial_language = "english";
		}
		
		mycontext = getApplicationContext();
		
	    TutorialHelper db_tutorials = new TutorialHelper(getApplicationContext());
	    db_tutorials.open();
	    String layout = db_tutorials.getLayout(layout_pos, tutorial_name);
	    int count = db_tutorials.getCount(tutorial_name);
	    db_tutorials.close();
	    
	    layout_pos++;
	    
	    ChangeExercise change_exercise = new ChangeExercise(mycontext);
	    change_exercise.openLayout(layout, layout_pos, count, stars, time, tutorial_name, native_language, tutorial_language, email, sound_effects);
	}
}
