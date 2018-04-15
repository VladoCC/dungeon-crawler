package ru.myitschool.cubegame.effects;

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

    public FloorClearingEffect(FloorEffect effect) {
        super(null);
        setName(name);
        description += "";
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setStackable(true);
        setStackSize(4096);
        setExpiring(true);
        setExpireTurns(1);
        setHide(true);
        this.effect = effect;
        type = 7;
    }

    @Override
    public void onExpire() {
        super.onExpire();
        effect.removeEffect(true);
    }
}
