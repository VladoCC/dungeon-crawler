package ru.myitschool.dcrawler.math;

/**
 * Created by Voyager on 04.08.2017.
 */
public abstract class MathAction {

    public abstract int act();

    public abstract String getDescription();

    public int max(){
        return act();
    }

    public int min(){
        return act();
    }
}
