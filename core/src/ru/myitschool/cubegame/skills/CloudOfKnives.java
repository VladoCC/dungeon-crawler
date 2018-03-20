package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.CloudOfKnivesEffect;
import ru.myitschool.cubegame.effects.FloorClearingEffect;
import ru.myitschool.cubegame.effects.FloorEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 30.11.2017.
 */
public class CloudOfKnives extends Skill {

    MathAction rollAction = new DiceAction(1, 20);
    MathAction attackAction = new DiceAction(1, 6);

    public CloudOfKnives(Entity doer) {
        super(doer);
        setIcon(new Texture("cloud_knives.png"));
        setName("Cloud ofKnives");
        setDescription("You creates cloud of psi energy, that deal damage to all creatures in zone instantly and everytimecreature starts turn in zone on enter the zone to the end of your next turn");
        setTargetCountMax(1);
        setDistanceMax(5);
        setDistanceMin(3);
        setRange(3);
        setTargetType(SKILL_TARGET_TYPE_FLOOR_SPLASH);
        setTypeDisplayer(SKILL_TARGET_TYPE_FLOOR_SPLASH);
        setType(SKILL_TYPE_ENCOUNTER);
        setWallTargets(true);
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
                    Entity entity = target.getEntity();
                    int damage = -attackAction.act();
                    entity.addHp(damage);
                    mark.addText(damage + "");
                } else {
                    System.out.println("MISS!");
                }
            }
        });
        addPlay(play);
    }

    @Override
    public void use() {
        attackAction = new DiceAction(1, 6);
        attackAction = countAttackAction(attackAction);
        FloorEffect effect = null;
        effect = new FloorEffect(getTargets(), new CloudOfKnivesEffect(effect, attackAction));
        getDoer().addEffect(new FloorClearingEffect(effect));
        super.use();
    }
}
