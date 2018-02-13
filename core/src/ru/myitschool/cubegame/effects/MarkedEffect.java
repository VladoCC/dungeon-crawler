package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 28.11.2017.
 */
public class MarkedEffect extends Effect {

    private String name = "Marked";
    private String description = "";
    private Texture icon = new Texture("mark.png");
    private boolean positive = false;
    private int penalty; //penalty is negative number
    private Entity marker;

    public MarkedEffect(Entity entity, Entity marker, int penalty) {
        super(entity);
        setName(name);
        description += "All your attacks except of attacks to creature that marked you gets " + penalty + " accuracy penalty.";
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setStackable(false);
        setStackSize(0);
        setExpiring(false);
        setExpireTurns(0);
        this.penalty = penalty;
        this.marker = marker;
        type = 6;
    }

    @Override
    public int accuracyBonus(int accuracy, Entity target) {
        if (target != marker){
            accuracy += penalty;
        }
        return accuracy;
    }
}
