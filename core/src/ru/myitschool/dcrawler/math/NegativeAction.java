package ru.myitschool.dcrawler.math;

public class NegativeAction extends MathAction {

    MathAction action;

    public NegativeAction(MathAction action) {
        this.action = action;
    }

    @Override
    public int act() {
        return -action.act();
    }

    /** returns negotiated max of inner action*/
    @Override
    public int max() {
        return -action.max();
    }

    /** returns negotiated min of inner action*/
    @Override
    public int min() {
        return -action.min();
    }

    @Override
    public String getDescription() {
        return "-(" + action.getDescription() + ")";
    }
}
