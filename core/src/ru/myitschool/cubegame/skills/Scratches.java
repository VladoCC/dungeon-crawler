package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 26.10.2017.
 */
public class Scratches extends Skill {

    public Scratches(final Entity doer) {
        super(doer);
        setIcon(new Texture("scratches.png"));
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setTargetType(SKILL_TARGET_TYPE_CHARACTER);
        setTypeDisplayer(SKILL_TARGET_TYPE_CHARACTER);
        setSkillAccuracyBonus(1);
        Action action = new Action(){
            @Override
            public void act(Target target, int success, FloatingDamageMark mark) {
                if (success == Play.TARGETING_CRIT_HIT || success == Play.TARGETING_HIT){
                    MathAction attackAction = new DiceAction(6);
                    Entity entity = target.getEntity();
                    int damage = -countAttackAction(attackAction).act();
                    if (success == Play.TARGETING_CRIT_HIT){
                        attackAction = new DiceAction(8);
                        damage = -countAttackAction(attackAction).max();
                    }
                    entity.addHp(damage);
                    mark.addText(damage + "");
                }
            }
        };
        addPlayContainer().getPlayerPlay().addAction(action);
    }
}