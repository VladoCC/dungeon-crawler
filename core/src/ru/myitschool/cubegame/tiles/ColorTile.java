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

    public ColorTile(Color color){
        this(color, getAlpha(color.a), true);
    }

    public ColorTile(Color color, float alpha, boolean filled) {
        super(null, 1, true, 1);
        color.a = alpha;
        Pixmap pixmap = new Pixmap(DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        if (filled) {
            pixmap.fill();
        } else {
            pixmap.drawRectangle(1, 1, DungeonTile.TILE_WIDTH - 2, DungeonTile.TILE_HEIGHT - 2);
        }
        TextureRegion tileRegion = new TextureRegion(new Texture(pixmap));
        setTextureRegion(tileRegion);
    }

    public static float getAlpha(float defaultAlpha){
        if (defaultAlpha == 1){
            return 0.75f;
        }
        return defaultAlpha;
    }

    public int getID() {
        return ID;
    }
}
