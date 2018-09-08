package scoreboard_stuff;

import java.util.ArrayList;

import tutorial_stuff.TutorialRecord;
import database_stuff.RecordHelper;
import adapter_stuff.MyResults_Adapter;
import android.app.ListFragment;
import android.os.Bundle;

public class My_TopScores extends ListFragment
{
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		RecordHelper db_records = new RecordHelper(getActivity());
		
		String email = getArguments().getString("email");
		ArrayList<TutorialRecord> records = db_records.getRecords_MyTopScores(email);
	    
		MyResults_Adapter adapter = new MyResults_Adapter(getActivity(), records, email);
		setListAdapter(adapter);
	}
}
