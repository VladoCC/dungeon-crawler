package ru.myitschool.cubegame.dungeon;

import java.awt.*;

/**
 * Created by Voyager on 23.04.2017.
 */
public class Exit {

    public static final int DIRECTION_NORTH = 0;
    public static final int DIRECTION_EAST = 1;
    public static final int DIRECTION_SOUTH = 2;
    public static final int DIRECTION_WEST = 3;

    private Point[] exitCells;

    private static int exitsMax;
    private static int exitsCount;

    private int cellCount;
    private int direction;
    private int filledCells = 0;

    private boolean opened = false;

    public Exit(int cellCount, int direction) {
        this.cellCount = cellCount;
        this.direction = direction;
        if (!opened) {
            exitsCount++;
        }
        exitCells = new Point[cellCount];
    }

    public static boolean canOpenDoor(){
        return exitsCount < exitsMax;
    }

    public static int getExitsLeft(){
        return exitsMax - exitsCount;
    }

    public static void setExitsMax(int max){
        exitsMax = max;
    }

    public static void resetExitsCount(){
        exitsCount = 0;
    }

    public boolean isFilled(){
        return filledCells == cellCount;
    }

    public boolean addCell(Point cell){
        if (filledCells < cellCount) {
            exitCells[filledCells] = cell;
            filledCells++;
            return true;
        }
        return false;
    }

    public void rotate(int rotation, int roomWidth, int roomHeight){
        for (int i = 0; i < rotation; i++){ //TODO remake rotations
            direction++;
            if (direction == 4){
                direction = 0;
            }
            for (Point point : exitCells) {
                int x = (int) point.getX();
                int y = (int) point.getY();
                point.x = roomWidth - y - 1;
                point.y = x;
            }
        }
    }

    public int getDirection() {
        return direction;
    }

    public Point[] getExitCells() {
        return exitCells;
    }

    public static int getExitsMax() {
        return exitsMax;
    }

    public static int getExitsCount() {
        return exitsCount;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
