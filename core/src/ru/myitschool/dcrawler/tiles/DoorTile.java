package ru.myitschool.dcrawler.tiles;

/**
 * Created by Voyager on 18.04.2017.
 */
public class DoorTile extends DungeonTile {

    private final int ID = 1;

    public DoorTile() {
        super(0, 1, true, 1);
        setDoor(true);
    }

    public int getID() {
        return ID;
    }
}
