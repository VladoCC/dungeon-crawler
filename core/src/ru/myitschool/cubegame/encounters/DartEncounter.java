package ru.myitschool.cubegame.encounters;

import ru.myitschool.cubegame.effects.BloodiedEffect;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 18.02.2018.
 */
public class DartEncounter extends Encounter {

    public DartEncounter() {
        setName("Dart trap");
        setText("You was shoot by trap and now you bleed");
    }

    @Override
    protected void activate(Entity entity) {
        entity.addEffect(new BloodiedEffect(entity, 1, 3));
    }
}
