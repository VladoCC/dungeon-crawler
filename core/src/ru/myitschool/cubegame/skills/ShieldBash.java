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

    MathAction rollAction = new DiceAction(1, 20);
    MathAction attackAction = new DiceAction(1, 4);

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
        setType(SKILL_TYPE_COOLDOWN_DICE);
        setCooldownMax(2);
        setObstruct(false);
        setWallTargets(false);
        Play play = new Play() {
            @Override
            public boolean check(Target target) {
                return rollAction.act() + getAccuracyBonus() > target.getEntity().getArmor();
            }
        };
        play.addAction(new Action() {
            @Override
            public void act(Target target, boolean success, FloatingDamageMark mark) {
                if (success) {
                    attackAction = new DiceAction(1, 4);
                    attackAction = countAttackAction(attackAction);
                    Entity entity = target.getEntity();
                    int damage = -attackAction.act();
                    entity.addHp(damage);
                    entity.addEffect(new ImmobilizedEffect(entity, 1));
                    mark.addText(damage + "");
                }
            }
        });
        addPlay(play);
    }
}
