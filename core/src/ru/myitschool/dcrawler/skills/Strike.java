package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.RepeatAction;
import ru.myitschool.dcrawler.math.SumAction;

/**
 * Created by Voyager on 15.11.2017.
 */
public class Strike extends Skill {
    
    MathAction attackAction = new SumAction(new RepeatAction(new DiceAction(6), 2));

    public Strike(Entity doer) {
        super(doer);
        setName("Strike");
        setDescription("Strike that deals " + attackAction.getDescription() + " damage");
        setIcon(new Texture("punch.png"));
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        addPlayContainer().getEnemyPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                    damage = -countAttackAction(attackAction).act();
                    if (success == Play.TARGETING_CRIT_HIT){
                        damage = -countAttackAction(attackAction).max();
                    }
                    target.getEntity().addHp(damage);
                    mark.addText(damage + "");
                }
            }
        });
    }
}
