package ru.myitschool.dcrawler.skills.action;

import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.NumberAction;
import ru.myitschool.dcrawler.skills.FloatingDamageMark;
import ru.myitschool.dcrawler.skills.Play;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;

/**
 * Created by Voyager on 28.06.2017.
 */
public abstract class Action {

    private Skill skill;

    public Action(Skill skill) {
        this.skill = skill;
    }

    public void act(Target target, int success, FloatingDamageMark mark){
        int damage = attack(target, success, mark);
        effect(target, success, damage, mark);
    }

    private int attack(Target target, int success, FloatingDamageMark mark){
        int damage = 0;
        //count and apply damage only if there is a target
        if (target != null) {
            MathAction action = null;
            MathAction critAction = null;

            if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                action = successDamage();
            } else if (success == Play.TARGETING_MISS || success == Play.TARGETING_CRIT_MISS) {
                action = failDamage();
            }

            if (action != null) {
                //add damage from buffs if action is able to do some damage
                if (action.max() > 0) {
                    action = skill.countAttackAction(action);
                } else if (action.max() < 0) {
                    action = skill.countHealAction(action);
                }
                damage += action.act();
            }

            if (success == Play.TARGETING_CRIT_HIT) {
                critAction = critSuccessDamage(damage);
            } else if (success == Play.TARGETING_CRIT_MISS) {
                critAction = critFailDamage(damage);
            }

            //apply crit damage if it was crit attack
            if (critAction != null) {
                damage += critAction.act();
            }
            target.getEntity().addHp(-damage);
            if (damage != 0) {
                mark.addText("" + (-damage));
            }
        }
        return damage;
    }

    private void effect(Target target, int success, int damage, FloatingDamageMark mark) {

    }

    protected abstract MathAction successDamage();

    protected abstract MathAction critSuccessDamage(int standardDamage);

    protected abstract MathAction failDamage();

    protected abstract MathAction critFailDamage(int standardDamage);

    protected abstract void beforeEffect(Target target, int damage, FloatingDamageMark mark);

    protected abstract void successEffect(Target target, int damage, FloatingDamageMark mark);

    protected abstract void critSuccessEffect(Target target, int damage, FloatingDamageMark mark);

    protected abstract void failEffect(Target target, int damage, FloatingDamageMark mark);

    protected abstract void critFailEffect(Target target, int damage, FloatingDamageMark mark);

    protected abstract void afterEffect(Target target, int damage, FloatingDamageMark mark);
}
