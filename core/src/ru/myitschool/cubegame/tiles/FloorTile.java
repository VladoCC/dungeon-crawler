package ru.myitschool.cubegame.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
