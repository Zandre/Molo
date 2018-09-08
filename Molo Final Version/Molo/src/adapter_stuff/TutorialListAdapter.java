package adapter_stuff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import tutorial_stuff.TutorialCompletedInformation;

import com.example.molo.R;
import com.example.molo.R.id;
import com.example.molo.R.layout;

import database_stuff.DatabaseCreater;
import database_stuff.RecordHelper;
import database_stuff.TutorialHelper;
import database_stuff.UserProfileHelper;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialListAdapter extends ArrayAdapter<TutorialCompletedInformation>
{
	  private final Context context;
	  ArrayList<TutorialCompletedInformation> tutorials;
	  String email;
	  
	  public TutorialListAdapter (Context context, ArrayList<TutorialCompletedInformation> tutorials, String email)
	  {
		    super(context, R.layout.rowlayout_tutoriallist, tutorials);
		    this.context = context;
		    this.tutorials = tutorials;
		    this.email = email;
	  }
	  
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) 
	  {
	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.rowlayout_tutoriallist, parent, false);
	        
	    TextView name = (TextView)rowView.findViewById(R.id.tv_tutorialName);
	    TextView description = (TextView)rowView.findViewById(R.id.tv_description);
	    name.setText(tutorials.get(position).getName());
	    description.setText(tutorials.get(position).getDescription());
	    
	    //IMAGEVIEW->	DESCRIPTION
	    ImageView image_description = (ImageView)rowView.findViewById(R.id.iv_tutoriallist);
	    String photo_name = tutorials.get(position).getPhoto();
		try 
		{
			InputStream ims = context.getAssets().open(photo_name);
			Drawable d = Drawable.createFromStream(ims, null);
			image_description.setImageDrawable(d);
			
			ims = null;
			d = null;
		} 
		catch (IOException e) 
		{
	    	File image_file = context.getExternalFilesDir("Download/"+photo_name);
	    	String filepath = image_file.toString();
	    	image_description.setImageDrawable(Drawable.createFromPath(filepath));
	    	
	    	image_file = null;;
		}
	    
	   
		
	    String tutorial_name = name.getText().toString();
	    String key = email+tutorial_name;
	     
	    //IMAGEVIEW->	COMPLETED YES/NO
	    ImageView completed_image = (ImageView)rowView.findViewById(R.id.iv_completed);
	    
	    RecordHelper db_records = new RecordHelper(context);    
	    if(db_records.SeeIfUserHasCompletedCertainTutorial(key) == true)
	    {
		    try 
		    {
				InputStream ims = context.getAssets().open("correct.png");
				Drawable d = Drawable.createFromStream(ims, null);
				completed_image.setImageDrawable(d);
				
				ims = null;
				d = null;
			} 
		    catch (IOException e) 
		    {
		    	return completed_image;
			}
	    }
	    else
	    {
		    try 
		    {
				InputStream ims = context.getAssets().open("incorrect.png");
				Drawable d = Drawable.createFromStream(ims, null);
				completed_image.setImageDrawable(d);
				
				ims = null;
				d = null;
			} 
		    catch (IOException e) 
		    {
		    	return completed_image;
			}
	    }
	    db_records.close();
	    
		
	    
	    //IMAGEVIEW->	LOCKED OR UNLOCKED
	    TutorialHelper tutorial = new TutorialHelper(context);
		int tutorial_level = tutorial.getTutorialLevel(tutorial_name);
		tutorial.close();
		
		UserProfileHelper user = new UserProfileHelper(context);
		int user_level = user.getUserLevel(email);
		user.close();
		
		ImageView access = (ImageView)rowView.findViewById(R.id.iv_access);
		if(user_level >= tutorial_level)
		{
		    try 
		    {
				InputStream ims = context.getAssets().open("unlock.png");
				Drawable d = Drawable.createFromStream(ims, null);
				access.setImageDrawable(d);
				
				ims = null;
				d = null;
			} 
		    catch (IOException e) 
		    {
		    	return completed_image;
			}
		}
		else
		{
		    try 
		    {
				InputStream ims = context.getAssets().open("lock.png");
				Drawable d = Drawable.createFromStream(ims, null);
				access.setImageDrawable(d);
				
				ims = null;
				d = null;
			} 
		    catch (IOException e) 
		    {
		    	return completed_image;
			}
		}
		System.gc();
	    return rowView;
	  }
}
