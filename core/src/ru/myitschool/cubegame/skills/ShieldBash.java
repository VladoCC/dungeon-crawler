package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.ImmobilizedEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class ShieldBash extends Skill {

    MathAction attackAction = new DiceAction(6);

    public ShieldBash(Entity doer) {
        super(doer);
        setIcon(new Texture("shield_bash.png"));
        setName("Shield Bash");
        setDescription("You bashes target with your shield immobilizes it and deal " + attackAction.getDescription() + " damage");
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        setType(SKILL_TYPE_COOLDOWN_DICE);
        setCooldownMax(2);
        setObstruct(false);
        setWallTargets(false);
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
                    entity.addEffect(new ImmobilizedEffect(entity, 1));
                    mark.addText(damage + "");
                }
            }
        });
        addPlayContainer();
    }
}
