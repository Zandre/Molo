package widget_stuff;

import android.os.SystemClock;
import android.widget.Chronometer;

public class Timer 
{
	public void StartTime(long secs, Chronometer myChronometer)
	{
		myChronometer.setBase(SystemClock.elapsedRealtime() - secs);
		myChronometer.start();
	}
	
	public long PauseTime(Chronometer myChronometer)
	{
		myChronometer.stop();
		long time_elapsed = SystemClock.elapsedRealtime() - myChronometer.getBase();
		return time_elapsed;
	}
}
