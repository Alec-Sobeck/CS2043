package test.alec;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import alec.Billboard;
import alec.BillboardList;
import alec.EnumSearchType;
import alec.IllegalFileFormatException;

/**
 * Tests the BillboardList by subjecting it to several different tests, which mimic typical usage of the BillboardList.
 * @author Alec Sobeck
 */
public class TestBillboardList 
{
	private BillboardList list;
	
	@Test
	public void test() 
	{
		testAdd();	
		testGetBoundaries();
		testSearch();
		testToStringArray();
		testResaveLocation();
		//Requires testResaveLocation to have run, to be in the correct state
		testIO();
		
		//--order matters, make sure this call is near the end before removes
		testSet();
		
		//--order matters, make sure to call this near the end before clear
		testRemoveAndClear();

	}
	
	/**
	 * Tests the resave, load, and save methods
	 */
	private void testIO()
	{
		//
		//Test resave()
		//
		//Test with valid location
		try 
		{
			list.resave();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			fail("File I/O resave failed - should be successful");
		} 
		catch (IllegalFileFormatException e) 
		{
			e.printStackTrace();
			fail("File I/O resave failed - should be successful");
		}
		//Test with null location
		try 
		{
			list.setResaveLocation(null);
			list.resave();
			fail("File I/O resave should've failed - null file location");
		} 
		catch (FileNotFoundException e) 
		{
			fail("File I/O resave failed for the wrong reason. Should've been null pointer.");
		} 
		catch (NullPointerException e) 
		{
		} 
		catch (IllegalFileFormatException e) 
		{
			fail("File I/O resave failed for the wrong reason. Should've been null pointer.");
		}
		//Test with invalid location
		try
		{
			list.setResaveLocation(new File("/mydir/test/whee/This file obviously doesn't exist.xml"));
			list.resave();
			fail("File I/O resave should've failed - with a file not found exception");
		} 
		catch (FileNotFoundException e) 
		{
		} 
		catch (IllegalFileFormatException e) 
		{
			fail("File I/O resave should've failed - with a file not found exception");
		}
		
		//
		//Test save()
		//
		//Test with valid location
		try 
		{
			list.save(new File("SaveTest.xml"));
		}
		catch (FileNotFoundException e) 
		{
			fail("File I/O save failed - should be successful");
		} 
		catch (IllegalFileFormatException e) 
		{
			fail("File I/O save failed - should be successful");	
		}
		//Test with null location
		try 
		{
			list.save(null);
			fail("File I/O save should've failed - null file location");
		} 
		catch (FileNotFoundException e) 
		{
			fail("File I/O resave failed for the wrong reason. Should've been null pointer.");
		} 
		catch (NullPointerException e) 
		{
		} 
		catch (IllegalFileFormatException e)
		{
			fail("File I/O resave failed for the wrong reason. Should've been null pointer.");
		}
		//Test with invalid location
		try 
		{
			list.save(new File("/mydir/test/whee/This file obviously doesn't exist"));
			fail("File I/O resave should've failed - with a file not found exception");
		} 
		catch (FileNotFoundException e) 
		{
		} 
		catch (IllegalFileFormatException e) 
		{
			fail("File I/O resave should've failed - with a file not found exception");
		}
		
		//
		//Test load()
		//
		BillboardList loadTestList = new BillboardList();
		//Test with valid location
		try 
		{
			loadTestList.load(new File("SaveTest.xml"));
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			fail("File I/O load failed - should be successful");
		} 
		catch (IllegalFileFormatException e) 
		{
			e.printStackTrace();
			fail("File I/O load failed - should be successful");
		}
		//Verify the loadTestList of billboards read in is legitimate
		assertTrue(loadTestList.getBillboards() != null);
		assertTrue(loadTestList.size() == 7);
		assertTrue(loadTestList.get(0).getTitle().equals("Advertisement1"));
		assertTrue(loadTestList.get(1).getTitle().equals("Advertisement2"));
		assertTrue(loadTestList.get(2).getTitle().equals("Advertisement3"));
		assertTrue(loadTestList.get(3).getTitle().equals("Advertisement4"));
		assertTrue(loadTestList.get(4).getTitle().equals("Advertisement1"));
		assertTrue(loadTestList.get(5).getTitle().equals("Advertisement6"));
		assertTrue(loadTestList.get(6).getTitle().equals("Advertisement5"));
		//Test with null location
		try
		{
			list.load(null);
			fail("File I/O load should've failed - null file location");
		}
		catch (FileNotFoundException e) 
		{
			fail("File I/O load failed for the wrong reason. Should've been null pointer.");
		} 
		catch (NullPointerException e) 
		{
		} 
		catch (IllegalFileFormatException e) 
		{
			fail("File I/O load failed for the wrong reason. Should've been null pointer.");
		}
		//Test with invalid location
		try 
		{
			list.load(new File("/mydir/test/whee/This file obviously doesn't exist"));
			fail("File I/O load should've failed - with a file not found exception");
		} 
		catch (FileNotFoundException e) 
		{
		} 
		catch (IllegalFileFormatException e) 
		{
			fail("File I/O load should've failed - with a file not found exception");
		}
		//Test with invalid file name
		try
		{
			list.load(new File("my file"));
			fail("File I/O load should've failed - with an IllegalFileFormatException");
		} 
		catch (FileNotFoundException e)
		{
			fail("File I/O load should've failed - with an IllegalFileFormatException");
		} 
		catch (IllegalFileFormatException e) 
		{
		}
		//Test with invalid file format
		try 
		{
			list.load(new File("my_invalid_file.xml"));
			fail("File I/O load should've failed - with a file not found exception");
		} 
		catch (FileNotFoundException e) 
		{
			fail("File I/O load should've failed - with a file not found exception");
		} 
		catch (IllegalFileFormatException e) 
		{
		}

		
	}
	
	/**
	 * Makes sure the resaveLocation methods function as expected
	 */
	private void testResaveLocation()
	{
		//There should be no resave location
		assertFalse(list.hasResaveLocation());

		//Set a resave location -- note the file need not exist for our purposes in this method
		File file = new File("test.xml");
		list.setResaveLocation(file);
		assertTrue(list.hasResaveLocation());
		//This following line should hold as they should both reference the exact same file
		assertTrue(list.getResaveLocation().getAbsolutePath().equals(file.getAbsolutePath()));
	}
	
	/**
	 * Tests the remove and the clear() methods of the billboardlist
	 */
	private void testRemoveAndClear()
	{
		try
		{
			list.remove(-1);
			fail("This should throw a IndexOutOfBoundsException");
		} 
		catch(IndexOutOfBoundsException e)
		{
		}
		try
		{
			list.remove(7);
			fail("This should throw a IndexOutOfBoundsException");
		} 
		catch(IndexOutOfBoundsException e)
		{
		}
		
		//Remove the first billboard
		list.remove(0);
		//Check that the list shifted properly
		assertTrue(list.get(0).getTitle().equals("Advertisement2"));
		assertTrue(list.size() == 6);
		
		//Remove what has now become the last billboard
		list.remove(5);
		//Check that the list shifted properly
		assertTrue(list.get(4).getTitle().equals("Advertisement6"));
		assertTrue(list.size() == 5);
		
		//Remove something left in the middle
		list.remove(2);
		assertTrue(list.size() == 4);
		//Ensure what's left is what we expect
		//Which means original elements 0, 3, and 6 are gone
		assertTrue(list.get(0).getTitle().equals("Advertisement2"));
		assertTrue(list.get(1).getTitle().equals("Advertisement3"));
		assertTrue(list.get(2).getTitle().equals("Advertisement1"));
		assertTrue(list.get(3).getTitle().equals("Advertisement6"));
	
		//Briefly test the clear() functionality 
		//There should be something in the list
		assertTrue(list.size() != 0);
		//Clear the list
		list.clear();
		assertTrue(list.size() == 0);
		
		//Now try to remove a row that isn't there
		//This mimics the "no rows available to remove" alternative case
		try 
		{
			list.remove(0);
			fail("This should throw a IndexOutOfBoundsException");
		} 
		catch(IndexOutOfBoundsException e)
		{
		}
	}
	
	/**
	 * Tests the set method of the BillboardList
	 */
	private void testSet()
	{
		//Test that a few values are what we expect initially
		assertTrue(list.get(0).getTitle().equals("Advertisement1"));
		assertTrue(list.get(1).getMessage().equals("Shop here!"));
		assertTrue(list.get(6).getLocation().equals("Moncton"));
		
		//Put in a new value in each column
		list.set(0, BillboardList.COLUMN_TITLE, "New_Title");
		list.set(1, BillboardList.COLUMN_MESSAGE,"New_message!gkldg\\]341");
		list.set(6, BillboardList.COLUMN_LOCATION, "Greater Moncton");
		
		//Check that the values changes as expected
		assertTrue(list.get(0).getTitle().equals("New_Title"));
		assertTrue(list.get(1).getMessage().equals("New_message!gkldg\\]341"));
		assertTrue(list.get(6).getLocation().equals("Greater Moncton"));

		//bounds checking

		//End of list @size()
		try 
		{
			list.set(7, 0, "");
			fail("This shouldn't be reached - an exception should be thrown");
		} 
		catch (IndexOutOfBoundsException e)
		{
		}
		
		//Start of the list @-1
		try 
		{
			list.set(-1, 0, "");
			fail("This shouldn't be reached - an exception should be thrown");
		} 
		catch (IndexOutOfBoundsException e)
		{
		}
		
		//Illegal column @-1
		try 
		{
			list.set(0, -1, "");
			fail("This shouldn't be reached - an exception should be thrown");
		} 
		catch (IndexOutOfBoundsException e) 
		{
		}
		
		//Illegal column @3
		try 
		{
			list.set(0, 3, "");
			fail("This shouldn't be reached - an exception should be thrown");
		} 
		catch (IndexOutOfBoundsException e) 
		{
		}
	}
	
	/**
	 * Tests the search method of the Billboardlist
	 */
	private void testSearch()
	{
		//Search for a unique title
		List<Billboard> search1 = list.search(EnumSearchType.TITLE, "Advertisement4");
		assertTrue(search1.size() == 1);
		assertTrue(search1.get(0).getTitle().equals("Advertisement4"));
		
		//Search for a unique location
		List<Billboard> search2 = list.search(EnumSearchType.LOCATION, "Ottawa");
		assertTrue(search2.size() == 1);
		assertTrue(search2.get(0).getLocation().equals("Ottawa"));

		//Search for a title that doesn't exist
		List<Billboard> search3 = list.search(EnumSearchType.TITLE, "100% not in the list.");
		assertTrue(search3.size() == 0);
		
		//Search for a location that doesn't exist
		List<Billboard> search4 = list.search(EnumSearchType.LOCATION, "Not in the list.");
		assertTrue(search4.size() == 0);
		
		//Search for a duplicated title
		List<Billboard> search5 = list.search(EnumSearchType.TITLE, "Advertisement1");
		assertTrue(search5.size() == 2);
		assertTrue(search5.get(0).getTitle().equals("Advertisement1"));
		assertTrue(search5.get(1).getTitle().equals("Advertisement1"));
		assertTrue(search5.get(0).getLocation().equals("Moncton"));
		assertTrue(search5.get(1).getLocation().equals("Saint Johns"));

		//Search for a duplicated location
		List<Billboard> search6 = list.search(EnumSearchType.LOCATION, "Moncton");
		assertTrue(search6.size() == 3);
		assertTrue(search6.get(0).getTitle().equals("Advertisement1"));
		assertTrue(search6.get(1).getTitle().equals("Advertisement4"));
		assertTrue(search6.get(2).getTitle().equals("Advertisement5"));
		assertTrue(search6.get(0).getLocation().equals("Moncton"));
		assertTrue(search6.get(1).getLocation().equals("Moncton"));
		assertTrue(search6.get(2).getLocation().equals("Moncton"));
	}

	/**
	 * Tests the asStringArray() method
	 */
	private void testToStringArray()
	{
		String[][] toStringArray = list.asStringArray();
		assertTrue(toStringArray.length == 7);
		assertTrue(toStringArray[0].length == 3);
		
		//Test one - completely correct
		String[][] expectedValues1 = {
				{ "Advertisement1", "Moncton", "Buy our product!" },
				{ "Advertisement2", "Fredericton", "Shop here!"  },
				{ "Advertisement3", "Saint John", "Top Quality!"  },
				{ "Advertisement4", "Moncton", "Best product out there!" },
				{ "Advertisement1", "Saint Johns", "Buy some product!" },
				{ "Advertisement6", "Ottawa", "Buy this product!" },
				{ "Advertisement5", "Moncton", "Buy our product!" },
		};
		try 
		{
			for(int i = 0; i < expectedValues1.length; i++)
			{
				for(int j = 0; j < expectedValues1[0].length; j++)
				{
					if(!toStringArray[i][j].equals(expectedValues1[i][j]))
					{
						fail("Value didn't match: " + toStringArray[i][j] + "; " + expectedValues1[i][j]);
					}
				}
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}

		//Test 2 - case sensitive changes. [Should fail on a few cells]
		String[][] incorrectValues1 = {
				{ "AdvertisemenT1", "Moncton", "Buy our product!" },
				{ "Advertisement2", "Fredericton", "Shop here!"  },
				{ "Advertisement3", "Saint john", "Top Quality!"  },
				{ "Advertisement4", "Moncton", "Best product out there!" },
				{ "Advertisement1", "Saint Johns", "Buy some product!" },
				{ "Advertisement6", "Ottawa", "Buy this product!" },
				{ "Advertisement5", "Moncton", "Buy our product!" },
		};
		try
		{
			for(int i = 0; i < incorrectValues1.length; i++)
			{
				for(int j = 0; j < incorrectValues1[0].length; j++)
				{
					if((i == 0 && j == 0) || (i == 2 && j == 1))
					{
						assertFalse(toStringArray[i][j].equals(incorrectValues1[i][j]));						
					}
					else
					{
						assertTrue(toStringArray[i][j].equals(incorrectValues1[i][j]));
					}
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}

		//Test 3 - obviously incorrect values[should fail on a few cells]
		String[][] incorrectValues2 = {
				{ "Advertisement1", "Moncton", "Buy our product!" },
				{ "Advertisement2", "Fredericton", "Shop here!"  },
				{ "Advertisement3", "<Saint John>", "Top Quality!"  },
				{ "Advertisement4", "Moncton", "Best product out there!" },
				{ "Advertisement1", "Saint Johns", "322Buy some product!" },
				{ "Advertisement6", "Ottawa", "Buy this product!" },
				{ "Advertisement5", "Moncton", "Buy our product!" },
		};
		try 
		{
			for(int i = 0; i < incorrectValues2.length; i++)
			{
				for(int j = 0; j < incorrectValues2[0].length; j++)
				{
					if((i == 4 && j == 2) || (i == 2 && j == 1))
					{
						assertFalse(toStringArray[i][j].equals(incorrectValues2[i][j]));						
					}
					else
					{
						assertTrue(toStringArray[i][j].equals(incorrectValues2[i][j]));
					}
				}
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	/**
	 * Tests the get() method's boundaries
	 */
	private void testGetBoundaries()
	{
		try
		{
			list.get(-1);
			fail("This shouldn't be reached - an exception should be thrown");
		} 
		catch (IndexOutOfBoundsException e) 
		{
		}
		assertTrue(list.get(0) != null);
		assertTrue(list.get(6) != null);
		try {
			list.get(7);
			fail("This shouldn't be reached - an exception should be thrown");
		} 
		catch (IndexOutOfBoundsException e) 
		{
		}	
	}

	/**
	 * Tests the add method
	 */
	private void testAdd()
	{
		//Write a test billboard
		list = new BillboardList();
		list.add(new Billboard("Advertisement1", "Buy our product!", "Moncton"));
		list.add(new Billboard("Advertisement2", "Shop here!", "Fredericton"));
		list.add(new Billboard("Advertisement3", "Top Quality!", "Saint John"));
		list.add(new Billboard("Advertisement4", "Best product out there!", "Moncton"));
		list.add(new Billboard("Advertisement1", "Buy some product!", "Saint Johns"));
		list.add(new Billboard("Advertisement6", "Buy this product!", "Ottawa"));
		list.add(new Billboard("Advertisement5", "Buy our product!", "Moncton"));
		
		//Do some bounds checking - forcing exceptions
		//End of the list
		try 
		{
			list.get(7);
			fail("This shouldn't be reached - an exception should be thrown");
		} 
		catch (IndexOutOfBoundsException e) 
		{
		}
		//Start of the list @-1
		try 
		{
			list.get(-1);
			fail("This shouldn't be reached - an exception should be thrown");
		} 
		catch (IndexOutOfBoundsException e) 
		{
		}

		//Test that the elements seem to be correct
		assertTrue(list.size() == 7);
		assertTrue(list.get(0).getTitle().equals("Advertisement1"));
		assertTrue(list.get(1).getTitle().equals("Advertisement2"));
		assertTrue(list.get(2).getTitle().equals("Advertisement3"));
		assertTrue(list.get(3).getTitle().equals("Advertisement4"));
		assertTrue(list.get(3).getMessage().equals("Best product out there!"));
		assertTrue(list.get(3).getLocation().equals("Moncton"));
		assertTrue(list.get(4).getTitle().equals("Advertisement1"));
		assertTrue(list.get(5).getTitle().equals("Advertisement6"));
		assertTrue(list.get(6).getTitle().equals("Advertisement5"));
		//Check the sizes match up, just to be safe
		assertTrue(list.getBillboards().size() == list.size());
	}
	
}

