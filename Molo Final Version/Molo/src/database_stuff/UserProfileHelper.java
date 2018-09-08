package database_stuff;

import java.util.ArrayList;

import tutorial_stuff.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserProfileHelper extends SQLiteOpenHelper
{
	
    private static String DB_PATH = "/data/data/com.example.molo/databases/";
    //private static String DB_NAME = "dummyMolo.db";
    private static String DB_NAME = "molo.db";
    private SQLiteDatabase database; 
    private final Context myContext;
    
	public UserProfileHelper(Context context) 
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
	
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    public void insertUser(String user_Name, String user_ID, String Password, String native_language)
    {
    	ContentValues element = new ContentValues();
    	database = getWritableDatabase();
    	
    	element.put("user_name", user_Name);
    	element.put("email", user_ID);
    	element.put("password", Password);
    	element.put("native_language", native_language);
    	element.put("level", 1);
    	element.put("lesson_experience", 0);
    	element.put("voice_experience", 0);
    	element.put("multi_experience", 0);
    	element.put("match_experience", 0);
    	element.put("writing_experience", 0);
    	
    	database.insert("user_profile", null, element);
    }
	
	public ArrayList<User> getUsers()
    {
		database = getReadableDatabase();
		
    	String SQL = "SELECT * FROM user_profile";
    	
    	Cursor cursor = database.rawQuery(SQL, null);

    	ArrayList<User> user_list = new ArrayList<User>();
    	
    	while(cursor.moveToNext())
    	{
    		String Name = cursor.getString(cursor.getColumnIndex("user_name"));
    		String Email = cursor.getString(cursor.getColumnIndex("email"));
    		String Password = cursor.getString(cursor.getColumnIndex("password"));
    		String Native_Language = cursor.getString(cursor.getColumnIndex("native_language"));
    		int Level = cursor.getInt(cursor.getColumnIndex("level"));
    		
    		user_list.add(new User(Name, Email, Password, Native_Language, Level));
    	}
    	cursor.close();
    	System.out.println("User List size- "+user_list.size());
		return user_list;
    }
	
	public User getUser(String email)
	{
		database = getReadableDatabase();
		
		String SQL = "SELECT * FROM user_profile "
				+ "WHERE email = '"+email+"'";
    	Cursor cursor = database.rawQuery(SQL, null);
    	cursor.moveToFirst();
    	
		String Name = cursor.getString(cursor.getColumnIndex("user_name"));
		String Password = cursor.getString(cursor.getColumnIndex("password"));
		String Native_Language = cursor.getString(cursor.getColumnIndex("native_language"));
		int Level = cursor.getInt(cursor.getColumnIndex("level"));
		
		User user = new User(Name, email, Password, Native_Language, Level);
		return user;
	}
	
	public boolean UserHasCompletedExercise(String column, String email)
	{
		boolean user_has_already_completed_this_type_of_exercise = true;
		
		database = getReadableDatabase();
		
		String SQL = "SELECT "+column+" "
					+"FROM user_profile "
					+"WHERE email = '"+email+"'";
		
    	Cursor cursor = database.rawQuery(SQL, null);
    	cursor.moveToFirst();
    	int experience = cursor.getInt(cursor.getColumnIndex(column));
    	cursor.close();
    	
    	if(experience == 0)
    	{
    		user_has_already_completed_this_type_of_exercise = false;
    	}
		
		return user_has_already_completed_this_type_of_exercise;
	}
	
	public void UpdateUserExperience(String column, String email)
	{
    	database = getWritableDatabase();
    	
    	database.execSQL("UPDATE user_profile "
    					+ "SET "+column+" = '1' "
    					+ "WHERE email = '"+email+"'");
	}
	
    public String getUserName(String email)
    {
    	database = getReadableDatabase();
    	String SQL = "SELECT * "
    				+ "FROM user_profile "
    				+ "WHERE email = '"+email+"'";
    	
    	Cursor cursor = database.rawQuery(SQL, null);
    	cursor.moveToFirst();
    	String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
    	cursor.close();
   
    	return user_name;
    }

    public int getUserLevel(String email)
    {
    	database = getReadableDatabase();
    	
    	String SQL = "SELECT * "
    				+ "FROM user_profile "
    				+ "WHERE email = '"+email+"'";
    	
    	Cursor cursor = database.rawQuery(SQL, null);
    	cursor.moveToFirst();
    	int level = cursor.getInt(cursor.getColumnIndex("level"));
    	return level;
    }

    public void UpdateUserLevel(String email, int user_level)
    {
    	database = getWritableDatabase();
    	
    	database.execSQL("UPDATE user_profile "
    					+ "SET level = '"+user_level+"' "
    					+ "WHERE email = '"+email+"'");
    }

    public void UpdateUserProfile(String email, String user_name, String password, String native_language)
    {
    	database = getWritableDatabase();
    	
    	database.execSQL("UPDATE user_profile "
    					+"SET user_name = '"+user_name+"' "
    					+"WHERE email = '"+email+"'");
    	
    	database.execSQL("UPDATE user_profile "
						+"SET password = '"+password+"' "
						+"WHERE email = '"+email+"'");
    	
    	database.execSQL("UPDATE user_profile "
						+"SET native_language = '"+native_language+"' "
						+"WHERE email = '"+email+"'");
    }
}