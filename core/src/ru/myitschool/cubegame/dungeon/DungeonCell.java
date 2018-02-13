package ru.myitschool.cubegame.dungeon;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.myitschool.cubegame.effects.CellEffect;
import ru.myitschool.cubegame.effects.Effect;
import ru.myitschool.cubegame.effects.FloorEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.entities.EventAdapter;
import ru.myitschool.cubegame.tiles.DungeonTile;

/**
 * Created by Voyager on 16.05.2017.
 */
public class DungeonCell extends TiledMapTileLayer.Cell {

    private boolean occupied = false;
    private Entity entity;
    private CellEffect effect;

    private int x;
    private int y;

    public DungeonCell() {
        super();
    }

    public DungeonCell(DungeonTile tile) {
        super();
        setTile(tile);
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (effect != null){
            effect.setEntity(entity);
        }
    }

    public boolean hasEntity(){
        return entity != null;
    }

    public CellEffect getEffect() {
        return effect;
    }

    public void setEffect(CellEffect effect) {
        this.effect = effect;
        if (effect != null){
            effect.setEntity(entity);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
