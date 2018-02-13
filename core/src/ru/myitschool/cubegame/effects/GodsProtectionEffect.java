package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 26.11.2017.
 */
public class GodsProtectionEffect extends Effect {

    private String name = "God's protection";
    private String description = "";
    private Texture icon = new Texture("gods_protection.png");
    private boolean positive = true;

    public GodsProtectionEffect(Entity entity) {
        super(entity);
        setName(name);
        description += "God protects you from all damage for this turn";
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setStackable(false);
        setStackSize(0);
        setExpiring(true);
        setExpireTurns(1);
        type = 5;
    }

    @Override
    public int onDamage(int damage) {
        return 0;
    }
}
