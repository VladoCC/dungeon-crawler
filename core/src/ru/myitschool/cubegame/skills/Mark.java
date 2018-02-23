package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.MarkedEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 28.11.2017.
 */
public class Mark extends Skill {

    MathAction rollAction = new DiceAction(1, 20);
    MarkedEffect effect = null;

    public Mark(Entity doer) {
        super(doer);
        setIcon(new Texture("mark.png"));
        setName("Mark");
        setDescription("You mark target and it gets penalty for all attacks except attacks targeted to you, but you can place only one mark at the time");
        setTargetCountMax(1);
        setDistanceMax(4);
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
                    if (effect != null){
                        effect.getEntity().removeEffect(effect);
                    }
                    effect = new MarkedEffect(target.getEntity(), getDoer(), -5);
                    target.getEntity().addEffect(effect);
                    mark.addText("Success");
                }
            }
        });
        addPlay(play);
    }
}
