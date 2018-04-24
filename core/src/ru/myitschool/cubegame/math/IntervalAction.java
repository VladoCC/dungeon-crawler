package ru.myitschool.cubegame.math;

import java.util.Random;

/**
 * Created by Voyager on 05.08.2017.
 */
public class IntervalAction extends MathAction {

    private int min;
    private int max;
    private Random random;

    public IntervalAction(int min, int max) {
        this.min = min;
        this.max = max;
        random = new Random();
    }

    @Override
    public int act() {
        return random.nextInt(max + 1 - min) + min;
    }

    @Override
    public String getDescription() {
        return "(" + min + "-" + max + ")";
    }

    @Override
    public int max() {
        return max;
    }

    @Override
    public int min() {
        return min;
    }
}
