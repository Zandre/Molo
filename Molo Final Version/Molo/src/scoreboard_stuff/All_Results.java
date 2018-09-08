package scoreboard_stuff;

import java.util.ArrayList;

import tutorial_stuff.TutorialRecord;
import database_stuff.RecordHelper;
import adapter_stuff.AllResults_Adapter;
import android.app.ListFragment;
import android.os.Bundle;

public class All_Results extends ListFragment
{
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		RecordHelper db_records = new RecordHelper(getActivity());
		ArrayList<TutorialRecord> records = db_records.getRecords_All();
	    
		AllResults_Adapter adapter = new AllResults_Adapter(getActivity(), records);
		setListAdapter(adapter);
	}
}
