package ru.myitschool.dcrawler.tiles;

/**
 * Created by Voyager on 18.04.2017.
 */
public class FloorTile extends DungeonTile {

    private final int ID = 0;

    public FloorTile() {
        super(0, 0, true, 1);
    }

    public int getID() {
        return ID;
    }
}
