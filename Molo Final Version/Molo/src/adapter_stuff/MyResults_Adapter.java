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

public class MyResults_Adapter extends ArrayAdapter<TutorialRecord> 
{
  private final Context context;
  ArrayList<TutorialRecord> records;
  String email;


  public MyResults_Adapter(Context context, ArrayList<TutorialRecord> records, String email) 
  {
    super(context, R.layout.rowlayout_tutorials, records);
    this.context = context;
    this.records = records;
    this.email = email;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) 
  {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.rowlayout_tutorials, parent, false);
        
    TextView tutorial_name = (TextView)rowView.findViewById(R.id.tv_scoreboard_tuts_user);
    TextView time = (TextView)rowView.findViewById(R.id.tv_scoreboard_tuts_time);
    RatingBar score = (RatingBar)rowView.findViewById(R.id.rb_scoreboard_tuts);
    TextView date = (TextView)rowView.findViewById(R.id.tv_scoreboard_tuts_date);

    int Time = records.get(position).getTime();
    int minutes = (Time % 3600)/60;
    int seconds = Time % 60;
    
    UserProfileHelper db_user = new UserProfileHelper(context);
    String User_Name = db_user.getUserName(records.get(position).getEmail());
  
    tutorial_name.setText(records.get(position).getTutorial());
    time.setText("Time\t\t"+minutes+":"+seconds);
    score.setRating(records.get(position).getScore());
    date.setText(records.get(position).getDate());

    return rowView;
  }
}