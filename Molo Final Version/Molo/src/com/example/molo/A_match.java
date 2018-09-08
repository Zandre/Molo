package com.example.molo;

import java.util.Random;

import database_stuff.DatabaseCreater;
import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import fillscreen_classes.Match;
import widget_stuff.Player;
import widget_stuff.Progressbar;
import widget_stuff.Stars;
import widget_stuff.Timer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class A_match extends Activity implements OnTouchListener, OnDragListener 
{
	private static Context mycontext;
	
	Timer timer = new Timer();
	Stars starZ = new Stars();
	Progressbar progress = new Progressbar();
	Player player = new Player();
	Match match;
	
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
		setContentView(R.layout.activity_a_match);
		
		mycontext = getApplicationContext();
		
	    TutorialHelper db_tutorials = new TutorialHelper(getApplicationContext());

		
		sound_effects = getIntent().getBooleanExtra("sound_effects", true);
		tutorial_name = getIntent().getStringExtra("tutorial_name");
		email = getIntent().getStringExtra("email");
		tutorial_language = getIntent().getStringExtra("tutorial_language");
		native_language = getIntent().getStringExtra("native_language");
		layout_position = getIntent().getIntExtra("layout_position", 0);
		layout = db_tutorials.getLayout(layout_position, tutorial_name);
		count = getIntent().getIntExtra("count", 0);
		stars = getIntent().getFloatExtra("stars", 0.0f);
		secs = getIntent().getLongExtra("time", 0);
		exercise_id = db_tutorials.getExerciseID(tutorial_name, layout_position);
		match = db_tutorials.getMatching(exercise_id, native_language, tutorial_language);
		
		ProgressBar progressbar = (ProgressBar)findViewById(R.id.pb_match);
		progress.initializeProgressbar(count, layout_position, progressbar);
	    
		RatingBar lifeline = (RatingBar)findViewById(R.id.rb_match);
	    starZ.InitializeStars(stars, lifeline);
	    
	    
	    Time = (Chronometer)findViewById(R.id.cm_match);
		timer.StartTime(secs, Time);
		
		findViewById(R.id.top_container).setOnDragListener(this);
		
		findViewById(R.id.tv_match_top1).setOnTouchListener(this);
		findViewById(R.id.tv_match_top2).setOnTouchListener(this);
		findViewById(R.id.tv_match_top3).setOnTouchListener(this);
		findViewById(R.id.tv_match_top4).setOnTouchListener(this);

		findViewById(R.id.box1).setOnDragListener(this);
		findViewById(R.id.box2).setOnDragListener(this);
		findViewById(R.id.box3).setOnDragListener(this);
		findViewById(R.id.box4).setOnDragListener(this);
		
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

	public void Progressbar(int count)
	{
		int layout_pos = getIntent().getIntExtra("layout_position", 0);
		
		System.out.println(">>>Match->Progressbar-> max: "+count+" position: "+layout_pos);

	    ProgressBar progressbar = (ProgressBar)findViewById(R.id.pb_match);
	    progressbar.setVisibility(ProgressBar.VISIBLE);
	    progressbar.setMax(count);
	    progressbar.setProgress(layout_pos);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.a_match, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		if(sound_effects == true)
		{
			menu.findItem(R.id.match_sounds).setChecked(true);
		}
		else
		{
			menu.findItem(R.id.match_sounds).setChecked(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.match_sounds:
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
		case R.id.match_help:
			{
				Help();
			}
			return true;
		case R.id.match_next:
			{
				UserProfileHelper user = new UserProfileHelper(mycontext);
				user.open();
				user.UpdateUserExperience("match_experience", email);
				user.close();
				
				checkAnswer();
			}
			return true;
		case R.id.match_reset:
			{
				ResetScreen(count, stars);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);	
		}
	}
	
	public void ResetScreen(int count, float stars)
	{
	    String layout = "match";
		
		int layout_pos = getIntent().getIntExtra("layout_position", 0);	
		
		Chronometer Time = (Chronometer)findViewById(R.id.cm_match);
		long time = timer.PauseTime(Time);
	    
	    String tutorial_name = getIntent().getStringExtra("tutorial_name");
	    
		String native_language = getIntent().getStringExtra("native_language");
		String tutorial_language = getIntent().getStringExtra("tutorial_language");
		String email = getIntent().getStringExtra("email");
	    
	    ChangeExercise change_exercise = new ChangeExercise(mycontext);
	    change_exercise.openLayout(layout, layout_pos, count, stars, time, tutorial_name, native_language, tutorial_language, email, sound_effects);
	}
	
	public void Help()
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(A_match.this);
		popup.setTitle("THE MATCHING EXERCISE");
		popup.setMessage("In this exercise you have to make associations between 4 different words/phrases. "
						+ "Drag the red words in the top container to one of the matching containers at the bottom.");
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
		boolean has_experience = user.UserHasCompletedExercise("match_experience", email);
		user.close();
		
		if(has_experience == false)
		{
			AlertDialog.Builder popup = new AlertDialog.Builder(A_match.this);
			popup.setTitle("The Matching Exercise");
			popup.setMessage("\nThis is a matching exercise. "
							+ "\n\nHere you have to make associations between 4 pairs of items. "
							+ "\nDrag the Red words/phrases at the top and drop them in their corresponding containers at the bottom. "
							+ "\nYou can reset the screen by selecting the option in the overflow menu. ");
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
	    TextView box1 = (TextView)findViewById(R.id.tv_match_box1);
	    TextView box2 = (TextView)findViewById(R.id.tv_match_box2);
	    TextView box3 = (TextView)findViewById(R.id.tv_match_box3);
	    TextView box4 = (TextView)findViewById(R.id.tv_match_box4);
	    TextView top1 = (TextView)findViewById(R.id.tv_match_top1);
	    TextView top2 = (TextView)findViewById(R.id.tv_match_top2);
	    TextView top3 = (TextView)findViewById(R.id.tv_match_top3);
	    TextView top4 = (TextView)findViewById(R.id.tv_match_top4);
		
		String[] native_language = match.getNativeLanguageOptions();
	    box1.setText(native_language[0]);
	    box2.setText(native_language[1]);
	    box3.setText(native_language[2]);
	    box4.setText(native_language[3]);
	    
	    Random randomNumber = new Random();
	    int randomTop1 = randomNumber.nextInt(4);
	    int randomTop2 = 0;
	    int randomTop3 = 0;
	    int randomTop4 = 0;
	    
	    if(randomTop1 == 1)
	    {
	    	randomTop2 = 4;
	    	randomTop3 = 3;
	    	randomTop4 = 2;
	    }
	    else if (randomTop1 == 2)
	    {
	    	randomTop2 = 1;
	    	randomTop3 = 4;
	    	randomTop4 = 3;
	    }
	    else if (randomTop1 == 3)
	    {
	    	randomTop2 = 1;
	    	randomTop3 = 2;
	    	randomTop4 = 4;
	    }
	    else if (randomTop1 == 4)
	    {
	    	randomTop2 = 3;
	    	randomTop3 = 2;
	    	randomTop4 = 1;
	    }
	    else if (randomTop1 == 0)
	    {
	    	randomTop1 = 3;
	    	randomTop2 = 2;
	    	randomTop3 = 1;
	    	randomTop4 = 4;
	    }
	    
	    String[] tutorial_langauge = match.getTutorialLanguageOptions();
	    top1.setText(tutorial_langauge[randomTop1-1]);
	    top2.setText(tutorial_langauge[randomTop2-1]);
	    top3.setText(tutorial_langauge[randomTop3-1]);
	    top4.setText(tutorial_langauge[randomTop4-1]);
	}
	
	@Override	
	public boolean onDrag(View v, DragEvent event) 
	{
		View view = (View) event.getLocalState();
		    switch (event.getAction()) 
		    {
		    	case DragEvent.ACTION_DRAG_ENDED:
		    		if(dropEventNotHandled(event))
		    		{
		    			//if drop somewhere illegal
		    			view.setVisibility(view.VISIBLE);
		    		}
		    		break;
		    	case DragEvent.ACTION_DROP:
					ViewGroup parent = (ViewGroup) view.getParent();
					parent.removeView(view);
					LinearLayout layout = (LinearLayout) v;
					if(layout.getChildCount() == 0)
					{
						//checks that each layout can only have 1 child
						layout.addView(view);
						view.setVisibility(View.VISIBLE);
					}
					else
					{
						parent.addView(view);
						view.setVisibility(View.VISIBLE);
					}
					break;
		    }
		    return true;
		     
	}

    private boolean dropEventNotHandled(DragEvent dragEvent) 
    {
        return !dragEvent.getResult();
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) 
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN) 
		{
			View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		    view.startDrag(null, shadowBuilder, view, 0);
		    view.setVisibility(View.INVISIBLE);
		    return true;
		} 
		else 
		{
		    return false;
		}
	}

	public void checkAnswer()
	{
	    TextView box1 = (TextView)findViewById(R.id.tv_match_box1);
	    TextView box2 = (TextView)findViewById(R.id.tv_match_box2);
	    TextView box3 = (TextView)findViewById(R.id.tv_match_box3);
	    TextView box4 = (TextView)findViewById(R.id.tv_match_box4);
		
	    layout_position++;
		TutorialHelper db_tutorials = new TutorialHelper(mycontext);
	    db_tutorials.open();
	    int answer = 0;
	    
	    String q1, q2, q3, q4, answer1, answer2, answer3, answer4, user_answer1, user_answer2, user_answer3, user_answer4;
	    q1 = box1.getText().toString();
	    q2 = box2.getText().toString();
	    q3 = box3.getText().toString();
	    q4 = box4.getText().toString();
	    answer1 = "";
	    answer2 = "";
	    answer3 = "";
	    answer4 = "";
	    user_answer1 = "";
	    user_answer2 = "";
	    user_answer3 = "";
	    user_answer4 = "";
	    
		
		
		LinearLayout container1 = (LinearLayout)findViewById(R.id.box1);
		LinearLayout container2 = (LinearLayout)findViewById(R.id.box2);
		LinearLayout container3 = (LinearLayout)findViewById(R.id.box3);
		LinearLayout container4 = (LinearLayout)findViewById(R.id.box4);
		
		if(container1.getChildCount() != 0)
		{
			View view = container1.getChildAt(0);
			TextView textview = (TextView)view;
			int number = 1;
			
			user_answer1 = textview.getText().toString();
			answer1 = db_tutorials.getMatchingCorrectAnswer(exercise_id, native_language, number);
			
			boolean checkAnswer = db_tutorials.CheckAnswer_Matching(exercise_id, user_answer1, native_language, number);
			if (checkAnswer == true)
			{
				//do nothing
			}
			else
			{
				answer++;
			}
		}
		else
		{
			answer++;
			user_answer1 = "empty";
			answer1 = db_tutorials.getMatchingCorrectAnswer(exercise_id, native_language, 1);
		}
		
		if(container2.getChildCount() != 0)
		{
			View view = container2.getChildAt(0);
			TextView textview = (TextView)view;
			int number = 2;
			
			user_answer2 = textview.getText().toString();
			answer2 = db_tutorials.getMatchingCorrectAnswer(exercise_id, native_language, number);
			
			boolean checkAnswer = db_tutorials.CheckAnswer_Matching(exercise_id, user_answer2, native_language, number);
			if (checkAnswer == true)
			{
				//do nothing
			}
			else
			{
				answer++;
			}
		}
		else
		{
			answer++;
			user_answer2 = "empty";
			answer2 = db_tutorials.getMatchingCorrectAnswer(exercise_id, native_language, 2);
		}
		
		if(container3.getChildCount() != 0)
		{
			View view = container3.getChildAt(0);
			TextView textview = (TextView)view;
			int number = 3;
			
			user_answer3 = textview.getText().toString();
			answer3 = db_tutorials.getMatchingCorrectAnswer(exercise_id, native_language, number);
						
			boolean checkAnswer = db_tutorials.CheckAnswer_Matching(exercise_id, user_answer3, native_language, number);
			if (checkAnswer == true)
			{
				//do nothing
			}
			else
			{
				answer++;
			}
		}
		else
		{
			answer++;
			user_answer3 = "empty";
			answer3 = db_tutorials.getMatchingCorrectAnswer(exercise_id, native_language, 3);
		}
		
		if(container4.getChildCount() != 0)
		{
			View view = container4.getChildAt(0);
			TextView textview = (TextView)view;
			int number = 4;
			
			user_answer4 = textview.getText().toString();
			answer4 = db_tutorials.getMatchingCorrectAnswer(exercise_id, native_language, number);
			
			boolean checkAnswer = db_tutorials.CheckAnswer_Matching(exercise_id, user_answer4, native_language, number);
			if (checkAnswer == true)
			{
				//do nothing
			}
			else
			{
				answer++;
			}
		}
		else
		{
			answer++;
			user_answer4 = "empty";
			answer4 = db_tutorials.getMatchingCorrectAnswer(exercise_id, native_language, 4);
		}
		
		db_tutorials.close();
		
		if (answer == 0)
		{
	    	Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
	    	if(sound_effects == true)
	    	{
	    		player.PlayCorrectAnswer(mycontext);
	    	}
	    	NextExercise();

		}
		else
		{
	    	Toast.makeText(this, "INCORRECT!", Toast.LENGTH_SHORT).show();
	    	if(sound_effects == true)
	    	{
	    		player.PlayINcorrectAnswer(mycontext);
	    	}
	    	IncorrectAnswer(q1, q2, q3, q4, 
	    			answer1, answer2, answer3, answer4, 
	    			user_answer1, user_answer2, user_answer3, user_answer4);
		}
		
	}
	
	public void NextExercise()
	{
		ChangeExercise change_exercise = new ChangeExercise(mycontext);
		long time = timer.PauseTime(Time);
		change_exercise.openLayout(layout, layout_position, count, stars, time, tutorial_name, native_language, tutorial_language, email, sound_effects);
	}
	
	public void IncorrectAnswer(String q1, String q2, String q3, String q4,
								String answer1, String answer2, String answer3, String answer4,
								String user_answer1, String user_answer2, String user_answer3, String user_answer4)
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
		intent.putExtra("incorrect_answer_time", 20000);
		
		intent.putExtra("q_1", q1);
		intent.putExtra("q_2", q2);
		intent.putExtra("q_3", q3);
		intent.putExtra("q_4", q4);
		
		intent.putExtra("answer_1", "Correct answer: "+answer1);
		intent.putExtra("answer_2", "Correct answer: "+answer2);
		intent.putExtra("answer_3", "Correct answer: "+answer3);
		intent.putExtra("answer_4", "Correct answer: "+answer4);
		
		intent.putExtra("your_answer1", "Your answer: "+user_answer1);
		intent.putExtra("your_answer2", "Your answer: "+user_answer2);
		intent.putExtra("your_answer3", "Your answer: "+user_answer3);
		intent.putExtra("your_answer4", "Your answer: "+user_answer4);
		
		startActivity(intent);
	}
}
