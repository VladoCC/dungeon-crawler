package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Shoot extends Skill {

    MathAction attackAction = new DiceAction(6);
    int accuracyBonus = 6;

    public Shoot(Entity doer) {
        super(doer);
        setIcon(new Texture("shoot.png"));
        setName("Shoot");
        setDescription("Shot that deals " + attackAction.getDescription() + " damage and accuracy bonus +" + accuracyBonus);
        setTargetCountMax(1);
        setDistanceMax(16);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        setObstruct(true);
        setSkillAccuracyBonus(accuracyBonus);
        addPlayContainer().getEnemyPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                    Entity entity = target.getEntity();
                    damage = -countAttackAction(attackAction).act();
                    if (success == Play.TARGETING_CRIT_HIT){
                        damage = -countAttackAction(attackAction).max();
                    }
                    entity.addHp(damage);
                    mark.addText(damage + "");
                }
            }
        });
        addPlayContainer();
    }
}
