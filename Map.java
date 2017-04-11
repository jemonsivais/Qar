import java.util.Random;

public class Map {
	/** The map. */
	place[][] grid;
	/** The number of walls in the grid. */
	int numOfWalls;
	
	/**
	 * A place can either be free, a wall, or occupied by a car.
	 * 
	 * @author Jesus Molina
	 *
	 */
	enum place{
		FREE,
		WALL,
		CAR
	}
	
	/**
	 * Creates an instance of a wall with the given dimensions and 
	 * places the given number of walls in the grid. 
	 * 
	 * @param xDim the width of the grid.
	 * @param yDim the height of the grid.
	 * @param walls the number of walls in the grid.
	 * 
	 */
	public Map(int xDim, int yDim, int walls){
		grid = new place[yDim][xDim];
		numOfWalls = walls;
		generateWalls();
	}
	
	/**
	 * Generates the walls inside the grid randomly
	 * and sets the borders of the grid.
	 * 
	 */
	private void generateWalls(){
		for(int i = 0; i < grid.length; i++){ //Start the grid with only free places.
			for(int e = 0; e < grid[i].length; e++){
				grid[i][e] = place.FREE;
			}
		}
		
		for(int i = 0; i < grid.length; i++){ //Set horizontal borders.
			grid[0][i] = place.WALL;
			grid[grid.length-1][i] = place.WALL;
		}
		
		for(int i = 0; i < grid[0].length; i++){ //Set vertical borders.
			grid[i][0] = place.WALL;
			grid[i][grid[i].length-1] = place.WALL;
		}
		
		//Place random wall in the middle.
		Random r = new Random();
		for(int i = 0; i < numOfWalls; i++){
			int randomX = r.nextInt(grid.length);
			int randomY = r.nextInt(grid[0].length);
			if(grid[randomY][randomX] != place.WALL)
				grid[randomY][randomX] = place.WALL;
			else
				i--;
		}
	}
	
	/**
	 * Insert a car at the given position.
	 * A car can't be placed at a wall.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @return true if the car was placed successfully
	 */
	public boolean insertCar(int x, int y){
		if(x == 0 || x == grid.length || y == 0 || y == grid.length)
			return false;
		if(grid[y][x] == place.WALL)
			return false;
		grid[y][x] = place.CAR;
		return true;
	}
	
	/**
	 * Prints the map.
	 */
	public void prettyPrint(){
		for(int i = 0; i < grid.length; i++){
			for(int e = 0; e < grid.length; e++){
				if(grid[i][e] == place.WALL)
					System.out.print("W ");
				else if(grid[i][e] == place.FREE)
					System.out.print("F ");
				else
					System.out.print("C ");
			}
			System.out.println();
		}
	}
}
