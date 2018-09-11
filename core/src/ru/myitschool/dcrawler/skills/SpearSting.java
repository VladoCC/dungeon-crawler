package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class SpearSting extends Skill {

    MathAction attackAction = new DiceAction(10);

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
        setTypeDisplayer(SKILL_TARGET_TYPE_FLOOR_WAVE);
        addPlayContainer().getEntityPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_CRIT_HIT || success == Play.TARGETING_HIT) {
                    Entity entity = target.getEntity();
                    damage = -countAttackAction(attackAction).act();
                    if (success == Play.TARGETING_CRIT_HIT){
                        damage = -countAttackAction(attackAction).max();
                    }
                    entity.addHp(damage);
                    mark.addText(damage + "");
                }
            }
        });
        addPlayContainer();
    }
}
