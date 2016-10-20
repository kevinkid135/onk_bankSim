package onk_simBank;

/**
 * exception class. Used to throw an exception when there's an invalid input.
 * 
 * @author Team Onk
 *
 */
public class InvalidInput extends Exception {

	/**
	 * Constructs an exception with a message
	 * 
	 * @param message
	 *            A specified message.
	 */

	public InvalidInput(String message) {
		super(message); // outputs message to be thrown
	}// end InvalidInput exception

}// end InvalidInput