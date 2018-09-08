package com.example.molo;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class IncorrectAnswer extends Activity 
{
	private static Context mycontext;
	
	int time;
	
	boolean sound_effects;
	long secs;
	float stars;
	int layout_position;
	int count;
	String tutorial_name;
	String tutorial_language;
	String native_language;
	String email;
	String layout;

	String q_1, q_2, q_3, q_4,
			answer_1, answer_2, answer_3, answer_4,
			your_answer1, your_answer2, your_answer3, your_answer4;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incorrect_answer);
		
		mycontext = getApplicationContext();
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowTitleEnabled(false);
		
		tutorial_name = getIntent().getStringExtra("tutorial_name");
		layout_position = getIntent().getIntExtra("layout_position", 0);
		tutorial_language = getIntent().getStringExtra("tutorial_language");
		native_language = getIntent().getStringExtra("native_language");
		sound_effects = getIntent().getBooleanExtra("sound_effects", true);
		email = getIntent().getStringExtra("email");
	    count = getIntent().getIntExtra("count", 0);
		stars = getIntent().getFloatExtra("stars", 0.0f);
	    secs = getIntent().getLongExtra("time", 0);
	    layout = getIntent().getStringExtra("layout");
	    time = getIntent().getIntExtra("incorrect_answer_time", 10000);
	    
	    //Questions
	    q_1 = getIntent().getStringExtra("q_1");
	    q_2 = getIntent().getStringExtra("q_2");
	    q_3 = getIntent().getStringExtra("q_3");
	    q_4 = getIntent().getStringExtra("q_4");

	    TextView q1 = (TextView)findViewById(R.id.tv_question1);
	    TextView q2 = (TextView)findViewById(R.id.tv_question2);
	    TextView q3 = (TextView)findViewById(R.id.tv_question3);
	    TextView q4 = (TextView)findViewById(R.id.tv_question4);
	    
	    q1.setText(q_1);
	    q2.setText(q_2);
	    q3.setText(q_3);
	    q4.setText(q_4);
	    
	    
	    //Correct Answers
	    answer_1 = getIntent().getStringExtra("answer_1");
	    answer_2 = getIntent().getStringExtra("answer_2");
	    answer_3 = getIntent().getStringExtra("answer_3");
	    answer_4 = getIntent().getStringExtra("answer_4");
	    
	    TextView answer1 = (TextView)findViewById(R.id.tv_correct_answer_1);
	    TextView answer2 = (TextView)findViewById(R.id.tv_correct_answer2);
	    TextView answer3 = (TextView)findViewById(R.id.tv_correct_answer3);
	    TextView answer4 = (TextView)findViewById(R.id.tv_correct_answer4);
	    
	    answer1.setText(answer_1);
	    answer2.setText(answer_2);
	    answer3.setText(answer_3);
	    answer4.setText(answer_3);
	    answer4.setText(answer_4);
	    
	    
	    //User answers
	    your_answer1 = getIntent().getStringExtra("your_answer1");
	    your_answer2 = getIntent().getStringExtra("your_answer2");
	    your_answer3 = getIntent().getStringExtra("your_answer3");
	    your_answer4 = getIntent().getStringExtra("your_answer4");
	    
	    TextView user_answer1 = (TextView)findViewById(R.id.tv_your_answer1);
	    TextView user_answer2 = (TextView)findViewById(R.id.tv_your_answer2);
	    TextView user_answer3 = (TextView)findViewById(R.id.tv_your_answer3);
	    TextView user_answer4 = (TextView)findViewById(R.id.tv_your_answer4);
	    
	    user_answer1.setText(your_answer1);
	    user_answer2.setText(your_answer2);
	    user_answer3.setText(your_answer3);
	    user_answer4.setText(your_answer4);
	    
	    final TextView tv_time = (TextView)findViewById(R.id.tv_incorrect_answer_time);
	    CountDownTimer timer = new CountDownTimer(time, 1000)
	    {
	    	public void onTick(long untilFinished)
	    	{
	    		String v = String.format("%02d", untilFinished/60000);
	    		int va = (int)((untilFinished%60000)/1000);
	    		tv_time.setText(v+":"+String.format("%02d", va));
	    	}

			@Override
			public void onFinish() 
			{
			    if (stars == 0.0f)
			    {
					AlertDialog.Builder popup = new AlertDialog.Builder(IncorrectAnswer.this);
					popup.setTitle("KEEP ON PRACTICING");
					popup.setMessage("Unfortunately you have failed to complete "+tutorial_name+"...");
					popup.setPositiveButton("OK", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							GoToDashBoard();
							dialog.cancel();
						}
					}); 
					popup.show();
			    }
			    else
			    {
			    	GoToNextExercise();
			    }
			}
	    };
	    timer.start();
	}
	
	@Override
	public void onBackPressed()
	{
	    if (stars == 0.0f)
	    {
			AlertDialog.Builder popup = new AlertDialog.Builder(IncorrectAnswer.this);
			popup.setTitle("KEEP ON PRACTICING");
			popup.setMessage("Unfortunately you have failed to complete "+tutorial_name+"...");
			popup.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					GoToDashBoard();
					dialog.cancel();
				}
			}); 
			popup.show();
	    }
	    else
	    {
	    	GoToNextExercise();
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.incorrect_answer, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.incorrect_next:
			{
			    if (stars == 0.0f)
			    {
					AlertDialog.Builder popup = new AlertDialog.Builder(IncorrectAnswer.this);
					popup.setTitle("KEEP ON PRACTICING");
					popup.setMessage("Unfortunately you have failed to complete "+tutorial_name+"...");
					popup.setPositiveButton("OK", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							GoToDashBoard();
							dialog.cancel();
						}
					}); 
					popup.show();
			    }
			    else
			    {
			    	GoToNextExercise();
			    }
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);	
		}
	}
	
	public void GoToDashBoard()
	{
		ChangeExercise go = new ChangeExercise(mycontext);
		go.GoToDashboard(native_language, email);
	}
	
	public void GoToNextExercise()
	{
		ChangeExercise go = new ChangeExercise(mycontext);
		go.openLayout(layout, layout_position, count, stars, secs, tutorial_name, native_language, tutorial_language, email, sound_effects);
	}
}

