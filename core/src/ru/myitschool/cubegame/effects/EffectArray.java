package ru.myitschool.cubegame.effects;

import java.util.ArrayList;

/**
 * Created by Voyager on 04.08.2017.
 */
public class EffectArray extends ArrayList<Effect> {

    int id = 0; //TODO reset after exiting of dungeon

    @Override
    public boolean add(Effect effect) {
        add(effect, size());
        return true;
    }

    public void add(Effect effect, int pos) {
        int size = super.size();
        if (effect.isStackable()){
            int stack = effect.getStackSize() - 1;
            int count = 0;
            for (int i = size - 1; i >= 0; i--){
                Effect oldEffet = get(i);
                if (oldEffet.getType() == effect.getType()){
                    count++;
                    if (count >= stack){
                        remove(i);
                    }
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                Effect oldEffet = get(i);
                if (oldEffet.getType() == effect.getType()){
                    remove(i);
                }
            }
        }
        if (pos > size()){
            pos = size();
        }
        super.add(pos, effect);
        effect.setId(id++);
    }

    public void removeId(int id){
        for (int i = 0; i < size(); i++) {
            Effect effect = get(i);
            if (effect.getId() == id) {
                remove(i);
                i--;
            }
        }
    }
}
