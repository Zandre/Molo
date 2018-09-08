package tutorial_stuff;

public class MultichoiceExercise 
{
	int id, alternative_answer_id;
	String phrase_english, correct_answer_english, 
			phrase_isixhosa, correct_answer_isixhosa, 
			photo;
	public MultichoiceExercise(int id,
								String phrase_english, String correct_answer_english,
								String phrase_isixhosa, String correct_answer_isixhosa,
								String photo, int alternative_answer_id)
			{
				this.id = id;

				this.phrase_english = phrase_english;
				this.correct_answer_english = correct_answer_english;
	
				this.phrase_isixhosa = phrase_isixhosa;
				this.correct_answer_isixhosa = correct_answer_isixhosa;

				this.photo = photo;
				
				this.alternative_answer_id = alternative_answer_id;
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
	public String getCorrectAnswerEnglish()
	{
		return correct_answer_english;
	}
	public String getPhraseIsiXhosa()
	{
		return phrase_isixhosa;
	}
	public String getCorrectAnswerIsiXhosa()
	{
		return correct_answer_isixhosa;
	}
	public String getPhoto()
	{
		return photo;
	}
	public int getAlternativAnswerID()
	{
		return alternative_answer_id;
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
	public void setCorrectAnswerEnglish(String CorrectEnglish)
	{
		correct_answer_english = CorrectEnglish;
	}
	public void setPhraseIsiXhosa(String Phrase_isiXhosa)
	{
		phrase_english = Phrase_isiXhosa;
	}
	public void setCorrectAnswerIsiXhosa(String CorrectIsiXhosa)
	{
		correct_answer_english = CorrectIsiXhosa;
	}
	public void setPhoto(String Photo)
	{
		photo = Photo;
	}
	public void setAlternativeAnswerID(int Alt_Answer_ID)
	{
		alternative_answer_id = Alt_Answer_ID;
	}
}
