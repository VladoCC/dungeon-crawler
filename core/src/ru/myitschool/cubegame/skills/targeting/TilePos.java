package ru.myitschool.cubegame.skills.targeting;

import ru.myitschool.cubegame.tiles.DungeonTile;

/**
 * Created by Voyager on 25.02.2018.
 */
public class TilePos {

    private int x;
    private int y;
    private DungeonTile tile;

    public TilePos(int x, int y, DungeonTile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public DungeonTile getTile() {
        return tile;
    }
}
