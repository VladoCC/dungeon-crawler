package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.effects.BloodiedEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.NumberAction;

/**
 * Created by Voyager on 18.11.2017.
 */
public class JaggedSword extends Skill {

    MathAction attackAction = new DiceAction(6);

    public JaggedSword(Entity doer) {
        super(doer);
        setName("Jagged sword");
        setDescription("Deal some damage and adds bleed to target");
        setIcon(new Texture("blood.png"));
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setTargetType(SKILL_TARGET_TYPE_ENTITY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENTITY);
        setType(SKILL_TYPE_COOLDOWN);
        setCooldownMax(4);

        addPlayContainer().getEnemyPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                Entity entity = target.getEntity();
                damage = 0;
                if (success == Play.TARGETING_HIT){
                    damage = -countAttackAction(attackAction).act();
                    entity.addEffect(new BloodiedEffect(entity, new NumberAction(3), 3));
                } else if (success == Play.TARGETING_CRIT_HIT){
                    damage = -countAttackAction(attackAction).max();
                    entity.addEffect(new BloodiedEffect(entity, new NumberAction(5), 3));
                }
                entity.addHp(damage);
                if (damage != 0) {
                    mark.addText(damage + "");
                }
            }
        });
        addPlayContainer();
    }
}
