package ru.myitschool.dcrawler.dungeon;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.pathfinding.GraphStorage;
import ru.myitschool.dcrawler.effects.CellEffect;
import ru.myitschool.dcrawler.effects.Effect;
import ru.myitschool.dcrawler.encounters.Encounter;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.event.EntityEvent;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.tiles.DungeonTile;

/**
 * Created by Voyager on 16.05.2017.
 */
public class DungeonCell extends TiledMapTileLayer.Cell implements EntityEvent {

    private boolean occupied = false;
    private Entity entity;
    private Array<CellEffect> effects = new Array<CellEffect>();

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
        for (CellEffect effect : effects){
            effect.setEntity(entity);
        }
    }

    public boolean hasEntity(){
        return entity != null;
    }

    public CellEffect getEffect(int index) {
        return effects.get(index);
    }

    public Array<CellEffect> getEffects() {
        return effects;
    }

    public void addEffect(CellEffect effect) {
        this.effects.add(effect);
        if (effect != null){
            effect.setEntity(entity);
        }
    }

    public void removeEffect(CellEffect effect){
        effects.removeValue(effect, true);
    }

    public void onStepTo(int x1, int y1, int x2, int y2, Entity entity){
        for (CellEffect effect : effects){
            effect.stepToAction(x1, y1, x2, y2, entity);
        }
    }

    public void onStepFrom(int x1, int y1, int x2, int y2, Entity entity){
        for (CellEffect effect : effects){
            effect.stepFromAction(x1, y1, x2, y2, entity);
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



    @Override
    public void startTurn() {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            effect.startTurn();
        }
    }

    @Override
    public void endTurn() {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            effect.endTurn();
        }
    }

    @Override
    public void startMove() {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            effect.startMove();
        }
    }

    @Override
    public void endMove() {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            effect.endMove();
        }
    }

    @Override
    public int countMp(boolean withMovement) {
        int mp = 0;
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            mp += effect.countMp(withMovement);
        }
        return mp;
    }

    @Override
    public boolean canUseSkill() {
        boolean use = false;
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            if (effect.isSkillUse()) {
                use = effect.canUseSkill();
            }
        }
        return use;
    }

    @Override
    public void startSkill() {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            effect.startSkill();
        }
    }

    @Override
    public void endSkill() {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            effect.endSkill();
        }
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            action = effect.attackBonus(action);
        }
        return action;
    }

    @Override
    public int accuracyBonus(int accuracy, Entity target) {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            accuracy = effect.accuracyBonus(accuracy, target);
        }
        return accuracy;
    }

    @Override
    public int onDamage(int damage) {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            damage = effect.onDamage(damage);
        }
        return damage;
    }

    @Override
    public int onHeal(int heal) {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            heal = effect.onHeal(heal);
        }
        return heal;
    }

    @Override
    public void onEncounter(Encounter encounter) {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            effect.onEncounter(encounter);
        }
    }

    @Override
    public void onDeath() {
        for (int i = 0; i < effects.size; i++){
            Effect effect = effects.get(i);
            effect.onDeath();
        }
        occupied = false;
        entity = null;
        GraphStorage.createBottomGraph();
    }
}
