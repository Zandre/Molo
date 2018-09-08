package fillscreen_classes;

public class Match 
{
	String n1, n2, n3, n4, t1, t2, t3, t4;
	
	public Match(String t1, String t2, String t3, String t4, String n1, String n2, String n3, String n4) 
	{
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		this.n4 = n4;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.t4 = t4;
	}
	
	public String[] getNativeLanguageOptions()
	{
		String[] native_language_options = new String[4];
		native_language_options[0] = n1;
		native_language_options[1] = n2;
		native_language_options[2] = n3;
		native_language_options[3] = n4;
		return native_language_options;
	}
	
	public String[] getTutorialLanguageOptions()
	{
		String[] tutorial_language_options = new String[4];
		tutorial_language_options[0] = t1;
		tutorial_language_options[1] = t2;
		tutorial_language_options[2] = t3;
		tutorial_language_options[3] = t4;
		return tutorial_language_options;
	}
}
