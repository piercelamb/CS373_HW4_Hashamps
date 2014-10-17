// CSE 373 Homework 4: HashMaps vs. Zombies 
// YOU DO NOT NEED TO MODIFY THIS INSTRUCTOR-PROVIDED FILE.
// Your code should work properly with an unmodified version of this file.

import java.io.*;
import java.util.*;

/**
 * This program runs the maze solver.
 * It reads the input data from the HvZ text file
 * that will be passed in to construct a HvZSimulation object,
 * then it calls methods on your HvZSimulation to simulate the game.
 */
public class HvZMain {
	private static final String DEFAULT_INPUT_FILENAME = "hvz1.txt";
	
	/** Runs the program. */
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("CSE 373 Humans vs. Zombies Tag Simulator");
		System.out.println("========================================");
		System.out.println("This program simulates a game of HvZT on campus.");
		System.out.println("The humans try to escape being infected by the hordes");
		System.out.println("of blood-thirsty zombies until the game is over.");
		System.out.println();
		
		Scanner input = openFile();
		HvZSimulation sim = readSimulation(input);
		runSimulation(sim, input);
	}
	
	/**
	 * Prompts the user to enter a file name containing HvZ input data.
	 * Defaults to "hvz1.txt".
	 * If the user types a partial file name, "hvz" prefix and ".txt" suffix will be added.
	 * Throws a FileNotFoundException if the input file is not found.
	 */
	public static Scanner openFile() throws FileNotFoundException {
		Scanner console = new Scanner(System.in);
		System.out.print("HvZ input file (Enter for " + DEFAULT_INPUT_FILENAME + ")? ");
		String filename = console.nextLine().trim();
		if (filename.length() == 1) {
			filename = "hvz" + filename + ".txt";   // shorthand, can type "2" -> "hvz2.txt"
		} else if (filename.length() > 0 && !filename.contains(".")) {
			filename = filename + ".txt";   // shorthand, can type "hvz2" -> "hvz2.txt"
		}
		if (filename.length() == 0 || !(new File(filename).exists())) {
			filename = DEFAULT_INPUT_FILENAME;
		}
		System.out.println();
		
		Scanner input = new Scanner(new File(filename));
		return input;
	}
	
	/**
	 * Reads HvZ sim data from the user's text file and uses it to construct and
	 * return a HvZSimulation object.
	 * Throws a FileNotFoundException if the input file is not found.
	 */
	public static HvZSimulation readSimulation(Scanner input) {
		String[] humans = input.nextLine().trim().split("[ ]*,[ ]*");
		String[] zombies = input.nextLine().trim().split("[ ]*,[ ]*");
		input.nextLine();
		HvZSimulation sim = new HvZSimulation(humans, zombies);
		return sim;
	}
	
	/**
	 * Runs HvZ sim using the commands in the given input file.
	 * Commands are things like "infect,Sue,Bill" to indicate that Sue infects Bill.
	 */
	public static void runSimulation(HvZSimulation sim, Scanner input) {
		System.out.println("Start of simulation");
		sim.display();
		
		while (input.hasNextLine()) {
			String line = input.nextLine().trim();
			if (line.length() == 0) {
				sim.nextDay();
				sim.display();
			} else if (line.startsWith("infect,")) {
				String[] tokens = line.split("[ ]*,[ ]*");
				if (tokens.length >= 3) {
					String zombie = tokens[1];
					String human = tokens[2];
					sim.infect(zombie, human);
				}
			}
		}
	}
}
