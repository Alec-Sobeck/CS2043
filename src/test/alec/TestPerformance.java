package test.alec;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import alec.Billboard;
import alec.BillboardList;
import alec.EnumSearchType;
import alec.IllegalFileFormatException;
import alec.XMLReader;
import alec.XMLWriter;

/**
 * Tests the performance [non-functional] metrics
 * @author Alec Sobeck
 */
public class TestPerformance 
{
	private List<File> filesToDelete = new ArrayList<File>();

	@Test
	public void testFileIO()
	{
		testFileIOWrite();
		testFileIORead();
		for(File file : filesToDelete)
		{
			file.delete();
		}
	}
	
	/**
	 * Tests FileIO writing on various numbers of elements in the list, from 5k - 100k
	 */
	public void testFileIOWrite() 
	{
		int successfulTests = 0;
		
		for(int j = 1; j <= 20; j++)
		{
			BillboardList testListWithMuchData = new BillboardList();
			for(int i = 0; i < 5000 * j; i++)
			{
				testListWithMuchData.add(new Billboard("" + i, "" + (i*2), "" + (i*3)));
			}

			XMLWriter writer = new XMLWriter();
			long start = System.currentTimeMillis();
			try {
				File file = new File("myinsanetest"+ j +".xml");
				this.filesToDelete.add(file);
				writer.write(file, testListWithMuchData.getBillboards());
			} catch (FileNotFoundException e) {
				fail("bad io");
			}
			long end = System.currentTimeMillis();
			System.out.println(end - start);
			if(end - start < 10000)
			{
				successfulTests++;
			}
		}
		assertTrue(successfulTests >= 19);
	}

	/**
	 * Tests FileIO reading on various numbers of elements in the list, from 5k - 100k
	 */
	public void testFileIORead() 
	{
		int successfulTests = 0;
		
		for(int j = 1; j <= 20; j++)
		{
			XMLReader reader = new XMLReader();
			List<Billboard> boards = null;
			long start = System.currentTimeMillis();
			try {
				boards = reader.readBillboards(new File("myinsanetest"+ j +".xml"));
			} catch (FileNotFoundException e) {
				fail("bad io");
			} catch (IllegalFileFormatException e) {
				fail("bad format");
			}
			
			long end = System.currentTimeMillis();
			System.out.println(end - start);
			if(end - start < 10000)
			{
				successfulTests++;
			}
			assertTrue(boards.size() == j * 5000);
		}
		assertTrue(successfulTests >= 19);
	}
	
	/**
	 * Tests the search performance requirement specified in the requirements doc.
	 */
	@Test
	public void testSearch()
	{
		//Create a billboardlist with 25000 elements in it. Use modulo math to create duplicates.
		BillboardList testListWithMuchData = new BillboardList();
		for(int i = 0; i < 25000; i++)
		{
			testListWithMuchData.add(new Billboard("" + i % 341, "" + (i*2) % 3402, "" + (i*3) % 2313));
		}
		
		final int TESTS_PER_ENUM = 2000;
		final int REQUIRED_SUCCESSFUL_TESTS = (int) (0.95 * TESTS_PER_ENUM * 3);
		final Random random = new Random();
		long start, end;
		int rand; 
		int successfulTests = 0;		
		//Conduct 2000 test searches on each EnumSearchType, using random values, and ensure they fall 
		//within the allowed time period.
		//Test on LOCATION
		for(int i = 0; i < TESTS_PER_ENUM; i++)
		{
			rand = random.nextInt(255);
			start = System.currentTimeMillis();
			testListWithMuchData.search(EnumSearchType.LOCATION, "" + rand);
			end = System.currentTimeMillis();
			if(end - start < 6000)
			{
				successfulTests++;
			}
		}
		//Test on MESSAGE
		for(int i = 0; i < TESTS_PER_ENUM; i++)
		{
			rand = random.nextInt(255);
			start = System.currentTimeMillis();
			testListWithMuchData.search(EnumSearchType.MESSAGE, "" + rand);
			end = System.currentTimeMillis();
			if(end - start < 6000)
			{
				successfulTests++;
			}
		}
		//Test on TITLE
		for(int i = 0; i < TESTS_PER_ENUM; i++)
		{
			rand = random.nextInt(255);
			start = System.currentTimeMillis();
			testListWithMuchData.search(EnumSearchType.TITLE, "" + rand);
			end = System.currentTimeMillis();
			if(end - start < 6000)
			{
				successfulTests++;
			}
		}
		
		System.out.println(successfulTests);
		assertTrue(successfulTests > REQUIRED_SUCCESSFUL_TESTS);
	}
	
	/**
	 * Tests the add element performance requirement specified in the requirements doc.
	 */
	@Test
	public void testAdd()
	{
		final Random random = new Random();
		long start, end;
		int totalIterations = 0;
		int successfulTests = 0;
		
		for(int j = 0; j < 10; j++)
		{
			BillboardList list = new BillboardList();

			//Value from 0-24999
			int numberOfIterations = random.nextInt(25000);
			totalIterations += numberOfIterations;

			//Add that many billboards to the list. 
			for(int i = 0; i < numberOfIterations; i++)
			{
				start = System.currentTimeMillis();
				list.add(new Billboard("" + i % 65, "" + (i*2) % 986, "" + (i*3) % 213));
				end = System.currentTimeMillis();
				if(end - start < 3000)
				{
					successfulTests++;
				}
			}
		}
		
		System.out.println(successfulTests + "/" + totalIterations);
		assertTrue(successfulTests > (totalIterations * 0.95));
	}
	
	/**
	 * Tests the remove element performance requirement specified in the requirements doc.
	 */
	@Test
	public void testRemove()
	{
		final Random random = new Random();
		long start, end;
		int totalIterations = 0;
		int successfulTests = 0;
		
		for(int j = 0; j < 10; j++)
		{
			BillboardList list = new BillboardList();

			//Value from 0-24999
			int numberOfIterations = random.nextInt(25000);
			totalIterations += numberOfIterations;

			//Add that many billboards to the list. 
			for(int i = 0; i < numberOfIterations; i++)
			{
				list.add(new Billboard("" + i % 65, "" + (i*2) % 986, "" + (i*3) % 213));
			}
			
			//Now remove them all 
			for(int i = 0; i < numberOfIterations; i++)
			{
				start = System.currentTimeMillis();
				list.remove(0);
				end = System.currentTimeMillis();
				if(end - start < 3000)
				{
					successfulTests++;
				}
			}
		}
		
		System.out.println(successfulTests + "/" + totalIterations);
		assertTrue(successfulTests > (totalIterations * 0.95));
	}
	
	/**
	 * Tests the modify element requirement specified in the requirements doc.
	 */
	@Test
	public void testModify()
	{
		final Random random = new Random();
		long start, end;
		int totalIterations = 0;
		int successfulTests = 0;
		
		for(int j = 0; j < 10; j++)
		{
			BillboardList list = new BillboardList();

			//Value from 0-24999
			int numberOfIterations = random.nextInt(25000);
			totalIterations += numberOfIterations;

			//Add that many billboards to the list. 
			for(int i = 0; i < numberOfIterations; i++)
			{
				list.add(new Billboard("" + i % 65, "" + (i*2) % 986, "" + (i*3) % 213));
			}
			
			//Now modify them all 
			for(int i = 0; i < numberOfIterations; i++)
			{
				start = System.currentTimeMillis();
				list.set(i, 0, "New Value1");
				list.set(i, 1, "New Value2");
				list.set(i, 2, "New Value3");
				end = System.currentTimeMillis();
				if(end - start < 3000)
				{
					successfulTests++;
				}
			}
		}
		
		System.out.println(successfulTests + "/" + totalIterations);
		assertTrue(successfulTests > (totalIterations * 0.95));
	
	}
		
}
