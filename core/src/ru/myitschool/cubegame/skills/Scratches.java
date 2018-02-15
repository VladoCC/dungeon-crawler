package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;
import ru.myitschool.cubegame.math.NumberAction;
import ru.myitschool.cubegame.math.SumAction;

/**
 * Created by Voyager on 26.10.2017.
 */
public class Scratches extends Skill {

    MathAction rollAction = new SumAction(new DiceAction(1, 20), new NumberAction(5));

    public Scratches(final Entity doer) {
        super(doer);
        setIcon(new Texture("scratches.png"));
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setTargetType(SKILL_TARGET_TYPE_CHARACTER);
        Play play = new Play() {
            @Override
            public boolean check(Target target) {
                Entity entity = target.getEntity();
                int armor = entity.getArmor();
                return true;//rollAction.act() + getAccuracyBonus() > armor;
            }
        };
        Action action = new Action() {
            @Override
            public void act(Target target, boolean success) {
                if (success){
                    MathAction attackAction = new SumAction(new DiceAction(1, 6), new NumberAction(4));
                    attackAction = countAttackAction(attackAction);
                    Entity entity = target.getEntity();
                    int damage = -attackAction.act();
                    entity.addHp(damage);
                }
            }
        };
        play.addAction(action);
        addPlay(play);
    }
}
