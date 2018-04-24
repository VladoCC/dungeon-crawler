package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;
import ru.myitschool.cubegame.math.RepeatAction;
import ru.myitschool.cubegame.math.SumAction;

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
        addPlayContainer().getEnemyPlay().addAction(new Action() {
            @Override
            public void act(Target target, int success, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                    int damage = -countAttackAction(attackAction).act();
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
