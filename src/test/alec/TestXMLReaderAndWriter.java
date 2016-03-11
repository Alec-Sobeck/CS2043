package test.alec;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import alec.Billboard;
import alec.BillboardList;
import alec.IllegalFileFormatException;
import alec.XMLReader;
import alec.XMLWriter;
import static org.junit.Assert.*;

/**
 * Tests the XMLReader and XMLWriter
 * @author Alec Sobeck
 */
public class TestXMLReaderAndWriter 
{
	/**
	 * Writes a BillboardList to disk and reads it back into memory from the file, to ensure the reader and writer may successfully reconstruct a file
	 */
	@Test
	private void readAndWriteTestList()
	{
		//Write a test billboard
		BillboardList test = new BillboardList();
		test.add(new Billboard("Advertisement1", "Buy our product!", "Moncton"));
		test.add(new Billboard("Advertisement2", "Shop here!", "Fredericton"));
		test.add(new Billboard("Advertisement3", "Top Quality!", "Saint John"));
		test.add(new Billboard("Advertisement4", "Best product out there!", "Moncton"));
		
		XMLWriter writer = new XMLWriter();
		try {
			writer.write(new File("test.xml"), test.getBillboards());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("File I/O Write failed.");
		}
				
		//Read billboard back in
		XMLReader reader = new XMLReader();
		BillboardList newList = new BillboardList();
		List<Billboard> boards = null;
		try {
			boards = reader.readBillboards(new File("test.xml"));
		} catch (FileNotFoundException e) {
			fail("File I/O Read failed.");
			e.printStackTrace();
		} catch (IllegalFileFormatException e) {
			fail("File I/O Read failed.");
			e.printStackTrace();
		}
		for(Billboard b : boards)
		{
			newList.add(b);
		}	
		//Test that the elements seem to be correct
		assertTrue(newList.size() == 4);
		assertTrue(newList.get(0).getTitle().equals("Advertisement1"));
		assertTrue(newList.get(1).getTitle().equals("Advertisement2"));
		assertTrue(newList.get(2).getTitle().equals("Advertisement3"));
		assertTrue(newList.get(3).getTitle().equals("Advertisement4"));
		
	}
	
}
