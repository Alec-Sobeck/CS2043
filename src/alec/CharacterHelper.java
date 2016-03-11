package alec;

/**
 * [Reused from one of Alec's projects] <br>
 * CharacterHelper defines methods that might be of use when dealing with characters (datatype 'char').
 * @author Alec Sobeck 
 * @author Matthew Robertson
 */
public class CharacterHelper 
{
	private final static String SPECIAL_CHARACTERS = "\'~=-+:;/*!@#$%^&*()\"{}_[]|\\?/<>,. ";
	
	/**
	 * Prevents instantiation
	 */
	private CharacterHelper()
	{
		
	}
	
	/**
	 * Checks if a string of text is fully valid. That is, every single character in the string is valid.
	 * @param str a String to check for validity
	 * @return a boolean, true if the string contains only valid characters; or false if it contains even one invalid character
	 */
	public static boolean isValid(String str)
	{
		for(int i = 0; i < str.length(); i++)
		{
			if(!isLegitimateCharacter(str.charAt(i)))
				return false;
		}
		return true;
	}
	
	/**
	 * Attempts to determine if a character is a legal alphabetic, digit, or special character. 
	 * A special character is defined as any of the following:<i> /*!@#$%^&*()\"{}_[]|\\?/<>,. </i> (includes ' ')
	 * @return a boolean which should indicate if the provided char is an alphabetic, digit, or special character 
	 */
	private static boolean isLegitimateCharacter(char c)
	{
		return Character.isLetterOrDigit(c) || isSpecialCharacter(c);
	}
	
	/**
	 * Determines if the provided character is a special character, that is to say it belongs to the following set:
	 * <i>/*!@#$%^&*()\"{}_[]|\\?/<>,. </i> (includes ' ')
	 * @param c a char to check against the set of special characters
	 * @return a boolean, true if the provided char falls into the set of special characters, otherwise false
	 */
	private static boolean isSpecialCharacter(char c) 
	{
		for (int i = 0; i < SPECIAL_CHARACTERS.length(); i++) 
		{
			if (c == SPECIAL_CHARACTERS.charAt(i)) 
			{
				return true;
			}
		}
		return false;
	}
}
