package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.entities.EventAdapter;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 04.06.2017.
 */
public class Effect extends EventAdapter implements Cloneable {

    protected int type;

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
        this.entity = entity;
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

    public Texture getIcon() {
        return icon;
    }

    public void setIcon(Texture icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSkillUse() {
        return skillUse;
    }

    public void setSkillUse(boolean skillUse) {
        this.skillUse = skillUse;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    public boolean isExpiring() {
        return expiring;
    }

    public void setExpiring(boolean expiring) {
        this.expiring = expiring;
    }

    public int getExpireTurns() {
        return expireTurns;
    }

    public void setExpireTurns(int expireTurns) {
        this.expireTurns = expireTurns;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void onExpire(){

    }

    @Override
    public Object clone() {
        try {
            Effect effect = (Effect) super.clone();
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

    }

    @Override
    public void endTurn() {
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

    }

    @Override
    public void endMove() {

    }

    @Override
    public int countMp(boolean withMovement) {
        return 0;
    }

    @Override
    public boolean canUseSkill() {
        return false;
    }

    @Override
    public void startSkill() {

    }

    @Override
    public void endSkill() {

    }

    @Override
    public MathAction attackBonus(MathAction action) {
        return action;
    }

    @Override
    public int onDamage(int damage) {
        return damage;
    }

    @Override
    public int onHeal(int heal) {
        return heal;
    }
}
