package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.effects.ImmobilizedEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.utils.Utils;

/**
 * Created by Voyager on 02.12.2017.
 */
public class ShieldBash extends Skill {

    MathAction attackAction = new DiceAction(6);
    int turns = 1;
    int distance = 1;

    public ShieldBash(Entity doer) {
        super(doer);
        setIcon(new Texture("shield_bash.png"));
        setName("Shield Bash");
        setDescription("You bashes target with your shield immobilizes it for " + turns + " turn(-s), trows it back for " + distance + " cell(-s) and deal " + attackAction.getDescription() + " damage");
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        setType(SKILL_TYPE_COOLDOWN_DICE);
        setCooldownMax(3);
        setObstruct(true);
        setWallTargets(false);
        addPlayContainer().getEnemyPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                    Entity entity = target.getEntity();
                    Utils.pushEntity(getDoer(), target.getEntity(), distance);
                    entity.addEffect(new ImmobilizedEffect(entity, turns));
                }
            }
        }.setAttack(attackAction));
        addPlayContainer();
    }
}
