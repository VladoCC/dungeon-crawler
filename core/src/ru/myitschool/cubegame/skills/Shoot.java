package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Shoot extends Skill {

    MathAction rollAction = new DiceAction(1, 20);
    MathAction attackAction = new DiceAction(1, 6);

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
        setObstruct(true);
        Play play = new Play() {
            @Override
            public boolean check(Target target) {
                return rollAction.act() + getAccuracyBonus() > target.getEntity().getArmor();
            }
        };
        play.addAction(new Action() {
            @Override
            public void act(Target target, boolean success) {
                if (success) {
                    attackAction = new DiceAction(1, 6);
                    attackAction = countAttackAction(attackAction);
                    Entity entity = target.getEntity();
                    entity.addHp(-attackAction.act());
                }
            }
        });
        addPlay(play);
    }
}
