package alec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;

/**
 * XMLReader provides a method {@link #readBillboards(File)} to read in a BillboardList xml file.
 * @author Alec Sobeck
 */
public class XMLReader 
{
	/**
	 * Constructor.
	 */
	public XMLReader()
	{
	}

	/**
	 * Reads in a List of Billboards stored in an XML file
	 * @param file the xml File to be loaded
	 * @return the List of Billboards contained in the XML file. 
	 * @throws FileNotFoundException - The File provided to the method does not exist (or can't be found in that directory)
	 * @throws IllegalFileFormatException - The File is formatted in a way such that it cannot be read
	 */
	public List<Billboard> readBillboards(File file) 
			throws FileNotFoundException, IllegalFileFormatException 
	{
		try 
		{
			List<Billboard> items = new ArrayList<Billboard>();
	
			// First, create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(file);
			// Setup a new eventReader
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// read the XML document
			Billboard board = null;
			while (eventReader.hasNext()) 
			{
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) 
				{
					String tagName = event.asStartElement().getName().getLocalPart();
					// If we have an item element, we create a new item
					if (tagName.equals(XMLWriter.BOARD_TAG)) {
						board = new Billboard();
					}
					if (tagName.equals(XMLWriter.TITLE_TAG)) 
					{
						event = eventReader.nextEvent();
						board.setTitle(event.asCharacters().getData());
						continue;
					}
					else if(tagName.equals(XMLWriter.LOCATION_TAG)) 
					{
						event = eventReader.nextEvent();
						board.setLocation(event.asCharacters().getData());
						continue;
					}
					else if (tagName.equals(XMLWriter.MESSAGE_TAG)) 
					{
						event = eventReader.nextEvent();
						board.setMessage(event.asCharacters().getData());
						continue;
					}
				}
	
				//Finished reading an item, so add it to the list
				if (event.isEndElement()) 
				{
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart().equals(XMLWriter.BOARD_TAG)) 
					{
						items.add(board);
					}
				}
			}
			return items;
		}
		catch (XMLStreamException e) 
		{
			throw new IllegalFileFormatException("Failed to read the xml file - bad format.");
		}
	}
}
