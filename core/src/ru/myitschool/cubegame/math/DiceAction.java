package ru.myitschool.cubegame.math;

import java.util.Random;

/**
 * Created by Voyager on 04.08.2017.
 */
public class DiceAction extends MathAction {

    private int count;
    private int dice;
    private Random random;

    public DiceAction(int count, int dice) {
        this.count = count;
        this.dice = dice;
        random = new Random();
    }


    @Override
    public int act() {
        int result = 0;
        for (int i = 0; i < count; i++) {
            result += random.nextInt(dice) + 1;
        }
        return result;
    }

    @Override
    public String getDescription() {
        return count + "d" + dice;
    }
}
