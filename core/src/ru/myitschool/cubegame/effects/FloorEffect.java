package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.DungeonCell;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 29.11.2017.
 */
public class FloorEffect {

    private static Array<FloorEffect> effects = new Array<FloorEffect>();

    Array<DungeonCell> cells = new Array<DungeonCell>();
    Color color = Color.PURPLE;
    boolean show = true;
    CellEffect effect;

    public FloorEffect(Array<DungeonCell> cells, CellEffect effect) {
        effects.add(this);
        effect.setFloorEffect(this);
        this.effect = effect;
        for (DungeonCell cell : cells){
            addCell(cell);
        }
        DungeonMap.updateEffects();
    }

    public FloorEffect(Array<DungeonCell> cells, CellEffect effect, Color color, boolean show) {
        this(cells, effect);
        this.color = color;
        this.show = show;
    }

    public void addCell(DungeonCell cell){
        cells.add(cell);
        cell.setEffect((CellEffect) effect.clone());
        DungeonMap.updateEffects();
    }

    public CellEffect getEffect() {
        return effect;
    }

    public void setEffect(CellEffect effect) {
        this.effect = effect;
    }

    public void setCells(Array<DungeonCell> cells) {
        this.cells = cells;
        DungeonMap.updateEffects();
    }

    public void removeEffect(){
        for (DungeonCell cell : cells) {
            cell.setEffect(null);
        }
        cells.clear();
        setShow(false);
        DungeonMap.updateEffects();
    }

    public boolean hasCell(int x, int y){
        DungeonCell cell = DungeonMap.getCell(x, y);
        for (DungeonCell thisCell : cells) {
            if (cell == thisCell){
                return true;
            }
        }
        return false;
    }

    public static Array<FloorEffect> getEffects() {
        return effects;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Array<DungeonCell> getCells() {
        return cells;
    }
}
