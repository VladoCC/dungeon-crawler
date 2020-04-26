package ru.myitschool.dcrawler.utils;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Array with additional functions of clipping, getting random element and stack/queue-style getters
 */
public class AdvancedArray<T> extends Array<T> {

    public boolean clip(int from, int to){
        if (from >= 0 && to < items.length && from <= to) {
            T[] objects = (T[]) new Object[to - from + 1];
            for (int i = 0; i < to - from + 1; i++) {
                objects[i] = items[from + i];
            }
            items = objects;
            size = objects.length;
            return true;
        }
        return false;
    }

    public T getFirst(){
        return items[0];
    }

    public T getLast(){
        return items[items.length - 1];
    }

    public T getRandom() {
        int random = SeededRandom.getInstance().nextInt(size);
        return items[random];
    }
}
