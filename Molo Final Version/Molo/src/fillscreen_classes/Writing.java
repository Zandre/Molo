package fillscreen_classes;

public class Writing 
{
	String phrase_native_language, hint_tutorial_langauge, correct_answer, photo;
	
	public Writing(String phrase_native_language, String hint_tutorial_langauge, 
					String correct_answer, String photo) 
	{
		this.phrase_native_language = phrase_native_language;
		this.hint_tutorial_langauge = hint_tutorial_langauge;
		this.correct_answer = correct_answer;
		this.photo = photo;
	}
	
	
	
	public String getPhrase()
	{
		return phrase_native_language;
	}
	public String getHint()
	{
		return hint_tutorial_langauge;
	}
	public String getAnswer()
	{
		return correct_answer;
	}
	public String getPhoto()
	{
		return photo;
	}

}
