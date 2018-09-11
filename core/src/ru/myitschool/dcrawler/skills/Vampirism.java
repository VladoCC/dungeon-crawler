package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.effects.BloodiedEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.NumberAction;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Vampirism extends Skill {

    MathAction attackAction = new DiceAction(6);
    int damage = 3;
    int turns = 3;

    public Vampirism(final Entity doer) {
        super(doer);
        setIcon(new Texture("vampirism.png"));
        setName("Vampirism");
        setDescription("You deal damage to your target and restore as many hp as it loses. If target will be lower then quarter of max hp, it will bleed " + turns +" turn(-s) for " + damage + " damage");
        setTargetCountMax(1);
        setDistanceMax(1);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENTITY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENTITY);
        addPlayContainer().getEnemyPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                    doer.addHp(damage);
                    if (target.getEntity().getHp() <= target.getEntity().getHpMax() / 4){
                        target.getEntity().addEffect(new BloodiedEffect(target.getEntity(), new NumberAction(damage), turns));
                    }
                    mark.addText(damage + "");
                    FloatingDamageMark damageMark = new FloatingDamageMark(doer.getTileX(), doer.getTileY(), -damage + "");
                    damageMark.show();
                }
            }
        }.setAttack(attackAction));
        addPlayContainer();
    }
}
