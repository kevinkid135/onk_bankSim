package onk_simBank;

// don't have to type System before every output
import static java.lang.System.out;

import java.io.*;
import java.util.*;

/**
 * A SimBank session class. It takes in input commands and conducts
 * transactions.
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
 * The user may terminate the program whenever they are asked to login.
 * 
 * Inputs: Valid accounts list file with filename corresponding to value of
 * ACCOUNT_LIST_FILENAME Outputs: Transaction summary file with filename
 * corresponding to value of TRANSACTION_SUMMARY_FILENAME
 * 
 * @author Team onk
 *
 */
public class SimBank {

	// declare constants indicating the current session type
	static final int LOGGED_OUT = 0;
	static final int ATM_MODE = 1;
	static final int AGENT_MODE = 2;
	static int sessionType = LOGGED_OUT;

	static ArrayList<Account> accList;
	final String ACCOUNT_LIST_FILENAME = "accountList.txt"; // filename of valid
															// accounts list
															// file

	static ArrayList<String> tranSummary;
	final String TRANSACTION_SUMMARY_FILENAME = "tranSum.txt"; // filename of
																// transaction
																// summary file

	Scanner in = new Scanner(System.in); // new scanner object
	String input; // used for user input

	/**
	 * Starts a new session by asking the user to login. 
	 * Continuously prompts user to login until either 'login' or 'exit' is entered. 
	 * 
	 * If 'login', goes to login() and asks for transactions afterwards.
	 * 
	 * If 'exit', the program is terminated.
	 */
	public void start() {

		// keep asking for login until sessionType is changed from LOGGED_OUT
		do {

			// ask user for command
			out.println("Please login:");
			input = in.nextLine();

			switch (input) {
			case "login":
				// login to the system
				login();
				break;
			case "exit":
				// exit the program
				System.exit(0);
				break;
			default:
				// invalid command
				System.out.println("Invalid command.");
			}

		} while (sessionType == LOGGED_OUT);

		// continuously runs transaction()
		// until loggedIn is set to false by result of transaction()
		boolean loggedIn = true;
		while (loggedIn) {
			loggedIn = transaction();
		}
	}

	/**
	 * Returns if an account matching the entered account number is in the
	 * accList array.
	 * 
	 * @param acc
	 *            is the account number to be checked
	 * @return true if it already exists, and false if it does not exist
	 */
	public boolean accountExist(String acc) {
		int accNum = Integer.parseInt(acc);

		// iterate through the accList until an Account with the matching
		// account number is found
		for (Account a : accList) {
			if (accNum == a.getAccNum())
				return true;
		}
		return false;
	}

	/**
	 * Return if the account number is valid syntactically.
	 * 
	 * An account number is valid syntactically if it consists of only
	 * alphanumeric characters, has a length of 8 characters, and does not start
	 * with 0.
	 * 
	 * @param acc
	 * @return true if the account number is valid syntactically, false
	 *         otherwise
	 */
	public boolean validAccount(String acc) {
		if (acc.matches("[0-9]+") && (acc.length() == 8) && !acc.startsWith("0"))
			return true;
		else
			return false;
	}

	/**
	 * Returns the Account matching the entered account number, if the account
	 * is in the accList array.
	 * 
	 * @param accNum
	 * @return the corresponding Account if found, null if not found
	 */
	public Account findAccount(String accNum) {
		int acc = Integer.parseInt(accNum);

		// iterate through the accList until an Account with the matching
		// account number is found
		for (Account a : accList) {
			if (a.getAccNum() == acc)
				return a;
		}
		return null;
	}

	/**
	 * Returns if the account name is valid syntactically.
	 * 
	 * An account name is valid syntactically if it has a length between 3 and
	 * 30 (inclusive), and does not start or end with spaces.
	 * 
	 * @param name
	 * @return true if the account name is valid syntactically, false otherwise
	 */
	public boolean validName(String name) {
		if (name.length() >= 3 && name.length() <= 30 && !name.startsWith(" ") && !name.endsWith(" "))
			return true;
		else
			return false;
	}

	/**
	 * Returns if the amount entered is valid syntactically.
	 * 
	 * An amount is valid syntactically if it only consists of numeric
	 * characters, and has a length between 3 and 8 (inclusive).
	 * 
	 * @param amount
	 * @return true if the amount is valid syntactically, false otherwise
	 */
	public boolean validAmount(String amount) {
		if (amount.matches("[0-9]+") && (amount.length() >= 3) && (amount.length() <= 8))
			return true;
		else
			return false;
	}

	/**
	 * Takes in the transaction code, 'to' account number, 'from' account
	 * number, amount, and account name.
	 * 
	 * Converts these values into a string following the format of a transaction
	 * message in the Transaction Summary File.
	 * 
	 * @param tranCode
	 *            is a two letter transaction code, where DE-deposit,
	 *            WD-withdrawal, TR-transfer, CR-create, DL-delete, ES-end of
	 *            session
	 * @param accNum1
	 *            is the first (to) account number. Account numbers are always
	 *            exactly eight decimal digits, not beginning with 0. If unused,
	 *            input empty string ("")
	 * @param accNum2
	 *            is the second (from) account number. Account numbers are
	 *            always exactly eight decimal digits, not beginning with 0. If
	 *            unused, input empty string ("")
	 * @param amount
	 *            is the amount in cents. If unused, enter 0
	 * @param name
	 *            is the account name. If unused, input empty string ("")
	 * 
	 * @return a string following the format of a transaction message in the
	 *         Transaction Summary File
	 */
	private static String toTransMsg(String tranCode, String accNum1, String accNum2, int amount, String name) {
		String amountString = "";

		// if transaction code is not 2 characters, print error message
		if (tranCode.length() != 2)
			System.out.println("ERROR: Transaction code is not the correct length.");

		// check if any account numbers are not used
		// change to default 00000000 if not used
		if (accNum1.isEmpty())
			accNum1 = "00000000";
		if (accNum2.isEmpty())
			accNum2 = "00000000";

		// pad the amount to 3 digits (if less than 3 digits)
		// and convert amount to a string
		if (amount == 0)
			amountString = "000";
		else if (amount < 10)
			amountString = "00" + String.valueOf(amount);
		else if (amount < 100)
			amountString = "0" + String.valueOf(amount);
		else
			amountString = String.valueOf(amount);

		// check if name parameter is not used
		// change to default *** if not used
		if (name.isEmpty())
			name = "***";

		// create and return transaction message string
		String s = tranCode + " " + accNum1 + " " + accNum2 + " " + amountString + " " + name;
		return s;
	}

	/**
	 * Return the current session type for this session.
	 * 
	 * @return sessionType, the current session type
	 */
	public static int getSessionType() {
		return sessionType;
	}

	/**
	 * Asks for a transaction, and calls the appropriate function.
	 * 
	 * Prints Invalid command if transaction command is not of the listed.
	 * 
	 * Returns a boolean indicating if user is to be still logged in (ie. false
	 * if logout is requested).
	 * 
	 * @return false if logging out, true otherwise
	 */
	private boolean transaction() {
		out.println("logout? Transaction?");
		input = in.nextLine();

		switch (input) {
		case "create":
			return transactionCreate();
		case "delete":
			return transactionDelete();
		case "deposit":
			return transactionDeposit();
		case "transfer":
			return transactionTransfer();
		case "withdraw":
			return transactionWithdraw();
		case "logout":
			return transactionLogout(); // will return false
		default:
			System.out.println("Invalid command");
			return true; // notifies that the user is still logged in
		}// Close switch statement

	}// End transaction method

	/**
	 * Continually asks for the session type (atm or agent mode) until valid
	 * input is entered.
	 * 
	 * Proceeds to read in the valid accounts file in accList and initializes
	 * the tranSummary array to be placed into the transaction summary file.
	 */
	private void login() {

		// exits loop once valid input is detected
		while (true) {
			out.println("atm or agent?");
			input = in.nextLine();

			if (input.equals("atm")) {
				sessionType = ATM_MODE;
				out.println("Logged in as atm.");
				break;
			} else if (input.equals("agent")) {
				sessionType = AGENT_MODE;
				out.println("Logged in as agent.");
				break;
			}
		}

		// read in valid accounts file after session type is accepted (loop
		// exited)

		accList = new ArrayList<Account>();
		tranSummary = new ArrayList<String>();

		// read in file
		try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_LIST_FILENAME))) {

			// add every line/account from the valid accounts file into the
			// accList array
			String currentLine;
			while ((currentLine = br.readLine()) != null) {

				Account tempAcc = new Account(Integer.parseInt(currentLine));
				accList.add(tempAcc);
			}

		} catch (IOException e) {
			System.out.println("Error: There was a problem while reading " + ACCOUNT_LIST_FILENAME);
			e.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * Allowed only if the session type is set to agent mode.
	 * 
	 * Asks for an account number and checks if it is syntactically correct and
	 * does not already exist in accList. If successful, asks for an account
	 * name and checks if that is syntactically valid. If successful, the
	 * transaction message is added to tranSummary array.
	 * 
	 * Note: The new account is not added to accList to prevent transactions on
	 * this account in the current session.
	 * 
	 * @return true to signify that the user is still logged in.
	 */
	private boolean transactionCreate() {

		// create transaction is allowed only in agent mode
		if (sessionType == AGENT_MODE) {

			// ask for account number
			System.out.println("Account number?");
			String acc = in.nextLine();

			// check if account number is valid and unique
			if (validAccount(acc)) {
				if (!accountExist(acc)) {

					// ask for account name
					System.out.println("Account name?");
					String name = in.nextLine();

					// check if account name is valid
					if (validName(name)) {

						// print and add transaction message to tranSummary
						System.out.println("Account " + acc + " created.");
						tranSummary.add(toTransMsg("CR", acc, "", 0, name));
					} else
						System.out.println("Invalid account name");
				} else {
					System.out.println("Account already exists");
				}
			} else
				System.out.println("Invalid account number.");
		} else
			System.out.println("Invalid command.");
		return true;
	}

	/**
	 * Allowed only if the session type is set to agent mode.
	 * 
	 * Asks for an account number and checks if it is syntactically correct and
	 * exists in accList. If successful, asks for the account name and checks
	 * that it is syntactically valid. If successful, the account is removed
	 * from the front-end list (accList) to prevent transactions, and adds the
	 * transaction message to tranSummary array.
	 * 
	 * @return true to signify that the user is still logged in.
	 */
	private boolean transactionDelete() {
		// delete transaction is allowed only in agent mode
		if (sessionType == AGENT_MODE) {

			// ask for account number
			System.out.println("Account number?");
			String acc = in.nextLine();

			// check if account number valid and exists in accList
			if (validAccount(acc) && accountExist(acc)) {

				// ask for account name
				System.out.println("Account name?");
				String name = in.nextLine();

				// check if account name is syntactically valid
				if (validName(name)) {

					Iterator<Account> it = accList.iterator();

					// iterate through accList to find and remove account
					while (it.hasNext()) {
						Account a = it.next();
						if (a.getAccNum() == Integer.parseInt(acc))
							it.remove();
					}

					// print and add transaction message to tranSummary
					System.out.println("Account " + acc + " deleted.");
					tranSummary.add(toTransMsg("DL", acc, "", 0, name));
				} else {
					System.out.println("Account already exists");
				}
			} else
				System.out.println("Invalid account number.");
		} else
			System.out.println("Invalid command.");
		return true;
	}

	/**
	 * Asks for an account number and amount to deposit into an account. Adds
	 * the transaction message to tranSummary array if both values are valid.
	 * 
	 * @return true to signify that the user is still logged in.
	 */
	private boolean transactionDeposit() {

		// asks for an account number
		System.out.println("Account number?");
		String acc = in.nextLine();

		// check if account number is valid and exists in accList
		if (validAccount(acc) && accountExist(acc)) {

			// asks for an amount
			System.out.println("Amount?");
			String num = in.nextLine();

			// check if syntax of amount is correct
			if (validAmount(num)) {
				int amount = Integer.parseInt(num);

				// find account
				Account a = findAccount(acc);
				try {

					// check if deposit is valid
					a.deposit(amount);

					System.out.println(num + " deposited into account " + acc + ".");

					// create and add transaction message to tranSummary array
					tranSummary.add(toTransMsg("DE", acc, "", amount, ""));

				} catch (InvalidInput e) {
					System.out.println(e.getMessage());
				}
			} else
				System.out.println("Invalid amount.");
		} else
			System.out.println("Invalid account number.");

		return true;
	}

	/**
	 * Asks for two account numbers and the amount to transfer between the
	 * accounts. Adds the transaction message to tranSummary array if all values
	 * are valid.
	 * 
	 * @return true to signify that the user is still logged in.
	 */
	private boolean transactionTransfer() {

		// account number FROM

		// ask user for account number to transfer from
		System.out.println("Account number?");
		String accNum1 = in.nextLine();

		// return if 'from' account number is NOT valid or does NOT exist
		// continue if valid and exists
		if (!validAccount(accNum1) || !accountExist(accNum1)) {
			System.out.println("Invalid account number.");
			return true;
		}

		// account number TO

		// ask user for account number to transfer to
		System.out.println("Account number?");
		String accNum2 = in.nextLine();

		// return if 'to' account number is NOT valid or does NOT exist;
		// continue if valid and exists
		if (!validAccount(accNum2) || !accountExist(accNum2)) {
			System.out.println("Invalid account number.");
			return true;
		}

		// AMOUNT

		// ask user for AMOUNT to transfer
		System.out.println("Amount?");
		String amountStr = in.nextLine();

		// return if amount is NOT valid; continue if valid
		if (!validAmount(amountStr)) {
			System.out.println("Invalid amount.");
			return true;
		}

		int amountInt = Integer.parseInt(amountStr);

		// find account in accList array and create Account object
		Account acc1 = findAccount(accNum1);
		Account acc2 = findAccount(accNum1);

		try {

			// check if withdraw amount from 'from' account is valid
			// and check if deposit amount into the 'to' account is valid
			acc1.withdraw(amountInt);
			acc2.deposit(amountInt);

			System.out.println("Transferred " + amountInt + " " + accNum1 + " to " + accNum2);

			// create transaction message and add it to tranSummary array
			tranSummary.add(toTransMsg("TR", accNum2, accNum1, amountInt, ""));

		} catch (InvalidInput e) {
			System.out.println(e.getMessage());
		}

		return true;
	}

	/**
	 * Asks for an account number and amount to withdraw from an account. Adds
	 * the transaction message to tranSummary array if both values are valid.
	 * 
	 * @return true to signify the user is still logged in
	 */
	private boolean transactionWithdraw() {

		// ask for account number
		System.out.println("Account number?");
		String acc = in.nextLine();

		// check if account number is valid and in accList
		if (validAccount(acc) && accountExist(acc)) {

			// ask for amount
			System.out.println("Amount?");
			String num = in.nextLine();

			// check if syntax of amount is correct
			if (validAmount(num)) {
				int amount = Integer.parseInt(num);

				// find account
				Account a = findAccount(acc);
				try {

					// check if withdraw is valid and add to session withdraw
					// total
					a.withdraw(amount);
					System.out.println(num + " withdrawn from account " + acc + ".");

					// create and add transaction message to tranSummary array
					tranSummary.add(toTransMsg("WD", "", acc, amount, ""));
				} catch (InvalidInput e) {
					System.out.println(e.getMessage());
				}
			} else
				System.out.println("Invalid amount");
		} else
			System.out.println("Invalid account number.");
		return true;
	}

	/**
	 * Indicate logged out session type, and add end of session transaction to
	 * tranSummary array. Return false to signify user is logging out.
	 * 
	 * Export all transaction summary file messages into a file with the name of
	 * the value of TRANSACTION_SUMMARY_FILENAME. If file doesn't exist, it will
	 * create it. If file already exists, it will overwrite it.
	 * 
	 * @return false to signify the user has logged out
	 */
	private boolean transactionLogout() {
		
		// indicate logged out session type
		sessionType = LOGGED_OUT;

		// add end of session message to tranSummary
		tranSummary.add(toTransMsg("ES", "", "", 0, ""));

		// export tranSummary array into file with the name of value of TRANSACTION_SUMMARY_FILENAME
		// create new the file if it doesn't exist, overwrite it if the file already exists
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_SUMMARY_FILENAME));
			
			// iterate through tranSummary array and write each message to a new line
			for (String str : tranSummary) {
				writer.write(str);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing to file: " + TRANSACTION_SUMMARY_FILENAME);
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("End of session.");
		return false;
	}
}
