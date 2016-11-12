package backEnd;

import java.io.*;
import java.util.ArrayList;

/**
 * Reads the merged transaction summary file and old master accounts file, and
 * apply the transactions to the appropriate accounts. Transactions are applied
 * if the values are valid and the constraints are met. If there are invalid
 * values or the transaction fails to meet the constraints, the transaction
 * fails and the program produces an error message before terminating. If all
 * transactions are successfully processed, a new master accounts file and valid
 * accounts file are output.
 * 
 * transSumFilename and masterAccListFilename are the name of the transaction
 * summary file and master account list file respectively. localTranSum is the
 * list of all the transactions message to be processes. localMasterAccList is
 * the list of all the accounts currently in use.
 * 
 * @author onk_Team
 *
 */
public class BackEnd {
	private static String transSumFilename;
	private static String masterAccListFilename;
	private static ArrayList<String> localTranSum = new ArrayList<String>();
	private static ArrayList<Account> localMasterAccList = new ArrayList<Account>();

	/**
	 * Reads through transaction summary file and applies transaction to the
	 * appropriate accounts in the master account list. Outputs the new master
	 * account list and valid account list
	 * 
	 * @param args
	 *            the filenames for the merged transaction summary file and
	 *            master account list file respectively.
	 */
	public static void main(String[] args) {
		transSumFilename = args[0];
		masterAccListFilename = args[1];

		// reads and saves the transaction summary file and master account list
		// into an array list
		try {
			tranSumToArrayList(transSumFilename);
			masterAccListToArrayList(masterAccListFilename);
		} catch (Exception e) {
			crash("Error creating arraylist from files.");
		}

		// apply transactions to the appropriate accounts
		doTransaction();

		// create new master account file and valid account file
		try {
			createMasterAccList();
			createNewValidAccList();
		} catch (Exception e) {
			crash("Error writing to file.");
		}
	}

	/**
	 * Reads transaction summary file and converts it into an array list for
	 * easy readability of lines of text
	 * 
	 * @param filename
	 *            the name of the merged transaction summary file
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void tranSumToArrayList(String filename)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "Cp1252"));
		String line;
		while ((line = br.readLine()) != null) {
			localTranSum.add(line);
		}
		br.close();
	}

	/**
	 * Reads master account list file and converts it into an array list of
	 * account objects for easy manipulation of attributes. Attributes for each
	 * account is set according to the information in the master account list
	 * file.
	 * 
	 * @param filename
	 *            the name of the master account list file
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void masterAccListToArrayList(String filename)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "Cp1252"));
		String line;
		while ((line = br.readLine()) != null) {
			String[] info = line.split(" ");
			// create account object
			Account a = new Account(Integer.parseInt(info[0]), Integer.parseInt(info[1]), info[2]);
			localMasterAccList.add(a);
		}
		br.close();
	}

	/**
	 * Creates the new master account list file from accounts and its attributes
	 * in the localMasterAccList.
	 * 
	 * @param None
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static void createMasterAccList() throws FileNotFoundException, UnsupportedEncodingException {
		// create/overwrite file
		PrintWriter w = new PrintWriter(masterAccListFilename, "UTF-8");
		for (Account a : localMasterAccList) {
			String s = Integer.toString(a.getAccNum()) + " " + Integer.toString(a.getBalance()) + " " + a.getName();
			w.println(s);
		}
		w.close();
	}

	/**
	 * Creates the new valid account list file from the accounts in the
	 * localMasterAccList.
	 * 
	 * @param None
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static void createNewValidAccList() throws FileNotFoundException, UnsupportedEncodingException {
		// create/overwrite file
		PrintWriter w = new PrintWriter("validAccList.txt", "UTF-8");
		for (Account a : localMasterAccList) {
			String s = Integer.toString(a.getAccNum());
			w.println(s);
		}
		w.close();
	}

	/**
	 * Apply the transactions listed in localTranSum to the accounts listed in
	 * the localMasterAccList. Values are checked for validity before
	 * transactions are processed, and attributes of accounts and accounts in
	 * localMasterAccList are updated. Invalid transactions outputs a message
	 * and terminates the program.
	 * 
	 * @param None
	 */
	private static void doTransaction() {
		// loop through each element in tranSum array
		for (String t : localTranSum) {
			String[] tranMsg = t.split(" ");
			// tranMsg[0] is transaction code
			int toAccNum = Integer.parseInt(tranMsg[1]);
			int fromAccNum = Integer.parseInt(tranMsg[2]);
			int amount = Integer.parseInt(tranMsg[3]);
			// tranMsg[4] is accName

			switch (tranMsg[0]) {
			case "DE": // deposit
				deposit(toAccNum, amount);
				break;
			case "WD": // withdraw
				withdraw(fromAccNum, amount);
				break;
			case "TR": // transfer
				transfer(toAccNum, fromAccNum, amount);
				break;
			case "CR": // create
				create(toAccNum, tranMsg[4]);
				break;
			case "DL": // delete
				delete(toAccNum, tranMsg[4]);
				break;
			case "ES": // end of session
				// ignores the ES line
				break;
			default:
				System.out.println("Transaction summary code is incorrect.");
				System.exit(1);
			}
		}
	}

	/**
	 * Checks the local master account list to see if the account already
	 * exists. If it does not already exist, it adds the account to the internal
	 * copy of the master accounts list. If it does exist, log a fatal error and
	 * stop the program.
	 * 
	 * @param accNum
	 *            the account number of the new account
	 * @param accName
	 *            the account name of the new account
	 */
	private static void create(int accNum, String accName) {
		// account exists
		if (findAccount(accNum) == null) {
			localMasterAccList.add(new Account(accNum, accName));
		} else {
			crash("Account already exists.");
		}
	}

	/**
	 * Checks the local master account list to see if the account exists. If the
	 * account exists, it deletes the account if the account balance is 0 and if
	 * the input name matches the account name. If the account doesn’t exist,
	 * the account balance is not 0, or the account name does not match, log a
	 * fatal error and stop the program.
	 * 
	 * @param accNum
	 *            the account number to delete
	 * @param accName
	 *            the account name to delete
	 */
	private static void delete(int accNum, String accName) {
		Account a = findAccount(accNum);
		// account does not exist
		if (a != null) {
			if (a.getBalance() == 0 && a.getName().equals(accName)) {
				localMasterAccList.remove(a);
			} else {
				crash("Delete conditions not met.");
			}
		} else {
			crash("Account does not exist.");
		}
	}

	/**
	 * Accepts an account number and an amount to withdraw, and checks the local
	 * master account list to see if the account exists. If the account exists,
	 * it checks if the withdrawn amount does not exceed the current balance in
	 * the account. If the account does not exist or the amount withdrawn
	 * exceeds the current balance in the account, log a fatal error and stop
	 * the program. A successful withdraw transaction will update the account
	 * balance according to the withdrawn amount.
	 * 
	 * @param accNum
	 *            the account number of the account to withdraw from
	 * @param amount
	 *            the amount to withdraw from the account
	 */
	private static void withdraw(int accNum, int amount) {
		Account a = findAccount(accNum);
		// account exists
		if (a != null) {
			int newBalance = a.getBalance() - amount;
			if (newBalance >= 0) {
				a.setBalance(newBalance);
			} else {
				crash("Withdrawing amount exceeds current balance.");
			}
		} else {
			crash("Account does not exist.");
		}
	}

	/**
	 * Accepts an account number and an amount to deposit, and checks the local
	 * master account list to see if the account exists. If the account exists,
	 * it checks if the deposit amount does not exceed the $999,999.99. If the
	 * account does not exist or the amount deposited exceeds $999,999.99, log a
	 * fatal error and stop the program. A successful deposit transaction will
	 * update the account balance according to the deposited amount.
	 * 
	 * @param accNum
	 *            the account number to deposit to
	 * @param amount
	 *            the amount to withdraw to the account
	 */
	private static void deposit(int accNum, int amount) {
		Account a = findAccount(accNum);
		if (a != null) {
			int newBalance = a.getBalance() + amount;
			if (newBalance < 99999999) {
				a.setBalance(newBalance);
			} else {
				crash("Deposit amount exceeds $999,999.99");
			}
		} else {
			crash("Account does not exist.");
		}
	}

	/**
	 * Checks the local master account list to see if both accounts exists. If
	 * both accounts exists, transfer the amount from one account to another if
	 * the balance of neither accounts will become negative or exceed
	 * $999,999.99. Since transfer is the junction of deposit and withdraw, the
	 * transfer transaction is processed by calling both method. A successful
	 * transfer transaction will update the account balances of both accounts
	 * according to the transferred amount.
	 * 
	 * @param toAccNum
	 *            the account number to deposit to
	 * @param fromAccNum
	 *            the account number to withdraw from
	 * @param amount
	 *            the amount to transfer from one account to the other
	 */
	private static void transfer(int toAccNum, int fromAccNum, int amount) {
		withdraw(fromAccNum, amount);
		deposit(toAccNum, amount);
	}

	/**
	 * Finds and returns an account if the account exists, and null otherwise.
	 * 
	 * @param accNum
	 *            the account number of the account to find
	 * @return the corresponding account if found, null otherwise
	 */
	private static Account findAccount(int accNum) {
		for (Account a : localMasterAccList) {
			if (accNum == a.getAccNum())
				return a; // account exists
		}
		return null; // account does not exist
	}

	/**
	 * Prints an error message to console and exits the program.
	 * 
	 * @param msg
	 */
	private static void crash(String msg) {
		System.out.println(msg);
		System.exit(1);
	}

}
