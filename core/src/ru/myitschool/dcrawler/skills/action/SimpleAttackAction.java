package ru.myitschool.dcrawler.skills.action;

import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.NumberAction;
import ru.myitschool.dcrawler.skills.Skill;

public class SimpleAttackAction extends ActionAdapter {

    private MathAction action;
    private MathAction critAction;

    public SimpleAttackAction(Skill skill, MathAction action) {
        super(skill);
        this.action = action;
        this.critAction = new NumberAction(action.max());
    }

    @Override
    protected MathAction successDamage() {
        return action;
    }

    @Override
    protected MathAction critSuccessDamage(int standardDamage) {
        return critAction;
    }
}
