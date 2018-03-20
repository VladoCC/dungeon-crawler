package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.ControlledEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 25.11.2017.
 */
public class MindControl extends Skill {

    MathAction rollAction = new DiceAction(1, 20);

    public MindControl(Entity doer) {
        super(doer);
        setName("Mind control");
        setDescription("You can control your target for one turn");
        setIcon(new Texture("mind_control.png"));
        setTargetCountMax(1);
        setDistanceMax(5);
        setDistanceMin(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        setType(SKILL_TYPE_COOLDOWN);
        setCooldownMax(4);
        Play play = new Play() {
            @Override
            public boolean check(Target target) {
                return rollAction.act() + getAccuracyBonus() > target.getEntity().getArmor();
            }
        };
        play.addAction(new Action() {
            @Override
            public void act(Target target, boolean success, FloatingDamageMark mark) {
                if (success){
                    target.getEntity().addEffect(new ControlledEffect(target.getEntity()));
                    mark.addText("Success");
                }
            }
        });
        addPlay(play);
    }
}
