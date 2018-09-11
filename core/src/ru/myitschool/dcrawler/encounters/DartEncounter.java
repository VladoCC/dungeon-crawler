package ru.myitschool.dcrawler.encounters;

import ru.myitschool.dcrawler.effects.BloodiedEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.NumberAction;

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
        entity.addEffect(new BloodiedEffect(entity, new NumberAction(1), 3));
    }
}
