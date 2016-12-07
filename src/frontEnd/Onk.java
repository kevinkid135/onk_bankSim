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
 * accListFilename
 * 
 * Outputs: Transaction summary file with filename corresponding to value of
 * tranSumFilename
 * 
 * @author Team Onk
 *
 */
public class Onk {

	private static String accountsListFilename;
	private static String transactionSummaryFilename;

	/**
	 * Initiates SimBank. It sets the accounts list filename and the transaction
	 * summary file filename to the command line arguments (args[0] and
	 * args[1]). It creates session objects, and allows the program to be
	 * continuously run despite the termination of a session. As a result,
	 * SimBank will not cease functioning when the session is switched from atm
	 * to agent, or vice versa.
	 * 
	 * @param args - the filenames for the accounts list and the transaction summary file respectively
	 * @throws InvalidInput
	 */
	public static void main(String[] args) throws InvalidInput {
		// create a bank instance
		SimBank s = new SimBank();

		// set accounts list and transaction summary file filenames
		accountsListFilename = args[0];
		transactionSummaryFilename = args[1];

		while (true)
			// since program never ends (until 'exit' transaction),
			// we use infinite loop
			s.start();
	}// end main

	/**
	 * Returns the filename for the accounts list file as a String.
	 * 
	 * @return filename for the accounts list file
	 */
	public static String getAccountsListFilename() {
		return accountsListFilename;
	}

	/**
	 * Returns the filename for the transaction summary file as a String.
	 * 
	 * @return filename for the transaction summary file
	 */
	public static String getTranSummaryFilename() {
		return transactionSummaryFilename;
	}
}
