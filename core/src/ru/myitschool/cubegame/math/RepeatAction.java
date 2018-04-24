package ru.myitschool.cubegame.math;

/**
 * Created by Voyager on 17.04.2018.
 */
public class RepeatAction extends ActionArray {

    public RepeatAction(MathAction action, int count) {
        for (int i = 0; i < count; i++) {
            addAction(action);
        }
    }
}
