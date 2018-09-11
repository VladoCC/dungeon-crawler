package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.dungeon.DungeonCell;
import ru.myitschool.dcrawler.dungeon.DungeonMap;
import ru.myitschool.dcrawler.skills.Target;

/**
 * Created by Voyager on 29.11.2017.
 */
public class FloorEffect {

    private static Array<FloorEffect> effects = new Array<FloorEffect>();

    Array<DungeonCell> cells = new Array<DungeonCell>();
    Array<Target> nullCells = new Array<Target>();

    Color color;
    boolean show = true;
    boolean activate = true;
    CellEffect effect;

    public FloorEffect(Array<Target> cells, CellEffect effect, Color color, boolean show, boolean activate) {
        effect.setFloorEffect(this);
        this.effect = effect;
        for (Target cell : cells){
            if (!activate || (activate && !addCell(cell))){
                nullCells.add(cell);
            }
        }
        this.activate = activate;
        this.color = color;
        this.show = show;
        if (activate) {
            effects.add(this);
            DungeonMap.updateEffects();
        }
    }

    public FloorEffect(Array<Target> cells, CellEffect effect, Color color, boolean show) {
        this(cells, effect, color, show, true);
    }

    public FloorEffect(Array<Target> cells, CellEffect effect, boolean show, boolean activate) {
        this(cells, effect, null, show, activate);
    }

    public FloorEffect(Array<Target> cells, CellEffect effect, boolean activate) {
        this(cells, effect, null, false, activate);
    }

    public void activate(){
        if (!activate){
            activate = true;
            Array<Target> newCells = new Array<Target>();
            for (int i = 0; i < nullCells.size; i++) {
                Target cell = nullCells.get(i);
                if (!addCell(cell)){
                    newCells.add(cell);
                }
            }
            nullCells = newCells;
        }
        effects.add(this);
        DungeonMap.updateEffects();
    }

    public boolean addCell(Target target) {
        DungeonCell cell = DungeonMap.getCell(target.getX(), target.getY());
        if (cell != null){
            cells.add(cell);
            cell.addEffect((CellEffect) effect.clone());
            return true;
        }
        return false;
    }

    public void updateCells(Vector2 movement){
        for (int i = 0; i < nullCells.size; i++) {
            Target target = nullCells.get(i);
            target.move(movement);
            if (addCell(target)) {
                nullCells.removeValue(target, false);
            }
        }
    }

    public static void updateEffects(Vector2 movements){
        for (FloorEffect effect : effects){
            effect.updateCells(movements);
        }
        DungeonMap.updateEffects();
    }

    public static void clearEffects(){
        for (FloorEffect effect : effects){
            effect.removeEffect(false);
        }
        effects.clear();
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

    public void removeEffect(boolean update){
        for (DungeonCell cell : cells) {
            cell.removeEffect(effect);
        }
        cells.clear();
        setShow(false);
        if (update) {
            DungeonMap.updateEffects();
        }
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
