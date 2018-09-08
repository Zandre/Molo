package tutorial_stuff;

public class TutorialRecord 
{
	String email, tutorial, date;
	int time, score;
	public TutorialRecord(String email, String tutorial, int time, int score, String date) 
	{
		this.email = email;
		this.tutorial = tutorial;
		this.time = time;
		this.score = score;
		this.date = date;
	}
	
	//GET METHODS
	public String getEmail()
	{
		return email;
	}
	public String getTutorial()
	{
		return tutorial;
	}
	public int getTime()
	{
		return time;
	}
	public int getScore()
	{
		return score;
	}
	public String getDate()
	{
		return date;
	}
	
	//SET METHODS
	public void setEmail(String Email)
	{
		email = Email;
	}
	public void setTutorial(String Tutorial)
	{
		tutorial = Tutorial;
	}
	public void setTime(int Time)
	{
		time = Time;
	}
	public void setScore(int Score)
	{
		score = Score;
	}
	public void setDate(String Date)
	{
		date = Date;
	}

}
