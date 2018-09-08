package fillscreen_classes;

public class Voice 
{
	String phrase_tutorial_language, phrase_native_language, audio, photo;
	
	public Voice(String phrase_tutorial_language, String phrase_native_language, 
			String audio, String photo)
	{
		this.phrase_tutorial_language = phrase_tutorial_language;
		this.phrase_native_language = phrase_native_language;
		this.audio = audio;
		this.photo = photo;
	}
	
	
	public String getPhraseTutorialLanguage()
	{
		return phrase_tutorial_language;
	}
	public String getPhraseNativeLangauge()
	{
		return phrase_native_language;
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
