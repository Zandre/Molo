package fillscreen_classes;

public class Lesson 
{
	String phrase_tutorial_language, definition_native_language, audio, photo;
	
	public Lesson(String phrase_tutorial_language, String definition_native_language, 
			String audio, String photo) 
	{
		this.phrase_tutorial_language = phrase_tutorial_language;
		this.definition_native_language = definition_native_language;
		this.audio = audio;
		this.photo = photo;
	}
	
	public String getPhrase()
	{
		return phrase_tutorial_language;
	}
	public String getDefinition()
	{
		return definition_native_language;
	}
	public String getAudio()
	{
		return audio;
	}
	public String getPhoto()
	{
		return photo;
	}

}
