package ru.myitschool.dcrawler.skills.action;

import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.Skill;

public class ComplexAttackAction extends ActionAdapter {

    private MathAction successAction;
    private MathAction critSuccessAction;
    private MathAction failAction;
    private MathAction critFailAction;

    public ComplexAttackAction(Skill skill, MathAction successAction, MathAction critSuccessAction, MathAction failAction, MathAction critFailAction) {
        super(skill);
        this.successAction = successAction;
        this.critSuccessAction = critSuccessAction;
        this.failAction = failAction;
        this.critFailAction = critFailAction;
    }

    @Override
    protected MathAction successDamage() {
        return successAction;
    }

    @Override
    protected MathAction critSuccessDamage(int standardDamage) {
        return critSuccessAction;
    }

    @Override
    protected MathAction failDamage() {
        return failAction;
    }

    @Override
    protected MathAction critFailDamage(int standardDamage) {
        return critFailAction;
    }
}
