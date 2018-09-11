package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 16.11.2017.
 */
public class Slash  extends Skill {

    MathAction attackAction = new DiceAction(8);

    public Slash(Entity doer) {
        super(doer);
        setIcon(new Texture("slash.png"));
        setName("Slash");
        setDescription("You swings with your halberd and deal damage to all enemies in front of you");
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setTypeDisplayer(SKILL_TARGET_TYPE_FLOOR_SWING);
        setTargetType(SKILL_TARGET_TYPE_FLOOR_SWING);
        setSkillAccuracyBonus(2);
        addPlayContainer().getEntityPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT){
                    damage = -countAttackAction(attackAction).act();
                    if (success == Play.TARGETING_CRIT_HIT){
                        damage = -countAttackAction(attackAction).max();
                    }
                    target.getEntity().addHp(damage);
                    mark.addText(damage + "");
                }
            }
        });
        addPlayContainer();
    }
}
