package ru.myitschool.cubegame.math;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Voyager on 18.04.2018.
 */
public class ActionArray {
    private Array<MathAction> actions = new Array<>(false, 2, MathAction.class);

    public void addAction(MathAction action){
        actions.add(action);
    }

    public MathAction[] getActions(){
        return actions.items;
    }
}
