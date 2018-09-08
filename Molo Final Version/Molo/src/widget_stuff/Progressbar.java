package widget_stuff;

import android.widget.ProgressBar;

public class Progressbar 
{
	public void initializeProgressbar(int count, int layout_position, ProgressBar progressbar) 
	{
	    progressbar.setVisibility(ProgressBar.VISIBLE);
	    progressbar.setMax(count);
	    progressbar.setProgress(layout_position);
	}
}
