package backEnd;

import java.util.ArrayList;

public class BackEnd {
	private static String transSumFilename;
	private static String masterAccListFilename;
	private static ArrayList<String> localMasterAccList = new ArrayList<String>();

	public static void main(String[] args) {
		transSumFilename = args[0];
		masterAccListFilename = args[1];
		
		// parse tranSum into string of arrays
		// parse master acc list into arraylist
		
		// loop through tranSum array
		for(;;){
			// if transaction code is deposit
			// if withdraw
			// if transfer
			// if create
			// if delete
			// else throw error
		}
	}

	public static void create(int accNum, String accName) {

	}

	public static void delete(int accNum, String accName) {

	}

	public static void withdraw(int amount, int accNum) {

	}
	
	public static void deposit(int amount, String accNum) {

	}
	
	public static void transfer(int accNum1, int accNum2, int amount) {

	}
	
	public static void accountExists(int accNum) {

	}
	
	public static void findAccount(int accNum) {

	}
	
	
}
