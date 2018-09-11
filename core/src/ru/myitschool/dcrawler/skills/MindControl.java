package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.effects.ControlledEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 25.11.2017.
 */
public class MindControl extends Skill {

    MathAction attackAction = new DiceAction(4);

    public MindControl(Entity doer) {
        super(doer);
        setName("Mind control");
        setDescription("You can control your target for one turn");
        setIcon(new Texture("mind_control.png"));
        setTargetCountMax(1);
        setDistanceMax(4);
        setDistanceMin(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        setType(SKILL_TYPE_ENCOUNTER);
        setObstruct(false);
        addPlayContainer().getEnemyPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT){
                    target.getEntity().addEffect(new ControlledEffect(target.getEntity()));
                    mark.addText("Success");
                }
            }
        }.setAttack(attackAction));
        addPlayContainer();
    }
}
