package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 25.11.2017.
 */
public class Bomb extends Skill {

    MathAction rollAction = new DiceAction(1, 20);
    MathAction attackAction = new DiceAction(1, 4);

    public Bomb(Entity doer) {
        super(doer);
        setName("Bomb");
        setDescription("Deal " + attackAction.getDescription() + " damage to all creatures in small zone");
        setIcon(new Texture("bomb.png"));
        setTargetCountMax(1);
        setDistanceMax(5);
        setDistanceMin(2);
        setRange(2);
        setTargetType(SKILL_TARGET_TYPE_FLOOR_SPLASH);
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
                if (success) {
                    attackAction = new DiceAction(1, 4);
                    attackAction = countAttackAction(attackAction);
                    Entity entity = target.getEntity();
                    int damage = -attackAction.act();
                    entity.addHp(damage);
                    mark.addText(damage + "");
                }
            }
        });
        addPlay(play);
    }
}
