package onk_simBank;

/**
 * Account class. Keeps track of the total withdrawn amounts for each account
 * and to check if withdraw/deposit amounts are valid.
 * 
 * @author Team Onk
 */
public class Account {
	private int accNum;
	private int withdrawTotal;

	/**
	 * Constructor for account. Sets the withdrawnTotal to 0 when account object
	 * is created.
	 * 
	 * @param acc
	 *            is the account number of the newly created account.
	 */
	public Account(int acc) {
		accNum = acc;
		withdrawTotal = 0;
	}// end Account constructor

	/**
	 * Returns the account number of the current account.
	 * 
	 * @return account number
	 */
	public int getAccNum() {
		return accNum;
	}

	/**
	 * Checks if the withdraw amount for the current account is within the
	 * constraints. In atm mode, only withdrawals of 100000 and under are
	 * accepted. In agent mode, only withdrawals of 99999999 and under are
	 * accepted. There is a total withdrawal limit of 100000 per account in atm
	 * mode, and no limit in agent mode.
	 * 
	 * @param num
	 *            is the amount being withdrawn from the current account
	 * @return true if the withdrawn amount is within the constraints, and false
	 *         otherwise.
	 * @throws InvalidInput
	 *             if the withdrawn amount is not within the constraints.
	 */
	public boolean withdraw(int num) throws InvalidInput {
		int sessionType = SimBank.getSessionType();

		// check for valid withdraw amount
		if (sessionType == SimBank.ATM_MODE) {

			// atm limit of 100000 per transaction, 100000 per session
			if (num > 100000)
				throw new InvalidInput("Invalid amount. Amount exceeds 100000.");
			else if (num < 0)
				throw new InvalidInput("Invalid amount.");
			else if ((withdrawTotal + num) > 100000)
				throw new InvalidInput("Invalid amount. Amount exceeds total limit of 100000.");
			else {
				withdrawTotal += num;
				return true;
			}
		} else if (sessionType == SimBank.AGENT_MODE) {

			// agent limit of 99999999 per transaction, no limit per session
			if (num > 99999999)
				throw new InvalidInput("Invalid amount. Amount exceeds 99999999.");
			else if (num < 0)
				throw new InvalidInput("Invalid amount.");
			else {
				withdrawTotal += num;
				return true;
			}
		} else { // should not be in this method if none if none of the other session
					// types
			return true;
		}
	}// end updateWithdrawTotal

	/**
	 * Checks if the deposit amount for the current account is within the
	 * constraints. In atm mode, only withdrawals of 100000 and under are
	 * accepted. In agent mode, only withdrawals of 99999999 and under are
	 * accepted.
	 * 
	 * @param num
	 *            is the amount being withdrawn from the current account
	 * @return true if the withdrawn amount is within the constraints, and false
	 *         otherwise.
	 * @throws InvalidInput
	 *             if the withdrawn amount is not within the constraints.
	 */
	public boolean deposit(int num) throws InvalidInput {
		int sessionType = SimBank.getSessionType();

		// check for valid deposit amount
		if (sessionType == 1) {

			// atm limit of 100000 per transaction, 100000 per session
			if (num > 100000) {
				throw new InvalidInput("Invalid amount.");
			} else if (num < 0)
				throw new InvalidInput("Invalid amount.");
			
			return true;
			
		} else if (sessionType == SimBank.AGENT_MODE) {

			// agent limit of 99999999 per transaction, no limit per session
			if (num > 99999999)
				throw new InvalidInput("Invalid amount. Amount exceeds 99999999.");
			else if (num < 0)
				throw new InvalidInput("Invalid amount.");
			
			return true;
		} else { // should not be in this method if none if none of the other session
					// types
			return true;
		}

	}// end deposit

}
