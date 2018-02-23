package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Vampirism extends Skill {

    MathAction rollAction = new DiceAction(1, 20);

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
                    MathAction attackAction = new DiceAction(1, 6);
                    attackAction = countAttackAction(attackAction);
                    Entity entity = target.getEntity();
                    int damage = -attackAction.act();
                    int hp = entity.addHp(damage);
                    doer.addHp(Math.abs(hp));
                    mark.addText(damage + "");
                }
            }
        });
        addPlay(play);
    }
}
