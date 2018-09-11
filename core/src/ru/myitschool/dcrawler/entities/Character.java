package ru.myitschool.dcrawler.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Voyager on 25.04.2017.
 */
public class Character extends Entity {

    private static final int CHARS_MAX = 4;

    private boolean moved = false;
    private static boolean created = false;

    private static Array<Character> chars = new Array<>(CHARS_MAX);

    public Character(Texture texture, Texture portrait, float x, float y, int hp, int hpMax, int mp, int mpMax, int armor) {
        super(texture, portrait, x, y, hp, hpMax, mp, mpMax, armor);
        chars.add(this);
        Entity.add(this);
    }

    public static Character getChar(int num) {
        return chars.get(num);
    }

    public static Array<Character> getChars() {
        return chars;
    }

    public static boolean isCreated() {
        return created;
    }

    public static void setCreated(boolean created) {
        Character.created = created;
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
