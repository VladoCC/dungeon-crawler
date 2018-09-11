package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;

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
        Action action = new Action(this){
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_CRIT_HIT || success == Play.TARGETING_HIT){
                    MathAction attackAction = new DiceAction(6);
                    Entity entity = target.getEntity();
                    damage = -countAttackAction(attackAction).act();
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