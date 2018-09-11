package ru.myitschool.dcrawler.math;

/**
 * Created by Voyager on 18.04.2018.
 */
public class MinAction extends MathAction {

    private MathAction[] actions;

    public MinAction(ActionArray array) {
        this(array.getActions());
    }

    public MinAction(MathAction... actions) {
        this.actions = actions;
    }

    @Override
    public int act() {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < actions.length; i++) {
            int num = actions[0].act();
            if (num < min){
                min = num;
            }
        }
        return min;
    }

    @Override
    public String getDescription() {
        String description = ("min(" + actions[0].getDescription());
        for (int i = 1; i < actions.length; i++) {
            description += ", " + actions[i];
        }
        description += ")";
        return description;
    }
}
