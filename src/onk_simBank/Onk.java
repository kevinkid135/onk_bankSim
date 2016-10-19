package onk_simBank;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Scanner;

public class Onk {

	public static void main(String[] args) {
		// create a bank instance
		SimBank s = new SimBank();
		
		while (true) // since program never ends, we use infinite loop
			s.start();
	}

}
