package ru.myitschool.cubegame.tiles;

/**
 * Created by Voyager on 08.07.2017.
 */
public class TargetTile extends DungeonTile {

    private final int ID = 5;

    public TargetTile() {
        super(5,true,1);
    }

    public int getID() {
        return ID;
    }
}
