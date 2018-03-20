package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;
import ru.myitschool.cubegame.math.NumberAction;
import ru.myitschool.cubegame.math.SumAction;

/**
 * Created by Voyager on 16.11.2017.
 */
public class Slash  extends Skill {

    MathAction rollAction = new SumAction(new DiceAction(1, 20), new NumberAction(2));

    public Slash(Entity doer) {
        super(doer);
        setIcon(new Texture("slash.png"));
        setName("Slash");
        setDescription("You swings with your halberd and deal damage to all enemies in front of you");
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setTypeDisplayer(SKILL_TARGET_TYPE_FLOOR_SWING);
        setTargetType(SKILL_TARGET_TYPE_FLOOR_SWING);
        Play play = new Play() {
            @Override
            public boolean check(Target target) {
                Entity entity = target.getEntity();
                if (entity != null) {
                    return rollAction.act() + getAccuracyBonus() > entity.getArmor();
                }
                return false;
            }
        };
        play.addAction(new Action() {
            @Override
            public void act(Target target, boolean success, FloatingDamageMark mark) {
                if (success){
                    MathAction attackAction = new DiceAction(1, 8);
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
