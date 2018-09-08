package com.example.molo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.example.molo.R.id;

import database_stuff.DatabaseCreater;
import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import fillscreen_classes.Multi;
import widget_stuff.Player;
import widget_stuff.Progressbar;
import widget_stuff.Stars;
import widget_stuff.Timer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class A_multi extends ActionBarActivity 
{
	private static Context mycontext;
	
	Timer timer = new Timer();
	Stars starZ = new Stars();
	Progressbar progress = new Progressbar();
	Player play = new Player();
	Multi multi;
	
	boolean sound_effects;
	long secs;
	float stars;
	int layout_position;
	int back_presses = 0;
	int count;
	int exercise_id;
	Chronometer Time;
	String tutorial_name;
	String tutorial_language;
	String native_language;
	String email;
	String layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a_multi);
		
		mycontext = getApplicationContext();
		
	    TutorialHelper db_tutorials = new TutorialHelper(getApplicationContext());
		
		tutorial_name = getIntent().getStringExtra("tutorial_name");
		email = getIntent().getStringExtra("email");
		tutorial_language = getIntent().getStringExtra("tutorial_language");
		native_language = getIntent().getStringExtra("native_language");
		layout_position = getIntent().getIntExtra("layout_position", 0);
		sound_effects = getIntent().getBooleanExtra("sound_effects", true);
		layout = db_tutorials.getLayout(layout_position, tutorial_name);
		count = getIntent().getIntExtra("count", 0);
		stars = getIntent().getFloatExtra("stars", 0.0f);
		secs = getIntent().getLongExtra("time", 0);
		exercise_id = db_tutorials.getExerciseID(tutorial_name, layout_position);
		multi = db_tutorials.getMulti(exercise_id, native_language, tutorial_language);
		
		
		ProgressBar progressbar = (ProgressBar)findViewById(R.id.pb_multi);
		progress.initializeProgressbar(count, layout_position, progressbar);
	    
		RatingBar lifeline = (RatingBar)findViewById(R.id.rb_multi);
	    starZ.InitializeStars(stars, lifeline);
	    
	    Time = (Chronometer)findViewById(R.id.cm_multi);
		timer.StartTime(secs, Time);
		
	    FillScreen();
	    
	    PreviousExperience();
	}
	
	@Override
	public void onBackPressed()
	{
		back_presses++;
		
		if (back_presses == 1)
		{
			Toast.makeText(this, "Press BACK again to return to Dashboard", Toast.LENGTH_SHORT).show();
		}
		else if (back_presses <= 2)
		{
			mycontext = getApplicationContext();
			String native_language = getIntent().getStringExtra("native_language");
			String email = getIntent().getStringExtra("email");
			
			ChangeExercise go = new ChangeExercise(mycontext);
			go.GoToDashboard(native_language, email);
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.a_multi, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		if(sound_effects == true)
		{
			menu.findItem(R.id.multi_sounds).setChecked(true);
		}
		else
		{
			menu.findItem(R.id.multi_sounds).setChecked(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.multi_sounds:
			{
				if(item.isChecked())
				{
					item.setChecked(false);
					sound_effects = false;
				}
				else
				{
					item.setChecked(true);
					sound_effects = true;
				}
			}
			return true;
		case R.id.multi_help:
			{
				Help();
			}
			return true;
		case R.id.multi_next:
			{
				UserProfileHelper user = new UserProfileHelper(mycontext);
				user.open();
				user.UpdateUserExperience("multi_experience", email);
				user.close();
				
				checkAnswer(stars, count, Time);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);	
		}
	}
			
	public void Help()
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(A_multi.this);
		popup.setTitle("THE MULTIPLE CHOICE EXERCISE");
		popup.setMessage("In this exercise you are given a foreign word/phrase & a visual aid at the top of the screen."
						+ "Choose the correct answer from one of the 5 options below");
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
	
	public void PreviousExperience()
	{
		UserProfileHelper user = new UserProfileHelper(mycontext);
		user.open();
		boolean has_experience = user.UserHasCompletedExercise("multi_experience", email);
		user.close();
		
		if(has_experience == false)
		{
			AlertDialog.Builder popup = new AlertDialog.Builder(A_multi.this);
			popup.setTitle("Multiple Choice Exercise");
			popup.setMessage("\nThis is a multiple choice exercise. "
							+ "\n\nMake a connection between the word displayed in Red at the top, and one of the 5 options beneath it. ");
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
	
	public void FillScreen()
	{
	    //>>>>>>>>>		TEXT VIEW
	    TextView hint = (TextView)findViewById(R.id.tv_multi);
	    hint.setText(multi.getPhrase());
	    
	    //>>>>>>>>>		RANDOM
	    Random randomNumber = new Random();
	    int val1 = randomNumber.nextInt(5);
	    int val2 = 0;
	    int val3 = 0;
	    int val4 = 0;
	    int val5 = 0;
	    
	    if(val1 == 1)
	    {
		    val2 = 2;
		    val3 = 3;
		    val4 = 4;
		    val5 = 5;
	    }
	    else if (val1 == 2)
	    {
	    	val2 = 1;
	    	val3 = 3;
	    	val4 = 4;
	    	val5 = 5;
	    }
	    else if (val1 == 3)
	    {
	    	val2 = 4;
	    	val3 = 5;
	    	val4 = 1;
	    	val5 = 2;
	    }
	    else if (val1 == 4)
	    {
	    	val2 = 5;
	    	val3 = 1;
	    	val4 = 2;
	    	val5 = 3;
	    }

	    else if (val1 ==5)
	    {
	    	val2 = 4;
	    	val3 = 3;
	    	val4 = 2;
	    	val5 = 1;
	    }
	    else if(val1 == 0)
	    {
	    	val1 = 3;
	    	val2 = 1;
	    	val3 = 4;
	    	val4 = 2;
	    	val5 = 5;
	    }
	    
	    
	    //>>>>>>>>>		RADIO GROUP
	    RadioButton rb1 = (RadioButton)findViewById(R.id.rb1_multi);
	    RadioButton rb2 = (RadioButton)findViewById(R.id.rb2_multi);
	    RadioButton rb3 = (RadioButton)findViewById(R.id.rb3_multi);
	    RadioButton rb4 = (RadioButton)findViewById(R.id.rb4_multi);
	    RadioButton rb5 = (RadioButton)findViewById(R.id.rb5_multi);
	    String[] alternatives = multi.getAlternatives();
	    rb1.setText(alternatives[val1-1]);
	    rb2.setText(alternatives[val2-1]);
	    rb3.setText(alternatives[val3-1]);
	    rb4.setText(alternatives[val4-1]);
	    rb5.setText(alternatives[val5-1]);
	    
	    
	    //>>>>>>>>>		IMAGE VIEW
	    String image_name = multi.getPhoto();
	    ImageView image = (ImageView)findViewById(R.id.iv_multi);
	    try 
	    {
			InputStream ims = getAssets().open(image_name);
			Drawable d = Drawable.createFromStream(ims, null);
			image.setImageDrawable(d);
		} 
	    catch (IOException e) 
	    {
	    	File image_file = mycontext.getExternalFilesDir("Download/"+image_name);
	    	String filepath = image_file.toString();
	    	image.setImageDrawable(Drawable.createFromPath(filepath));
		}  
	}

	public void checkAnswer(float stars, int count, Chronometer Time)
	{
		layout_position++;
		String correct_answer = multi.getAnswer();
	    
	    RadioGroup group = (RadioGroup)findViewById(R.id.rg_multi);
	    final String user_answer = ((RadioButton)findViewById(group.getCheckedRadioButtonId())).getText().toString();
	    
	    if(user_answer.equals(correct_answer))
	    {
    		Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
    		if(sound_effects == true)
    		{
    			play.PlayCorrectAnswer(mycontext);
    		}
    		NextExercise();
	    }
	    else
	    {
    		Toast.makeText(this, "INCORRECT!", Toast.LENGTH_SHORT).show();
    		if(sound_effects == true)
    		{
    			play.PlayINcorrectAnswer(mycontext);
    		}
    		IncorrectAnswer(correct_answer, user_answer);
	    }
	}
	
	public void NextExercise()
	{
		ChangeExercise change_exercise = new ChangeExercise(mycontext);
		long time = timer.PauseTime(Time);
		change_exercise.openLayout(layout, layout_position, count, stars, time, tutorial_name, native_language, tutorial_language, email, sound_effects);
	}
	
	public void IncorrectAnswer(String correct_answer, String user_answer)
	{
		long time = timer.PauseTime(Time);
		stars--;
		
		Intent intent = new Intent(this, IncorrectAnswer.class);
		
		intent.putExtra("layout_position", layout_position);
		intent.putExtra("count", count);
		intent.putExtra("stars", stars);
		intent.putExtra("time", time);
		intent.putExtra("sound_effects", sound_effects);
		intent.putExtra("tutorial_name", tutorial_name);
		intent.putExtra("native_language", native_language);
		intent.putExtra("tutorial_language", tutorial_language);
		intent.putExtra("email", email);
		intent.putExtra("layout", layout);
		intent.putExtra("incorrect_answer_time", 10000);
		
		String empty = "";
		TextView hint = (TextView)findViewById(R.id.tv_multi);
		String question = hint.getText().toString();
		intent.putExtra("q_1", question);
		intent.putExtra("q_2", empty);
		intent.putExtra("q_3", empty);
		intent.putExtra("q_4", empty);
		
		intent.putExtra("answer_1", "Correct answer: "+correct_answer);
		intent.putExtra("answer_2", empty);
		intent.putExtra("answer_3", empty);
		intent.putExtra("answer_4", empty);
		
		intent.putExtra("your_answer1", "Your answer: "+user_answer);
		intent.putExtra("your_answer2", empty);
		intent.putExtra("your_answer3", empty);
		intent.putExtra("your_answer4", empty);
		
		startActivity(intent);
	}
}
