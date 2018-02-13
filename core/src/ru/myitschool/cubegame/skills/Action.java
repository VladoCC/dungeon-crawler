package ru.myitschool.cubegame.skills;

import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 28.06.2017.
 */
public interface Action {

    void act(Target target, boolean success);
}
