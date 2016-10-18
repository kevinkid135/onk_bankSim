package onk_simBank;

// Add search method for account numbers?
// How can we prevent the creation of multiple accounts with the same account number?

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Scanner;

public class SimBank {
	static int sessionType = 0; // 0 = not logged in; 1 = atm; 2 = agent
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
		// While loop used for repeated prompt after invalid command.
		boolean command = false;
		while(!command){
			out.println("logout? Transaction?");
			input = in.nextLine();
			switch(input){
			case "create":
				transactionCreate();
				command = true;
				break;
			case "delete":
				transactionDelete();
				command = true;
				break;
			case "deposit":
				transactionDeposit();
				command = true;
				break;
			case "logout":
				transactionLogout();
				command = true;
				break;
			case "transfer":
				transactionTransfer();
				command = true;
				break;
			case "withdraw":
				transactionWithdraw();
				command = true;
				break;
				// None of the options above inputed
			default:
				System.out.println("Invalid command");
			}// Close switch statement
		}//end while-loop
	}// End transaction method

	/**
	 * Reads in user input for an account number and checks if it exists in accList.
	 * Attempts to create and insert a new Account into the accList if it does not exist.
	 * Throws an InvalidInput exception if it already exists.
	 */
	private String transactionCreate() {
		if(sessionType == 2){
			// get user input
			out.println("Account number?");
			// check if it's valid (8 digits, does not start with 0) - throw InvalidInput if not valid

			// check if it already exists - throw Invalid Input if it exists

			// If valid and unique, create transaction message (Add to account list? How to prevent transaction?)

			return "TT AAA BBB CCCC"; // TO-DO
		}else{
			System.out.println("Invalid command.");
			return null;
		}
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
		int accNum;
		int amount;
		out.println("Account number?");
		input = in.nextLine();
		// Check arrayList for matching account number - implement a search function?
		// If match found, use Account to try deposit - success deposit return transaction message
		// If match not found, print error and return to transaction()
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

	public static int getSessionType() {
		return sessionType;
	}
}
