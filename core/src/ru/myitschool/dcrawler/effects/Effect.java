package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.event.EntityEventAdapter;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 04.06.2017.
 */
public abstract class Effect extends EntityEventAdapter implements Cloneable {

    protected String type;

    private Entity entity;
    private Texture icon;
    private boolean hide;
    private boolean delete;
    private boolean skillUse;
    private boolean positive;
    private String name;
    private String description;
    private int id;
    private boolean stackable;
    private int stackSize;
    private boolean expiring;
    private int expireTurns;

    public Effect(Entity entity) {
        setEntity(entity);
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public abstract Texture getIcon();

    public abstract String getName();

    public abstract String getDescription();

    public abstract boolean isSkillUse();

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract boolean isPositive();

    public abstract boolean isStackable();

    public abstract int getStackSize();

    public abstract boolean isExpiring();

    public abstract int getExpireTurns();

    public abstract String getType();

    public void onExpire(){

    }

    @Override
    public Object clone() {
        try {
            Effect effect = (Effect) super.clone();
            effect.setDelete(isDelete());
            effect.setHide(isHide());
            return effect;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /** Events */
    public void adding(){

    }

    @Override
    public void startTurn() {
        super.startTurn();
    }

    @Override
    public void endTurn() {
        super.endTurn();
        System.out.println(expireTurns + " abra-kad-abra");
        if (expiring){
            if (expireTurns > 0){
                expireTurns--;
            } else {
                setDelete(true);
                onExpire();
            }
        }
    }

    @Override
    public void startMove() {
        super.startMove();
    }

    @Override
    public void endMove() {
        super.endMove();
    }

    @Override
    public int countMp(boolean withMovement) {
        super.countMp(withMovement);
        return 0;
    }

    @Override
    public boolean canUseSkill() {
        super.canUseSkill();
        return false;
    }

    @Override
    public void startSkill() {
        super.startSkill();
    }

    @Override
    public void endSkill() {
        super.endSkill();
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        super.attackBonus(action);
        return action;
    }

    @Override
    public int onDamage(int damage) {
        super.onDamage(damage);
        return damage;
    }

    @Override
    public int onHeal(int heal) {
        super.onHeal(heal);
        return heal;
    }
}
