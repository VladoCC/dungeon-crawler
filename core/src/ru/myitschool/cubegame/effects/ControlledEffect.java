package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 24.11.2017.
 */
public class ControlledEffect extends Effect {

    private String name = "Mind control";
    private String description = "Someone else controls your mind for this turn.";
    private Texture icon = new Texture("mind_control.png");
    private boolean used = false;
    private boolean positive = false;

    public ControlledEffect(Entity entity) {
        super(entity);
        setName(name);
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setExpiring(true);
        setExpireTurns(1);
        entity.setControlled(true);
        type = 4;
    }

    @Override
    public void endTurn() {
        super.endTurn();
        getEntity().setControlled(false);
    }
}
