package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.BloodiedEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

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
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        setType(SKILL_TYPE_COOLDOWN);
        setCooldownMax(4);

        addPlayContainer().getEnemyPlay().addAction(new Action() {
            @Override
            public void act(Target target, int success, FloatingDamageMark mark) {
                Entity entity = target.getEntity();
                int damage = 0;
                if (success == Play.TARGETING_HIT){
                    damage = -countAttackAction(attackAction).act();
                    entity.addEffect(new BloodiedEffect(entity, 3, 3));
                } else if (success == Play.TARGETING_CRIT_HIT){
                    damage = -countAttackAction(attackAction).max();
                    entity.addEffect(new BloodiedEffect(entity, 5, 3));
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
