package test.alec;

import static org.junit.Assert.*;
import alec.CharacterHelper;

/**
 * Tests some inputs to the CharacterHelper class
 * @author Alec Sobeck
 */
public class TestCharacterHelper
{
	/**
	 * Tests some different possible inputs to the CharacterHelper class
	 */
	@org.junit.Test
	public void test() 
	{
		//Test some various possibilities:
		
		//Uppercase, lowercase, and digit mix
		assertTrue(CharacterHelper.isValid("Hello World!x34"));
		
		//Test with some special characters that are valid
		assertTrue(CharacterHelper.isValid("';%@#$@#$%^&*()';31';12{]["));
		
		//Test with an invalid characters. 
		// \t the tab character is invalid.
		assertFalse(CharacterHelper.isValid("\t"));
		// \n the tab character is invalid.
		assertFalse(CharacterHelper.isValid("\n"));
		
		//Test with the empty string - which should be valid
		assertTrue(CharacterHelper.isValid(""));

		//Test a string in another language, which should also be valid.
		//This sentence is "This is a sentence translated to another language." in CZech
		assertTrue(CharacterHelper.isValid("To je věta překládána do jiného jazyka."));
	
	}

}
