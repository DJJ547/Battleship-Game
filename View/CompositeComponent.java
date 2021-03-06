package View;

import Message.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * A class that contains grid, ships
 * @author Taeho Lee
 */
public class CompositeComponent extends JComponent{

    /**
     * Constructor of CompositeComponent
     * @param q A BlockingQueue to communicate with the controller.
     */
    public CompositeComponent (BlockingQueue<Message> q){
        queue = q;

        gridCoordinate = new SquareShape[ROWS][COLUMNS];

        playerGrid = new Grid(ROWS,COLUMNS,0,0);
        AIGrid = new Grid(ROWS,COLUMNS,(ROWS + 2) * SquareShape.getWidth(),0);

        int r = 0, c =0;
        for(Object s: playerGrid){
            if(r >= ROWS + 1){
                System.out.println("GridCoordinate assignment out of bound!!!");
            }
            gridCoordinate[r][c++] = (SquareShape) s;
            if (ROWS > r){
                if (COLUMNS <= c) {
                    r += 1;
                    c = 0;
                }
            }


        }

        ships = new ArrayList<>();


        addMouseMotionListener(new CompositeComponent.PlayerGridMouseMotionListener());
//        addMouseListener(new GridMouseClickListener());
        addMouseListener(new ShipMouseClickListener());
        addMouseMotionListener(new ShipMouseMotionListener());
    }

    /**
     * A method for drawing all the components inside the CompositeComponent
     * @param g graphical context
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        AIGrid.draw(g2);
        playerGrid.draw(g2);
        for(ShipView s: ships){
            s.draw(g2);
        }

    }

    /**
     * Method for adding ships in CompositeComponent object
     * @param ship ShipView object being added.
     */
    public void addShip(ShipView ship){

        ships.add(ship);
    }

    /**
     * returns the ArrayList of ShipView objects inside the CompositeComponent object.
     * @return ArrayList of shipView objects.
     */
    public ArrayList<ShipView> getShipList(){
        return ships;
    }

    /**
     * Reset all the attributes in the CompositeComponent object.
     */
    public void reset(){
        gridCoordinate = new SquareShape[ROWS][COLUMNS];

        playerGrid = new Grid(ROWS,COLUMNS,0,0);
        AIGrid = new Grid(ROWS,COLUMNS,(ROWS + 2) * SquareShape.getWidth(),0);

        int r = 0, c =0;
        for(Object s: playerGrid){
            if(r >= ROWS + 1){
                System.out.println("GridCoordinate assignment out of bound!!!");
            }
            gridCoordinate[r][c++] = (SquareShape) s;
            if (ROWS > r){
                if (COLUMNS <= c) {
                    r += 1;
                    c = 0;
                }
            }


        }

        ships = new ArrayList<>();




    }

    /**
     * A MouseMotionListener for grids
     */
    private class PlayerGridMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent event){
            mousePoint = event.getPoint();

            iterateGrid(( playerGrid));
            iterateGrid(AIGrid);

            repaint();
        }

        private void iterateGrid(Grid aiGrid) {
            SquareShape s;
            for (Object temp : aiGrid){
                s = (SquareShape) temp;
                if(s.getColor() == Color.WHITE || s.getColor() == Color.LIGHT_GRAY) {
                    if (s.contains(mousePoint)) {
                        s.setColor(Color.LIGHT_GRAY);
                    } else {
                        s.setColor(Color.WHITE);
                    }
                }
            }
        }
    }

    /**
     * A MouseListener for clicking the AI grid to fire missile
     */
    private class AIGridMouseClickListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event){
            mousePoint = event.getPoint();
            SquareShape s;
            for (Object temp : AIGrid){
                s = (SquareShape) temp;
                if(s.contains(mousePoint)){
                    if(s.getColor() != Color.BLACK && s.getColor() != Color.RED){

                        int[] coordinates = AIGrid.indexOf(s);
                        try {
                            queue.put(new FireMissileMessage(coordinates[1],coordinates[0]));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

//                    if(!s.isSelected()){
//                        s.setSelected(true);
//                        s.setColor(Color.BLACK);
//
//
//                    }else{
//                        s.setSelected(false);
//                        s.setColor(Color.WHITE);
//                    }
                }
            }
            repaint();
        }
    }

    /**
     * MouseListener for clicking the ships and releasing on the grid.
     */
    private class ShipMouseClickListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event)
        {

            mousePoint = event.getPoint();

            for (ShipView s: ships){
                if(s.contains(event.getPoint())){
                    if(event.getButton() == 1){
                        if(s.indexOf()[1] == -1 && s.indexOf()[0] == -1 ){
                            s.setSelected(true);
                            s.setPosition(event.getX() - SquareShape.getWidth() / 2, event.getY() -SquareShape.getWidth() / 2);

                        }else{
                            s.setSelected(true);
                            s.setPosition(event.getX() - SquareShape.getWidth() / 2, event.getY() -SquareShape.getWidth() / 2);
                            try {
                                queue.put(new RemoveShipMessage(s));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }else if(event.getButton() == 3){
                        if(s.isSelected()){
                            s.rotate();
                        }
                    }
                }

            }
            repaint();


        }


        @Override
        public void mouseReleased(MouseEvent event){
            if(event.getButton() == 1){
                Point p = event.getPoint();
                for(ShipView ship : ships){
                    if(ship.isSelected()){
                        if(!playerGrid.contains(event.getPoint())){
                            ship.resetPosition();
                            System.out.println("ship placing failed: ship is outside the grid");
                        }else {
                            for (int i = 0; i < ROWS; i++){
                                for (int j = 0; j < COLUMNS; j++){
                                    SquareShape square = gridCoordinate[i][j];
                                    if(square.contains(p))
                                         if( ((ROWS - i) >= ship.getLength() && ship.isVertical())
                                            || ( (COLUMNS - j) >= ship.getLength()) && !ship.isVertical() ){

                                                 int[] index = playerGrid.indexOf(square);
                                                 ship.setIndex(index[0], index[1]);
                                                 ship.setPosition(square.getPosition());
                                                 ship.setSelected(false);
                                                 System.out.println("ship placing SUCCEED: ["+ index[1] +"]["+index[0]+"]");

                                                 PlaceNewShipMessage message =
                                                         new PlaceNewShipMessage(ship);
                                                 try {
                                                     queue.put(message);
                                                 } catch (InterruptedException e) {
                                                     e.printStackTrace();
                                                 }

                                        }else{
                                             ship.resetPosition();
                                             System.out.println("ship placing failed: size doesn't fit");
                                         }
                                }
                            }

                        }

                    }
                }

                repaint();
            }

        }
    }

    /**
     * MouseListener for dragging ships on the GUI.
     */
    private class ShipMouseMotionListener extends MouseMotionAdapter{

        @Override
        public void mouseDragged(MouseEvent event){
            Point lastMousePoint = mousePoint;
            mousePoint = event.getPoint();
            for(ShipView s : ships){
                if (s.isSelected())
                {
                    double dx = mousePoint.getX() - lastMousePoint.getX();
                    double dy = mousePoint.getY() - lastMousePoint.getY();
                    s.translate((int) dx, (int) dy);
                }

            }


            repaint();
        }
    }

    /**
     * Method that enables clicking the AI grid.
     */
    public void enableClicking(){
        addMouseListener(clickListener);
    }

    /**
     * Method that disables clicking the AI grid.
     */
    public void disableClicking(){
        removeMouseListener(clickListener);
    }

    /**
     * Method that converts all the ships into green squares inside the grid. used after finished placing.
     */
    public void convertShipsToGridColors(){
        for(ShipView ship: ships){

            int len = ship.getLength();
            int x = ship.indexOf()[1];
            int y = ship.indexOf()[0];
            int xi = ship.isVertical()? 0 : 1;
            int yi = !ship.isVertical()? 0 : 1;
            SquareShape square;

            for(int i = 0; i <len; i++){


                square = playerGrid.getSquare(x +xi * i ,y + yi * i);
                square.setColor(Color.GREEN);
            }
        }

        ships = new ArrayList<>();
        repaint();
    }

    /**
     * Method for shooting player.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param hit whether the missile is hit or not.
     */
    public void shootPlayer(int x, int y, boolean hit){
        SquareShape square = playerGrid.getSquare(x,y);
        if(hit){
            square.setColor(Color.BLUE);
        }else{
            square.setColor(Color.BLACK);
        }
        repaint();
    }

    /**
     * Method for shooting AI.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param hit whether the missile is hit or not.
     */
    public void shootAI(int x, int y, boolean hit){

        SquareShape square = AIGrid.getSquare(x,y);
        if(hit){
            square.setColor(Color.RED);
        }else{
            square.setColor(Color.BLACK);
        }
        repaint();
    }





    private SquareShape[][] gridCoordinate;
    private Point mousePoint;
    private Grid playerGrid;
    private Grid AIGrid;
    private ArrayList<ShipView> ships;
    private BlockingQueue<Message> queue;
    private int ROWS = 10;
    private int COLUMNS = 10;
    private AIGridMouseClickListener clickListener = new AIGridMouseClickListener();

}
