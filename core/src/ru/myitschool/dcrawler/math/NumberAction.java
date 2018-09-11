package ru.myitschool.dcrawler.math;

/**
 * Created by Voyager on 04.08.2017.
 */
public class NumberAction extends MathAction {

    private int result;

    public NumberAction(int result) {
        this.result = result;
    }

    @Override
    public int act() {
        return result;
    }

    @Override
    public String getDescription() {
        return "" + result;
    }
}
