package ru.myitschool.dcrawler.math;

/**
 * Created by Voyager on 06.08.2017.
 */
public class DivideAction extends MathAction {

    private MathAction dividend;
    private MathAction divider;

    public DivideAction(MathAction dividend, MathAction divider) {
        this.dividend = dividend;
        this.divider = divider;
    }

    @Override
    public int act() {
        return dividend.act() / divider.act();
    }

    @Override
    public String getDescription() {
        return "(" + dividend.getDescription() + ")" + " / " + "(" + divider.getDescription() + ")";
    }

    @Override
    public int max() {
        return dividend.max() / divider.min();
    }

    @Override
    public int min() {
        return dividend.min() / divider.max();
    }
}
