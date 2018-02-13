package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.ImmobilizedEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Immobilize extends Skill {

    MathAction rollAction = new DiceAction(1, 20);

    public Immobilize(Entity doer) {
        super(doer);
        setIcon(new Texture("immobilization.png"));
        setName("Immobilize");
        setDescription("You immobilizes your target");
        setTargetCountMax(1);
        setDistanceMax(5);
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
            public void act(Target target, boolean success) {
                if (success) {
                    target.getEntity().addEffect(new ImmobilizedEffect(target.getEntity(), 1));
                }
            }
        });
        addPlay(play);
    }
}
