package onk_simBank;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Scanner;

public class SimBank {
	int sessionType = 0; // 0 = not logged in; 1 = atm; 2 = agent
	ArrayList<String> tranSummary;
	ArrayList<Account> accList;
	Scanner in = new Scanner(System.in); // new scanner object
	String input;

	public void start() {
		// import account list into accList array

		// run login script
		login();

		transaction();
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
				out.println("atm or agent?");
				input = in.nextLine();
				if (input.equals("atm")) {
					sessionType = 1;
					out.println("Logged in as atm.");
				} else if (input.equals("agent")) {
					sessionType = 2;
					out.println("Logged in as agent.");
				} else {
					// invalid input
				}

			} else {
				// invalid input
			}
		} while (sessionType == 0);
	}

	/**
	 * Method that runs method according to user input
	 */
	private void transaction() {
		out.println("logout? Transaction?");
		input = in.nextLine();
		switch (input) {
		case "create":
			transactionCreate();
		case "delete":
			transactionDelete();
		case "deposit":
			transactionDeposit();
		case "logout":
			transactionLogout();
		case "transfer":
			transactionTransfer();
		case "withdraw":
			transactionWithdraw();
		}

	}

	/**
	 * Reads in user input for an account number and checks if it exists in accList.
	 * Attempts to create and insert a new Account into the accList if it does not exist.
	 * Throws an InvalidInput exception if it already exists.
	 */
	private String transactionCreate() {
		// get user input
		
		// check if it's valid (here or in account?)
		
		// check if it already exists (here or in account?)
		
		// throw InvalidInput exception if it doesn't exist
		
		return "TT AAA BBB CCCC"; // TO-DO
	}

	/**
	 * Creates a transaction summary line for deletion
	 */
	private String transactionDelete() {
		// create delete string
		return "TT AAA BBB CCCC"; // TO-DO
	}

	/**
	 * Deposits an amount into an account
	 * 
	 * @return
	 */
	private String transactionDeposit() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	/**
	 * 
	 * @return
	 */
	private String transactionLogout() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	private String transactionTransfer() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	private String transactionWithdraw() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	public int getSessionType() {
		return sessionType;
	}
}
