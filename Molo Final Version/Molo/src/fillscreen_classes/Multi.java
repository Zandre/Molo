package fillscreen_classes;

import java.lang.reflect.Array;

public class Multi 
{
	String phrase_native_language, correct_answer, photo, 
			alt_1, alt_2, alt_3, alt_4;
	
	public Multi(String phrase_native_language, String correct_answer, String photo, 
			String alt_1, String alt_2, String alt_3, String alt_4) 
	{
		this.phrase_native_language = phrase_native_language;
		this.correct_answer = correct_answer;
		this.photo = photo;
		this.alt_1 = alt_1;
		this.alt_2 = alt_2;
		this.alt_3 = alt_3;
		this.alt_4 = alt_4;
	}
	
	public String getPhrase()
	{
		return phrase_native_language;
	}
	public String getAnswer()
	{
		return correct_answer;
	}
	public String getPhoto()
	{
		return photo;
	}
	public String[] getAlternatives()
	{
		String[] alternatives = new String[5];
		alternatives[0] = alt_1;
		alternatives[1] = alt_2;
		alternatives[2] = alt_3;
		alternatives[3] = alt_4;
		alternatives[4] = correct_answer;
		return alternatives;
	}
}
