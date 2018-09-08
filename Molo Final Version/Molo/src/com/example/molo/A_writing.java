package com.example.molo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import database_stuff.DatabaseCreater;
import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import fillscreen_classes.Writing;
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
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class A_writing extends ActionBarActivity 
{    
	private static Context mycontext;
	
	Timer timer = new Timer();
	Stars starZ = new Stars();
	Progressbar progress = new Progressbar();
	Player player = new Player();
	Writing writing;
	
	boolean sound_effects;
	long secs;
	float stars;
	int layout_position;
	int exercise_id;
	int back_presses = 0;
	int count;
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
		setContentView(R.layout.activity_a_writing);
		
		mycontext = getApplicationContext();
		
	    TutorialHelper db_tutorials = new TutorialHelper(getApplicationContext());
		db_tutorials.open();
		
	    email = getIntent().getStringExtra("email");
		sound_effects = getIntent().getBooleanExtra("sound_effects", true);
		tutorial_name = getIntent().getStringExtra("tutorial_name");
		tutorial_language = getIntent().getStringExtra("tutorial_language");
		native_language = getIntent().getStringExtra("native_language");
		layout_position = getIntent().getIntExtra("layout_position", 0);
	    layout = db_tutorials.getLayout(layout_position, tutorial_name);
		count = getIntent().getIntExtra("count", 0);
		stars = getIntent().getFloatExtra("stars", 0.0f);
		secs = getIntent().getLongExtra("time", 0);
		exercise_id = db_tutorials.getExerciseID(tutorial_name, layout_position);
		writing = db_tutorials.getWriting(exercise_id, native_language, tutorial_language);
		
		db_tutorials.close();
		

		ProgressBar progressbar = (ProgressBar)findViewById(R.id.pb_writing);
		progress.initializeProgressbar(count, layout_position, progressbar);
	    
		RatingBar lifeline = (RatingBar)findViewById(R.id.rb_writing);
	    starZ.InitializeStars(stars, lifeline);
	    
	    Time = (Chronometer)findViewById(R.id.cm_writing);
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
		inflater.inflate(R.menu.a_writing, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		if(sound_effects == true)
		{
			menu.findItem(R.id.writing_sounds).setChecked(true);
		}
		else
		{
			menu.findItem(R.id.writing_sounds).setChecked(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.writing_sounds:
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
		case R.id.writing_help:
			{
				Help();
			}
			return true;
		case R.id.writing_next:
			{
				checkAnswer(stars, count, Time);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);	
		}
	}
	
	public void Help()
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(A_writing.this);
		popup.setTitle("THE WRITING EXERCISE");
		popup.setMessage("In this exercise you must answer the question at the top of the screen. "
						+ "Enter the answer in the input area at the bottom. Be sure to use the correct spelling!");
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
		boolean has_experience = user.UserHasCompletedExercise("writing_experience", email);
		user.close();
		
		if(has_experience == false)
		{
			AlertDialog.Builder popup = new AlertDialog.Builder(A_writing.this);
			popup.setTitle("The Writing Exercise");
			popup.setMessage("\nThis is a writing exercise. "
							+ "\n\nLook at the question/instruction at the top of the screen. "
							+ "\nType in your answer to the question/instruction in the foreign language at the bottom of the screen. "
							+ "\nRemeber, your spelling is important here. ");
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
	    //>>>>>>>>>>	TEXT VIEW
	    TextView tv_sentence = (TextView)findViewById(R.id.tv_writing_sentence);
	    tv_sentence.setText(writing.getPhrase());
	    
//	    TextView tv_hint = (TextView)findViewById(R.id.tv_writing_hint);
//	    tv_hint.setText(writing.getHint());
	    
	    
	    //>>>>>>>>>		IMAGE VIEW
	    ImageView image = (ImageView)findViewById(R.id.iv_writing);
	    String image_name = writing.getPhoto();
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
		UserProfileHelper user = new UserProfileHelper(mycontext);
		user.open();
		user.UpdateUserExperience("writing_experience", email);
		user.close();
		
		
	    layout_position++;
		long time = timer.PauseTime(Time);
	    final ChangeExercise change_exercise = new ChangeExercise(mycontext);
	    
	    EditText et_answer = (EditText)findViewById(R.id.et_writing);
	    String user_answer = et_answer.getText().toString();
	    
	    //remove punctuation at the end of the answer
	    user_answer = user_answer.replaceAll("[^a-zA-Z]+$", "");
	    
	    if (user_answer.toLowerCase().equals((writing.getAnswer()).toLowerCase()))
	    {
	    	Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
	    	if(sound_effects == true)
	    	{
	    		player.PlayCorrectAnswer(mycontext);
	    	}
	    	change_exercise.openLayout(layout, layout_position, count, stars, time, tutorial_name, native_language, tutorial_language, email, sound_effects);
	    }
	    else
	    {
	    	stars--;
	    	long timE = timer.PauseTime(Time);
	    	Toast.makeText(this, "INCORRECT!", Toast.LENGTH_SHORT).show();
	    	if(sound_effects == true)
	    	{
	    		player.PlayINcorrectAnswer(mycontext);
	    	}
	    		
	    		Intent intent = new Intent(this, IncorrectAnswer.class);
	    		
				intent.putExtra("layout_position", layout_position);
				intent.putExtra("count", count);
				intent.putExtra("stars", stars);
				intent.putExtra("time", timE);
				intent.putExtra("sound_effects", sound_effects);
				intent.putExtra("tutorial_name", tutorial_name);
				intent.putExtra("native_language", native_language);
				intent.putExtra("tutorial_language", tutorial_language);
				intent.putExtra("email", email);
				intent.putExtra("layout", layout);
				intent.putExtra("incorrect_answer_time", 10000);
				
				String empty = "";
				
				TextView tv_sentence = (TextView)findViewById(R.id.tv_writing_sentence);
				String question = tv_sentence.getText().toString();
				intent.putExtra("q_1", question);
				intent.putExtra("q_2", empty);
				intent.putExtra("q_3", empty);
				intent.putExtra("q_4", empty);
				
				String answer = writing.getAnswer();
				intent.putExtra("answer_1", "Correct answer: "+answer);
				intent.putExtra("answer_2", empty);
				intent.putExtra("answer_3", empty);
				intent.putExtra("answer_4", empty);
				
				intent.putExtra("your_answer1", "Your answer: "+user_answer);
				intent.putExtra("your_answer2", empty);
				intent.putExtra("your_answer3", empty);
				intent.putExtra("your_answer4", empty);
				
				
				startActivity(intent);	
	    }
	    
	    //GO TO NEXT SCREEN
	    if (stars == 0.0f)
	    {
			AlertDialog.Builder popup = new AlertDialog.Builder(A_writing.this);
			popup.setTitle("KEEP ON PRACTICING");
			popup.setMessage("Unfortunately you have failed to complete "+tutorial_name+"...");
			popup.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					change_exercise.GoToDashboard(native_language, email);
					dialog.cancel();
				}
			}); 
			popup.show();
	    }

	}
}
