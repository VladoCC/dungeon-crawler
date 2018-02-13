package ru.myitschool.cubegame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Voyager on 25.04.2017.
 */
public class Character extends Entity {

    private static int pos = 0;
    private static final int CHARS_MAX = 4;

    private int stepsMax = 6;
    private int actionsMax = 1;
    private int actionCount = 0;

    private boolean moved = false;

    private static Character[] chars = new Character[CHARS_MAX];

    public Character(Texture texture, Texture portrait, float x, float y, int hp, int hpMax, int mp, int mpMax, int armor) {
        super(texture, portrait, x, y, hp, hpMax, mp, mpMax, armor);
        if (pos < CHARS_MAX){
            chars[pos] = this;
            pos++;
        }
        Entity.add(this);
    }

    public static Character getChar(int num) {
        return chars[num];
    }

    public static Character[] getChars() {
        return chars;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean turnIsEnded() {
        return false;
    }

    @Override
    public void endTurn() {
        super.endTurn();
    }

    @Override
    public void startTurn() {
        moved = false;
        setMp(getMpMax());
        super.startTurn();
    }

    @Override
    public void startMove() {
        super.startMove();
    }

    @Override
    public void endMove() {
        moved = true;
        super.endMove();
        setMp(0);
    }
}
