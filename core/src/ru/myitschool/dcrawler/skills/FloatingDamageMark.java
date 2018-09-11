package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Voyager on 21.02.2018.
 */
public class FloatingDamageMark {

    public final static float MAX_TIME = 2.5f;

    private static Array<FloatingDamageMark> marks = new Array<FloatingDamageMark>();

    private int tileX;
    private int tileY;
    private String defaultText;
    private String text = "";
    private int textsCount = 0;
    private float time = 0f;

    public FloatingDamageMark(int tileX, int tileY, String defaultText) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.defaultText = defaultText;
    }

    public void addText(String text){
        if (textsCount > 0){
            this.text += "/";
        }
        this.text += text;
        textsCount++;
    }

    public void show(){
        marks.add(this);
    }

    public static void update(float delta){
        for (FloatingDamageMark mark : marks){
            mark.time += delta;
            if (mark.time >= MAX_TIME){
                mark.time = 0f;
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

    public static Array<FloatingDamageMark> getMarks() {
        return marks;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public String getText() {
        if (textsCount > 0){
            return text;
        }
        return defaultText;
    }

    public float getTime() {
        return time;
    }
}
