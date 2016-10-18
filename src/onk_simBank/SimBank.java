package onk_simBank;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Scanner;

public class SimBank {
	int sessionType = 0; // 0 = not logged in; 1 = atm; 2 = agent
	ArrayList<String> tranSummary;
	ArrayList<Account> accList;

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
			Scanner reader = new Scanner(System.in); // new scanner object
			String input;
			// login
			out.println("Please login:");
			input = reader.nextLine();
			// check for valid input
			if (input == "login") {
				out.println("atm or agent?");
				input = reader.nextLine();
				if (input == "atm") {
					sessionType = 1;
					out.println("Logged in as atm.");
				} else if (input == "agent") {
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
	 * Function that runs transaction methods
	 */
	private void transaction() {
		out.println("logout? Transaction?");
		
		switch (input) {
		case "create":

		case "delete":

		case "deposit":

		case "logout":

		case "transfer":

		case "withdraw":
		}

	}

	private void create() {

	}

	private void delete() {

	}

	private void deposit() {

	}

	private void logout() {

	}

	private void transfer() {

	}

	private void withdraw() {

	}
}
