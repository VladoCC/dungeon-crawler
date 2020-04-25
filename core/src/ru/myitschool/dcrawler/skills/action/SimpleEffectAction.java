package ru.myitschool.dcrawler.skills.action;

import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.FloatingDamageMark;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;

public abstract class SimpleEffectAction extends Action {

    public SimpleEffectAction(Skill skill) {
        super(skill);
    }

    @Override
    protected MathAction successDamage() {
        return null;
    }

    @Override
    protected MathAction critSuccessDamage(int standardDamage) {
        return null;
    }

    @Override
    protected MathAction failDamage() {
        return null;
    }

    @Override
    protected MathAction critFailDamage(int standardDamage) {
        return null;
    }

    @Override
    protected void critSuccessEffect(Target target, int damage, FloatingDamageMark mark) {

    }

    @Override
    protected void failEffect(Target target, int damage, FloatingDamageMark mark) {

    }

    @Override
    protected void critFailEffect(Target target, int damage, FloatingDamageMark mark) {

    }


    @Override
    protected void beforeEffect(Target target, int damage, FloatingDamageMark mark) {

    }

    @Override
    protected void afterEffect(Target target, int damage, FloatingDamageMark mark) {

    }
}
