package database_stuff;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tutorial_stuff.TutorialCompletedInformation;
import tutorial_stuff.TutorialRecord;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordHelper extends SQLiteOpenHelper
{
    private static String DB_PATH = "/data/data/com.example.molo/databases/";
    //private static String DB_NAME = "dummyMolo.db";
    private static String DB_NAME = "molo.db";
    private SQLiteDatabase database; 
    private final Context myContext;
    
	public RecordHelper(Context context) 
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
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 
    public ArrayList<TutorialRecord> getRecords_All()
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * "
    				+ "From Records ";
    	
    	Cursor cursor = database.rawQuery(SQL, null);
    	
    	ArrayList<TutorialRecord> records = new ArrayList<TutorialRecord>();
    	
    	while(cursor.moveToNext())
    	{
    		String Key = cursor.getString(cursor.getColumnIndex("key"));
    		int i = Key.lastIndexOf("Tutorial");
    		String email = Key.substring(0, i);
    		String Tutorial = Key.substring(i);
    		int Time = cursor.getInt(cursor.getColumnIndex("time"));
    		int Score = cursor.getInt(cursor.getColumnIndex("score"));
    		String Date = cursor.getString(cursor.getColumnIndex("date"));

    		records.add(new TutorialRecord(email, Tutorial, Time, Score, Date));
    	}
		cursor.close();

    	return records;
    }
    
    public ArrayList<TutorialRecord> getRecords_Mine(String email)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * From Records "
				+ "WHERE key LIKE '%"+email+"%' "
				+ "ORDER BY key ASC";
    	
    	Cursor cursor = database.rawQuery(SQL, null);
    	
    	ArrayList<TutorialRecord> records = new ArrayList<TutorialRecord>();

    	while(cursor.moveToNext())
    	{
    		int Time = cursor.getInt(cursor.getColumnIndex("time"));
    		int Score = cursor.getInt(cursor.getColumnIndex("score"));
    		String Date = cursor.getString(cursor.getColumnIndex("date"));
    		String key = cursor.getString(cursor.getColumnIndex("key"));
    		String Tutorial = key.replaceAll(email, "");

    		records.add(new TutorialRecord(email, Tutorial, Time, Score, Date));
    	}
		cursor.close();
    	return records;
    }
    
    public ArrayList<TutorialRecord> getRecords_TopScores(String email)
    {
    	database = getReadableDatabase();
    	
    	TutorialHelper db_tutorials = new TutorialHelper(myContext);
    	
    	ArrayList<String> tutorials = db_tutorials.getTutorials();
    	ArrayList<TutorialRecord> records = new ArrayList<TutorialRecord>();
    	
    	for (int x = 0; x < tutorials.size(); x++)
    	{
    		String SQL = "SELECT key, MAX(score), MIN(time), date "
    					+ "FROM Records "
    					+ "WHERE key Like '%"+tutorials.get(x)+"%'";

    		Cursor cursor = database.rawQuery(SQL, null);
    		
    		if(cursor.moveToNext())
    		{
    			String Key = cursor.getString(cursor.getColumnIndex("key"));
    			if(Key != null && !Key.isEmpty())
    			{
	    			String Tutorial = Key.replaceAll(email, "");
	    			int Time = cursor.getInt(cursor.getColumnIndex("MIN(time)"));
	    			int Score = cursor.getInt(cursor.getColumnIndex("MAX(score)"));
	    			String Date = cursor.getString(cursor.getColumnIndex("date"));
	    		    
	    			records.add(new TutorialRecord(email, Tutorial, Time, Score, Date));
    			}
    		}
    		cursor.close();
    	}
    	return records;
    }
    
    public ArrayList<TutorialRecord> getRecords_MyTopScores(String email)
    {
    	database = getReadableDatabase();
    	
    	TutorialHelper db_tutorials = new TutorialHelper(myContext);
    	
    	ArrayList<String> tutorials = db_tutorials.getTutorials();
    	ArrayList<TutorialRecord> records = new ArrayList<TutorialRecord>();
    	
    	for (int x = 0; x < tutorials.size(); x++)
    	{
    		String key = email+tutorials.get(x);
    		
    		String SQL = "SELECT key, MAX(score), MIN(time), date "
    					+ "FROM Records "
    					+ "WHERE key = '"+key+"'";

    		Cursor cursor = database.rawQuery(SQL, null);
    		
    		if(cursor.moveToNext())
    		{
    			String Key = cursor.getString(cursor.getColumnIndex("key"));
    			if(Key != null && !Key.isEmpty())
    			{
	    			System.out.println("KEY: "+Key);
	    			String Tutorial = Key.replaceAll(email, "");
	    			int Time = cursor.getInt(cursor.getColumnIndex("MIN(time)"));
	    			int Score = cursor.getInt(cursor.getColumnIndex("MAX(score)"));
	    			String Date = cursor.getString(cursor.getColumnIndex("date"));
	    		    
	    			records.add(new TutorialRecord(email, Tutorial, Time, Score, Date));
    			}
    		}
    		cursor.close();
    	}
    	return records;
    }

    public ArrayList<TutorialCompletedInformation> getTutorials_AND_Info()
    {
    	String SQL = "SELECT * FROM Tutorial";
    	Cursor cursor = database.rawQuery(SQL, null);
    	
    	ArrayList<TutorialCompletedInformation> tutorials = new ArrayList<TutorialCompletedInformation>();
    	
    	Boolean one = false;
    	while(cursor.moveToNext())
    	{
    		String name = cursor.getString(cursor.getColumnIndex("Tut_ID"));
    		String description = cursor.getString(cursor.getColumnIndex("description"));
    		String photo = cursor.getString(cursor.getColumnIndex("photo"));
    		
    		tutorials.add(new TutorialCompletedInformation(name, description, photo));
    	}
    	
    	cursor.close();
    	return tutorials;
    }
    
    public void insertCompletedTutorial(String Email, String Tutorial_name, int Time, int Score, String Date)
    {
    	ContentValues element = new ContentValues();
    	database = getWritableDatabase();
    	
    	element.put("key", Email+Tutorial_name);
    	element.put("time", Time);
    	element.put("score", Score);
    	element.put("date", Date);
    	
    	database.insert("Records", null, element);
    }
    
    public boolean SeeIfUserHasCompletedCertainTutorial(String key)
    { 
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT key "
    				+ "FROM Records "
    				+ "WHERE key = '"+key+"'";
    	Cursor cursor = database.rawQuery(SQL, null);
    	if(cursor.moveToNext())
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }


//Unused methods
    public ArrayList<TutorialRecord> getRecords_FilterByTutorial(String tutorial_name)
    {
    	String SQL = "SELECT * From Records "
    				+ "WHERE key "
    				+ "LIKE '%"+tutorial_name+"%' "
    				+ "ORDER BY key DESC";
    	
    	Cursor cursor = database.rawQuery(SQL, null);
    	
    	ArrayList<TutorialRecord> records = new ArrayList<TutorialRecord>();
    	UserProfileHelper user = new UserProfileHelper(myContext);
    	
    	Boolean one = false;
    	while(cursor.moveToNext())
    	{
    		String Key = cursor.getString(cursor.getColumnIndex("key"));
    		String email = Key.replaceAll(tutorial_name, "");
    		int Time = cursor.getInt(cursor.getColumnIndex("time"));
    		int Score = cursor.getInt(cursor.getColumnIndex("score"));
    		String Date = cursor.getString(cursor.getColumnIndex("date"));

    		records.add(new TutorialRecord(email, tutorial_name, Time, Score, Date));
    	}
		cursor.close();
    	return records;
    }

    public ArrayList<TutorialRecord> getRecords_FilterByTutorialAND_Name(String tutorial_name, String email)
    {
    	String SQL = "SELECT * From Records "
    				+ "WHERE key = '"+email+tutorial_name+"' "
    				+ "ORDER BY score DESC";
    	
    	Cursor cursor = database.rawQuery(SQL, null);
    	
    	ArrayList<TutorialRecord> records = new ArrayList<TutorialRecord>();
    	UserProfileHelper user = new UserProfileHelper(myContext);
    	
    	Boolean one = false;
    	while(cursor.moveToNext())
    	{
    		int Time = cursor.getInt(cursor.getColumnIndex("time"));
    		int Score = cursor.getInt(cursor.getColumnIndex("score"));
    		String Date = cursor.getString(cursor.getColumnIndex("date"));

    		records.add(new TutorialRecord(email, tutorial_name, Time, Score, Date));
    	}
		cursor.close();
    	return records;
    }

    public ArrayList<String> getSCorebaordStuff()
    {
    	String SQL = "SELECT * FROM RECORDS";
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
    		String name = cursor.getString(cursor.getColumnIndex("tutorial"));
    		tutorials.add(name);
    	}
    	return tutorials;
    }
}
