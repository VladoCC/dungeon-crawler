package ru.myitschool.dcrawler.dungeon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

/**
 * Created by Voyager on 09.05.2017.
 */
public class Door {

    private static Array<Door> doors = new Array<Door>();

    private int direction;

    Point[] doorCells;

    public Door(int direction, Point[] doorCells) {
        this.direction = direction;
        this.doorCells = doorCells;
        doors.add(this);
    }

    public int getDirection() {
        return direction;
    }
    
    public static int getDoorIndex(int x, int y){
        for (int i = 0; i < doors.size; i++) {
            Door door = doors.get(i);
            for (Point cell : door.doorCells) {
                if (cell.x == x && cell.y == y) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Door getDoor(int x, int y){
        return getDoor(getDoorIndex(x, y));
    }

    public static Door getDoor(int index){
        if (index != -1) {
            return doors.get(index);
        }
        return null;
    }

    public static void removeDoor(int x, int y){
        removeDoor(getDoorIndex(x, y));
    }

    public static void removeDoor(int index){
        if (index != -1) {
            doors.removeIndex(index);
        }
    }

    public Point[] getDoorCells() {
        return doorCells;
    }

    public void moveDoor(Vector2 tiles){
        for (Point cell : doorCells){
            cell.x += tiles.x;
            cell.y += tiles.y;
        }
    }

    public static void moveDoors(Vector2 tiles){
        for (Door door1 : doors){
            for (Point cell : door1.getDoorCells()){
                System.out.println("Door cell: (" + cell.x + "; " + cell.y + ")");
            }
        }
        System.out.println();
        for (Door door : doors){
            door.moveDoor(tiles);
        }
        for (Door door1 : doors){
            for (Point cell : door1.getDoorCells()){
                System.out.println("Door cell: (" + cell.x + "; " + cell.y + ")");
            }
        }
    }

    public static Array<Door> getDoors() {
        return doors;
    }
}
