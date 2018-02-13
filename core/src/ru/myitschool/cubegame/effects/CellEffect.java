package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.screens.DungeonScreen;

/**
 * Created by Voyager on 01.12.2017.
 */
public class CellEffect extends Effect implements Cloneable {

    FloorEffect floorEffect;
    int index;
    static int count = 0;

    public CellEffect(FloorEffect floorEffect) {
        super(null);
        this.floorEffect = floorEffect;
        index = DungeonScreen.addLog("");
    }

    public FloorEffect getFloorEffect() {
        return floorEffect;
    }

    public void setFloorEffect(FloorEffect floorEffect) {
        this.floorEffect = floorEffect;
    }

    public void onStepTo(int x1, int y1, int x2, int y2, Entity entity){
        if (!hasCell(x1, y1) && hasCell(x2, y2)){
            stepToAction(entity);
            count++;
            DungeonScreen.changeLog(count + ": To zone", index);
            System.out.println("TO ZONE!!!");
        } else if (hasCell(x1, y1) && hasCell(x2, y2)){
            movingInZoneAction(entity);
            count++;
            DungeonScreen.changeLog(count + ": In zone", index);
            System.out.println("IN ZONE!!!");
        }
    }

    protected void stepToAction(Entity entity){

    }

    protected void movingInZoneAction(Entity entity){

    }

    public void onStepFrom(int x1, int y1, int x2, int y2, Entity entity){
        if (!hasCell(x2, y2) && hasCell(x1, y1)){
            stepFromAction(entity);
            count++;
            DungeonScreen.changeLog(count + ": From zone", index);
            System.out.println("FROM ZONE!!!");
        }
    }

    protected void stepFromAction(Entity entity){

    }

    private boolean hasCell(int x, int y) {
        return floorEffect.hasCell(x, y);
    }

    @Override
    protected Object clone() {
        try {
            CellEffect effect = (CellEffect) super.clone();
            effect.setStackSize(getStackSize());
            effect.setType(getType());
            effect.setStackable(isStackable());
            effect.setSkillUse(isSkillUse());
            effect.setDelete(isDelete());
            effect.setDescription(getDescription());
            effect.setExpireTurns(getExpireTurns());
            effect.setExpiring(isExpiring());
            effect.setHide(isHide());
            effect.setIcon(getIcon());
            effect.setName(getName());
            effect.setPositive(isPositive());
            effect.setFloorEffect(getFloorEffect());
            return effect;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
