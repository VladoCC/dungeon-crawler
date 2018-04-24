package ru.myitschool.cubegame.math;

/**
 * Created by Voyager on 18.04.2018.
 */
public class StatementAction extends MathAction {

    private Statement statement;
    private MathAction trueAction;
    private MathAction falseAction;

    public StatementAction(Statement statement, MathAction trueAction, MathAction falseAction) {
        this.statement = statement;
        this.trueAction = trueAction;
        this.falseAction = falseAction;
    }

    @Override
    public int act() {
        if (statement.result()){
            return trueAction.act();
        } else {
            return falseAction.act();
        }
    }

    @Override
    public String getDescription() {
        return "if(statement(" + statement.getDescription() + "), true(" + trueAction.getDescription() + "), false(" + falseAction.getDescription() + ")";
    }
}
