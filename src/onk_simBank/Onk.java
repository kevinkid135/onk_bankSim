package onk_simBank;

/**
 * Main class to create session object. The object will run indefinitely as
 * required.
 * 
 * This class runs the SimBank class repeatedly (until the program is terminated
 * from the 'exit' transaction).
 * 
 * From the SimBank class:
 * 
 * Starts by asking user to login and subsequently for a session type, followed
 * by reading in the valid accounts file after successful login.
 * 
 * User can then enter any transactions that their session type permits them to.
 * Or, they may logout at which point the session ends and all transactions
 * during the session are written into the transaction summary file.
 * 
 * The user may then start another session as the program asks for a login.
 * 
 * The user may terminate the program using the 'exit' transaction whenever they
 * are asked to login.
 * 
 * Inputs: Valid accounts list file with filename corresponding to value of
 * ACCOUNT_LIST_FILENAME
 * 
 * Outputs: Transaction summary file with filename corresponding to value of
 * TRANSACTION_SUMMARY_FILENAME
 * 
 * @author Team Onk
 *
 */
public class Onk {

	public static void main(String[] args) throws InvalidInput {
		// create a bank instance
		SimBank s = new SimBank();

		while (true)
			// since program never ends (until 'exit' transaction),
			// we use infinite loop
			s.start();
	}// end main

}
