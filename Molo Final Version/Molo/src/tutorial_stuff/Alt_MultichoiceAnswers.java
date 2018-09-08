package tutorial_stuff;

public class Alt_MultichoiceAnswers 
{
	int id;
	String isixhosa_1, isixhosa_2, isixhosa_3, isixhosa_4, 
			english_1, english_2, english_3, english_4;
	
	public Alt_MultichoiceAnswers(int id,
									String isixhosa_1, String isixhosa_2,
									String isixhosa_3, String isixhosa_4,
									String english_1, String english_2, 
									String english_3, String english_4)
			{
				this.id = id;
				this.isixhosa_1 = isixhosa_1;
				this.isixhosa_2 = isixhosa_2;
				this.isixhosa_3 = isixhosa_3;
				this.isixhosa_4 = isixhosa_4;
				this.english_1 = english_1;
				this.english_2 = english_2;
				this.english_3 = english_3;
				this.english_4 = english_4;
			}


	//GET METHODS
	public int getID()
	{
		return id;
	}
	public String getEnglish_1()
	{
		return english_1;
	}
	public String getEnglish_2()
	{
		return english_2;
	}
	public String getEnglish_3()
	{
		return english_3;
	}
	public String getEnglish_4()
	{
		return english_4;
	}
	public String getIsiXhosa_1()
	{
		return isixhosa_1;
	}
	public String getIsiXhosa_2()
	{
		return isixhosa_2;
	}
	public String getIsiXhosa_3()
	{
		return isixhosa_3;
	}
	public String getIsiXhosa_4()
	{
		return isixhosa_4;
	}
	
	//SET METHODS
	public void setID(int ID)
	{
		id = ID;
	}
	public void setEnglish_1(String English_1)
	{
		english_1 = English_1;
	}
	public void setEnglish_2(String English_2)
	{
		english_2 = English_2;
	}
	public void setEnglish_3(String English_3)
	{
		english_3 = English_3;
	}
	public void setEnglish_4(String English_4)
	{
		english_4 = English_4;
	}
	public void setIsiXhosa_1(String IsiXhosa_1)
	{
		isixhosa_1 = IsiXhosa_1;
	}
	public void setIsiXhosa_2(String IsiXhosa_2)
	{
		isixhosa_2 = IsiXhosa_2;
	}
	public void setIsiXhosa_3(String IsiXhosa_3)
	{
		isixhosa_3 = IsiXhosa_3;
	}
	public void setIsiXhosa_4(String IsiXhosa_4)
	{
		isixhosa_4 = IsiXhosa_4;
	}
}
