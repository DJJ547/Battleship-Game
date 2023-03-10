package Model;
import java.util.ArrayList;
import java.util.Random;

/** Represents an AI player.
 * @author JiaJun Dai
*/
public class AIPlayerModel {
	private final int MAP_SIZE = 10;
	private char[][] testGrid;
	private boolean[][] isFiredMap;
	private boolean[][] shipPosMap;
	private ArrayList<ShipModel> shipList;
	int numOfShipRemain;
	int numOfFiring;
	int numOfShipPlaced;
	int numOfShipDamaged;
	int numOfShipDestroyed;
	Random rand = new Random();
	
	/**
	 * Constructor for class AIPlayerModel
	 */
	public AIPlayerModel() {
		shipList = new ArrayList<ShipModel>();
		numOfShipRemain = 0;
		numOfFiring = 0;
		numOfShipPlaced = 0;
		numOfShipDamaged = 0;
		numOfShipDestroyed = 0;
		resetTestGrid();
	}
	
	private void setShipInTestGrid(int[][] shipPos, char shipLabel) {
		for(int i = 0; i < shipPos.length; i++) {
			testGrid[shipPos[i][1]][shipPos[i][0]] = shipLabel;
		}
	}
	
	private void removeSingleShipFromTestGrid(int[][] shipPos) {
		for(int i = 0; i < shipPos.length; i++) {
			testGrid[shipPos[i][1]][shipPos[i][0]] = '.';
			numOfFiring++;
		}
	}
	
	private void registerMissedFiringOnTestGrid(int[] firingPos) {
		if(testGrid[firingPos[1]][firingPos[0]] == '.') {
			testGrid[firingPos[1]][firingPos[0]] = 'X';
			numOfFiring++;
		}
	}
	
	private void registerHitFiringOnTestGrid(int[] firingPos) {
		if(testGrid[firingPos[1]][firingPos[0]] == 2 || testGrid[firingPos[1]][firingPos[0]] == 3 || 
				testGrid[firingPos[1]][firingPos[0]] == 4 || testGrid[firingPos[1]][firingPos[0]] == 5) {
			testGrid[firingPos[1]][firingPos[0]] = Character.toLowerCase(testGrid[firingPos[1]][firingPos[0]]);
			numOfFiring++;
		}
	}
	
	private void resetTestGrid() {
		testGrid = new char [MAP_SIZE][MAP_SIZE];
		isFiredMap = new boolean [MAP_SIZE][MAP_SIZE];
		for (int x = 0; x < MAP_SIZE; x++) {
			for (int y = 0; y < MAP_SIZE; y++) {
				testGrid[x][y] = '.';
				isFiredMap[x][y] = false;
				shipPosMap[x][y] = false;
			}
		}
	}
	
	/**
	* Display method to display player grid in 2d-array for testing
	* @param 
	* @return 
	*/
	public void displayTestGrid() {
		System.out.print("play ");
		for(int colNum = 0; colNum < MAP_SIZE; colNum++) {
			System.out.print(colNum + "   ");
		}
		System.out.println();
		System.out.println();
		for(int x = 0; x < MAP_SIZE; x++) {
			System.out.print(x + "    ");
			for(int y = 0; y < MAP_SIZE; y++) {
				System.out.print(testGrid[x][y] + "   ");
			}
			System.out.println();
			System.out.println();
		}
		System.out.println("numOfFiring: " + numOfFiring + ", " + "numOfshipRemain: " + numOfShipRemain);
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	/**
	* Resets player grid and all variables
	* @param 
	* @return
	*/
	public void resetAll() {
		numOfShipRemain = 0;
		numOfFiring = 0;
		numOfShipPlaced = 0;
		numOfShipDamaged = 0;
		numOfShipDestroyed = 0;
		resetTestGrid();
	}
	
	/**
	* This set ship method will generate one ship position
	* @param s ship that is added into PlayerModel
	* @return true if ship does not overlap with another ship on grid
	* @return false if ship overlaps with another ship on grid
	*/
	public boolean randomlyPlaceAllShip (int[] allShipLength) {
		for(int i = 0; i < allShipLength.length; i++) {
			int[][] shipStartPos;
			boolean isVertical = rand.nextBoolean();
			int shipLength = allShipLength[i];
			
		}
//		if(checkIfShipOutOfBound(shipPos) && checkIfShipOverlap(shipPos)) {
//			shipList.add(s);
//			setShipInTestGrid(shipPos, s.getGridDisplay());
//			numOfShipPlaced++;
//			return true;
//		}
		return false;
	}
	
	private int[] getAllRowsOrColsWithNoSpace(int len, boolean isVertical) {
		ArrayList<Integer> rowsWithNoSpace = new ArrayList<Integer>();
		if(! isVertical) {
			for(int i = 0; i < shipPosMap.length; i++) {
				int availableSpaceCounter = 0;
				for(int j = 0; j < shipPosMap[i].length - len; j++) {
					if(! shipPosMap[i][j]) {
						availableSpaceCounter++;
					}else {
						availableSpaceCounter = 0;
					}
					if(availableSpaceCounter >= len) {
						break;
					}
				}
				rowsWithNoSpace.add(i);
			}
		}else {
			
		}
	}
	
	/**
	* This will check if the ship position is out of bound
	*/
	private boolean checkIfShipOutOfBound (int[][] shipPos) {
		for(int i = 0; i < shipPos.length; i++) {
			if(shipPos[i][0] < 0 || shipPos[i][1] < 0 || shipPos[i][0] >= MAP_SIZE || shipPos[i][1] >= MAP_SIZE) {
				return false;
			}
		}
		return true;
	}
	
	/**
	* This will check if the ship position overlaps with another ship
	*/
	private boolean checkIfShipOverlap (int[][] shipPos) {
		for(int i = 0; i < shipPos.length; i++) {
			for(int j = 0; j < shipList.size(); j++) {
				for(int k = 0; k < shipList.get(j).getShipPos().length; k++) {
					if(shipPos[i][0] == shipList.get(j).getShipPos()[k][0] && shipPos[i][1] == shipList.get(j).getShipPos()[k][1]) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	* check if player is game over. numOfHits is the number of player ship
	* parts that has been hit.
	* @param 
	* @return true - numOfHits is equal to 17
	* @return false - numOfHits is not equal(below) to 17
	*/
	public boolean isGameOver() {
		if(numOfShipDestroyed == numOfShipPlaced) {
			return true;
		}
		return false;
	}
	
	public int[] getAIFiringPos(PlayerModel p) {
		int yPos = getRandomWithExclusion(rand, 0, 9, p.getIsAllFiredRows());
		int xPos = getRandomWithExclusion(rand, 0, 9, p.getIsFiredColNum(yPos));
		int[] firingPos = new int[]{xPos, yPos};
		return firingPos;
	}
	
	private int getRandomWithExclusion(Random rnd, int start, int end, int... exclude) {
	    int random = start + rnd.nextInt(end - start + 1 - exclude.length);
	    for (int ex : exclude) {
	        if (random < ex) {
	            break;
	        }
	        random++;
	    }
	    return random;
	}
		
	public boolean registerPlayerFiringPos(int[] firingPos) {
		if(!isFiredMap[firingPos[1]][firingPos[0]]) {
			for(int i = 0; i < shipList.size(); i++) {
				if(shipList.get(i).registerHit(firingPos)) {
					registerHitFiringOnTestGrid(firingPos);
					isFiredMap[firingPos[1]][firingPos[0]] = true;
					if(shipList.get(i).checkIfSunk()) {
						shipList.remove(i);
						numOfShipRemain--;
						numOfShipDestroyed++;
					}else {
						numOfShipDamaged++;
					}
					return true;
				}
			}
			registerMissedFiringOnTestGrid(firingPos);
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		int[] shipPos1 = {0, 0};
		ShipModel sm1 = new ShipModel(shipPos1, 4, false);
		int[] shipPos2 = {0, 1};
		ShipModel sm2 = new ShipModel(shipPos2, 4, false);
		int[] shipPos3 = {0, 2};
		ShipModel sm3 = new ShipModel(shipPos3, 4, false);
		PlayerModel p1 = new PlayerModel();
		System.out.println(p1.placeSingleShip(sm1));
		System.out.println(p1.placeSingleShip(sm2));
		System.out.println(p1.placeSingleShip(sm3));
		p1.displayTestGrid();
		AIPlayerModel p2 = new AIPlayerModel();
		int[] firingPos1 = p2.getAIFiringPos(p1);
		p1.registerAIFiringPos(firingPos1);
		p1.displayTestGrid();
		
		int[] firingPos2 = p2.getAIFiringPos(p1);
		p1.registerAIFiringPos(firingPos2);
		p1.displayTestGrid();
		
		
	}
}
