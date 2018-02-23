package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class SpearSting extends Skill {

    MathAction rollAction = new DiceAction(1, 20);
    MathAction attackAction = new DiceAction(1, 12);

    public SpearSting(Entity doer) {
        super(doer);
        setIcon(new Texture("spear_sting.png"));
        setName("Spear sting");
        setDescription("You attacks all enemies that stands in two cells in front of you and deal " + attackAction.getDescription() + " damage");
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setRange(2);
        setTargetType(SKILL_TARGET_TYPE_FLOOR_WAVE);
        Play play = new Play() {
            @Override
            public boolean check(Target target) {
                if (target.getEntity() != null) {
                    return rollAction.act() + getAccuracyBonus() > target.getEntity().getArmor();
                }
                return false;
            }
        };
        play.addAction(new Action() {
            @Override
            public void act(Target target, boolean success, FloatingDamageMark mark) {
                if (success) {
                    attackAction = new DiceAction(1, 12);
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
