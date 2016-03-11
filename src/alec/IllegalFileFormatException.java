package alec;

/**
 * Thrown if an illegal file is given to a BillboardList method
 * @author Alec Sobeck
 */
public class IllegalFileFormatException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new IllegalFileFormatException with no message
	 */
	public IllegalFileFormatException()
	{
		super();
	}
	
	/**
	 * Constructs a new IllegalFileFormatException with a message
	 */
	public IllegalFileFormatException(String message)
	{
		super(message);
	}
}
