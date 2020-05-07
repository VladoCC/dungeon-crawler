package ru.myitschool.dcrawler.entities.skills.action;

import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.entities.skills.Skill;

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
