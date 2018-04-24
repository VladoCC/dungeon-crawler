package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Vampirism extends Skill {

    public Vampirism(final Entity doer) {
        super(doer);
        setIcon(new Texture("vampirism.png"));
        setName("Vampirism");
        setDescription("You deal damage to your target and restore as many hp as it loses");
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        addPlayContainer().getEnemyPlay().addAction(new Action() {
            @Override
            public void act(Target target, int success, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                    MathAction attackAction = new DiceAction(6);
                    Entity entity = target.getEntity();
                    int damage = -countAttackAction(attackAction).act();
                    if (success == Play.TARGETING_CRIT_HIT){
                        damage = -countAttackAction(attackAction).max();
                    }
                    int hp = entity.addHp(damage);
                    doer.addHp(Math.abs(hp));
                    mark.addText(damage + "");
                    FloatingDamageMark damageMark = new FloatingDamageMark(doer.getTileX(), doer.getTileY(), -damage + "");
                    damageMark.show();
                }
            }
        });
        addPlayContainer();
    }
}
