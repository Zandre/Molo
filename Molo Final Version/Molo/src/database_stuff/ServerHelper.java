package database_stuff;

import java.util.ArrayList;

import tutorial_stuff.Alt_MultichoiceAnswers;
import tutorial_stuff.Lesson;
import tutorial_stuff.MatchingExercise;
import tutorial_stuff.MultichoiceExercise;
import tutorial_stuff.Tutorial;
import tutorial_stuff.TutorialStep;
import tutorial_stuff.VoiceExercise;
import tutorial_stuff.WritingExercise;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServerHelper extends SQLiteOpenHelper
{
    private static String DB_PATH = "/data/data/com.example.molo/databases/";
    //private static String DB_NAME = "dummyMolo.db";
    private static String DB_NAME = "molo.db";
    private SQLiteDatabase database; 
    private final Context myContext;
    
	public ServerHelper(Context context) 
    {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
	
    public void open() throws SQLException
    {
        String myPath = DB_PATH + DB_NAME;
    	database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
 
    @Override
	public synchronized void close() 
    {
    	    if(database != null)
    		    database.close();
 
    	    super.close();
	}
	
    @Override
	public void onCreate(SQLiteDatabase db) 
	{
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{

	}
	
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	//EXERCISES
	
	public void Update_Lessons(ArrayList<Lesson> new_lessons)
	{
		database = getWritableDatabase();
		
		database.execSQL("DELETE FROM Lesson_exercise");
		
		for(int x = 0; x < new_lessons.size(); x++)
		{
			ContentValues element = new ContentValues();
			
			int id = new_lessons.get(x).getID();
			String phrase_english = new_lessons.get(x).getPhraseEnglish();
			String definition_english = new_lessons.get(x).getDefinitionEnglish();
			String audio_english = new_lessons.get(x).getAudioEnglish();
			String phrase_isixhosa = new_lessons.get(x).getPhraseIsiXhosa();
			String definition_isixhosaString = new_lessons.get(x).getDefinitionIsiXhosa();
			String audio_isixhosa = new_lessons.get(x).getAudioIsiXhosa();
			String photo = new_lessons.get(x).getPhoto();
			
			element.put("id", id);
			element.put("phrase_english", phrase_english);
			element.put("definition_english", definition_english);
			element.put("audio_english", audio_english);
			element.put("phrase_isixhosa", phrase_isixhosa);
			element.put("definition_isixhosa", definition_isixhosaString);
			element.put("audio_isixhosa", audio_isixhosa);
			element.put("photo", photo);
			
			database.insert("Lesson_exercise", null, element);
		}
		
		System.out.println("SYNCHRONIZED molo.db -> Table: Lesson_exercise");
	} 
	
	public void Update_Voices(ArrayList<VoiceExercise> new_voices)
	{
		database = getWritableDatabase();
		
		database.execSQL("DELETE FROM Voice_exercise");
		
		for(int x = 0; x < new_voices.size(); x++)
		{
			ContentValues element = new ContentValues();
			
			int id = new_voices.get(x).getID();
			String phrase_english = new_voices.get(x).getPhraseEnglish();
			String audio_english = new_voices.get(x).getAudioEnglish();
			String phrase_isixhosa = new_voices.get(x).getPhraseIsiXhosa();
			String audio_isixhosa = new_voices.get(x).getAudioIsiXhosa();
			String photo = new_voices.get(x).getPhoto();
			
			element.put("id", id);
			element.put("phrase_english", phrase_english);
			element.put("audio_english", audio_english);
			element.put("phrase_isixhosa", phrase_isixhosa);
			element.put("audio_isixhosa", audio_isixhosa);
			element.put("photo", photo);
			
			database.insert("Voice_exercise", null, element);
		}
		System.out.println("SYNCHRONIZED molo.db -> Table: Voice_exercise");
	}

	public void Update_Writers(ArrayList<WritingExercise> new_writers)
	{
		database = getWritableDatabase();
		
		database.execSQL("DELETE FROM Writing_exercise");
		
		for(int x = 0; x < new_writers.size(); x++)
		{
			ContentValues element = new ContentValues();
			
			int id = new_writers.get(x).getID();
			String phrase_english = new_writers.get(x).getPhraseEnglish();
			String hint_english = new_writers.get(x).getPhraseEnglish();
			String correct_answer_english = new_writers.get(x).getCorrectAnswerEnglish();
			String phrase_isixhosa = new_writers.get(x).getPhraseIsiXhosa();
			String hint_isixhosa = new_writers.get(x).getHintIsiXhosa();
			String correct_answer_isixhosa = new_writers.get(x).getCorrectAnswerIsiXhosa();
			String photo = new_writers.get(x).getPhoto();
			
			element.put("id", id);
			element.put("phrase_english", phrase_english);
			element.put("hint_english", hint_english);
			element.put("correct_answer_english", correct_answer_english);
			element.put("phrase_isixhosa", phrase_isixhosa);
			element.put("hint_isixhosa", hint_isixhosa);
			element.put("correct_answer_isixhosa", correct_answer_isixhosa);
			element.put("photo", photo);
			
			database.insert("Writing_exercise", null, element);
		}
		System.out.println("SYNCHRONIZED molo.db -> Table: Writing_exercise");
	}

	public void Update_Multi(ArrayList<MultichoiceExercise> new_multi)
	{
		database = getWritableDatabase();
		
		database.execSQL("DELETE FROM Multi_choice_exercise");
		
		for(int x = 0; x < new_multi.size(); x++)
		{
			ContentValues element = new ContentValues();
			
			int id = new_multi.get(x).getID();
			String phrase_english = new_multi.get(x).getPhraseEnglish();
			String correct_answer_english = new_multi.get(x).getCorrectAnswerEnglish(); 
			String phrase_isixhosa = new_multi.get(x).getPhraseIsiXhosa();
			String correct_answer_isixhosa = new_multi.get(x).getCorrectAnswerIsiXhosa();
			String photo = new_multi.get(x).getPhoto();
			int alternative_answer_id = new_multi.get(x).getAlternativAnswerID();

			element.put("id", id);
			element.put("phrase_english", phrase_english);
			element.put("correct_answer_english", correct_answer_english);
			element.put("phrase_isixhosa", phrase_isixhosa);
			element.put("correct_answer_isixhosa", correct_answer_isixhosa);
			element.put("photo", photo);
			element.put("alt_answer_ID", alternative_answer_id);
			
			database.insert("Multi_choice_exercise", null, element);
		}
		System.out.println("SYNCHRONIZED molo.db -> Table: Multi_choice_exercise");
	}
	
	public void Update_AltMultichoiceAnswers(ArrayList<Alt_MultichoiceAnswers> new_alt)
	{
		database = getWritableDatabase();
		
		database.execSQL("DELETE FROM Alt_Multichoice_Answers");
		
		for(int x = 0; x < new_alt.size(); x++)
		{
			ContentValues element = new ContentValues();
			
			int id = new_alt.get(x).getID();
			String isixhosa_1 = new_alt.get(x).getIsiXhosa_1();
			String isixhosa_2 = new_alt.get(x).getIsiXhosa_2();
			String isixhosa_3 = new_alt.get(x).getIsiXhosa_3();
			String isixhosa_4 = new_alt.get(x).getIsiXhosa_4();
			String english_1 = new_alt.get(x).getEnglish_1();
			String english_2 = new_alt.get(x).getEnglish_2();
			String english_3 = new_alt.get(x).getEnglish_3();
			String english_4 = new_alt.get(x).getEnglish_4();
			
			element.put("alt_answer_ID", id);
			element.put("isixhosa_1", isixhosa_1);
			element.put("isixhosa_2", isixhosa_2);
			element.put("isixhosa_3", isixhosa_3);
			element.put("isixhosa_4", isixhosa_4);
			element.put("english_1", english_1);
			element.put("english_2", english_2);
			element.put("english_3", english_3);
			element.put("english_4", english_4);
			
			database.insert("Alt_Multichoice_Answers", null, element);
		}
		System.out.println("SYNCHRONIZED molo.db -> Table: Alt_Multichoice_Answers");
	}

	public void Update_Matches(ArrayList<MatchingExercise> new_matches)
	{
		database = getWritableDatabase();
		
		database.execSQL("DELETE FROM Match_exercise");
		
		for(int x = 0; x < new_matches.size(); x++)
		{
			ContentValues element = new ContentValues();
			
			int id = new_matches.get(x).getID();
			String isixhosa_1 = new_matches.get(x).getIsiXhosa_1();
			String isixhosa_2 = new_matches.get(x).getIsiXhosa_2();
			String isixhosa_3 = new_matches.get(x).getIsiXhosa_3();
			String isixhosa_4 = new_matches.get(x).getIsiXhosa_4();
			String english_1 = new_matches.get(x).getEnglish_1();
			String english_2 = new_matches.get(x).getEnglish_2();
			String english_3 = new_matches.get(x).getEnglish_3();
			String english_4 = new_matches.get(x).getEnglish_4();
			
			element.put("id", id);
			element.put("isixhosa_1", isixhosa_1);
			element.put("isixhosa_2", isixhosa_2);
			element.put("isixhosa_3", isixhosa_3);
			element.put("isixhosa_4", isixhosa_4);
			element.put("english_1", english_1);
			element.put("english_2", english_2);
			element.put("english_3", english_3);
			element.put("english_4", english_4);
			
			database.insert("Match_exercise", null, element);
		}
		System.out.println("SYNCHRONIZED molo.db -> Table: Match_exercise");
	}

	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	//TUTORIAL + STEPS
	
	public void Update_TutorialSteps(ArrayList<TutorialStep> new_steps)
	{
		database = getWritableDatabase();
		
		database.execSQL("DELETE FROM Tutorial_step");
		
		for(int x = 0; x < new_steps.size(); x++)
		{
			ContentValues element = new ContentValues();
			
			int id = new_steps.get(x).getID();
			String tutorial_name = new_steps.get(x).getTutorial();
			String layout = new_steps.get(x).getLayout();
			int exercise_id = new_steps.get(x).getExerciseID();
			
			element.put("id", id);
			element.put("tutorial", tutorial_name);
			element.put("layout", layout);
			element.put("exercise_id", exercise_id);
			
			database.insert("Tutorial_step", null, element);
		}
		System.out.println("SYNCHRONIZED molo.db -> Table: Tutorial_step");
	}
	
	public void Update_Tutorial(ArrayList<Tutorial> new_tutorials)
	{
		database = getWritableDatabase();
		
		database.execSQL("DELETE FROM Tutorial");
		
		for(int x= 0; x < new_tutorials.size(); x++)
		{
			ContentValues element = new ContentValues();
			
			String name = new_tutorials.get(x).getName();
			String description = new_tutorials.get(x).getDescription();
			String photo = new_tutorials.get(x).getPhoto();
			int level = new_tutorials.get(x).getLevel();
			
			element.put("Tut_ID", name);
			element.put("description", description);
			element.put("photo", photo);
			element.put("level", level);
			
			database.insert("Tutorial", null, element);
		}
		System.out.println("SYNCHRONIZED molo.db -> Table: Tutorial");
	}
}
