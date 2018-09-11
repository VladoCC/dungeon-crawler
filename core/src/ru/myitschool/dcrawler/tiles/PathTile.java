package ru.myitschool.dcrawler.tiles;

/**
 * Created by Voyager on 04.05.2017.
 */
public class PathTile extends DungeonTile {

    private final int ID = 4;

    public PathTile() {
        super(4, true, 1);
    }

    public int getID() {
        return ID;
    }
}
