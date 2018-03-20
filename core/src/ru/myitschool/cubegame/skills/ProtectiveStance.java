package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.ProtectionEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;
import ru.myitschool.cubegame.math.NumberAction;
import ru.myitschool.cubegame.math.SumAction;

/**
 * Created by Voyager on 05.11.2017.
 */
public class ProtectiveStance extends Skill {

    MathAction rollAction = new SumAction(new DiceAction(1, 20), new NumberAction(2));
    MathAction attackAction = new DiceAction(1, 6);
    int armor = 5;

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
                    attackAction = new DiceAction(1, 6);
                    attackAction = countAttackAction(attackAction);
                    int damage = -attackAction.act();
                    target.getEntity().addHp(damage);
                    mark.addText(damage + "");
                }
                doer.addEffect(new ProtectionEffect(doer, armor));
            }
        });
        addPlay(play);
    }
}
