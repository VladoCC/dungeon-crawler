package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Voyager on 29.11.2017.
 */
public class FloorClearingEffect extends Effect {

    private String name = "";
    private String description = "";
    private Texture icon = null;
    private boolean positive = true;
    FloorEffect effect;
    private int turns;

    public FloorClearingEffect(FloorEffect effect, int turns) {
        super(null);
        setHide(true);
        this.effect = effect;
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
        return description;
    }

    @Override
    public boolean isSkillUse() {
        return false;
    }

    @Override
    public boolean isPositive() {
        return true;
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public int getStackSize() {
        return 4096;
    }

    @Override
    public boolean isExpiring() {
        return true;
    }

    @Override
    public int getExpireTurns() {
        return turns;
    }

    @Override
    public String getType() {
        return "main.dcrawler.effect.floor_clearing";
    }

    @Override
    public void onExpire() {
        super.onExpire();
        effect.removeEffect(true);
    }
}
