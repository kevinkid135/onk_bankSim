package onk_simBank;

// don't have to type System before every output
import static java.lang.System.out;

// file input outputs, and arraylist
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
 * Inputs: Valid accounts list file with filename corresponding to value of ACCOUNT_LIST_FILENAME
 * Outputs: Transaction summary file with filename corresponding to value of TRANSACTION_SUMMARY_FILENAME
 * 
 * @author Team onk
 *
 */
public class SimBank {
	static final int LOGGED_OUT = 0;
	static final int ATM_MODE = 1;
	static final int AGENT_MODE = 2;
	static int sessionType = LOGGED_OUT;

	static ArrayList<Account> accList;
	final String ACCOUNT_LIST_FILENAME = "accountList.txt"; // filename of valid accounts list file

	static ArrayList<String> tranSummary;
	final String TRANSACTION_SUMMARY_FILENAME = "tranSum.txt"; // filename of transaction summary file

	Scanner in = new Scanner(System.in); // new scanner object
	String input; // used for user input

	/**
	 * Starts the session by importing the accounList file, into an array. Also
	 * Initializes the tranSummary array to be placed into the Transaction
	 * Summary file. Proceeds to ask user to login and handles the transactions
	 * depending on what state the session is in.
	 * 
	 * @throws InvalidInput
	 */
	public void start() throws InvalidInput {

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

		boolean loggedIn = true;
		while (loggedIn) {
			loggedIn = transaction();
		}
	}

	/**
	 * Check if an account already exists in accList
	 * 
	 * @param acc
	 *            is the account number to be checked
	 * @return true if it already exists, and false if it does not exist
	 */
	public boolean accountExist(String acc) {
		int accNum = Integer.parseInt(acc);
		for (Account a : accList) {
			if (accNum == a.getAccNum())
				return true;
		}
		return false;
	}

	/**
	 * Checks if account number is valid syntactically
	 * 
	 * @param acc
	 * @return
	 */
	public boolean validAccount(String acc) {
		if (acc.matches("[0-9]+") && (acc.length() == 8) && !acc.startsWith("0"))
			return true;
		else
			return false;
	}

	/**
	 * Finds if the account is in the accList array
	 * 
	 * @param accNum
	 * @return true if found, and false if not found
	 */
	public Account findAccount(String accNum) {
		int acc = Integer.parseInt(accNum);
		for (Account a : accList) {
			if (a.getAccNum() == acc)
				return a;
		}
		return null;
	}

	/**
	 * Checks if account name is valid syntactically
	 * 
	 * @param name
	 * @return
	 */
	public boolean validName(String name) {
		if (name.length() >= 3 && name.length() <= 30 && !name.startsWith(" ") && !name.endsWith(" "))
			return true;
		else
			return false;
	}

	/**
	 * Checks if the amount entered is valid syntactically
	 * 
	 * @param amount
	 * @return
	 */
	public boolean validAmount(String amount) {
		if (amount.matches("[0-9]+") && (amount.length() >= 3) && (amount.length() <= 8))
			return true;
		else
			return false;
	}

	/**
	 * Converts the transaction command into a string following the format of
	 * the Transaction Summary File.
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
	 * @return a string following the format of the Transaction Summary File
	 */
	private static String toTransMsg(String tranCode, String accNum1, String accNum2, int amount, String name) {
		String amountString = "";

		// if transaction code is not 2 characters, print error message
		if (tranCode.length() != 2)
			System.out.println("ERROR: Transaction code is not the correct length.");

		// check if any accNum are not used
		if (accNum1.isEmpty())
			accNum1 = "00000000";
		if (accNum2.isEmpty())
			accNum2 = "00000000";

		// pad the amount
		// convert amount to a string
		if (amount == 0)
			amountString = "000";
		else if (amount < 10)
			amountString = "00" + String.valueOf(amount);
		else if (amount < 100)
			amountString = "0" + String.valueOf(amount);
		else
			amountString = String.valueOf(amount);

		// check if name parameter is not used
		if (name.isEmpty())
			name = "***";

		// creates string using parameters
		String s = tranCode + " " + accNum1 + " " + accNum2 + " " + amountString + " " + name;
		return s;
	}

	/**
	 * Signifies what state the current session is. 0 for not logged in, 1 for
	 * atm, 2 for agent.
	 * 
	 * @return sessionType.
	 */
	public static int getSessionType() {
		return sessionType;
	}

	/**
	 * Calls transaction functions, according to user input
	 * 
	 * @return false only when logging out
	 * @throws InvalidInput
	 */
	private boolean transaction() throws InvalidInput {
		// While loop used for repeated prompt after invalid command.
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
	 * Asks for the session type (atm or agent mode) until valid input is
	 * entered, and reads in the valid accounts file in accList and initializes
	 * the tranSummary array
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
	 * Reads in user input for an account number and checks if it exists in
	 * accList. If account creation is successful, transaction message added to
	 * transSummary. New account not added to accList to prevent transactions on
	 * new account.
	 * 
	 * @return true to signify that the user is still logged in.
	 */
	private boolean transactionCreate() {
		// Create allowed only in agent mode
		if (sessionType == AGENT_MODE) {
			// get user input
			System.out.println("Account number?");
			String acc = in.nextLine();
			// check if it's valid and unique
			if (validAccount(acc)) {
				if (!accountExist(acc)) {
					System.out.println("Account name?");
					String name = in.nextLine();
					// check valid account name
					if (validName(name)) {
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
	 * Reads in user input for an account number and checks if it exists in
	 * accList. If account number is in accList, the account is removed from the
	 * front-end list to prevent transactions. Transaction message for deletion
	 * of the account is added to tranSummary.
	 * 
	 * @return true to signify that the user is still logged in.
	 */
	private boolean transactionDelete() {
		// Delete allowed only in agent mode
		if (sessionType == AGENT_MODE) {
			// get user input
			System.out.println("Account number?");
			String acc = in.nextLine();
			// check if it's valid and in accList
			if (validAccount(acc) && accountExist(acc)) {
				System.out.println("Account name?");
				String name = in.nextLine();
				// check valid account name
				if (validName(name)) {
					Iterator<Account> it = accList.iterator();
					// iterate through accList to find and remove account
					while (it.hasNext()) {
						Account a = it.next();
						if (a.getAccNum() == Integer.parseInt(acc))
							it.remove();
					} // close while-loop
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
	 * Deposits an amount into an account
	 * 
	 * @return true to signify that the user is still logged in.
	 * @throws InvalidInput
	 */
	private boolean transactionDeposit() throws InvalidInput {

		System.out.println("Account number?");
		String acc = in.nextLine();

		// check if it's valid and in accList
		if (validAccount(acc) && accountExist(acc)) {

			System.out.println("Amount?");
			String num = in.nextLine();

			// check if syntax of amount is appropriate
			if (validAmount(num)) {
				int amount = Integer.parseInt(num);

				// find account and check if deposit is valid
				Account a = findAccount(acc);
				try {

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
	 * Transfers an amount from one account to another
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

			// withdraw amount from 'from' account
			// and deposit the amount into the 'to' account
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
	 * Withdraw from account
	 * 
	 * @return true to signify the user is still logged in
	 */
	private boolean transactionWithdraw() {
		System.out.println("Account number?");
		String acc = in.nextLine();

		// check if it's valid and in accList
		if (validAccount(acc) && accountExist(acc)) {

			System.out.println("Amount?");
			String num = in.nextLine();

			// check if syntax of amount is appropriate
			if (validAmount(num)) {
				int amount = Integer.parseInt(num);

				// find account and check if withdraw is valid
				Account a = findAccount(acc);
				try {

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
	 * log out of session and export tranSummary array to a new file. If file
	 * doesn't exist, it will create it. If file already exists, it will
	 * overwrite it.
	 * 
	 * @return false to signify the user has logged out
	 */
	private boolean transactionLogout() {
		sessionType = LOGGED_OUT;

		// add end of session to transSummary
		tranSummary.add(toTransMsg("ES", "", "", 0, ""));

		// export tranSum array into textfile
		// overwrites if file already exists
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_SUMMARY_FILENAME));
			for (String str : tranSummary) {
				writer.write(str);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("error writing to file: " + TRANSACTION_SUMMARY_FILENAME);
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("End of session.");
		return false;
	}
}
