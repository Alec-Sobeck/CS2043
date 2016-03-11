package alec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BillboardList stores some number of Billboards, and provides useful methods to do things such as:
 * save them to disk, load a file that was saved previously, or manipulate/query the Billboards in 
 * the list at this time.
 * @author Alec Sobeck
 */
public class BillboardList 
{
	private final static int TOTAL_COLUMNS = 3;
	public final static int COLUMN_TITLE = 0, 
								COLUMN_LOCATION = 1, 
								COLUMN_MESSAGE = 2;
	private List<Billboard> billboards;
	private File resaveLocation;
	
	/**
	 * Constructor that creates a new List of billboards with initial billboards stored.
	 */
	public BillboardList()
	{
		billboards = new ArrayList<Billboard>();
	}

	/**
	 * Sets a data attribute of one of the Billboards in the BillboardList to 
	 * a new value.
	 * @param row an int, which corresponds to the index of the billboard to modify
	 * @param column an int, which corresponds to the exact data attribute of the billboard
	 * to modify. This corresponds to the constants {@link #COLUMN_TITLE}, {@link #COLUMN_LOCATION}
	 * and {@value #COLUMN_MESSAGE}
	 */
	public void set(int row, int column, String value)
	{
		if(row < 0 || row >= size())
		{
			throw new IndexOutOfBoundsException("row: " + row + " is out of bounds.");
		}
		if(column < 0 || column >= TOTAL_COLUMNS)
		{
			throw new IndexOutOfBoundsException("column: " + column + " is out of bounds.");
		}
		Billboard board = get(row);
		if(column == COLUMN_TITLE)
		{
			board.setTitle(value);
		}
		else if(column == COLUMN_LOCATION)
		{
			board.setLocation(value);
		}
		else if(column == COLUMN_MESSAGE)
		{
			board.setMessage(value);
		}
	}

	/**
	 * Reads an XML file at the specified location and adds all the Billboards 
	 * read to this BillboardList.
	 * @param file the location of the File to read
	 * @throws FileNotFoundException - The provided file does not exist on disk.
	 * @throws IllegalFileFormatException - the file name doesn't end with .xml or the file exists
	 * on disk, but couldn't be read in
	 */
	public void load(File file) 
			throws FileNotFoundException, IllegalFileFormatException
	{
		if(file == null)
		{
			throw new NullPointerException("Provided file is null. The load function requires a non-null file.");
		}
		if(!file.exists())
		{
			throw new FileNotFoundException("The file provided does not exist on disk.");
		}
		//Fail if the filename doesn't end with .xml
		if(!file.getName().endsWith(".xml"))
		{
			throw new IllegalFileFormatException("File Name doesn't end with .xml");
		}
		
		XMLReader reader = new XMLReader();
		for(Billboard b : reader.readBillboards(file))
		{
			billboards.add(b);
		}
		resaveLocation = file;
	}
	
	/**
	 * Saves the BillboardList to the specified File on disk, as an XML File.
	 * @param file the File location to save the BillboardList. This will be stored 
	 * automatically so the {@link #resave()} method may successfully resave to 
	 * this same location.
	 * @throws FileNotFoundException - The provided file does not exist on disk.
	 * @throws IllegalFileFormatException - the file name doesn't end with .xml
	 */
	public void save(File file) 
			throws FileNotFoundException, IllegalFileFormatException
	{
		if(file == null)
		{
			throw new NullPointerException("Provided file is null. The save function requires a non-null file.");
		}
		//Try to create the file before resaving it
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new FileNotFoundException("The file provided does not exist on disk.");
		}
		if(!file.exists())
		{
			throw new FileNotFoundException("The file provided does not exist on disk.");
		}
		//Fail if the filename doesn't end with .xml
		if(!file.getName().endsWith(".xml"))
		{
			throw new IllegalFileFormatException("File Name doesn't end with .xml");
		}
		XMLWriter writer = new XMLWriter();
		writer.write(file, billboards);
		resaveLocation = file;
	}
	
	/**
	 * Saves the BillboardList to disk at the resaveLocation stored inside the 
	 * BillboardList. 
	 * @throws FileNotFoundException - the BillboardList has a resave location 
	 * stored, but that file doesn't actually exist on disk.
	 * @throws IllegalFileFormatException - the file name doesn't end with .xml
	 */
	public void resave() 
			throws FileNotFoundException, IllegalFileFormatException
	{
		if(resaveLocation == null)
		{
			throw new NullPointerException("Resave Location is null. Resave function requires a not-null resave location. ");
		}
		//Try to create the file before resaving it
		try {
			resaveLocation.createNewFile();
		} catch (IOException e) {
			throw new FileNotFoundException("No such file on disk.");
		}
		if(!resaveLocation.exists())
		{
			throw new FileNotFoundException("Resave location does not exist on disk.");
		}
		//Fail if the filename doesn't end with .xml
		if(!resaveLocation.getName().endsWith(".xml"))
		{
			throw new IllegalFileFormatException("File Name doesn't end with .xml");
		}
		XMLWriter writer = new XMLWriter();
		writer.write(resaveLocation, billboards);
	}
	
	/**
	 * Generates a String[][] which contains the data in this BillboardList. 
	 * @return a String[][] which contains the data in this table in an appropriate 
	 * order. This array will have dimensions [NUMBER_OF_BILLBOARDS][TOTAL_COLUMNS]
	 */
	public String[][] asStringArray()
	{
		String[][] values = new String[billboards.size()][TOTAL_COLUMNS];
		for(int i = 0; i < billboards.size(); i++)
		{
			Billboard board = billboards.get(i);
			values[i][COLUMN_TITLE] = board.getTitle();
			values[i][COLUMN_LOCATION] = board.getLocation();
			values[i][COLUMN_MESSAGE] = board.getMessage();
		}
		return values;
	}
	
	/**
	 * Performs a case in-sensitive search, finding everything that matches the provided value String
	 * @param type an EnumSearchType which corresponds to the attribute the search will be based on
	 * @param value a String which will be used to find relevant search results.
	 * @return a List of Billboards, which match the provided search criteria
	 */
	public List<Billboard> search(EnumSearchType type, String value)
	{
		List<Billboard> results = new ArrayList<Billboard>();
		//Search based on a Location
		if(type == EnumSearchType.LOCATION)
		{
			for(int i = 0; i < billboards.size(); i++)
			{
				if(billboards.get(i).getLocation().equalsIgnoreCase(value))
				{
					results.add(billboards.get(i));
				}
			}
		}
		//Search based on a Title
		if(type == EnumSearchType.TITLE)
		{
			for(int i = 0; i < billboards.size(); i++)
			{
				if(billboards.get(i).getTitle().equalsIgnoreCase(value))
				{
					results.add(billboards.get(i));
				}
			}
		}
		//Search based on a Message
		if(type == EnumSearchType.MESSAGE)
		{
			for(int i = 0; i < billboards.size(); i++)
			{
				if(billboards.get(i).getMessage().equalsIgnoreCase(value))
				{
					results.add(billboards.get(i));
				}
			}
		}
		return results;
	}
	
	/**
	 * Removes the Billboard at the specified index from this list.
	 * @param index an int, which specifies the index of the Billboard to remove from the BillboardList. 
	 * This index is zero based (the first element is found at index 0).
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public void remove(int index)
	{
		billboards.remove(index);
	}
	
	/**
	 * Gets the Billboard object stored at the specified index.
	 * @param index an int, which specifies the index of the Billboard to fetch 
	 * from the BillboardList. This index is zero based (the first element is found at index 0).
	 * @return the Billboard stored at the specified index 
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public Billboard get(int index)
	{
		return billboards.get(index);
	}
	
	/**
	 * Accessor method for billboards.
	 */
	public List<Billboard> getBillboards()
	{
		return billboards;
	}

	/**
	 * Adds a Billboard to the end of the BillboardList
	 * @param b a Billboard to add to the end of the list
	 */
	public void add(Billboard b)
	{
		billboards.add(b);
	}
	
	/**
	 * Removes all the Billboards from this BillboardList.
	 */
	public void clear()
	{
		billboards.clear();
	}

	/**
	 * Checks if there is a resave location for this BillboardList, for the resave() functionality.
	 * @return a boolean, true if there is a File location to resave this BillboardList to; or false if there is none
	 */
	public boolean hasResaveLocation()
	{
		return resaveLocation != null;
	}

	/**
	 * Accessor method for resaveLocation.
	 */
	public File getResaveLocation() 
	{
		return resaveLocation;
	}

	/**
	 * Mutator method for resaveLocation.
	 */
	public void setResaveLocation(File resaveLocation) 
	{
		this.resaveLocation = resaveLocation;
	}

	/**
	 * Gets the total number of billboards stored in this BillboardList.
	 * @return an int, the number of billboards in this BillboardList
	 */
	public int size()
	{
		return billboards.size();
	}
}
