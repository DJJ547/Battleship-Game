package Model;
import java.util.Random;
import java.util.ArrayList;

/** Represents a player.
 * @author JiaJun Dai
 * @author Kevin Lopez
*/
public class PlayerModel {
	private final int MAP_SIZE = 10;
	private char[][] testGrid;
	private boolean[][] isFiredMap;
	private ArrayList<ShipModel> shipList;
	int numOfShipRemain;
	int numOfFiring;
	int numOfShipPlaced;
	int numOfShipDamaged;
	int numOfShipDestroyed;
	Random rand = new Random();
	
	/**
	 * Constructor for class PlayerModel
	 */
	public PlayerModel() {
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
	public boolean placeSingleShip (ShipModel s) {
		int[][] shipPos = s.getShipPos();
		if(checkIfShipOutOfBound(shipPos) && checkIfShipOverlap(shipPos)) {
			shipList.add(s);
			setShipInTestGrid(shipPos, s.getTestGridDisplay());
			numOfShipPlaced++;
			return true;
		}
		return false;
	}
	
	/**
	 * Remove one ship from the player grid
	 * @param x x position of the ship
	 * @param y y position of the ship 
	 * @param length length of the ship
	 * @param vertical ship placement is vertical or horizontal
	 * @return
	 */
	public void removeSingleShip(ShipModel s) {
		int[][] shipPos = s.getShipPos();
		shipList.remove(s);
		numOfShipPlaced--;
		removeSingleShipFromTestGrid(shipPos);
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
	
	public int[] getPlayerFiringPos(int[] firingPos) {
		return firingPos;
	}
	
	public boolean registerAIFiringPos(int[] firingPos) {
		if(! isFiredMap[firingPos[1]][firingPos[0]]) {
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
	
	//======================================AI firing Logic===============================================================
	public int[] getIsAllFiredRows() {
		ArrayList<Integer> allFiredRows = new ArrayList<Integer>();
		for(int i = 0; i < isFiredMap.length; i++) {
			boolean isRowAllFired = true;
			for(int j = 0; j <  isFiredMap[i].length; j++) {
				if(! isFiredMap[i][j]) {
					isRowAllFired = false;
					break;
				}
			}
			if(isRowAllFired) {
				allFiredRows.add(i);
			}
		}
		Object[] isAllFiredRowsObjArray = allFiredRows.toArray();
		int len = isAllFiredRowsObjArray.length;
		int[] isAllFiredIntArray = new int[len];
		for(int i = 0; i < len; i++) {
			isAllFiredIntArray[i] = (int)isAllFiredRowsObjArray[i];
		}
		return isAllFiredIntArray;
	}

	public int[] getIsFiredColNum(int row) {
		ArrayList<Integer> allFiredColNum = new ArrayList<Integer>();
		for(int i = 0; i < isFiredMap[row].length; i++) {
			if(isFiredMap[row][i]) {
				allFiredColNum.add(i);
			}
		}
		
		Object[] isFiredColNumObjArray = allFiredColNum.toArray();
		int len = isFiredColNumObjArray.length;
		int[] isFiredColNumIntArray = new int[len];
		for(int i = 0; i < len; i++) {
			isFiredColNumIntArray[i] = (int)isFiredColNumObjArray[i];
		}
		return isFiredColNumIntArray;
	}
	
	public boolean[][] getIsFiredMap(){
		return isFiredMap;
	}
	
	//====================================================================================

	public static void main(String[] args) {
		int[] shipPos1 = {1, 1};
		ShipModel sm1 = new ShipModel(shipPos1, 2, true);
		int[] shipPos2 = {1, 1};
		ShipModel sm2 = new ShipModel(shipPos2, 3, false);
		int[] shipPos3 = {1, 0};
		ShipModel sm3 = new ShipModel(shipPos3, 3, false);
		PlayerModel p1 = new PlayerModel();
		System.out.println(p1.placeSingleShip(sm1));
		System.out.println(p1.placeSingleShip(sm2));
		System.out.println(p1.placeSingleShip(sm3));
		p1.displayTestGrid();
		
		int[] firingPos1 = {1,1};
		p1.registerAIFiringPos(firingPos1);
		p1.displayTestGrid();
		
		int[] firingPos2 = {2,1};
		p1.registerAIFiringPos(firingPos2);
		p1.displayTestGrid();
		
		int[] firingPos3 = {2,1};
		System.out.println(p1.registerAIFiringPos(firingPos3));
		p1.displayTestGrid();
	}
}