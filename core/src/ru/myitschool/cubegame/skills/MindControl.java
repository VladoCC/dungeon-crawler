package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.ControlledEffect;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 25.11.2017.
 */
public class MindControl extends Skill {

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
        addPlayContainer().getEnemyPlay().addAction(new Action() {
            @Override
            public void act(Target target, int success, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT){
                    target.getEntity().addEffect(new ControlledEffect(target.getEntity()));
                    mark.addText("Success");
                }
            }
        });
        addPlayContainer();
    }
}
