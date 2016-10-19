package onk_simBank;

// Add search method for account numbers?
// Why are we returning a string? Can't we just add it to the arrayList and return void?
// How can we prevent the creation of multiple accounts with the same account number?

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Scanner;

public class SimBank {
	static int sessionType = 0; // 0 = not logged in; 1 = atm; 2 = agent
	static ArrayList<String> tranSummary;
	static ArrayList<Account> accList;
	Scanner in = new Scanner(System.in); // new scanner object
	String input; // used for user input

	public void start() {
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
	 * login method
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
	 * Calls transaction functions, according to user input
	 * 
	 * @return false only when logging out
	 */
	private boolean transaction() {
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
			return true; // requests the user to enter a valid transaction
		}// Close switch statement
	}// End transaction method

	/**
	 * Reads in user input for an account number and checks if it exists in
	 * accList. Attempts to create and insert a new Account into the accList if
	 * it does not exist. Throws an InvalidInput exception if it already exists.
	 */
	private boolean transactionCreate() {
		if (sessionType == 2) {
			// get user input
			System.out.println("Account number?");
			String acc = in.nextLine();
			// check if it's valid and unique
			if (validAccount(acc)) {
				if (!accountExist(acc)) {
					System.out.println("Account name?");
					String name = in.nextLine();
					if (validName(name)) {
						System.out.println("Account " + acc + " created.");
						tranSummary.add(toTransMsg("CR", Integer.parseInt(acc), 0, 0, name));
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
	 * Creates a transaction summary line for deletion
	 */
	private boolean transactionDelete() {
		System.out.println("Delete");
		// create delete string
		// return "TT AAA BBB CCCC"; // TO-DO
		return true;
	}

	/**
	 * Deposits an amount into an account
	 * 
	 * @return
	 */
	private boolean transactionDeposit() {
		System.out.println("Deposit");
		// Check arrayList for matching account number - implement a search
		// function?
		// If match found, use Account to try deposit - success deposit return
		// transaction message
		// If match not found, print error and return to transaction()
		// return "TT AAA BBB CCCC"; // TO-DO
		return true;
	}

	private boolean transactionTransfer() {
		System.out.println("Transfer");
		// return "TT AAA BBB CCCC"; // TO-DO
		return true;
	}

	private boolean transactionWithdraw() {
		System.out.println("Withdraw");
		// return "TT AAA BBB CCCC"; // TO-DO
		return true;
	}

	/**
	 * 
	 * @return
	 */
	private boolean transactionLogout() {
		sessionType = 0;
		System.out.println("End of session.");
		return false;
	}

	public boolean accountExist(String acc) {
		int accNum = Integer.parseInt(acc);
		for (Account a : accList) {
			if (accNum == a.getAccNum())
				return true;
		}
		return false;
	}

	/**
	 * Checks if account number is valid.
	 * 
	 * @param acc
	 * @return
	 */
	public boolean validAccount(String acc) {
		if (acc.matches("[0-9]+") && (acc.length() == 8)
				&& !acc.startsWith("0"))
			return true;
		else
			return false;
	}

	/**
	 * Checks if account name is valid.
	 * 
	 * @param name
	 * @return
	 */
	public boolean validName(String name) {
		if (name.length() >= 3 && name.length() <= 30 && !name.startsWith(" ")
				&& !name.endsWith(" "))
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
	 *            enter 0
	 * @param accNum2
	 *            is the second (from) account number. Account numbers are
	 *            always exactly eight decimal digits, not beginning with 0. If
	 *            unused, enter 0
	 * @param amount
	 *            is the amount in cents. If unused, enter 0
	 * @param name
	 *            is the account name. If unused, enter empty string ""
	 * @return a string following the format of the Transaction Summary File
	 */
	private static String toTransMsg(String tranCode, int accNum1, int accNum2,
			int amount, String name) {
		// if transaction code is not 2 characters, throw exception
		if (tranCode.length() != 2)
			System.out.println("ERROR: Transaction code is not the correct length.");
		
		String accNum1String, accNum2String, amountString;
		// convert accNum1 to a strong
		if (accNum1 == 0)
			accNum1String = "00000000";
		else
			accNum1String = String.valueOf(accNum1);

		// convert accNum2 to a string
		if (accNum2 == 0)
			accNum2String = "00000000";
		else
			accNum2String = String.valueOf(accNum2);

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
		if (name == "")
			name = "***";
		
		// creates string using parameters
		String s = tranCode + " " + accNum1String + " " + accNum2String + " "
				+ amountString + " " + name;
		return s;
	}

	public static int getSessionType() {
		return sessionType;
	}

}
