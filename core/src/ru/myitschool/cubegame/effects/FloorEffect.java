package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.DungeonCell;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.skills.Target;

/**
 * Created by Voyager on 29.11.2017.
 */
public class FloorEffect {

    private static Array<FloorEffect> effects = new Array<FloorEffect>();

    Array<DungeonCell> cells = new Array<DungeonCell>();
    Array<Target> nullCells = new Array<Target>();

    Color color = Color.PURPLE;
    boolean show = true;
    CellEffect effect;

    public FloorEffect(Array<Target> cells, CellEffect effect) {
        effects.add(this);
        effect.setFloorEffect(this);
        this.effect = effect;
        for (Target cell : cells){
            if (!addCell(cell)){
                nullCells.add(cell);
            }
        }
        DungeonMap.updateEffects();
    }

    public FloorEffect(Array<Target> cells, CellEffect effect, Color color, boolean show) {
        this(cells, effect);
        this.color = color;
        this.show = show;
    }

    public boolean addCell(Target target) {
        DungeonCell cell = DungeonMap.getCell(target.getX(), target.getY());
        if (cell != null){
            cells.add(cell);
            cell.addEffect((CellEffect) effect.clone());
            DungeonMap.updateEffects();
            return true;
        }
        return false;
    }

    public void updateCells(Vector2 movement){
        for (Target target : nullCells){
            target.move(movement);
            if (addCell(target)){
                nullCells.removeValue(target, false);
            }
        }
    }

    public static void updateEffects(Vector2 movements){
        for (FloorEffect effect : effects){
            effect.updateCells(movements);
        }
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
            cell.removeEffect(effect);
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

    public Array<Target> getNullCells() {
        return nullCells;
    }
}
