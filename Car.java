import java.util.Random;

/**
 * Car representation for self-driving car program using Q Learning algorithm
 * It can turn left and right, and has information about its surroundings and 
 * its alive status.
 * 
 * @author Jesus Molina
 *
 */
public class Car {
	/** Number of actions this car can perform. */
	final int NUM_OF_ACTIONS = 3;
	/** Number of states this car takes in consideration (distances from wall)*/
	final int STATE_SPACE = 3;
	
	/** Direction this car is going. */
	direction dir;
	/** Alive status */
	boolean alive;
	/** Position*/
	int xPositionInMap;
	int yPositionInMap;
	
	/** Distance from this car to the nearest wall in its left. */
	int distanceFromWallLeft;
	/** Distance from this car to the nearest wall in its right. */
	int distanceFromWallRight;
	/** Distance from this car to the nearest wall in front. */
	int distanceFromWallFront;
	
	/** Map where this car is driving. */
	Map m;
	

	/**
	 * Actions that can be performed.
	 *
	 */
	enum actions {
		TURN_RIGHT, TURN_LEFT, NOTHING
	}

	/**
	 * Directions this car can be going. 
	 *
	 */
	enum direction {
		NORTH, SOUTH, EAST, WEST
	}


	/**
	 * Initializes the direction to NORTH.
	 */
	public Car() {
		dir = direction.NORTH;
		alive = true;
	}
	
	/**
	 * Sets the given direction as the initial direction this car is going.
	 * @param d the initial direction of the car.
	 */
	public Car(direction d, Map m, int x, int y) {
		dir = d;
		alive = true;
		this.m =m;
		xPositionInMap = x;
		yPositionInMap = y;
		updateDistances();
	}

	/**
	 * Changes the direction to the left.
	 */
	public void turnLeft() {
		switch (dir) {
			case NORTH:
				dir = direction.WEST;
				break;
			case EAST:
				dir = direction.NORTH;
				break;
			case WEST:
				dir = direction.SOUTH;
				break;
			case SOUTH:
				dir = direction.EAST;
				break;
		}
	}
	
	/**
	 * Changes the direction to the right.
	 */
	public void turnRight() {
		switch (dir) {
			case NORTH:
				dir = direction.EAST;
				break;
			case EAST:
				dir = direction.SOUTH;
				break;
			case WEST:
				dir = direction.NORTH;
				break;
			case SOUTH:
				dir = direction.WEST;
				break;
		}
	}
	
	/**
	 * Do nothing.
	 */
	public void doNothing(){
		
	}
	
	/**
	 * Changes the alive status of this car to false.
	 */
	public void kill(){
		alive = false;
	}
	
	/**
	 * Returns the alive status of this car.
	 * @return the alive status of this car.
	 */
	public boolean isAlive(){
		return alive;
	}
	
	/**
	 * Returns a random action from the possible actions.
	 * @return a random action.
	 */
	public actions randomAction(){
		Random r = new Random();
		int action = r.nextInt(NUM_OF_ACTIONS);
		if (action == 0){
			return actions.TURN_LEFT;
		}
		else if(action == 1){
			return actions.TURN_RIGHT;
		}else{
			return actions.NOTHING;
		}
	}
	
	/**
	 * Turns he direction of the car corresponding to the given action.
	 * 
	 * @param a the action that the car must perform.
	 */
	public void performAction(actions a){
		switch (a){
		case TURN_LEFT:
			turnLeft();
			break;
		case TURN_RIGHT:
			turnRight();
			break;
		case NOTHING:
			doNothing();
			break;
		}
		tick();
		updateDistances();
	}
	
	/**
	 * Sets the map to the given one.
	 * @param m the given map.
	 */
	public void setMap(Map m){
		this.m = m;
	}
	
	/**
	 * Advances the car one position in the direction it is facing.
	 * 
	 */
	public void tick(){
		if(dir == direction.NORTH){
			if(!m.insertCar(xPositionInMap, yPositionInMap -1)){
				kill();
			}
			else{
				yPositionInMap -= 1;
			}
			
		}
		if(dir == Car.direction.SOUTH){
			if(!m.insertCar(xPositionInMap, yPositionInMap + 1)){
				kill();
			}
			else{
				yPositionInMap += 1;
			}	
		}
		if(dir == direction.EAST){
			if(!m.insertCar(xPositionInMap + 1, yPositionInMap)){
				kill();
			}
			else{
				xPositionInMap += 1;
			}
			
		}
		if(dir == Car.direction.WEST){
			if(!m.insertCar(xPositionInMap - 1, yPositionInMap)){
				kill();
			}
			else{
				xPositionInMap -= 1;
			}	
		}
	}
	
	/**
	 * Updates the distances from this car to the nearest walls at
	 * front, left, and right.
	 */
	public void updateDistances(){
		distanceFromWallFront = 0;
		distanceFromWallLeft = 0;
		distanceFromWallRight = 0;
		
		int x = xPositionInMap;
		int y = yPositionInMap;
		if(dir == direction.NORTH){
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallFront ++;
				y--;
			}
			x = xPositionInMap;
			y = yPositionInMap;
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallLeft ++;
				x--;
			}
			x = xPositionInMap;
			y = yPositionInMap;
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallRight ++;
				x++;
			}
			
		}
		if(dir == direction.SOUTH){
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallFront ++;
				y++;
			}
			x = xPositionInMap;
			y = yPositionInMap;
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallLeft ++;
				x++;
			}
			x = xPositionInMap;
			y = yPositionInMap;
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallRight ++;
				x--;
			}
		}
		if(dir == direction.WEST){
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallFront ++;
				x--;
			}
			x = xPositionInMap;
			y = yPositionInMap;
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallLeft ++;
				y++;
			}
			x = xPositionInMap;
			y = yPositionInMap;
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallRight ++;
				y--;
			}
			
		}
		if(dir == direction.EAST){
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallFront ++;
				x++;
			}
			x = xPositionInMap;
			y = yPositionInMap;
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallLeft ++;
				y--;
			}
			x = xPositionInMap;
			y = yPositionInMap;
			while(m.grid[y][x] != Map.place.WALL){
				distanceFromWallRight ++;
				y++;
			}
			
		}
	}
	
	/**
	 * Returns a copy of this instance.
	 */
	public Car copy(){
		Car c = new Car();
		c.distanceFromWallFront = distanceFromWallFront;
		c.distanceFromWallLeft = distanceFromWallLeft;
		c.distanceFromWallRight = distanceFromWallRight;
		return c;
	}
}
