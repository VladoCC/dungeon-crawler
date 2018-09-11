package ru.myitschool.dcrawler.math;

/**
 * Created by Voyager on 04.08.2017.
 */
public class SumAction extends MathAction {

    private MathAction[] actions;

    public SumAction(ActionArray array){
        this(array.getActions());
    }

    public SumAction(MathAction... actions) {
        this.actions = actions;
    }

    @Override
    public int act() {
        int sum = 0;
        for (int i = 0; i < actions.length; i++) {
            sum += actions[i].act();
        }
        return sum;
    }

    @Override
    public String getDescription() {
        String description = "" + actions[0].getDescription();
        for (int i = 1; i < actions.length; i++) {
            description += " + " + actions[i].getDescription();
        }
        return description;
    }

    @Override
    public int max() {
        int sum = 0;
        for (int i = 0; i < actions.length; i++) {
            sum += actions[i].max();
        }
        return sum;
    }

    @Override
    public int min() {
        int sum = 0;
        for (int i = 0; i < actions.length; i++) {
            sum += actions[i].min();
        }
        return sum;
    }
}
