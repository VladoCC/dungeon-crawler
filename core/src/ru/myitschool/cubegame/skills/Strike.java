package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 15.11.2017.
 */
public class Strike extends Skill {

    MathAction rollAction = new DiceAction(1, 20);
    MathAction attackAction = new DiceAction(2, 6);

    public Strike(Entity doer) {
        super(doer);
        setName("Strike");
        setDescription("Strike that deals " + attackAction.getDescription() + " damage");
        setIcon(new Texture("punch.png"));
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setTargetType(SKILL_TARGET_TYPE_CHARACTER);
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
                    attackAction = new DiceAction(2, 6);
                    attackAction = countAttackAction(attackAction);
                    int damage = -attackAction.act();
                    target.getEntity().addHp(damage);
                    mark.addText(damage + "");
                }
            }
        });
        addPlay(play);
    }
}
