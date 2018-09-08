package com.example.molo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import fillscreen_classes.Lesson;
import widget_stuff.Player;
import widget_stuff.Progressbar;
import widget_stuff.Stars;
import widget_stuff.Timer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class A_lesson extends Activity 
{

	private static Context mycontext;
	
	Player player = new Player();
	Timer timer = new Timer();
	Stars starZ = new Stars();
	Progressbar progress = new Progressbar();
	Lesson lesson;
	
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
	String audio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a_lesson);
		
		mycontext = getApplicationContext();
		
		TutorialHelper db_tutorial = new TutorialHelper(getApplicationContext());
		
		tutorial_name = getIntent().getStringExtra("tutorial_name");
		layout_position = getIntent().getIntExtra("layout_position", 0);
		tutorial_language = getIntent().getStringExtra("tutorial_language");
		native_language = getIntent().getStringExtra("native_language");
		sound_effects = getIntent().getBooleanExtra("sound_effects", true);
		email = getIntent().getStringExtra("email");
	    count = getIntent().getIntExtra("count", 0);
		stars = getIntent().getFloatExtra("stars", 0.0f);
	    secs = getIntent().getLongExtra("time", 0);
	    layout = db_tutorial.getLayout(layout_position, tutorial_name);
	    exercise_id = db_tutorial.getExerciseID(tutorial_name, layout_position);
	    lesson = db_tutorial.getLesson(exercise_id, native_language, tutorial_language);

	    
	    ProgressBar progressbar = (ProgressBar)findViewById(R.id.pb_lesson);
		progress.initializeProgressbar(count, layout_position, progressbar);
		
		RatingBar lifeline = (RatingBar)findViewById(R.id.rb_lesson);
	    starZ.InitializeStars(stars, lifeline);
		
	    Time = (Chronometer)findViewById(R.id.cm_lesson);
		timer.StartTime(secs, Time);
		
		FillScreen();
		
		PreviousExperience();
		
		//BUTTON
		final ImageButton button = (ImageButton)findViewById(R.id.lesson_media_button);
		button.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				if(player.isPlaying() == false)
				{
					player.PlaySound();
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
		inflater.inflate(R.menu.a_lesson, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		if(sound_effects == true)
		{
			menu.findItem(R.id.lesson_sounds).setChecked(true);
		}
		else
		{
			menu.findItem(R.id.lesson_sounds).setChecked(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.lesson_sounds:
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
		case R.id.lesson_help:
			{
				Help();
			}
			return true;
		case R.id.lesson_next:
			{
			    long time = timer.PauseTime(Time);
			    
			    player.ReleaseSound();
			    
			    UserProfileHelper user = new UserProfileHelper(mycontext);
			    user.open();
			    user.UpdateUserExperience("lesson_experience", email);
			    user.close();

			    ChangeExercise change_exercise = new ChangeExercise(mycontext);
			    change_exercise.openLayout(layout, layout_position+1, count, stars, time, 
			    							tutorial_name, native_language, tutorial_language, email, sound_effects);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);	
		}
	}
	
	public void Help()
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(A_lesson.this);
		popup.setTitle("A NEW LESSON");
		popup.setMessage("The lessons are meant to teach you new words & phrases. "
				+ "The meaning of the word is displayed at the top, while the foreign word is at the bottom."
				+ "A visual aid is given to help you make a mental connection to the new word."
				+ "You can also listen to the pronunciation of the new word with the media player button");
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
		boolean has_experience = user.UserHasCompletedExercise("lesson_experience", email);
		user.close();
		
		if(has_experience == false)
		{
			AlertDialog.Builder popup = new AlertDialog.Builder(A_lesson.this);
			popup.setTitle("A NEW LESSON");
			popup.setMessage("\nThis is a new lesson. "
							+ "\n\nThe definition of the new word is displayed at the top in Black. "
							+ "\nThe new word is displayed at the bottom in Red. "
							+ "\nUse the image to help you make a mental connection to the new word. "
							+ "\nUse the mediaplayer button to listen to the correct pronunciation of the new word.");
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
	    TextView bottom = (TextView)findViewById(R.id.tv_lesson_bottom);
	    bottom.setText(lesson.getPhrase());
	    TextView top = (TextView)findViewById(R.id.tv_lesson_top);
	    top.setText(lesson.getDefinition());
	    
	    
	    //>>>>>>>>>>	MEDIA PLAYER
	    String audio = lesson.getAudio();
	    player.PrepapreSound(mycontext, audio);

		
	    //>>>>>>>>>>	IMAGE VIEW
	    String image_name = lesson.getPhoto();
	    ImageView image = (ImageView)findViewById(R.id.iv_lesson);
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
}
