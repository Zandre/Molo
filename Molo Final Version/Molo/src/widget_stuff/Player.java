package widget_stuff;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class Player 
{
	private MediaPlayer mediaplayer = new MediaPlayer();
	
	public void PrepapreSound(Context mycontext, String audio)
	{
		try
		{
			AssetFileDescriptor afd = mycontext.getAssets().openFd(audio);
			mediaplayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			afd.close();
			mediaplayer.prepare();
		}
		catch(Exception e)
		{
			//e.printStackTrace();
	    	File recording_file = mycontext.getExternalFilesDir("Download/"+audio);
	    	String filepath = recording_file.toString();
	    	try
	    	{
				mediaplayer.setDataSource(filepath);
				mediaplayer.prepare();
			} 
	    	catch (IllegalArgumentException e1) 
	    	{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
	    	catch (SecurityException e1) 
	    	{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
	    	catch (IllegalStateException e1) 
	    	{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
	    	catch (IOException e1) 
	    	{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
		}
	}
	
	public void PlaySound()
	{
		mediaplayer.start();
	}
	
	public void PauseSound()
	{
		mediaplayer.pause();
	}
	
	public void StopSound(Context mycontext, String audio)
	{
		mediaplayer.reset();
		PrepapreSound(mycontext, audio);
	}
	
	public void ReleaseSound()
	{
		mediaplayer.release();
	}
	
	public void PlayCorrectAnswer(Context mycontext)
	{

		mediaplayer = new MediaPlayer();
		try
		{
			AssetFileDescriptor afd = mycontext.getAssets().openFd("correct.wav");
			mediaplayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			afd.close();
			mediaplayer.prepare();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mediaplayer.start();
	}
	
	public void PlayINcorrectAnswer(Context mycontext)
	{

		mediaplayer = new MediaPlayer();
		try
		{
			AssetFileDescriptor afd = mycontext.getAssets().openFd("incorrect.wav");
			mediaplayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			afd.close();
			mediaplayer.prepare();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mediaplayer.start();
	}
	
	public void PrepareRecorderSound()
	{
		try
		{
			mediaplayer.reset();
			mediaplayer.setDataSource("/sdcard/recording1.mp3");
			mediaplayer.prepare();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isPlaying()
	{
		return mediaplayer.isPlaying();
	}
		


}
