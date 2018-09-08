package tutorial_stuff;

public class TutorialStep 
{
	int id, exercise_id;
	String tutorial, layout;
	
	public TutorialStep(int id, String tutorial, String layout, int exercise_id)
	{
		this.id = id;
		this.tutorial = tutorial;
		this.layout = layout;
		this.exercise_id = exercise_id;
	}
	
	//GET METHODS
	public int getID()
	{
		return id;
	}
	public String getTutorial()
	{
		return tutorial;
	}
	public String getLayout()
	{
		return layout;
	}
	public int getExerciseID()
	{
		return exercise_id;
	}


	//SET METHODS
	public void setID(int ID)
	{
		id = ID;
	}
	public void setTutorial(String Tutorial)
	{
		tutorial = Tutorial;
	}
	public void setLayout(String Layout)
	{
		layout = Layout;
	}
	public void setExerciseID(int ExerciseID)
	{
		exercise_id = ExerciseID;
	}
}
