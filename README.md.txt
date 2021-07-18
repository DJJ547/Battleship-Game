# Battleship-Game (JAVA)
Participants: JiaJun Dai, Taeho Lee, Kevin Lopez, Nikola Klier

This is a strategy type guessing game between the player and AI. After the player and AI finish setting up positions for their ships, they take turns to fire shots on each other's sea map. Once a shot from one side htis, that side gets to fire next shot consecutively. The winner will be the one who destroys every ship in his opponent’s grid. 

This program uses MVC pattern and valve control.

Size of player grid: 10 x 10

Number of ships each side has:
1. one aircraft carrier (length 5)
2. one cruiser (length 4)
3. two destroyer (length 3)
4. one submarine (length 2)

Below are images showing how this game operates:
1. This is the screen after we run the game. player grid is on the left and AI grid is on the right.
![ScreenShot](images/StartScreen.png)
2. For player to place ships, simply drag the ships (green square) onto the player grid. Use the right mouse button to place ship either horizontally or vertically.
![ScreenShot](images/PlacingScreen.png)
3. This is the ingame screen. left click on the AI grid to fire a shot. once hit, player gets a chance to fire next shot consecutively. (same for AI)
![ScreenShot](images/IngameScreen.png)
4. This is the winning screen. once all ships on one side are destroyed, the game ends, and the textfield down the bottom will address who's the winner.
![ScreenShot](images/WinningScreen.png)
