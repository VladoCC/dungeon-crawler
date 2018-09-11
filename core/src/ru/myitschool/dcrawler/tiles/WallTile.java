package ru.myitschool.dcrawler.tiles;

/**
 * Created by Voyager on 18.04.2017.
 */
public class WallTile extends DungeonTile {

    private final int ID = 2;

    public WallTile() {
        super(2, false, -1);
    }

    public int getID() {
        return ID;
    }
}
