package com.example.molo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import database_stuff.DatabaseCreater;
import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import fillscreen_classes.Voice;
import widget_stuff.Player;
import widget_stuff.Progressbar;
import widget_stuff.Stars;
import widget_stuff.Timer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class A_voice extends ActionBarActivity 
{

	private static Context mycontext;
	
	Player player = new Player();
	Player recording = new Player();
	MediaRecorder recorder = new MediaRecorder();
	Timer timer = new Timer();
	Stars starZ = new Stars();
	Progressbar progress = new Progressbar();
	Voice voice;
	
	boolean sound_effects;
	boolean is_recording = true;
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
		setContentView(R.layout.activity_a_voice);
		
		mycontext = getApplicationContext();
		
	    TutorialHelper db_tutorials = new TutorialHelper(getApplicationContext());
				
		tutorial_name = getIntent().getStringExtra("tutorial_name");
		email = getIntent().getStringExtra("email");
		layout_position = getIntent().getIntExtra("layout_position", 0);
	    tutorial_language = getIntent().getStringExtra("tutorial_language");
	    native_language = getIntent().getStringExtra("native_language");
	    sound_effects = getIntent().getBooleanExtra("sound_effects", true);
	    layout = db_tutorials.getLayout(layout_position, tutorial_name);
	    exercise_id = db_tutorials.getExerciseID(tutorial_name, layout_position);
	    count = getIntent().getIntExtra("count", 0);
	    stars = getIntent().getFloatExtra("stars", 0.0f);
	    secs = getIntent().getLongExtra("time", 0);
	    Time = (Chronometer)findViewById(R.id.cm_voice);
	    voice = db_tutorials.getVoice(exercise_id, native_language, tutorial_language);

	    
	    ProgressBar progressbar = (ProgressBar)findViewById(R.id.pb_voice);
	    progress.initializeProgressbar(count, layout_position, progressbar);
	    
		RatingBar lifeline = (RatingBar)findViewById(R.id.rb_voice);
	    starZ.InitializeStars(stars, lifeline);
	    

		timer.StartTime(secs, Time);
		final long time = timer.PauseTime(Time);
	    
		FillScreen();
		
		PreviousExperience();
		
		
		//PLAY BUTTON
		ImageButton play = (ImageButton)findViewById(R.id.voice_media_play);
		play.setOnClickListener(new View.OnClickListener()
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

		//RECORD BUTTON
		final ImageButton record = (ImageButton)findViewById(R.id.voice_media_record);
		record.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				recording.PrepareRecorderSound();
				recording.PlaySound();
				is_recording = true;
				record.setImageResource(R.drawable.record);
			}
		});

		record.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View view, MotionEvent event) 
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					record.setBackgroundResource(R.drawable.rec_pressed);
					if(is_recording == true)
					{
						try 
						{
							recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
							recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
							recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
							recorder.setOutputFile("/storage/sdcard0/recording1.mp3");
							recorder.prepare();
						} 
						catch(Exception e)
						{
							e.printStackTrace();
						}
						recorder.start();
					}

				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					record.setBackgroundResource(R.drawable.rec_normal);
					if(is_recording == true)
					{
						recorder.reset();
						recording.PrepareRecorderSound();
						is_recording = false;
						record.setImageResource(R.drawable.play);
					}
					else if(is_recording == false)
					{
						view.performClick();
					}
				}
				return true;
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
		inflater.inflate(R.menu.a_voice, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		if(sound_effects == true)
		{
			menu.findItem(R.id.voice_sounds).setChecked(true);
		}
		else
		{
			menu.findItem(R.id.voice_sounds).setChecked(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.voice_sounds:
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
					return true;
			case R.id.voice_help:
				{
					Help();
				}
				return true;
			case R.id.voice_next:
				{
				    long time = timer.PauseTime(Time);
				    
				    player.ReleaseSound();
				    
				    UserProfileHelper user = new UserProfileHelper(mycontext);
				    user.open();
				    user.UpdateUserExperience("voice_experience", email);
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
		AlertDialog.Builder popup = new AlertDialog.Builder(A_voice.this);
		popup.setTitle("THE VOICE EXERCISE");
		popup.setMessage("In this exercise you can practice your pronunciation. "
						+ "Listen to the correct pronunciation by using the media button at the bottom left side of the screen."
						+ "Hold the bottom right down to record yourself, and click the button AFTER recording to listen.");
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
		boolean has_experience = user.UserHasCompletedExercise("voice_experience", email);
		user.close();
		
		if(has_experience == false)
		{
			AlertDialog.Builder popup = new AlertDialog.Builder(A_voice.this);
			popup.setTitle("The Voice Exercise");
			popup.setMessage("\nThis is a voice exercise. "
							+ "\n\nHere you get the opportunity to practise your pronunciation. "
							+ "\nUse the Red button to listen to the correct pronunciation. "
							+ "\nHOLD DOWN the Blue button to record yourself, and TAP it afterwards to listen. "
							+ "\nYou evaluate yourself in this exercise, so practise untill you get it right! It makes no difference to your time or score.");
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
	    //>>>>>>>	TEXT VIEW
	    TextView tv_top = (TextView)findViewById(R.id.tv_voice_top);
	    tv_top.setText(voice.getPhraseTutorialLanguage());
//	    TextView rec = (TextView)findViewById(R.id.tv_voice_bottom);
//	    rec.setText(voice[1]);
	    
	    
	    //>>>>>>>	MEDIA PLAYER
	    String audio = voice.getAudio();
	    player.PrepapreSound(mycontext, audio);
		
		
		//>>>>>>	IMAGE VIEW
	    String image_name = voice.getPhoto();
	    ImageView image = (ImageView)findViewById(R.id.iv_voice);
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
