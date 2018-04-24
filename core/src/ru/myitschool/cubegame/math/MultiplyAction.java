package ru.myitschool.cubegame.math;

/**
 * Created by Voyager on 04.08.2017.
 */
public class MultiplyAction extends MathAction {

    private MathAction[] actions;

    public MultiplyAction(ActionArray array) {
        this(array.getActions());
    }

    public MultiplyAction(MathAction... actions) {
        this.actions = actions;
    }

    @Override
    public int act() {
        int mult = 1;
        for (int i = 0; i < actions.length; i++) {
            mult *= actions[i].act();
        }
        return mult;
    }

    @Override
    public String getDescription() {
        String description = "(" + actions[0].getDescription() + ")";
        for (int i = 1; i < actions.length; i++) {
            description += " * " + "(" + actions[i].getDescription() + ")";
        }
        return description;
    }

    @Override
    public int max() {
        int mult = 1;
        for (int i = 0; i < actions.length; i++) {
            mult *= actions[i].max();
        }
        return mult;
    }

    @Override
    public int min() {
        int mult = 1;
        for (int i = 0; i < actions.length; i++) {
            mult *= actions[i].min();
        }
        return mult;
    }
}
