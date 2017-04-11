import java.util.HashMap;
import java.util.Random;

import org.javatuples.Pair;


/**
 * Calculates the optimal policy for the value of actions of a Car
 * in a Map.
 * 
 * @author Jesus Molina
 *
 */
public class QLearning {
	/** The car that performs the actions. */
	Car car;
	/** The map where the car is going to move. */
	Map map;
	/** Learning rate of the algorithm. (The amount of override new information will cause on past). */
	final double alpha = 0.1;
	/** Discount factor of the algorithm. (How far in the future you consider the events). */
	final double gamma = 0.9;

	/** Stores the q values of an action on a state. */
	HashMap<Pair<int[], Car.actions>, Double> qValues;

	/**
	 * initializes the q values of actions;
	 */
	public QLearning() {
		qValues = new HashMap<Pair<int[], Car.actions>, Double>();
	}
	
	/**
	 * Runs the algorithm and prints the calculated policy.
	 * @param args
	 */
	public static void main(String[] args) {
		QLearning q = new QLearning();
		q.run();
		q.printPolicy();
	}

	/**
	 * Prints the policy obtained after the training algorithm. 
	 */
	private void printPolicy() {
		double turnLeftWhenWallLeft = 0;
		double turnRightWhenWallRight = 0;
		double nothingWhenWallFront = 0;

		int nothingCounter = 0;
		int leftCounter = 0;
		int rightCounter = 0;

		double allElse = 0;
		int allElseCounter = 0;

		for (Pair<int[], Car.actions> stateAction : qValues.keySet()) {
			if (stateAction.getValue0()[0] == 1 && stateAction.getValue1() == Car.actions.NOTHING) {
				nothingWhenWallFront += qValues.get(stateAction);
				nothingCounter++;
			}
			else if (stateAction.getValue0()[1] == 1 && stateAction.getValue1() == Car.actions.TURN_LEFT) {
				turnLeftWhenWallLeft += qValues.get(stateAction);
				leftCounter++;
			}
			else if (stateAction.getValue0()[2] == 1 && stateAction.getValue1() == Car.actions.TURN_RIGHT) {
				turnRightWhenWallRight += qValues.get(stateAction);
				rightCounter++;
			}
			else{
				allElse += qValues.get(stateAction);
				allElseCounter++;
			}
		}

		System.out.println("Average nothing when wall front : " + nothingWhenWallFront / nothingCounter);
		System.out.println("Average left when wall left: " + turnLeftWhenWallLeft / leftCounter);
		System.out.println("Average right when wall right: " + turnRightWhenWallRight / rightCounter);
		System.out.println();
		System.out.println("Average all else: "+allElse/allElseCounter);
	}

	/**
	 * Calculates the rewards of the actions on the possible states.
	 * A state is conformed of the distances between the car and the walls.
	 * the distances are the direct cardinal distance from the car to a wall in 
	 * the map to the front, the left, and the right.
	 * 
	 * The algorithm updates the q values of an action in a state as:
	 * Q(distances, action) = Q(distances, action) + alpha * (reward + gamma * maxQ(distances) - Q(distances,action))
	 * where maxQ(distances) is the maximum Q obtainable in the new state.
	 * 
	 * The algorithm will run 1000 simulations with new maps and a new car every time.
	 * 
	 */
	private void run() {
		for (int i = 0; i < 1000; i++) { // Training period
			
			qLearning();
		}

	}

	private void qLearning() {
		Random r = new Random();
		map = new Map(10, 10, 30);
		int xCar;
		int yCar;
		
		// inserting car in a valid position. 
		do{
			xCar = r.nextInt(10);
			yCar = r.nextInt(10);
		}while(!map.insertCar(xCar, yCar));
		
		car = new Car(Car.direction.NORTH, map, xCar, yCar);
		
		// Simulation
		while (car.isAlive()) {
			Car.actions actionPerformed = car.randomAction(); // pick random action
			
			Car before = car.copy(); //Save the state of the car before performing the action
			
			double q = getQ(car, actionPerformed); // obtain current q value before the change of state
			car.performAction(actionPerformed); // perform chosen action. (change of state)
			double maxQ = getMaxQ(car); // get maximum Q obtainable in the new state

			int reward = car.isAlive() ? 0 : -100; // punish if car crashed. 
			double value = q + alpha * (reward + gamma * maxQ - q); //calculate new Q value
			setQ(before, actionPerformed, value); //set the q value to the previous state.
		}
	}

	/**
	 * Returns the maximum Q value in a state.
	 * 
	 * @param c the car that holds the distances from wall
	 * @return the maximum q obtainable in this state.
	 */
	private double getMaxQ(Car c) {
		int[] distances = new int[] { c.distanceFromWallFront, c.distanceFromWallLeft, c.distanceFromWallRight };

		Pair<int[], Car.actions> stateActionNothing = exists(distances, Car.actions.NOTHING);
		if (stateActionNothing == null)
			stateActionNothing = new Pair<int[], Car.actions>(distances, Car.actions.NOTHING);

		if (qValues.get(stateActionNothing) == null) {
			qValues.put(stateActionNothing, 0.0);
		}
		Pair<int[], Car.actions> stateActionLeft = exists(distances, Car.actions.TURN_LEFT);
		if (stateActionLeft == null)
			stateActionLeft = new Pair<int[], Car.actions>(distances, Car.actions.TURN_LEFT);

		if (qValues.get(stateActionLeft) == null) {
			qValues.put(stateActionLeft, 0.0);
		}

		Pair<int[], Car.actions> stateActionRight = exists(distances, Car.actions.TURN_RIGHT);
		if (stateActionRight == null)
			stateActionRight = new Pair<int[], Car.actions>(distances, Car.actions.TURN_RIGHT);

		if (qValues.get(stateActionRight) == null) {
			qValues.put(stateActionRight, 0.0);
		}

		double value1 = qValues.get(stateActionNothing);
		double value2 = qValues.get(stateActionLeft);
		double value3 = qValues.get(stateActionRight);

		double[] values = new double[] { value1, value2, value3 };		
		
		double max = values[0];
		
		for (int i = 0; i < 3; i++)
			if (values[i] >= max)
				max = values[i];

		return max;
	}

	/**
	 * Sets the q value of the state with the given action to the given value.
	 * 
	 * @param c the car that holds the distances from walls. 
	 * @param actionPerformed the action that was performed. 
	 * @param value the value given.
	 */
	private void setQ(Car c, Car.actions actionPerformed, double value) {
		int[] distances = new int[] { c.distanceFromWallFront, c.distanceFromWallLeft, c.distanceFromWallRight };
		Pair<int[], Car.actions> stateAction = exists(distances, actionPerformed);
		if (stateAction == null)
			stateAction = new Pair<int[], Car.actions>(distances, actionPerformed);

		qValues.put(stateAction, value);
	}

	/**
	 * Obtains the q value of the given state and given action. 
	 * @param c the car that holds the distances from walls. 
	 * @param actionPerformed the action performed.
	 * @return the qw value of the given state and the action performed. 
	 */
	private double getQ(Car c, Car.actions actionPerformed) {
		int[] distances = new int[] { c.distanceFromWallFront, c.distanceFromWallLeft, c.distanceFromWallRight };
		Pair<int[], Car.actions> stateAction = exists(distances, actionPerformed);

		if (stateAction == null)
			stateAction = new Pair<int[], Car.actions>(distances, actionPerformed);

		if (qValues.get(stateAction) == null) {
			qValues.put(stateAction, 0.0);
		}

		return qValues.get(stateAction);
	}

	/**
	 * returns the hash key in the map if it exists.
	 * 
	 * @param distances the state. 
	 * @param action the action.
	 * @return the key that matches the given state and given action.
	 */
	private Pair<int[], Car.actions> exists(int[] distances, Car.actions action) {
		for (Pair<int[], Car.actions> key : qValues.keySet()) {
			int[] arr = key.getValue0();
			if (arr[0] == distances[0] && arr[1] == distances[1] && arr[2] == distances[2] && action == key.getValue1())
				return key;
		}
		return null;
	}

}
