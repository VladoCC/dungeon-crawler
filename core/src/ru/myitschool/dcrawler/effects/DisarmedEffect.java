package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;

public class DisarmedEffect extends Effect {

    private String name = "Disarmed";
    private String description = "";
    private Texture icon = new Texture("disarm.png");
    private boolean positive = false;
    private int turns;

    public DisarmedEffect(Entity entity, int turns) {
        super(entity);
        this.turns = turns;
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
        return "You can not attack for " + turns + "turn(-s)";
    }

    @Override
    public boolean isSkillUse() {
        return true;
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
        return "main.dcrawler.effect.disarmed";
    }

    @Override
    public boolean canUseSkill() {
        return false;
    }
}
