// CSE 373 Homework 4: HashMaps vs. Zombies 
// YOU DO NOT NEED TO MODIFY THIS INSTRUCTOR-PROVIDED FILE.
// Your code should work properly with an unmodified version of this file.

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

// TODO: remove these import statements once I am done with Part B
import java.util.Map;
import java.util.HashMap;

/**
 * An HvZSimulation object manages the state of a Humans vs. Zombies Tag game.
 * It keeps track of all humans and zombies and who has killed whom,
 * and allows zombies to infect humans, to die of starvation, and to decide
 * which side wins the overall game.
 */
public class HvZSimulation {
	private static final int ZOMBIE_STARVE = 2;  // days w/o eating til zombie starves
	
	private Set<HvZPlayer> humans;              // all current humans
	private Set<HvZPlayer> zombies;             // all current zombies
	private Map<String, HvZPlayer> players;     // name   -> Player
	private Map<HvZPlayer, String> kills;       // zombie -> who it has killed
	private Map<HvZPlayer, Integer> lastKill;   // zombie -> how many days ago it killed
	private int day;
	
	/**
	 * Constructs a new HvZ simulation to manage the given initial set of human
	 * players and zombie players.
	 * If either array contains any duplicate names, the duplicates are discarded.
	 * Throws a NullPointerException if either array or any element is null.
	 */
	public HvZSimulation(String[] humanNames, String[] zombieNames) {
		if (humanNames == null || zombieNames == null) {
			throw new NullPointerException();
		}
		
		this.humans = new HashSet<HvZPlayer>();
		this.zombies = new HashSet<HvZPlayer>();
		this.players = new HashMap<String, HvZPlayer>();
		this.kills = new HashMap<HvZPlayer, String>();
		this.lastKill = new HashMap<HvZPlayer, Integer>();
		this.day = 1;
		
		// add the humans
		for (String humanName : humanNames) {
			if (humanName == null) {
				throw new NullPointerException();
			}
			HvZPlayer human = new HvZPlayer(humanName, true);
			humans.add(human);
			players.put(humanName, human);
		}
		
		// add the zombies
		for (String zombieName : zombieNames) {
			if (zombieName == null) {
				throw new NullPointerException();
			}
			HvZPlayer zombie = new HvZPlayer(zombieName, false);
			zombies.add(zombie);
			players.put(zombieName, zombie);
			lastKill.put(zombie, 1);
		}
	}
	
	/**
	 * Returns the current day of the simulation; it starts on day 1
	 * and increases by 1 each time nextDay is called.
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * Records that the given zombie has infected the given human player.
	 * The human becomes a new zombie.
	 * The kill is also recorded for the given zombie in its kill list/count.
	 * Throws a NullPointerException if either name is null.
	 * Throws an IllegalArgumentException if either player is not found or is
	 * not the expected type (human or zombie).
	 */
	public void infect(String zombieName, String humanName) {
		System.out.println(zombieName + " infects " + humanName);
		
		// check for invalid arguments
		if (humanName == null || zombieName == null) {
			throw new NullPointerException();
		}
		if (!players.containsKey(zombieName) || !players.containsKey(humanName)) {
			throw new IllegalArgumentException("simulation does not contain one of "
					+ zombieName + " or " + humanName);
		}
		
		HvZPlayer human = players.get(humanName);
		HvZPlayer zombie = players.get(zombieName);
		if (!human.isHuman() || zombie.isHuman()) {
			throw new IllegalArgumentException(zombieName + " must be a zombie and "
					+ humanName + " must be a human");
		}
		
		// convert the human into a zombie
		humans.remove(human);
		human.becomeZombie();
		lastKill.put(human, 0);
		zombies.add(human);
		
		// record the kill for the zombie
		// (must remove and re-add any key objects in a hash set/map that change
		// state, because the object's hash code changes)
		String kill = humanName;
		if (kills.containsKey(zombie)) {
			kill = kills.get(zombie) + "+" + humanName;
		}
		kills.remove(zombie);
		zombies.remove(zombie);
		lastKill.remove(zombie);
		
		zombie.recordKill(); 
		zombies.add(zombie); 
		kills.put(zombie, kill);
		lastKill.put(zombie, 0);
	}
	
	/**
	 * Advances the state of the game by one day.
	 * Most significantly, any zombies that have not killed a human recently
	 * die of starvation and are removed from the game.
	 */
	public void nextDay() {
		Iterator<HvZPlayer> itr = zombies.iterator();
		while (itr.hasNext()) {
			HvZPlayer zombie = itr.next();
			int last = lastKill.get(zombie);
			
			if (last >= ZOMBIE_STARVE) {
				// zombie did not eat anybody, so it starves
				System.out.println(zombie.getName() + " dies of starvation");
				itr.remove();
				players.remove(zombie.getName());
				kills.remove(zombie);
				lastKill.remove(zombie);
			} else {
				lastKill.put(zombie, last + 1);
			}
		}
		
		System.out.println("End of day " + day);
		day++;
	}
	
	/**
	 * Prints the current state of the simulation to System.out, including
	 * all human and zombie players, which zombies killed which humans,
	 * and which side (if any) has won the game.
	 */
	public void display() {
		System.out.println("Humans : " + humans);
		System.out.println("Zombies: " + zombies);
		System.out.println("Kills  : " + kills);
		if (humans.isEmpty()) {
			System.out.println("The zombie horde has won!");
		} else if (zombies.isEmpty()) {
			System.out.println("The human resistance has won!");
		} else {
			System.out.println("The humans are still clinging to survival!");
		}
		System.out.println();
	}
}
