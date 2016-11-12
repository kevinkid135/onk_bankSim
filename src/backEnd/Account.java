package backEnd;

/**
 * Account class. Holds the account number, balance, and name of the account. 
 * 
 * @author onk_Team
 *
 */
public class Account {

	private int number;
	private int balance;
	private String name;

	/**
	 * Constructor for new accounts, which occurs when there's a valid create
	 * transaction. Sets balance to 0.
	 * 
	 * @param accNum
	 *            the account number of the account
	 * @param accName
	 *            the account name of the account
	 */
	public Account(int accNum, String accName) {
		number = accNum;
		balance = 0;
		name = accName;
	}

	/**
	 * Constructor for accounts, which occurs when reading in the old master
	 * account list file.
	 * 
	 * @param accNum
	 *            the account number of the account
	 * @param accBalance
	 *            the starting balance of the account, as indicated by the old
	 *            masters account
	 * @param accName
	 *            the account name of the account
	 */
	public Account(int accNum, int accBalance, String accName) {
		number = accNum;
		balance = accBalance;
		name = accName;
	}

	/**
	 * Returns the account number of the current account.
	 * 
	 * @return account number
	 */
	public int getAccNum() {
		return number;
	}

	/**
	 * Returns the balance of the current account.
	 * 
	 * @return account balance
	 */
	public int getBalance() {
		return balance;
	}

	/**
	 * Returns the name of the current account.
	 * 
	 * @return account name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the balance of the current account to the new balance.
	 * 
	 * @param newBalance
	 *            the new balance of the current account.
	 */
	public void setBalance(int newBalance) {
		balance = newBalance;
	}

}
