package onk_simBank;

// Why are we returning a string? Can't we just add it to the arrayList and return void?
// How can we prevent the creation of multiple accounts with the same account number?

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class SimBank {
	static int sessionType = 0; // 0 = not logged in; 1 = atm; 2 = agent
	ArrayList<String> tranSummary;
	ArrayList<Account> accList;
	Scanner in = new Scanner(System.in); // new scanner object
	String input;

	public void start() {
		accList = new ArrayList<Account>();
		tranSummary = new ArrayList<String>();
		// import account list into accList array
		Account one = new Account(12345678);
		accList.add(one);
		// run login script
		login();
		while(true)
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
	private boolean transaction() {
		// While loop used for repeated prompt after invalid command.
		out.println("logout? Transaction?");
		input = in.nextLine();
		switch(input){
		case "create":
			return transactionCreate();
		case "delete":
			transactionDelete();
			break;
		case "deposit":
			transactionDeposit();
			break;
		case "logout":
			transactionLogout();
			break;
		case "transfer":
			transactionTransfer();
			break;
		case "withdraw":
			transactionWithdraw();
			break;
			// None of the options above inputed
		default:
			System.out.println("Invalid command");
		}// Close switch statement
		return false;
	}// End transaction method

	/**
	 * Reads in user input for an account number and checks if it exists in accList.
	 * If account creation is successful, transaction message added to transSummary.
	 * New account not added to accList to prevent transactions on new account.
	 */
	private boolean transactionCreate(){
		//Create allowed only in agent mode
		if(sessionType == 2){
			// get user input
			System.out.println("Account number?");
			String acc = in.nextLine();
			// check if it's valid and unique
			if(validAccount(acc)){
				if(!accountExist(acc)){
					System.out.println("Account name?");
					String name = in.nextLine();
					//check valid account name
					if(validName(name)){
						System.out.println("Account " + acc + " created.");
						String transMessage = "CR " + acc + "00000000 000 " + name;
						tranSummary.add(transMessage);
					}else
						System.out.println("Invalid account name");
				}
				else{
					System.out.println("Account already exists");
				}
			} else
				System.out.println("Invalid account number.");
		} else
			System.out.println("Invalid command.");
		return true;
	}

	/**
	 * Reads in user input for an account number and checks if it exists in accList.
	 * If account number is in accList, the account is removed from the front-end list to prevent transactions.
	 * Transaction message for deletion of the account is added to tranSummary.
	 */
	private boolean transactionDelete() {
		//Delete allowed only in agent mode
		if(sessionType == 2){
			// get user input
			System.out.println("Account number?");
			String acc = in.nextLine();
			// check if it's valid and in accList
			if(validAccount(acc) && accountExist(acc)){
				System.out.println("Account name?");
				String name = in.nextLine();
				//check valid account name
				if(validName(name)){
					Iterator<Account> it = accList.iterator();
					// iterate through accList to find and remove account
					while(it.hasNext()){
						Account a = it.next();
						if(a.getAccNum() == Integer.parseInt(acc))
							it.remove();
					}// close while-loop
					System.out.println("Account " + acc + " deleted.");
					String transMessage = "DL " + acc + "00000000 000 " + name;
					tranSummary.add(transMessage);
				}
				else{
					System.out.println("Account already exists");
				}
			} else
				System.out.println("Invalid account number.");
		} else
			System.out.println("Invalid command.");
		return true;
		// create delete string
		//return "TT AAA BBB CCCC"; // TO-DO
	}

	/**
	 * Deposits an amount into an account
	 * 
	 * @return
	 */
	private boolean transactionDeposit() {
		System.out.println("Deposit");
		// Check arrayList for matching account number - implement a search function?
		// If match found, use Account to try deposit - success deposit return transaction message
		// If match not found, print error and return to transaction()
		//return "TT AAA BBB CCCC"; // TO-DO
		return true;
	}

	/**
	 * 
	 * @return
	 */
	private boolean transactionLogout() {
		System.out.println("Logout");
		//return "TT AAA BBB CCCC"; // TO-DO
		return true;
	}

	private boolean transactionTransfer() {
		System.out.println("Transfer");
		//return "TT AAA BBB CCCC"; // TO-DO
		return true;
	}

	private boolean transactionWithdraw() {
		System.out.println("Withdraw");
		//return "TT AAA BBB CCCC"; // TO-DO
		return true;
	}


	public boolean accountExist(String acc){
		int accNum = Integer.parseInt(acc);
		for(Account a:accList){
			if(accNum == a.getAccNum())
				return true;
		}
		return false;
	}

	/**
	 * Checks if account number is valid.
	 * @param acc
	 * @return
	 */
	public boolean validAccount(String acc){
		if(acc.matches("[0-9]+") && (acc.length() == 8) && !acc.startsWith("0"))
			return true;
		else
			return false;
	}

	/**
	 * Checks if account name is valid.
	 * @param name
	 * @return
	 */
	public boolean validName(String name){
		if(name.length()>= 3 && name.length() <= 30 && !name.startsWith(" ") && !name.endsWith(" "))
			return true;
		else
			return false;
	}

	public static int getSessionType() {
		return sessionType;
	}

}
