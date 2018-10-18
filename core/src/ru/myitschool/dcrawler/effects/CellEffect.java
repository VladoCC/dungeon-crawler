package ru.myitschool.dcrawler.effects;

import ru.myitschool.dcrawler.entities.Entity;

/**
 * Created by Voyager on 01.12.2017.
 */
public abstract class CellEffect extends Effect {

    FloorEffect floorEffect;

    public CellEffect(FloorEffect floorEffect) {
        super(null);
        this.floorEffect = floorEffect;
    }

    public FloorEffect getFloorEffect() {
        return floorEffect;
    }

    public void setFloorEffect(FloorEffect floorEffect) {
        this.floorEffect = floorEffect;
    }

    public void stepToAction(int x1, int y1, int x2, int y2, Entity entity){
        if (!hasCell(x1, y1) && hasCell(x2, y2)){
            onStepTo(entity);
            System.out.println("TO ZONE!!!");
        } else if (hasCell(x1, y1) && hasCell(x2, y2)){
            onMovingInZone(entity);
            System.out.println("IN ZONE!!!");
        }
    }

    protected void onStepTo(Entity entity){

    }

    protected void onMovingInZone(Entity entity){

    }

    public void stepFromAction(int x1, int y1, int x2, int y2, Entity entity){
        if (!hasCell(x2, y2) && hasCell(x1, y1)){
            onStepFrom(entity);
            System.out.println("FROM ZONE!!!");
        }
    }

    protected void onStepFrom(Entity entity){

    }

    private boolean hasCell(int x, int y) {
        return floorEffect.hasCell(x, y);
    }

    @Override
    public Object clone() {
        try {
            CellEffect effect = (CellEffect) super.clone();
            effect.setFloorEffect(getFloorEffect());
            return effect;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellEffect effect = (CellEffect) o;

        return floorEffect != null ? floorEffect.equals(effect.floorEffect) : effect.floorEffect == null;
    }
}
