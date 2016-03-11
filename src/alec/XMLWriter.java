package alec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Writes billboards to disk as an xml file. Liberal usage of the flush() method is so formatting 
 * isn't written inside tags.
 * @author Alec Sobeck
 */
public class XMLWriter 
{
	public static final String TITLE_TAG = "title";
	public static final String MESSAGE_TAG = "message";
	public static final String LOCATION_TAG = "location";
	public static final String BOARD_TAG = "Billboard";
	public static final String OUTER_TAG = "BillboardList";
	private XMLOutputFactory outputFactory;
	private XMLStreamWriter writer;

	/**
	 * Constructor.
	 */
	public XMLWriter()
	{
	}
	
	/**
	 * Writes the billboard objects to XML file.
	 * @param filepath name of XML file to write
	 * @param items the billboard items to write to the xml file
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 */
	public void write(File file, List<Billboard> items) 
			throws FileNotFoundException
	{
		try 
		{
			writeStart(file);
			for(Billboard board : items)
			{
				add(board);
			}
			writeEnd();
		} 
		catch (XMLStreamException e) 
		{
			//There is no alternative case for this because there is no known way
			//to invoke an XMLStreamException with this code. Saving simply fails
			//if this happens. 
			e.printStackTrace();
			System.err.println("[Unexpected Error]: Failed to write xml file (XMLStreamException).");
		}
	}

	/**
	 * Opens the XMLFile and creates the starting tags:
	 * <?xml version "1.0" encoding="UTF-8"?> and <Billboards>
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 */
	private void writeStart(File file) 
			throws FileNotFoundException, XMLStreamException 
	{
		// create an XMLOutputFactory
		outputFactory = XMLOutputFactory.newInstance();
		writer = outputFactory.createXMLStreamWriter(new FileOutputStream(file, false));
		//Write document start
		writer.setPrefix("", "");
		writer.writeStartDocument();
		writer.flush();
		//Write the BillboardList tag
		writer.writeDTD("\n");
		writer.flush();
		writer.writeStartElement("", OUTER_TAG);
		writer.flush();
		writer.writeDTD("\n");
		writer.flush();
	}

	/**
	 * Adds a billboard element to XML document. 
	 * @throws XMLStreamException 
	 */
	private void add(Billboard board) 
			throws XMLStreamException 
	{
		//Indentation - 1 tab
		writer.writeDTD("\t");
		writer.flush();
		//Write an open Billboard tag
		writer.writeStartElement("", BOARD_TAG);
		writer.flush();
		writer.writeDTD("\n");
		writer.flush();
		// Write the different values
		createNode(TITLE_TAG, board.getTitle());
		createNode(MESSAGE_TAG, board.getMessage());
		createNode(LOCATION_TAG, board.getLocation());

		//Indentation - 1 tab
		writer.writeDTD("\t");
		writer.flush();
		//Write the closing Billboard tag
		writer.writeEndElement();
		writer.flush();
		writer.writeDTD("\n");
		writer.flush();
	}

	/**
	 * Closes the XML file and writes the document end.
	 * @throws XMLStreamException 
	 */
	private void writeEnd() 
			throws XMLStreamException  
	{
		//Write the closing BillboardList tag
		writer.writeEndElement();
		writer.flush();
		writer.writeDTD("\n");
		writer.flush();
		writer.close();
	}

	/**
	 * Creates a node in the XML file. A node is a set of opening and closing tags with a value inside them, all on the same line.
	 * @param eventWriter
	 * @param name
	 * @param value
	 * @throws XMLStreamException
	 */
	private void createNode(String name, String value) 
			throws XMLStreamException 
	{
		//Indentation (2 tabs)
		writer.writeDTD("\t");
		writer.writeDTD("\t");
		writer.flush();
		//Write opening tag
		writer.writeStartElement("", name);
		writer.flush();
		//Write the contents of the tag
		writer.writeCharacters(value);
		writer.flush();
		//Write closing tag
		writer.writeEndElement();
		writer.flush();
		writer.writeDTD("\n");
		writer.flush();
	}
}
