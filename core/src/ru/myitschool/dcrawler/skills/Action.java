package ru.myitschool.dcrawler.skills;

import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.NumberAction;

/**
 * Created by Voyager on 28.06.2017.
 */
public abstract class Action {

    private MathAction attack = new NumberAction(0);
    private Skill skill;

    public Action(Skill skill) {
        this.skill = skill;
    }

    protected void act(Target target, int success, FloatingDamageMark mark){
        int damage = attack(target, success, mark);
        effect(target, success, damage, mark);
    }

    public Action setAttack(MathAction attack) {
        this.attack = attack;
        return this;
    }

    public int attack(Target target, int success, FloatingDamageMark mark){
        if (success == Play.TARGETING_HIT || success == Play.TARGETING_HIT){
            int damage = skill.countAttackAction(attack).act();
            if (success == Play.TARGETING_CRIT_HIT){
                damage = skill.countAttackAction(attack).max();
            }
            target.getEntity().addHp(-damage);
            if (damage != 0) {
                mark.addText("" + (-damage));
            }
            return damage;
        }
        return 0;
    }

    public abstract void effect(Target target, int success, int damage, FloatingDamageMark mark);
}
