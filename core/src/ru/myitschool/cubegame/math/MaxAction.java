package ru.myitschool.cubegame.math;

/**
 * Created by Voyager on 18.04.2018.
 */
public class MaxAction extends MathAction {

    private MathAction[] actions;

    public MaxAction(ActionArray array) {
        this(array.getActions());
    }

    public MaxAction(MathAction... actions) {
        this.actions = actions;
    }

    @Override
    public int act() {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < actions.length; i++) {
            int num = actions[0].act();
            if (num > max){
                max = num;
            }
        }
        return max;
    }

    @Override
    public String getDescription() {
        String description = ("max(" + actions[0].getDescription());
        for (int i = 1; i < actions.length; i++) {
            description += ", " + actions[i];
        }
        description += ")";
        return description;
    }
}
