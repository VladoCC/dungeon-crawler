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
        setDescription("You can heal your ally for" + healAction.getDescription());
        setTargetCountMax(1);
        setDistanceMax(4);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_CHARACTER);
        setTypeDisplayer(SKILL_TARGET_TYPE_CHARACTER);
        PlayContainer container = addPlayContainer();
        container.getPlayerPlay().setPlayCheck(new PlayCheck(this) {
            @Override
            public int check(Target target) {
                return Play.TARGETING_HIT;
            }
        });
        container.getPlayerPlay().addAction(new Action() {
            @Override
            public void act(Target target, int success, FloatingDamageMark mark) {
                Entity entity = target.getEntity();
                int heal = 0;
                if (success == Play.TARGETING_HIT) {
                    heal = healAction.act();
                } else if (success == Play.TARGETING_CRIT_HIT){
                    heal = healAction.max();
                }
                entity.addHp(heal);
                if (heal != 0) {
                    mark.addText(heal + "");
                }
            }
        });
        addPlayContainer();
    }
}
