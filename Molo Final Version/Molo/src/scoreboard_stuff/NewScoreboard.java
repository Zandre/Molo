package scoreboard_stuff;

import com.example.molo.ChangeExercise;
import com.example.molo.R;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class NewScoreboard extends FragmentActivity 
{
	private static Context mycontext;
	String email;
	String native_language;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_scoreboard);

		native_language = getIntent().getStringExtra("native_language");
		email = getIntent().getStringExtra("email");
		
		final Bundle bundle = new Bundle();
		bundle.putString("email", email);
		
		ActionBar actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		
		Tab all_results = actionbar.newTab();
		all_results.setText("All Results");
		all_results.setIcon(R.drawable.ic_action_view_as_list);
		all_results.setTabListener(new TabListener()
		{
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft)
			{	
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) 
			{
				ListFragment all_results = new All_Results();
				getFragmentManager().beginTransaction().replace(R.id.container, all_results).commit();
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) 
			{	
			}
		});
		
		Tab my_results = actionbar.newTab();
		my_results.setText("My Results");
		my_results.setIcon(R.drawable.dark_ic_action_person);
		my_results.setTabListener(new TabListener()
		{
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) 
			{
				ListFragment my_results = new My_Results();
				my_results.setArguments(bundle);
				getFragmentManager().beginTransaction().replace(R.id.container, my_results).commit();
				
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		Tab top_scores = actionbar.newTab();
		top_scores.setText("Top Scores");
		top_scores.setIcon(R.drawable.trophy2);
		top_scores.setTabListener(new TabListener() 
		{
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) 
			{
				ListFragment top_scores = new TopScores();
				top_scores.setArguments(bundle);
				getFragmentManager().beginTransaction().replace(R.id.container, top_scores).commit();
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) 
			{
				// TODO Auto-generated method stub
				
			}
		});		
		
		Tab my_topscores = actionbar.newTab();
		my_topscores.setText("My Top Scores");
		my_topscores.setIcon(R.drawable.ic_action_star);
		my_topscores.setTabListener(new TabListener() 
		{
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) 
			{
				ListFragment my_top_scores = new My_TopScores();
				my_top_scores.setArguments(bundle);
				getFragmentManager().beginTransaction().replace(R.id.container, my_top_scores).commit();
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) 
			{
				// TODO Auto-generated method stub
				
			}
		});

		actionbar.addTab(all_results);
		actionbar.addTab(my_results);
		actionbar.addTab(top_scores);
		actionbar.addTab(my_topscores);
		
	}
	
	public void onBackPressed()
	{
			mycontext = getApplicationContext();
			
			ChangeExercise go = new ChangeExercise(mycontext);
			go.GoToDashboard(native_language, email);
	}
}
