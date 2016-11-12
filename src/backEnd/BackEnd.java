package backEnd;

import java.io.*;
import java.util.ArrayList;

/**
 * Applies transactions to master account list
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
	 * Reads through transaction summary file and applies transaction to master
	 * account list. Outputs new master account list and valid account list
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		transSumFilename = args[0];
		masterAccListFilename = args[1];

		try {
			tranSumToArrayList(transSumFilename);
			masterAccListToArrayList(masterAccListFilename);
		} catch (Exception e) {
			crash("Error creating arraylist from files.");
		}

		// loop through tranSum array
		for (String t : localTranSum) {
			String[] tranMsg = t.split(" ");
			// tranMsg[0] is transaction code
			int toAccNum = Integer.parseInt(tranMsg[1]);
			int fromAccNum = Integer.parseInt(tranMsg[2]);
			int amount = Integer.parseInt(tranMsg[3]);
			// tranMsg[4] is accName

			switch (tranMsg[0]) {
			// if transaction code is deposit
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

		// create new master account file using localMasterAccList
		try {
			createMasterAccList();
			createNewValidAccList();
		} catch (Exception e) {
			crash("Error writing to file.");
		}
	}

	/**
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static void createNewValidAccList() throws FileNotFoundException,
			UnsupportedEncodingException {
		// create/overwrite file
		PrintWriter w = new PrintWriter("validAccList.txt", "UTF-8");
		for (Account a : localMasterAccList) {
			String s = Integer.toString(a.getAccNum());
			w.println(s);
		}
		w.close();
	}

	/**
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static void createMasterAccList() throws FileNotFoundException,
			UnsupportedEncodingException {
		// create/overwrite file
		PrintWriter w = new PrintWriter(masterAccListFilename, "UTF-8");
		for (Account a : localMasterAccList) {
			String s = Integer.toString(a.getAccNum()) + " "
					+ Integer.toString(a.getBalance()) + " " + a.getName();
			w.println(s);
		}
		w.close();
	}

	/**
	 * Reads master account list and converts it into an arraylist of account
	 * objects for easy manipulation of attributes
	 * 
	 * @param filename
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void masterAccListToArrayList(String filename)
			throws UnsupportedEncodingException, FileNotFoundException,
			IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename), "Cp1252"));
		String line;
		while ((line = br.readLine()) != null) {
			String[] info = line.split(" ");
			// create account object
			Account a = new Account(Integer.parseInt(info[0]),
					Integer.parseInt(info[1]), info[2]);
			localMasterAccList.add(a);
		}
		br.close();
	}

	/**
	 * Reads transaction summary and converts it into an arryalist for easy
	 * readability of lines of text
	 * 
	 * @param filename
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void tranSumToArrayList(String filename)
			throws UnsupportedEncodingException, FileNotFoundException,
			IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename), "Cp1252"));
		String line;
		while ((line = br.readLine()) != null) {
			localTranSum.add(line);
		}
		br.close();
	}

	/**
	 * Creates an account if it does not already exist in the local master
	 * account list
	 * 
	 * @param accNum
	 * @param accName
	 */
	public static void create(int accNum, String accName) {
		// account exists
		if (findAccount(accNum) == null) {
			localMasterAccList.add(new Account(accNum, accName));
		} else {
			crash("Account already exists.");
		}
	}

	/**
	 * Delete account if account exists, balance is 0, and account name matches
	 * 
	 * @param accNum
	 * @param accName
	 */
	public static void delete(int accNum, String accName) {
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
	 * Withdraws an amount from the account and updates balance
	 * 
	 * @param accNum
	 * @param amount
	 */
	public static void withdraw(int accNum, int amount) {
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
	 * Deposit amount into account
	 * 
	 * @param accNum
	 * @param amount
	 */
	public static void deposit(int accNum, int amount) {
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
	 * Transfer a certain amount from one account to another
	 * 
	 * @param toAccNum
	 * @param fromAccNum
	 * @param amount
	 */
	public static void transfer(int toAccNum, int fromAccNum, int amount) {
		withdraw(fromAccNum, amount);
		deposit(toAccNum, amount);
	}

	/**
	 * Determine if the account exists and returns account if it exists, and
	 * null otherwise
	 * 
	 * @param accNum
	 * @return
	 */
	public static Account findAccount(int accNum) {
		for (Account a : localMasterAccList) {
			if (accNum == a.getAccNum())
				return a; // account exists
		}
		return null; // account does not exist
	}

	/**
	 * Prints error message into console and exits program
	 * 
	 * @param msg
	 */
	public static void crash(String msg) {
		System.out.println(msg);
		System.exit(1);
	}

}
