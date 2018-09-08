package com.example.molo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import scoreboard_stuff.NewScoreboard;
import database_stuff.RecordHelper;
import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ChangeExercise extends Activity
{
	private final Context mycontext;
	
	public ChangeExercise(Context context)
	{
		this.mycontext = context;
	}
	
	public void openLayout(String layout, int layout_pos, int count, float stars, long time, String tutorial_name, String native_language, String tutorial_language, String email, boolean sound_effects)
	{
		System.out.println("<<<CHANGE EXERCISE>>>"
				+ "\nLayout--------> "+layout
				+ "\nLayoutPos-----> "+layout_pos
				+ "\nTutorialName--> "+tutorial_name
				+ "\nEmail---------> "+email);
		
		if(layout.equals("writing"))
		{
			Intent intent = new Intent(mycontext, A_writing.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("layout_position", layout_pos);
			intent.putExtra("count", count);
			intent.putExtra("stars", stars);
			intent.putExtra("time", time);
			intent.putExtra("sound_effects", sound_effects);
			intent.putExtra("tutorial_name", tutorial_name);
			intent.putExtra("native_language", native_language);
			intent.putExtra("tutorial_language", tutorial_language);
			intent.putExtra("email", email);
			mycontext.startActivity(intent);
		}
		else if (layout.equals("voice"))
		{
			Intent intent = new Intent(mycontext, A_voice.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("layout_position", layout_pos);
			intent.putExtra("count", count);
			intent.putExtra("stars", stars);
			intent.putExtra("time", time);
			intent.putExtra("sound_effects", sound_effects);
			intent.putExtra("tutorial_name", tutorial_name);
			intent.putExtra("native_language", native_language);
			intent.putExtra("tutorial_language", tutorial_language);
			intent.putExtra("email", email);
			mycontext.startActivity(intent);
		}
		else if (layout.equals("multi"))
		{
			Intent intent = new Intent(mycontext, A_multi.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("layout_position", layout_pos);
			intent.putExtra("count", count);
			intent.putExtra("stars", stars);
			intent.putExtra("time", time);
			intent.putExtra("sound_effects", sound_effects);
			intent.putExtra("tutorial_name", tutorial_name);
			intent.putExtra("native_language", native_language);
			intent.putExtra("tutorial_language", tutorial_language);
			intent.putExtra("email", email);
			mycontext.startActivity(intent);
		}
		else if (layout.equals("match"))
		{
			Intent intent = new Intent(mycontext, A_match.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("layout_position", layout_pos);
			intent.putExtra("count", count);
			intent.putExtra("stars", stars);
			intent.putExtra("time", time);
			intent.putExtra("sound_effects", sound_effects);
			intent.putExtra("tutorial_name", tutorial_name);
			intent.putExtra("native_language", native_language);
			intent.putExtra("tutorial_language", tutorial_language);
			intent.putExtra("email", email);
			mycontext.startActivity(intent);
		}
		else if (layout.equals("lesson"))
		{
			Intent intent = new Intent(mycontext, A_lesson.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("layout_position", layout_pos);
			intent.putExtra("count", count);
			intent.putExtra("stars", stars);
			intent.putExtra("time", time);
			intent.putExtra("sound_effects", sound_effects);
			intent.putExtra("tutorial_name", tutorial_name);
			intent.putExtra("native_language", native_language);
			intent.putExtra("tutorial_language", tutorial_language);
			intent.putExtra("email", email);
			mycontext.startActivity(intent);
		}
		else if (layout.equals("last"))
		{
			int Time = Math.round((int)time/1000);
			int score = Math.round(stars);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			
			RecordHelper db_records = new RecordHelper(mycontext);
			db_records.open();
			db_records.insertCompletedTutorial(email, tutorial_name, Time, score, date);
			db_records.close();

			//Update user's level when applicable
			TutorialHelper tutorial = new TutorialHelper(mycontext);
			tutorial.open();
			int tutorial_level = tutorial.getTutorialLevel(tutorial_name);
			
			UserProfileHelper user = new UserProfileHelper(mycontext);
			int user_level = user.getUserLevel(email);
			
			if(user_level == tutorial_level)
			{
				user_level++;
				user.UpdateUserLevel(email, user_level);
			}
			tutorial.close();
			
			
			Intent intent = new Intent(mycontext, Dashboard.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("layout_position", layout_pos);
			intent.putExtra("count", count);
			intent.putExtra("stars", stars);
			intent.putExtra("time", time);
			intent.putExtra("tutorial_name", tutorial_name);
			intent.putExtra("native_language", native_language);
			intent.putExtra("tutorial_language", tutorial_language);
			intent.putExtra("email", email);
			mycontext.startActivity(intent);
		}
	}
	
	public void GoToDashboard(String native_language, String email)
	{
		Intent intent = new Intent(mycontext, Dashboard.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("native_language", native_language);
		intent.putExtra("email", email);
		mycontext.startActivity(intent);
	}
	
	public void GoToScorebaordOptions(String native_language, String email)
	{
		Intent intent = new Intent(mycontext, NewScoreboard.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("native_language", native_language);
		intent.putExtra("email", email);
		mycontext.startActivity(intent);
	}
}
