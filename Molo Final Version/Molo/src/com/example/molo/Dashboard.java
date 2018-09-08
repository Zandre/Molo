package com.example.molo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database_stuff.JSONParser;
import database_stuff.ServerHelper;
import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import scoreboard_stuff.NewScoreboard;
import tutorial_stuff.Alt_MultichoiceAnswers;
import tutorial_stuff.Lesson;
import tutorial_stuff.MatchingExercise;
import tutorial_stuff.MultichoiceExercise;
import tutorial_stuff.Tutorial;
import tutorial_stuff.TutorialStep;
import tutorial_stuff.VoiceExercise;
import tutorial_stuff.WritingExercise;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Dashboard extends ActionBarActivity 
{
	int back_pesses = 0;
	
	ArrayList<Tutorial> tutorials = new ArrayList<Tutorial>();
	ArrayList<TutorialStep> steps = new ArrayList<TutorialStep>();
	ArrayList<Lesson> lessons = new ArrayList<Lesson>();
	ArrayList<VoiceExercise> voices = new ArrayList<VoiceExercise>();
	ArrayList<WritingExercise> writers = new ArrayList<WritingExercise>();
	ArrayList<MultichoiceExercise> multi = new ArrayList<MultichoiceExercise>();
	ArrayList<Alt_MultichoiceAnswers> alt_multi = new ArrayList<Alt_MultichoiceAnswers>();
	ArrayList<MatchingExercise> matches = new ArrayList<MatchingExercise>();
	
	ProgressDialog spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		String email = getIntent().getStringExtra("email");
		
		UserProfileHelper db_user = new UserProfileHelper(getApplicationContext());
		db_user.open();
		
		//ACTION BAR
		ActionBar actionbar = getActionBar();
		actionbar.setTitle(db_user.getUserName(email));
		db_user.close();

		//go to TUTORIALS
		Button btn_tuts = (Button)findViewById(R.id.btn_DB_Tuts);
		btn_tuts.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View view) 
			{
				final String email = getIntent().getStringExtra("email");
				final String native_language = getIntent().getStringExtra("native_language");
				TutorialList(view, email, native_language);
			}
		});
		
		
		//go to SCOREBOARD
		Button scoreboard = (Button)findViewById(R.id.btn_DB_scoreboard);
		scoreboard.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				Scoreboard(v);
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		back_pesses++;
		
		if (back_pesses == 1)
		{
			Toast.makeText(this, "Press BACK again to leave", Toast.LENGTH_SHORT).show();
		}
		else if (back_pesses <= 2)
		{
			//EXit the app when back is pressed twice			
			Intent intent = new Intent(this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("Exit me", true);
			startActivity(intent);
			finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dashboard, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.dashboard_profile:
			MyProfile();
			return true;
		case R.id.dashboard_sync:
			spinner = new ProgressDialog(this);
			spinner.setTitle("Synchronizing");
			spinner.setMessage("Please be patient while Molo is being updated...");
			spinner.show();
			spinner.setCancelable(false);
			spinner.setCanceledOnTouchOutside(false);
			new GetFromServer().execute();
			return true;
		case R.id.dashboard_leave:
			Intent intent = new Intent(this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("Exit me", true);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);	
		}
	}
	
	public void Scoreboard (View view)
	{
		String email = getIntent().getStringExtra("email");
		String native_language = getIntent().getStringExtra("native_language");
		
		Intent intent = new Intent(this, NewScoreboard.class);
		intent.putExtra("email", email);
		intent.putExtra("native_language", native_language);
		
		startActivity(intent);
	}
	
	public void TutorialList (View view, String email, String native_language)
	{
		Intent intent = new Intent(this, TutorialList.class);
		intent.putExtra("email", email);
		intent.putExtra("native_language", native_language);
		startActivity(intent);
	}

	public void MyProfile()
	{
		String email = getIntent().getStringExtra("email");
		String native_language = getIntent().getStringExtra("native_language");
		
		Intent intent = new Intent(this, Profile.class);
		intent.putExtra("email", email);
		intent.putExtra("native_language", native_language);
		
		startActivity(intent);
	}
	
	public class GetFromServer extends AsyncTask<String, String, String>
	{
		
		@Override
		protected String doInBackground(String... params) 
		{
			ServerHelper db_server = new ServerHelper(getApplicationContext());
			
			// #1
			getLessons();
			db_server.Update_Lessons(lessons);
			lessons.clear();
			
			// #2
			getVoices();
			db_server.Update_Voices(voices);
			voices.clear();
			
			// #3
			getWriters();
			db_server.Update_Writers(writers);
			writers.clear();
			
			// #4
			getMulti();
			db_server.Update_Multi(multi);
			multi.clear();
			
			getAlternative_MultichoiceAnswers();
			db_server.Update_AltMultichoiceAnswers(alt_multi);
			alt_multi.clear();
			
			// #5
			getMatches();
			db_server.Update_Matches(matches);
			matches.clear();
			
			// #6 ->	DOWNLOAD STUFF
			ArrayList<String> all_filenames = new ArrayList<String>();
			ArrayList<String> must_download = new ArrayList<String>();
			try 
			{
				getAssetsFiles(all_filenames);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			getDownloads(all_filenames);
			
			TutorialHelper tutorial = new TutorialHelper(getApplicationContext());
			tutorial.getImages(must_download, all_filenames);
			tutorial.getRecordings(must_download, all_filenames);
			DownloadNow(must_download);
			
			// #7
			getTutorialSteps();
			db_server.Update_TutorialSteps(steps);
			steps.clear();
			
			// #8
			getTutorials();
			db_server.Update_Tutorial(tutorials);
			tutorials.clear();
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			spinner.dismiss();
		}
		
		
		// #1
		public void getLessons()
		{
			String TAG_SUCCESS = "success";
			String id = "";
			String phrase_english = "";
			String definition_english = "";
			String audio_english = "";
			String phrase_isixhosa = "";
			String definition_isixhosa = "";
			String audio_isixhosa = "";
			String photo = "";
			
			ArrayList<NameValuePair> Lessons = new ArrayList<NameValuePair>();
			Lessons.add(new BasicNameValuePair("id", id));
			Lessons.add(new BasicNameValuePair("phrase_english", phrase_english));
			Lessons.add(new BasicNameValuePair("definition_english", definition_english));
			Lessons.add(new BasicNameValuePair("audio_english", audio_english));
			Lessons.add(new BasicNameValuePair("phrase_isixhosa", phrase_isixhosa));
			Lessons.add(new BasicNameValuePair("definition_isixhosa", definition_isixhosa));
			Lessons.add(new BasicNameValuePair("audio_isixhosa", audio_isixhosa));
			Lessons.add(new BasicNameValuePair("photo", photo));
			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest("http://drm.csdev.nmmu.ac.za/Zandre/getLessons.php", "GET", Lessons);
			
			String TAG_NODES = "no";
			JSONArray NodesArray = null;
			
			try
			{
				int success = (json.getInt(TAG_SUCCESS));
				if(success == 1)
				{
					NodesArray = json.getJSONArray(TAG_NODES);
					
					for(int x = 0; x <= NodesArray.length()-1; x++)
					{
						JSONObject cursor = NodesArray.getJSONObject(x);
						String ID = cursor.getString("id");
						String Phrase_english = cursor.getString("phrase_english");
						String Definition_english = cursor.getString("definition_english");
						String Audio_english = cursor.getString("audio_english");
						String Phrase_isixhosa = cursor.getString("phrase_isixhosa");
						String Definition_isixhosa = cursor.getString("definition_isixhosa");
						String Audio_isixhosa = cursor.getString("audio_isixhosa");
						String Photo = cursor.getString("photo");
						
						int Id = Integer.parseInt(ID);
						
						//Make object, and chuck into arraylist
						Lesson new_lesson = new Lesson(Id, 
												Phrase_english, Definition_english, Audio_english, 
												Phrase_isixhosa, Definition_isixhosa, Audio_isixhosa,
												Photo);
						
						lessons.add(new_lesson);
					}
				}
				
			}
			catch (JSONException d)
			{
				d.printStackTrace();
			}
		}

		// #2
		public void getVoices()
		{
			String TAG_SUCCESS = "success";
			String id = "";
			String phrase_english = "";
			String audio_english = "";
			String phrase_isixhosa = "";
			String audio_isixhosa = "";
			String photo = "";
			
			ArrayList<NameValuePair> Voices = new ArrayList<NameValuePair>();
			Voices.add(new BasicNameValuePair("id", id));
			Voices.add(new BasicNameValuePair("phrase_english", phrase_english));
			Voices.add(new BasicNameValuePair("audio_english", audio_english));
			Voices.add(new BasicNameValuePair("phrase_isixhosa", phrase_isixhosa));
			Voices.add(new BasicNameValuePair("audio_isixhosa", audio_isixhosa));
			Voices.add(new BasicNameValuePair("photo", photo));
			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest("http://drm.csdev.nmmu.ac.za/Zandre/getVoiceExercises.php", "GET", Voices);
			
			String TAG_NODES = "no";
			JSONArray NodesArray = null;
			
			try
			{
				int success = (json.getInt(TAG_SUCCESS));
				if(success == 1)
				{
					NodesArray = json.getJSONArray(TAG_NODES);
					
					for(int x = 0; x <= NodesArray.length()-1; x++)
					{
						JSONObject cursor = NodesArray.getJSONObject(x);
						String ID = cursor.getString("id");
						String Phrase_english = cursor.getString("phrase_english");
						String Audio_english = cursor.getString("audio_english");
						String Phrase_isixhosa = cursor.getString("phrase_isixhosa");
						String Audio_isixhosa = cursor.getString("audio_isixhosa");
						String Photo = cursor.getString("photo");
						
						int Id = Integer.parseInt(ID);
						
						//Make object, and chuck into arraylist
						VoiceExercise new_voice = new VoiceExercise(Id, 
												Phrase_english, Audio_english, 
												Phrase_isixhosa, Audio_isixhosa,
												Photo);
						
						voices.add(new_voice);
					}
				}
				
			}
			catch (JSONException d)
			{
				d.printStackTrace();
			}
		}
		
		// #3
		public void getWriters()
		{
			String TAG_SUCCESS = "success";
			String id = "";
			String phrase_english = "";
			String hint_english = "";
			String correct_answer_english = "";
			String phrase_isixhosa = "";
			String hint_isixhosa = "";
			String correct_answer_isixhosa = "";
			String photo = "";
			
			ArrayList<NameValuePair> Writers = new ArrayList<NameValuePair>();
			Writers.add(new BasicNameValuePair("id", id));
			Writers.add(new BasicNameValuePair("phrase_english", phrase_english));
			Writers.add(new BasicNameValuePair("audio_english", hint_english));
			Writers.add(new BasicNameValuePair("correct_answer_english", correct_answer_english));
			Writers.add(new BasicNameValuePair("phrase_isixhosa", phrase_isixhosa));
			Writers.add(new BasicNameValuePair("audio_isixhosa", hint_isixhosa));
			Writers.add(new BasicNameValuePair("correct_answer_isixhosa", correct_answer_isixhosa));
			Writers.add(new BasicNameValuePair("photo", photo));
			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest("http://drm.csdev.nmmu.ac.za/Zandre/getWritingExercises.php", "GET", Writers);
			
			String TAG_NODES = "no";
			JSONArray NodesArray = null;
			
			try
			{
				int success = (json.getInt(TAG_SUCCESS));
				if(success == 1)
				{
					NodesArray = json.getJSONArray(TAG_NODES);
					
					for(int x = 0; x <= NodesArray.length()-1; x++)
					{
						JSONObject cursor = NodesArray.getJSONObject(x);
						String ID = cursor.getString("id");
						String Phrase_english = cursor.getString("phrase_english");
						String Hint_english = cursor.getString("hint_english");
						String Correct_answer_english = cursor.getString("correct_answer_english");
						String Phrase_isixhosa = cursor.getString("phrase_isixhosa");
						String Hint_isixhosa = cursor.getString("hint_isixhosa");
						String Correct_answer_isixhosa = cursor.getString("correct_answer_isixhosa");
						String Photo = cursor.getString("photo");
						
						int Id = Integer.parseInt(ID);
						
						//Make object, and chuck into arraylist
						WritingExercise new_writer = new WritingExercise(Id, 
												Phrase_english, Hint_english, Correct_answer_english,
												Phrase_isixhosa, Hint_isixhosa, Correct_answer_isixhosa,
												Photo);
						
						writers.add(new_writer);
					}
				}
				
			}
			catch (JSONException d)
			{
				d.printStackTrace();
			}
		}
		
		// #4
		public void getMulti()
		{
			String TAG_SUCCESS = "success";
			String id = "";
			String phrase_english = "";
			String correct_answer_english = "";
			String phrase_isixhosa = "";
			String correct_answer_isixhosa = "";
			String photo = "";
			String alt_answer_id = "";
			
			ArrayList<NameValuePair> Multi = new ArrayList<NameValuePair>();
			Multi.add(new BasicNameValuePair("id", id));
			Multi.add(new BasicNameValuePair("phrase_english", phrase_english));
			Multi.add(new BasicNameValuePair("correct_answer_english", correct_answer_english));
			Multi.add(new BasicNameValuePair("phrase_isixhosa", phrase_isixhosa));
			Multi.add(new BasicNameValuePair("correct_answer_isixhosa", correct_answer_isixhosa));
			Multi.add(new BasicNameValuePair("photo", photo));
			Multi.add(new BasicNameValuePair("alt_answer_id", alt_answer_id));
			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest("http://drm.csdev.nmmu.ac.za/Zandre/getMultiChoiceExercises.php", "GET", Multi);
			
			String TAG_NODES = "no";
			JSONArray NodesArray = null;
			
			try
			{
				int success = (json.getInt(TAG_SUCCESS));
				if(success == 1)
				{
					NodesArray = json.getJSONArray(TAG_NODES);
					
					for(int x = 0; x <= NodesArray.length()-1; x++)
					{
						JSONObject cursor = NodesArray.getJSONObject(x);
						String ID = cursor.getString("id");
						String Phrase_english = cursor.getString("phrase_english");
						String Correct_answer_english = cursor.getString("correct_answer_english");
						String Phrase_isixhosa = cursor.getString("phrase_isixhosa");
						String Correct_answer_isixhosa = cursor.getString("correct_answer_isixhosa");
						String Photo = cursor.getString("photo");
						String Alt_answer_id = cursor.getString("alt_answer_id");
						
						int Id = Integer.parseInt(ID);
						int Alt_ID = Integer.parseInt(Alt_answer_id);
						
						//Make object, and chuck into arraylist
						MultichoiceExercise new_multi = new MultichoiceExercise(Id, 
												Phrase_english, Correct_answer_english,
												Phrase_isixhosa, Correct_answer_isixhosa,
												Photo, Alt_ID);
						
						multi.add(new_multi);
					}
				}
				
			}
			catch (JSONException d)
			{
				d.printStackTrace();
			}
		}
		public void getAlternative_MultichoiceAnswers()
		{
			String TAG_SUCCESS = "success";
			String alt_answer_id = "";
			String isixhosa_1 = "";
			String isixhosa_2 = "";
			String isixhosa_3 = "";
			String isixhosa_4 = "";
			String english_1 = "";
			String english_2 = "";
			String english_3 = "";
			String english_4 = "";
			
			ArrayList<NameValuePair> Alt_answers = new ArrayList<NameValuePair>();
			Alt_answers.add(new BasicNameValuePair("alt_answer_id", alt_answer_id));
			Alt_answers.add(new BasicNameValuePair("1_isixhosa", isixhosa_1));
			Alt_answers.add(new BasicNameValuePair("2_isixhosa", isixhosa_2));
			Alt_answers.add(new BasicNameValuePair("3_isixhosa", isixhosa_3));
			Alt_answers.add(new BasicNameValuePair("4_isixhosa", isixhosa_4));
			Alt_answers.add(new BasicNameValuePair("1_english", english_1));
			Alt_answers.add(new BasicNameValuePair("2_english", english_2));
			Alt_answers.add(new BasicNameValuePair("3_english", english_3));
			Alt_answers.add(new BasicNameValuePair("4_english", english_4));
			
			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest("http://drm.csdev.nmmu.ac.za/Zandre/getAlternativeMultiChoiceAnswers.php", "GET", Alt_answers);
			
			String TAG_NODES = "no";
			JSONArray NodesArray = null;
			
			try
			{
				int success = (json.getInt(TAG_SUCCESS));
				if(success == 1)
				{
					NodesArray = json.getJSONArray(TAG_NODES);
					
					for(int x = 0; x <= NodesArray.length()-1; x++)
					{
						JSONObject cursor = NodesArray.getJSONObject(x);
						String Alt_answer_id = cursor.getString("alt_answer_id");
						String Isixhosa_1 = cursor.getString("1_isixhosa");
						String Isixhosa_2 = cursor.getString("2_isixhosa");
						String Isixhosa_3 = cursor.getString("3_isixhosa");
						String Isixhosa_4 = cursor.getString("4_isixhosa");
						String English_1 = cursor.getString("1_english");
						String English_2 = cursor.getString("2_english");
						String English_3 = cursor.getString("3_english");
						String English_4 = cursor.getString("4_english");
						
						int Alt_ID = Integer.parseInt(Alt_answer_id);
						
						//Make object, and chuck into arraylist
						Alt_MultichoiceAnswers new_alt = new Alt_MultichoiceAnswers(Alt_ID, 
																Isixhosa_1, Isixhosa_2, 
																Isixhosa_3, Isixhosa_4, 
																English_1, English_2, 
																English_3, English_4);
						
						alt_multi.add(new_alt);
					}
				}
				
			}
			catch (JSONException d)
			{
				d.printStackTrace();
			}
		}
		
		// #5
		public void getMatches()
		{
			String TAG_SUCCESS = "success";
			String id = "";
			String english_1 = "";
			String english_2 = "";
			String english_3 = "";
			String english_4 = "";
			String isixhosa_1 = "";
			String isixhosa_2 = "";
			String isixhosa_3 = "";
			String isixhosa_4 = "";

			
			ArrayList<NameValuePair> Matches = new ArrayList<NameValuePair>();
			Matches.add(new BasicNameValuePair("id", id));
			Matches.add(new BasicNameValuePair("english_1", english_1));
			Matches.add(new BasicNameValuePair("english_2", english_2));
			Matches.add(new BasicNameValuePair("english_3", english_3));
			Matches.add(new BasicNameValuePair("english_4", english_4));
			Matches.add(new BasicNameValuePair("isixhosa_1", isixhosa_1));
			Matches.add(new BasicNameValuePair("isixhosa_2", isixhosa_2));
			Matches.add(new BasicNameValuePair("isixhosa_3", isixhosa_3));
			Matches.add(new BasicNameValuePair("isixhosa_4", isixhosa_4));

			
			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest("http://drm.csdev.nmmu.ac.za/Zandre/getMatchingExercises.php", "GET", Matches);
			
			String TAG_NODES = "no";
			JSONArray NodesArray = null;
			
			try
			{
				int success = (json.getInt(TAG_SUCCESS));
				if(success == 1)
				{
					NodesArray = json.getJSONArray(TAG_NODES);
					
					for(int x = 0; x <= NodesArray.length()-1; x++)
					{
						JSONObject cursor = NodesArray.getJSONObject(x);
						String Id = cursor.getString("id");
						String English_1 = cursor.getString("english_1");
						String English_2 = cursor.getString("english_2");
						String English_3 = cursor.getString("english_3");
						String English_4 = cursor.getString("english_4");
						String Isixhosa_1 = cursor.getString("isixhosa_1");
						String Isixhosa_2 = cursor.getString("isixhosa_2");
						String Isixhosa_3 = cursor.getString("isixhosa_3");
						String Isixhosa_4 = cursor.getString("isixhosa_4");

						
						int ID = Integer.parseInt(Id);
						
						//Make object, and chuck into arraylist
						MatchingExercise new_match = new MatchingExercise(ID,
																English_1, English_2, 
																English_3, English_4,
																Isixhosa_1, Isixhosa_2, 
																Isixhosa_3, Isixhosa_4);
						
						matches.add(new_match);
					}
				}
				
			}
			catch (JSONException d)
			{
				d.printStackTrace();
			}
		}
		
		// #7
		public void getTutorialSteps()
		{
			String TAG_SUCCESS = "success";
			String id = "";
			String Tut_ID = "";
			String layout = "";
			String exercise_id = "";
			
			ArrayList<NameValuePair> Steps = new ArrayList<NameValuePair>();
			Steps.add(new BasicNameValuePair("id", id));
			Steps.add(new BasicNameValuePair("Tut_ID", Tut_ID));
			Steps.add(new BasicNameValuePair("layout", layout));
			Steps.add(new BasicNameValuePair("exercise_id", exercise_id));

			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest("http://drm.csdev.nmmu.ac.za/Zandre/getTutorialSteps.php", "GET", Steps);
			
			String TAG_NODES = "no";
			JSONArray NodesArray = null;
			
			try
			{
				int success = (json.getInt(TAG_SUCCESS));
				if(success == 1)
				{
					NodesArray = json.getJSONArray(TAG_NODES);
					
					for(int x = 0; x <= NodesArray.length()-1; x++)
					{
						JSONObject cursor = NodesArray.getJSONObject(x);
						String ID = cursor.getString("id");
						String TUT_ID = cursor.getString("Tut_ID");
						String Layout = cursor.getString("layout");
						String Exercise_ID = cursor.getString("exercise_id");
						
						int Id = Integer.parseInt(ID);
						int exercise_ID = Integer.parseInt(Exercise_ID);
						
						//Make object, and chuck into arraylist
						TutorialStep new_step = new TutorialStep(Id, TUT_ID, Layout, exercise_ID);
						steps.add(new_step);
					}
				}
				
			}
			catch (JSONException d)
			{
				d.printStackTrace();
			}
		}
		
		// #8
		public void getTutorials()
		{
			String TAG_SUCCESS = "success";
			String Tut_ID = "";
			String description = "";
			String level = "";
			String photo = "";
			

			ArrayList<NameValuePair> Tutorials = new ArrayList<NameValuePair>();
			
			Tutorials.add(new BasicNameValuePair("Tut_ID", Tut_ID ));
			Tutorials.add(new BasicNameValuePair("description", description));
			Tutorials.add(new BasicNameValuePair("level", level));
			Tutorials.add(new BasicNameValuePair("photo", photo));

			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest("http://drm.csdev.nmmu.ac.za/Zandre/getTutorials.php", "GET", Tutorials);
			
			String TAG_NODES = "no";
			JSONArray NodesArray = null;
		
			try
			{
				int success = (json.getInt(TAG_SUCCESS));
				if(success == 1)
				{
					NodesArray = json.getJSONArray(TAG_NODES);
					
					for(int x = 0; x <= NodesArray.length()-1; x++)
					{
						JSONObject cursor = NodesArray.getJSONObject(x);
						String TUT_ID = cursor.getString("Tut_ID");
						String Description = cursor.getString("description");
						String Level = cursor.getString("level");
						String Photo = cursor.getString("photo");
						
						int LeveL = Integer.parseInt(Level);
						
						//Make object, and chuck into arraylist
						Tutorial new_tutorial = new Tutorial(TUT_ID, Description, LeveL, Photo);
						tutorials.add(new_tutorial);
					}
				}
				
			}catch (JSONException d)
			{
				d.printStackTrace();
			}	
		}

	}
	
	public void DownloadNow(ArrayList<String> must_download)
	{
		String serviceString = Context.DOWNLOAD_SERVICE;
		DownloadManager download;
		download = (DownloadManager)getSystemService(serviceString);
		
		for(int x = 0; x < must_download.size(); x++)
		{
			System.out.println("DOWNLOADING "+must_download.get(x).toString());
			Uri uri = Uri.parse("http://drm.csdev.nmmu.ac.za/Zandre/"+must_download.get(x).toString());
			DownloadManager.Request request = new DownloadManager.Request(uri);
			request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, must_download.get(x).toString());
			request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
			request.setTitle(must_download.get(x).toString());
			long reference = download.enqueue(request);
		}
	}

	public ArrayList<String> getAssetsFiles(ArrayList<String> filenames) throws IOException
	{
		AssetManager assets = getApplicationContext().getAssets();
		String[] files = assets.list("");
		for (int x = 0; x < files.length; x++)
		{
			filenames.add(files[x].toString());
		}
		return filenames;
	}
	
	public ArrayList<String> getDownloads(ArrayList<String> filenames)
	{

		File path = getApplicationContext().getExternalFilesDir("Download");
		File[] downloads = path.listFiles();
		
		for (int x = 0; x < downloads.length; x++)
		{
			String filepath = path.toString();
			String new_filename = downloads[x].toString();
			new_filename = new_filename.replaceAll(filepath, "");
			new_filename = new_filename.replaceAll("/", "");

			filenames.add(new_filename.toString());
		}
		return filenames;
	}
}
