package ru.myitschool.dcrawler.math;

import java.util.Random;

/**
 * Created by Voyager on 04.08.2017.
 */
public class DiceAction extends MathAction {

    private int dice;
    private Random random;

    public DiceAction(int dice) {
        this.dice = dice;
        random = new Random();
    }


    @Override
    public int act() {
        int result = 0;
        result += random.nextInt(dice) + 1;
        return result;
    }

    @Override
    public String getDescription() {
        return "d" + dice;
    }

    @Override
    public int max() {
        return dice;
    }

    @Override
    public int min() {
        return 1;
    }
}
