package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;

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
        entity.setControlled(true);
    }

    @Override
    public Texture getIcon() {
        return icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isSkillUse() {
        return false;
    }

    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public int getStackSize() {
        return 0;
    }

    @Override
    public boolean isExpiring() {
        return true;
    }

    @Override
    public int getExpireTurns() {
        return 1;
    }

    @Override
    public String getType() {
        return "main.dcrawler.effect.controlled";
    }

    @Override
    public void endTurn() {
        super.endTurn();
        getEntity().setControlled(false);
    }
}
