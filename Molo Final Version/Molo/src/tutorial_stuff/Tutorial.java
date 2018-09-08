package tutorial_stuff;

public class Tutorial 
{
	String name, description, photo;
	int level;
	
	public Tutorial(String name, String description, int level, String photo)
	{
		this.name = name;
		this.description = description;
		this.level = level;
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
	public int getLevel()
	{
		return level;
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
	public void setLevel(int Level)
	{
		level = Level;
	}
}
