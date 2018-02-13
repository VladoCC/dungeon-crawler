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

    MathAction rollAction = new DiceAction(1, 20);

    public JaggedSword(Entity doer) {
        super(doer);
        setName("Jagged sword");
        setDescription("Deal some damage and adds bleed to target");
        setIcon(new Texture("blood.png"));
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setType(SKILL_TYPE_COOLDOWN);
        setCooldownMax(4);
        Play play = new Play() {
            @Override
            public boolean check(Target target) {
                return rollAction.act() + getAccuracyBonus() > target.getEntity().getArmor();
            }
        };
        play.addAction(new Action() {
            @Override
            public void act(Target target, boolean success) {
                if (success){
                    MathAction attackAction = new DiceAction(1, 6);
                    attackAction = countAttackAction(attackAction);
                    Entity entity = target.getEntity();
                    entity.addHp(-attackAction.act());
                    entity.addEffect(new BloodiedEffect(entity, 3, 3));
                }
            }
        });
        addPlay(play);
    }
}
