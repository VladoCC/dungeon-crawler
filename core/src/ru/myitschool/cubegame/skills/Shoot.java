package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Shoot extends Skill {

    MathAction attackAction = new DiceAction(6);

    public Shoot(Entity doer) {
        super(doer);
        setIcon(new Texture("shoot.png"));
        setName("Shoot");
        setDescription("Shot that deals damage");
        setTargetCountMax(1);
        setDistanceMax(6);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        setObstruct(true);
        addPlayContainer().getEnemyPlay().addAction(new Action() {
            @Override
            public void act(Target target, int success, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                    Entity entity = target.getEntity();
                    int damage = -countAttackAction(attackAction).act();
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
