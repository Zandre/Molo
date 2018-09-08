package scoreboard_stuff;

import java.util.ArrayList;

import tutorial_stuff.TutorialRecord;
import adapter_stuff.AllResults_Adapter;
import android.app.ListFragment;
import android.os.Bundle;
import database_stuff.RecordHelper;

public class TopScores extends ListFragment
{
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		RecordHelper db_records = new RecordHelper(getActivity());
		String email = getArguments().getString("email");
		ArrayList<TutorialRecord> records = db_records.getRecords_TopScores(email);

	    
		AllResults_Adapter adapter = new AllResults_Adapter(getActivity(), records);
		setListAdapter(adapter);
	}
}
