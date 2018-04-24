package ru.myitschool.cubegame.skills;

/**
 * Created by Voyager on 28.06.2017.
 */
public interface Action {

    void act(Target target, int success, FloatingDamageMark mark);
}
