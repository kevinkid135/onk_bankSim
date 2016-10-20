package onk_simBank;

// Why are we returning a string? Can't we just add it to the arrayList and return void?
// How can we prevent the creation of multiple accounts with the same account number?

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class SimBank {
	static int sessionType = 0; // 0 = not logged in; 1 = atm; 2 = agent
	static ArrayList<String> tranSummary;
	static ArrayList<Account> accList;
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
		accList = new ArrayList<Account>();
		tranSummary = new ArrayList<String>();
		// import account list into accList array
		Account one = new Account(12345678);
		accList.add(one);
		// run login script
		login();
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
	private static String toTransMsg(String tranCode, String accNum1,
			String accNum2, int amount, String name) {
		// if transaction code is not 2 characters, print error message
		if (tranCode.length() != 2)
			System.out
					.println("ERROR: Transaction code is not the correct length.");

		// check if any accNum are not used
		if (accNum1.isEmpty())
			accNum1 = "00000000";
		if (accNum2.isEmpty())
			accNum2 = "00000000";

		// check if name parameter is not used
		if (name.isEmpty())
			name = "***";

		// creates string using parameters
		String s = tranCode + " " + accNum1 + " " + accNum2 + " " + amount
				+ " " + name;
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
	 * prompts user to login, and enter atm or agent. Also check whether or not
	 * inputs are valid.
	 */
	private void login() {
		do {
			// login
			out.println("Please login:");
			input = in.nextLine();
			// check for valid input
			if (input.equals("login")) {
				while (true) { // exits loop once a valid input detected
					out.println("atm or agent?");
					input = in.nextLine();
					if (input.equals("atm")) {
						sessionType = 1;
						out.println("Logged in as atm.");
						break;
					} else if (input.equals("agent")) {
						sessionType = 2;
						out.println("Logged in as agent.");
						break;
					}
				}
			}
		} while (sessionType == 0);
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
		if (sessionType == 2) {
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
		if (sessionType == 2) {
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
					System.out.println(num + " deposited into account " + acc
							+ ".");
					
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

		// ask user for account number to transfer FROM
		System.out.println("Account number?");
		String accNum1 = in.nextLine();

		// return if 'from' account number is NOT valid or does NOT exist
		// continue if valid and exists
		if (!validAccount(accNum1) || !accountExist(accNum1)) {
			System.out.println("Invalid account number.");
			return true;
		}

		// ask user for account number to transfer TO
		System.out.println("Account number?");
		String accNum2 = in.nextLine();

		// return if 'to' account number is NOT valid or does NOT exist;
		// continue if valid and exists
		if (!validAccount(accNum2) || !accountExist(accNum2)) {
			System.out.println("Invalid account number.");
			return true;
		}

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
			// String transMessage = toTransMsg(); // UNFINISHED; need to get
			// the updates

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
		System.out.println("Withdraw"); // delete once finished

		System.out.println("Account number?");
		String acc = in.nextLine();

		// KEVIN TO DO

		// validAccount()
		if (validAccount(acc)) {

		}
		// validAmount()
		// do transaction

		return true;
	}

	/**
	 * log out of session
	 * 
	 * @return false to signify the user has logged out
	 */
	private boolean transactionLogout() {
		sessionType = 0;
		System.out.println("End of session.");
		return false;
	}
}
