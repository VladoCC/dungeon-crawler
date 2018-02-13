package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.IntervalAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 26.11.2017.
 */
public class Heal extends Skill {

    MathAction healAction = new IntervalAction(1, 8);

    public Heal(Entity doer) {
        super(doer);
        setIcon(new Texture("heal.png"));
        setName("Heal");
        setDescription("You can heal your ally for" + healAction.act());
        setTargetCountMax(1);
        setDistanceMax(4);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_CHARACTER);
        Play play = new Play() {
            @Override
            public boolean check(Target target) {
                return true;
            }
        };
        play.addAction(new Action() {
            @Override
            public void act(Target target, boolean success) {
                if (success) {
                    Entity entity = target.getEntity();
                    entity.addHp(healAction.act());
                }
            }
        });
        addPlay(play);
    }
}
