package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;

/**
 * Created by Voyager on 04.06.2017.
 */
public class AttackEffect extends Effect {

    private String name = "Attack";
    private String description = "You may attack or make second move in this turn.";
    private Texture icon = new Texture("sword.png");
    private boolean used = false;
    private boolean positive = true;

    public AttackEffect(Entity entity) {
        super(entity);
        setName(name);
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setSkillUse(true);
        type = 1;
    }

    @Override
    public void startMove() {
        if (getEntity().isMoved()) {
            getEntity().addMp(getEntity().getMpMax());
            used = true;
        }if (getEntity().isMoved()){
            setDelete(true);
            Entity.updateSkills();
        }
    }

    @Override
    public void endMove() {
    }

    @Override
    public int countMp(boolean withMovement) {
        if (getEntity().isMoved()/* && getEntity().getPath() != null */&& !isDelete() && !used) {
            if (!getEntity().isMovement()) {
                System.out.println("Adding MP");
                setHide(true);
                return getEntity().getMpMax();
            }
        } else if (getEntity().isMoved() && !withMovement && !isDelete() && !used){
            if (!getEntity().isMovement()) {
                return getEntity().getMpMax();
            }
        } else if (isHide() && !getEntity().isMovement()) {
            setHide(false);
        }
        return 0;
    }

    @Override
    public boolean canUseSkill() {
        return !isDelete();
    }

    @Override
    public void endSkill() {
        setDelete(true);
        Entity.updateSkills();
    }

    @Override
    public void endTurn() {
        setDelete(true);
    }
}
