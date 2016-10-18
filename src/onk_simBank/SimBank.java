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
			create();
		case "delete":
			delete();
		case "deposit":
			deposit();
		case "logout":
			logout();
		case "transfer":
			transfer();
		case "withdraw":
			withdraw();
		}

	}

	/**
	 * Creates a temporary account object, and confirms that there's no conflict
	 * before inserting the account object into the list
	 */
	private String create() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	/**
	 * Creates a transaction summary line for deletion
	 */
	private String delete() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	private String deposit() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	private String logout() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	private String transfer() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	private String withdraw() {
		return "TT AAA BBB CCCC"; // TO-DO
	}

	public int getSessionType() {
		return sessionType;
	}
}
