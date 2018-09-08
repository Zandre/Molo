package tutorial_stuff;

public class Lesson 
{
	int id;
	String 	phrase_english, definition_english, audio_english,
			phrase_isixhosa, definition_isixhosa, audio_isixhosa,
			photo;
	
	public Lesson(int id,
					String phrase_english, String definition_english, String audio_english,
					String phrase_isixhosa, String definition_isixhosa, String audio_isixhosa,
					String photo) 
	{
		this.id = id;
		
		this.phrase_english = phrase_english;
		this.definition_english = definition_english;
		this.audio_english = audio_english;
		
		this.phrase_isixhosa = phrase_isixhosa;
		this.definition_isixhosa = definition_isixhosa;
		this.audio_isixhosa = audio_isixhosa;
		
		this.photo = photo;
	}
	
	//GET METHODS
	public int getID()
	{
		return id;
	}
	public String getPhraseEnglish()
	{
		return phrase_english;
	}
	public String getDefinitionEnglish()
	{
		return definition_english;
	}
	public String getAudioEnglish()
	{
		return audio_english;
	}
	public String getPhraseIsiXhosa()
	{
		return phrase_isixhosa;
	}
	public String getDefinitionIsiXhosa()
	{
		return definition_isixhosa;
	}
 	public String getAudioIsiXhosa()
	{
		return audio_isixhosa;
	}
	public String getPhoto()
	{
		return photo;
	}

	//SET METHODS
	public void setID(int ID)
	{
		id = ID;
	}
	public void setPhraseEnlish(String Phrase_English)
	{
		phrase_english = Phrase_English;
	}
	public void setDefinitionEnglish(String Definition_English)
	{
		definition_english = Definition_English;
	}
	public void setAudioEnglish(String AudioEnglish)
	{
		audio_english = AudioEnglish;
	}
	public void setPhraseIsiXhosa(String Phrase_isiXhosa)
	{
		phrase_english = Phrase_isiXhosa;
	}
	public void setDefinitionIsiXhosa(String Definition_IsiXhosa)
	{
		definition_isixhosa = Definition_IsiXhosa;
	}
	public void setAudioIsiXhosa(String AudioIsiXhosa)
	{
		audio_isixhosa = AudioIsiXhosa;
	}
	public void setPhoto(String Photo)
	{
		photo = Photo;
	}

}
