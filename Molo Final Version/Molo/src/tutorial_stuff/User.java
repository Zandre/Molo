package tutorial_stuff;

public class User 
{

	String name, email, password, native_language;
	int level;
	
	public User(String name, String email, String password, String native_language, int level)
	{
		this.name = name;
		this.email = email;
		this.password = password;
		this.native_language = native_language;
		this.level = level;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String Name)
	{
		name = Name;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String Email)
	{
		email = Email;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String Password)
	{
		password = Password;
	}
	public String getNativeLanguage()
	{
		return native_language;
	}
	public void setNativeLanguage(String Native_Language)
	{
		native_language = Native_Language;
	}
	public int getLevel()
	{
		return level;
	}
	public void setLevel(int Level)
	{
		level = Level;
	}
}