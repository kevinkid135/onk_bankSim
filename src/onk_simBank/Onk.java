package onk_simBank;

public class Onk{
	
	public static void main(String[] args) throws InvalidInput {
		// create a bank instance
		SimBank s = new SimBank();
		
		while (true) // since program never ends, we use infinite loop
			s.start();
	}//end main

}
