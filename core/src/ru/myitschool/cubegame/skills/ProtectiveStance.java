package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.ProtectionEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 05.11.2017.
 */
public class ProtectiveStance extends Skill {

    MathAction attackAction = new DiceAction(4);
    int armor = 3;

    public ProtectiveStance(final Entity doer) {
        super(doer);
        setName("Protective stance");
        setDescription("You deal " + attackAction.getDescription() + " damage and take up a protective stance that gives you 5 additional armor");
        setIcon(new Texture("protectivestance.png"));
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setTargetType(SKILL_TARGET_TYPE_CHARACTER);
        setTypeDisplayer(SKILL_TARGET_TYPE_CHARACTER);
        addPlayContainer().getPlayerPlay().addAction(new Action() {
            @Override
            public void act(Target target, int success, FloatingDamageMark mark) {

                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT){
                    int damage = -countAttackAction(attackAction).act();
                    if (success == Play.TARGETING_CRIT_HIT){
                        damage = -countAttackAction(attackAction).max();
                    }
                    target.getEntity().addHp(damage);
                    mark.addText(damage + "");
                    doer.addEffect(new ProtectionEffect(doer, armor));
                }
            }
        });
    }
}
