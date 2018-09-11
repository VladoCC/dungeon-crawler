package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 25.11.2017.
 */
public class Bomb extends Skill {

    MathAction rollAction = new DiceAction(20);
    MathAction attackAction = new DiceAction(4);

    public Bomb(Entity doer) {
        super(doer);
        setName("Bomb");
        setDescription("Deal " + attackAction.getDescription() + " damage to all creatures in small zone");
        setIcon(new Texture("bomb.png"));
        setTargetCountMax(1);
        setDistanceMax(8);
        setDistanceMin(2);
        setRange(2);
        setTargetType(SKILL_TARGET_TYPE_FLOOR_SPLASH);
        setTypeDisplayer(SKILL_TARGET_TYPE_FLOOR_SPLASH);
        addPlayContainer().getEntityPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                Entity entity = target.getEntity();
                damage = 0;
                if (success == Play.TARGETING_HIT) {
                    damage = -countAttackAction(attackAction).act();
                } else if (success == Play.TARGETING_CRIT_HIT){
                    damage = -countAttackAction(attackAction).max();
                }
                entity.addHp(damage);
                if (damage != 0) {
                    mark.addText(damage + "");
                }
            }
        });
    }
}
