package Model;

import java.util.Arrays;

/** Represents a ship.
 * @author JiaJun Dai
*/
public class ShipModel {
	int[][] shipPos;
	private int length;
	private int shipHealth;
	private boolean vertical;
	private boolean[] isDamaged;
	
//	/**
//	 * Constructor for class ShipModel, use for AIPlayerModel.setShip()
//	 * @param length length of the ship
//	 */
//	public ShipModel(int length) {
//		this.length = length;
//		shipHealth = length;
//		shipPos = new int[length][2];
//		isDamaged = new boolean[length];
//		for(int i = 0; i < isDamaged.length; i++) {
//			isDamaged[i] = false;
//		}
//	}
	
//	/**
//	 * Constructor for class ShipModel, use for AIPlayerModel.setShip()
//	 * @param length length of the ship
//	 */
//	public ShipModel(int length) {
//		this.length = length;
//		shipPos = new int[length][2];
//		shipHealth = length;
//		isDamaged = new boolean[length];
//	}
	
	/**
	 * Constructor for class ShipModel, use for PlayerModel.setShip()
	 * @param x
	 * @param y
	 * @param length
	 * @param vertical
	 */
	public ShipModel(int[] shipStartPos, int length, boolean vertical) {
		this.length = length;
		this.vertical = vertical;
		shipHealth = length;
		shipPos = new int[length][2];
		for(int i = 0; i < length; i++) {
			shipPos[i] = shipStartPos.clone();
			if(vertical) {
				shipStartPos[1]++;
			}else {
				shipStartPos[0]++;
			}
		}
		isDamaged = new boolean[length];
		for(int i = 0; i < isDamaged.length; i++) {
			isDamaged[i] = false;
		}
	}
	
	public boolean registerHit(int[] hitPos) {
		for (int i = 0; i < shipPos.length; i++) {
			if (shipPos[i][0] == hitPos[0] && shipPos[i][1] == hitPos[1]) {
				isDamaged[i] = true;
				shipHealth--;
				return true;
			}
		}
		return false;
	}
	
	public boolean checkIfSunk() {
		if (shipHealth == 0) {
			return true;
		}else
			return false;
	}
	
	/**
	 * get the grid display number for each ship with different length
	 * @return int the numbers representing each ship, whether it's length 2,3,4,5
	 */
	public char getTestGridDisplay() {
		if(length == 2) {
			return 'S';
		}else if(length == 3) {
			return 'D';
		}else if(length == 4) {
			return 'C';
		}else if(length == 5) {
			return 'A';
		}else {
			return (char)length;
		}
	}
	
	/**
	 * get all x positions of the ship
	 * @return xPos an array contains all ship's x position
	 */
	public int[][] getShipPos() {
		return shipPos;
	}
	
	/**
	 * check if the ship placement is vertical or horizontal
	 * @return true the ship placement is vertical
	 * @return false the ship placement is horizontal
	 */
	public boolean isVertical() {
		return vertical;
	}
	
	/**
	 * get the ship length
	 * @return length ship length
	 */
	public int getLength() {
		return length;
	}
	
//	public static void main(String[] args) {
//		int[] shipPos = {1, 1};
//		ShipModel sm = new ShipModel(shipPos, 2, true);
//		for(int i = 0; i < sm.shipPos.length; i++) {
//			for(int j = 0; j < sm.shipPos[i].length; j++) {
//				System.out.println(sm.shipPos[i][j]);
//			}
//		}
//	}
}
