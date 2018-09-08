package database_stuff;

import java.util.ArrayList;

import fillscreen_classes.Lesson;
import fillscreen_classes.Match;
import fillscreen_classes.Multi;
import fillscreen_classes.Voice;
import fillscreen_classes.Writing;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TutorialHelper extends SQLiteOpenHelper 
{
    private static String DB_PATH = "/data/data/com.example.molo/databases/";
    //private static String DB_NAME = "dummyMolo.db";
    private static String DB_NAME = "molo.db";
    private SQLiteDatabase database; 
    private final Context myContext;
    
    public TutorialHelper(Context context) 
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
	
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    public ArrayList<String> getTutorials()
    { 	
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT DISTINCT Tut_ID FROM Tutorial";
    	Cursor cursor = database.rawQuery(SQL, null);
    	
    	ArrayList<String> tutorials = new ArrayList<String>();
    	
    	Boolean one = false;
    	while(cursor.moveToNext() != false)
    	{
    		if (one == false)
    		{
    			cursor.moveToFirst();
    			one = true;
    		}
    		String name = cursor.getString(cursor.getColumnIndex("Tut_ID"));
    		tutorials.add(name);
    	}
    	return tutorials;
    }

    public int getTutorialLevel(String tutorial_name)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT *	"
    				+ "FROM Tutorial "
    				+ "WHERE Tut_ID = '"+tutorial_name+"'";
    	
    	Cursor cursor = database.rawQuery(SQL, null);
    	cursor.moveToFirst();
    	int level = cursor.getInt(cursor.getColumnIndex("level"));
    	return level;
    }
    
    public String getLayout(int layout_position, String tutorial_name)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * FROM Tutorial_step WHERE tutorial = '"+tutorial_name+"'";
    	Cursor cursor = database.rawQuery(SQL, null);
    	
    	if(cursor != null)
    	{
    		cursor.moveToPosition(layout_position);
    	}
    	
    	String layout;
    	if (cursor.isAfterLast())
    	{
    		layout = "last";
    	}
    	else
    	{
    		layout = cursor.getString(cursor.getColumnIndex("layout"));
    	}
    	
    	cursor.close();
    	
    	return layout;
    }
    
    public int getCount(String tutorial_name)
    {
    	String SQL = "SELECT * FROM Tutorial_step WHERE tutorial = '"+tutorial_name+"'";
    	Cursor cursor = database.rawQuery(SQL, null);
    	int total = cursor.getCount();
    	System.out.println("COUNT: "+total);
    	return total;
    }

    public int getExerciseID(String tutorial_name, int layout_position)
    { 
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * FROM Tutorial_step "
    				+ "WHERE tutorial = '"+tutorial_name+"'";
    	Cursor cursor = database.rawQuery(SQL, null);

    	if(cursor != null)
    	{
    		cursor.moveToPosition(layout_position-1);
    	}
    	
    	int exercise_id = cursor.getInt(cursor.getColumnIndex("exercise_id"));
    	return exercise_id;
    }

    
    //FILLSCREEN STUFF
    public Lesson getLesson(int exercise_id, String native_language, String tutorial_language) 
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * FROM Lesson_exercise "
    				+ "WHERE id = '"+exercise_id+"'";
    	Cursor cursor = database.rawQuery(SQL, null);

    	if(cursor != null)
    	{
    		cursor.moveToFirst();
    	}
    	
    	String phrase = cursor.getString(cursor.getColumnIndex("phrase_"+tutorial_language));
    	String definition = cursor.getString(cursor.getColumnIndex("definition_"+native_language));
    	String audio = cursor.getString(cursor.getColumnIndex("audio_"+tutorial_language));
    	String photo = cursor.getString(cursor.getColumnIndex("photo"));
    	cursor.close();
    	
    	Lesson lesson = new Lesson(phrase, definition, audio, photo);
    	return lesson;
    }
    
    public Voice getVoice(int exercise_id, String native_language, String tutorial_language)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * FROM Voice_exercise "
    				+ "WHERE id = '"+exercise_id+"'";
    	Cursor cursor = database.rawQuery(SQL, null);

    	if(cursor != null)
    	{
    		cursor.moveToFirst();
    	}
    	
    	String tut_phrase = cursor.getString(cursor.getColumnIndex("phrase_"+tutorial_language));
    	String native_phrase = cursor.getString(cursor.getColumnIndex("phrase_"+native_language));
    	String audio = cursor.getString(cursor.getColumnIndex("audio_"+tutorial_language));
    	String photo = cursor.getString(cursor.getColumnIndex("photo"));
    	cursor.close();
    	
    	Voice voice = new Voice(tut_phrase, native_phrase, audio, photo);
    	return voice;
    }  
    
    public Writing getWriting(int exercise_id, String native_language, String tutorial_language)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * FROM Writing_exercise "
    				+ "WHERE id = '"+exercise_id+"'";
    	Cursor cursor = database.rawQuery(SQL, null);

    	if(cursor != null)
    	{
    		cursor.moveToFirst();
    	}
    	
    	String phrase = cursor.getString(cursor.getColumnIndex("phrase_"+native_language));
    	String hint = cursor.getString(cursor.getColumnIndex("hint_"+tutorial_language));
    	String answer = cursor.getString(cursor.getColumnIndex("correct_answer_"+tutorial_language));
    	String photo = cursor.getString(cursor.getColumnIndex("photo"));
    	cursor.close();
    	
    	Writing writing = new Writing(phrase, hint, answer, photo);
    	return writing;
    }

    public Multi getMulti(int exercise_id, String native_language, String tutorial_language)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * FROM Multi_choice_exercise "
    				+ "WHERE id = '"+exercise_id+"'";
    	Cursor cursor = database.rawQuery(SQL, null);

    	if(cursor != null)
    	{
    		cursor.moveToFirst();
    	}
    	
    	
    	String phrase = cursor.getString(cursor.getColumnIndex("phrase_"+native_language));
    	String answer = cursor.getString(cursor.getColumnIndex("correct_answer_"+tutorial_language));
    	String photo = cursor.getString(cursor.getColumnIndex("photo"));
    	cursor.close();
    	
    	String SQL2 = "SELECT * FROM Alt_Multichoice_Answers WHERE alt_answer_ID = '"+exercise_id+"'";
    	Cursor cursor2 = database.rawQuery(SQL2, null);

    	if(cursor2 != null)
    	{
    		cursor2.moveToFirst();
    	}
    	String alt1 = cursor2.getString(cursor2.getColumnIndex(tutorial_language+"_1"));
    	String alt2 = cursor2.getString(cursor2.getColumnIndex(tutorial_language+"_2"));
    	String alt3 = cursor2.getString(cursor2.getColumnIndex(tutorial_language+"_3"));
    	String alt4 = cursor2.getString(cursor2.getColumnIndex(tutorial_language+"_4"));
    	cursor2.close();
    	
    	Multi multi = new Multi(phrase, answer, photo, alt1, alt2, alt3, alt4);
    	return multi;
    }
    
    public Match getMatching(int exercise_id, String native_language, String tutorial_language)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * FROM Match_exercise "
    				+ "WHERE id = '"+exercise_id+"'";
    	Cursor cursor = database.rawQuery(SQL, null);

    	if(cursor != null)
    	{
    		cursor.moveToFirst();
    	}
    	

    	
    	//The Tutorial Langauge
    	String t1 = cursor.getString(cursor.getColumnIndex(tutorial_language+"_1"));
    	String t2 = cursor.getString(cursor.getColumnIndex(tutorial_language+"_2"));
    	String t3 = cursor.getString(cursor.getColumnIndex(tutorial_language+"_3"));
    	String t4 = cursor.getString(cursor.getColumnIndex(tutorial_language+"_4"));

    	//User's Native Language
    	String n1 = cursor.getString(cursor.getColumnIndex(native_language+"_1"));
    	String n2 = cursor.getString(cursor.getColumnIndex(native_language+"_2"));
    	String n3 = cursor.getString(cursor.getColumnIndex(native_language+"_3"));
    	String n4 = cursor.getString(cursor.getColumnIndex(native_language+"_4"));

    	Match match = new Match(n1, n2, n3, n4, t1, t2, t3, t4);
    	return match;
    }
    
    public boolean CheckAnswer_Matching(int exercise_id, String user_answer, String native_language, int number)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * FROM Match_exercise "
    				+ "WHERE id = '"+exercise_id+"'";
    	Cursor cursor = database.rawQuery(SQL, null);

    	if(cursor != null)
    	{
    		cursor.moveToFirst();
    	}
    	
    	String db_answer = cursor.getString(cursor.getColumnIndex(native_language+"_"+number));
    	
    	if(user_answer.equals(db_answer))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public String getMatchingCorrectAnswer(int exercise_id, String native_language, int number)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * FROM Match_exercise "
    				+ "WHERE id = '"+exercise_id+"'";
    	Cursor cursor = database.rawQuery(SQL, null);

    	if(cursor != null)
    	{
    		cursor.moveToFirst();
    	}
    	
    	String correct_answer = cursor.getString(cursor.getColumnIndex(native_language+"_"+number));
    	return correct_answer;
    }
    
    
    
    //DOWNLOAD STUFF
    public ArrayList<String> getImages(ArrayList<String> must_download, ArrayList<String> all_filenames)
    {
    	database = getReadableDatabase();
    	
    	String[] table_names = {"Writing_exercise", "Voice_exercise", "Multi_choice_exercise", "Lesson_exercise", "Tutorial"};
    	for(int x = 0; x < table_names.length; x++)
    	{
    		String SQL = "SELECT * FROM "+table_names[x];
    		Cursor cursor = database.rawQuery(SQL, null);
    		while(cursor.moveToNext())
    		{
    			String image = cursor.getString(cursor.getColumnIndex("photo"));
    			if(!all_filenames.contains(image.toString()) && !must_download.contains(image.toString()))
    			{
    				must_download.add(image);
    			}
    		}
    	}
    	return must_download;
    }
    
    public ArrayList<String> getRecordings(ArrayList<String> must_download, ArrayList<String> all_filenames)
    {
    	database = getReadableDatabase();
    	
    	String[] table_names = {"Voice_exercise", "Lesson_exercise"};
    	for(int x = 0; x < table_names.length; x++)
    	{
    		String SQL = "SELECT * FROM "+table_names[x];
    		Cursor cursor = database.rawQuery(SQL, null);
    		while(cursor.moveToNext())
    		{
    			String audio_english = cursor.getString(cursor.getColumnIndex("audio_english"));
    			if(!all_filenames.contains(audio_english) && !must_download.contains(audio_english))
    			{
    				must_download.add(audio_english);
    			}
    			
    			String audio_isixhosa = cursor.getString(cursor.getColumnIndex("audio_isixhosa"));
    			if(!all_filenames.contains(audio_isixhosa) && !must_download.contains(audio_isixhosa))
    			{
    				must_download.add(audio_isixhosa);
    			}
    		}
    	}
    	return must_download;
    }
}
