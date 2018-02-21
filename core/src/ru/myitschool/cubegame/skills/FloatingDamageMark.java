package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Voyager on 21.02.2018.
 */
public class FloatingDamageMark {

    private final static int STEPS_MAX = 5;
    private final static float STEP_DELAY = 0.5f;

    private static Array<FloatingDamageMark> marks = new Array<FloatingDamageMark>();

    private int tileX;
    private int tileY;
    private String text;
    private int step = 0;
    private float time = 0f;

    public FloatingDamageMark(int tileX, int tileY, String text) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.text = text;
        marks.add(this);
    }

    public static void update(float delta){
        for (FloatingDamageMark mark : marks){
            mark.time += delta;
            if (mark.time >= STEP_DELAY){
                mark.time = 0f;
                mark.step++;
            }
            if (mark.step >= STEPS_MAX){
                marks.removeValue(mark, false);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloatingDamageMark mark = (FloatingDamageMark) o;

        if (tileX != mark.tileX) return false;
        if (tileY != mark.tileY) return false;
        return text != null ? text.equals(mark.text) : mark.text == null;
    }
}
