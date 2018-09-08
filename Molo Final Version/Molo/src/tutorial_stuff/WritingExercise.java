package tutorial_stuff;

public class WritingExercise 
{
	int id;
	String phrase_english, hint_english, correct_answer_english,
			phrase_isixhosa, hint_isixhosa, correct_answer_isixhosa,
			photo;
	
	public WritingExercise(int id,
							String phrase_english, String hint_english, 
							String correct_answer_english,
							String phrase_isixhosa, String hint_isixhosa,
							String correct_answer_isixhosa,
							String photo)
	{
		this.id = id;
		
		this.phrase_english = phrase_english;
		this.hint_english = hint_english;
		this.correct_answer_english = correct_answer_english;
		
		this.phrase_isixhosa = phrase_isixhosa;
		this.hint_isixhosa = hint_isixhosa;
		this.correct_answer_isixhosa = correct_answer_isixhosa;
		
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
	public String getHintEnglish()
	{
		return hint_english;
	}
	public String getCorrectAnswerEnglish()
	{
		return correct_answer_english;
	}
	public String getPhraseIsiXhosa()
	{
		return phrase_isixhosa;
	}
	public String getHintIsiXhosa()
	{
		return hint_isixhosa;
	}
	public String getCorrectAnswerIsiXhosa()
	{
		return correct_answer_isixhosa;
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
	public void setHintEnglish(String Hint_English)
	{
		hint_english = Hint_English;
	}
	public void setCorrectAnswerEnglish(String CorrectEnglish)
	{
		correct_answer_english = CorrectEnglish;
	}
	public void setPhraseIsiXhosa(String Phrase_isiXhosa)
	{
		phrase_english = Phrase_isiXhosa;
	}
	public void setHintIsiXhosa(String Hint_isiXhosa)
	{
		hint_english = Hint_isiXhosa;
	}
	public void setCorrectAnswerIsiXhosa(String CorrectIsiXhosa)
	{
		correct_answer_english = CorrectIsiXhosa;
	}
	public void setPhoto(String Photo)
	{
		photo = Photo;
	}	
}
