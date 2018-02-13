package ru.myitschool.cubegame.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Voyager on 07.12.2017.
 */
public class ColorTile extends DungeonTile {

    private final int ID = 1;

    public ColorTile(Color color) {
        super(null, 1, true, 1);
        color.a = 0.75f;
        Pixmap pixmap = new Pixmap(DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        TextureRegion tileRegion = new TextureRegion(new Texture(pixmap));
        setTextureRegion(tileRegion);
    }

    public int getID() {
        return ID;
    }
}
