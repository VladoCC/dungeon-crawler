package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 28.06.2017.
 */
public abstract class Play {

    Array<Action> actions = new Array<Action>();

    public abstract boolean check(Target target);

    public void addAction(Action action){
        actions.add(action);
    }

    public void act(Target target){
        boolean success = check(target);
        for (Action action : actions){
            action.act(target, success);
        }
    }
}
