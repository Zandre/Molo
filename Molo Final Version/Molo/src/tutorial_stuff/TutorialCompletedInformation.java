package tutorial_stuff;

public class TutorialCompletedInformation 
{

	String name, description, photo;
	int time, score;
	public TutorialCompletedInformation(String name, String description, String photo) 
	{
		this.name = name;
		this.description = description;
		this.photo = photo;
	}
	
	//GET METHODS
	public String getName()
	{
		return name;
	}
	public String getDescription()
	{
		return description;
	}
	public String getPhoto()
	{
		return photo;
	}
	//SET METHODS
	public void setName(String Name)
	{
		name = Name;
	}
	public void setDescriptio(String Description)
	{
		description = Description;
	}
	public void setPhoto(String Photo)
	{
		photo = Photo;
	}
}
