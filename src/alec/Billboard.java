package alec;

/**
 * Stores information about a single billboard - its title, message, location.
 * @author Alec Sobeck
 */
public class Billboard 
{
	private String title;
	private String message;
	private String location;
	
	/**
	 * Constructor.
	 */
	public Billboard()
	{
		title = "";
		message = "";
		location = "";
	}

	/**
	 * Constructor.
	 */
	public Billboard(String title, String message, String location)
	{
		this.title = title;
		this.message = message;
		this.location = location;
	}

	/**
	 * Accessor method for message.
	 */
	public String getMessage() 
	{
		return message;
	}

	/**
	 * Mutator method for message.
	 */
	public void setMessage(String message) 
	{
		this.message = message;
	}

	/**
	 * Accessor method for title.
	 */
	public String getTitle() 
	{
		return title;
	}

	/**
	 * Mutator method for title.
	 */
	public void setTitle(String title) 
	{
		this.title = title;
	}

	/**
	 * Accessor method for location.
	 */
	public String getLocation() 
	{
		return location;
	}

	/**
	 * Mutator method for location.
	 */
	public void setLocation(String location) 
	{
		this.location = location;
	}

	/**
	 * Provides the standard toString() definition from CS1073.
	 */
	@Override
	public String toString()
	{
		return "Title: " + title + ", Location: " + location +  ", Message: " + message;
	}

	/**
	 * Provides a formatted String that is useful for displaying a search result.
	 */
	public String toList()
	{
		return "Billboard[title=" + title + "; message=" + message + "; location=" + location + "]";
	}
}