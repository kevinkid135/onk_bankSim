package onk_simBank;

/**
 * A class to keep track of the total withdrawn and deposited amounts for each account.
 * @author N.Lin
 *
 */
public class Account {
	private int accNum;
	private int withdrawTotal;
	
	/**
	 * Constructor for account. Sets the withdrawnTotal and depositTotal to 0 when account object is created
	 * @param acc
	 */
	public Account(int acc){
		accNum = acc;
		withdrawTotal = 0;
	}// end Account constructor
	
	/*
	public int getWithdrawTotal(){
		return withdrawTotal;
	}
	
	public int getDepositTotal(){
		return depositTotal;
	}
	*/
	
	public int getAccNum(){
		return accNum;
	}
	
	
	public void withdraw(int num) throws InvalidInput{
		int sessionType = SimBank.getSessionType();
		//check for valid withdraw amount
		if(sessionType==1){
		// atm limit of 1000 per transaction, 1000 per session
			if(num > 1000)
				throw new InvalidInput("Invalid amount.");
			else if(num < 0)
				throw new InvalidInput("Invalid amount.");
			else if((withdrawTotal + num) > 1000)
				throw new InvalidInput("Invalid amount.");
			else
				withdrawTotal += num;
		}else if(sessionType==2){
		// agent limit of 99999999 per transaction, no limit per session
			if(num > 99999999)
				throw new InvalidInput("Invalid amount.");
			else if(num < 0)
				throw new InvalidInput("Invalid amount.");
			else
				withdrawTotal += num;
		}// end if-else for sessionTypes
	}// end updateWithdrawTotal
	
	public void deposit(int acc, int num) throws InvalidInput{
		int sessionType = SimBank.getSessionType();
		// check for valid deposit amount
		if(sessionType==1){
		// atm limit of 1000 per transaction, 1000 per session
			if(num > 1000)
				throw new InvalidInput("Invalid amount.");
			else if(num < 0)
				throw new InvalidInput("Invalid amount.");
		}else if(sessionType==2){
		// agent limit of 99999999 per transaction, no limit per session
			if(num > 99999999)
				throw new InvalidInput("Invalid amount.");
			else if(num < 0)
				throw new InvalidInput("Invalid amount.");
		}// end if-else for sessionTypes
	}
	
}
