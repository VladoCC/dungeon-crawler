package ru.myitschool.dcrawler.entities.skills.action;

import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.NumberAction;
import ru.myitschool.dcrawler.entities.skills.FloatingDamageMark;
import ru.myitschool.dcrawler.entities.skills.Skill;
import ru.myitschool.dcrawler.entities.skills.Target;

public abstract class SimpleCombinedAction extends Action {

    private MathAction action;
    private MathAction critAction;

    public SimpleCombinedAction(Skill skill, MathAction action) {
        super(skill);
        this.action = action;
        this.critAction = new NumberAction(action.max());
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
