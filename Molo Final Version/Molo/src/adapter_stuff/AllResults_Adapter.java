package adapter_stuff;

import java.util.ArrayList;

import tutorial_stuff.TutorialRecord;

import com.example.molo.R;
import com.example.molo.R.id;
import com.example.molo.R.layout;

import database_stuff.UserProfileHelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class AllResults_Adapter extends ArrayAdapter<TutorialRecord> 
{
  private final Context context;
  ArrayList<TutorialRecord> records;


  public AllResults_Adapter(Context context, ArrayList<TutorialRecord> records) 
  {
    super(context, R.layout.rowlayout_all, records);
    this.context = context;
    this.records = records;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) 
  {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.rowlayout_all, parent, false);
        
    TextView user_name = (TextView)rowView.findViewById(R.id.tv_scoreboard_user);
    TextView tutorial = (TextView)rowView.findViewById(R.id.tv_scoreboard_tut);
    TextView time = (TextView)rowView.findViewById(R.id.tv_scoreboard_time);
    RatingBar score = (RatingBar)rowView.findViewById(R.id.rb_scoreboard_all);
    TextView date = (TextView)rowView.findViewById(R.id.tv_scoreboard_date);

    int Time = records.get(position).getTime();
    int minutes = (Time % 3600)/60;
    int seconds = Time % 60;
    String Email = records.get(position).getEmail();
   
    UserProfileHelper db_user = new UserProfileHelper(context);  
    db_user.open();
    String User_Name = db_user.getUserName(Email);
    db_user.close();
    		
    user_name.setText(User_Name);
    tutorial.setText(records.get(position).getTutorial());
    time.setText("Time\t\t"+minutes+":"+seconds);
    score.setRating(records.get(position).getScore());
    date.setText(records.get(position).getDate());

    return rowView;
  }
}
