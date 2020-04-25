package ru.myitschool.dcrawler.skills.action;

import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.FloatingDamageMark;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;

public abstract class ComplexEffectAction extends Action {

    public ComplexEffectAction(Skill skill) {
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
}
