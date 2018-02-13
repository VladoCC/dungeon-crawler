package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 02.12.2017.
 */
public class ImmobilizedEffect extends Effect {

    private String name = "Immobilized";
    private String description = "";
    private Texture icon = new Texture("immobilization.png");
    private boolean positive = false;

    public ImmobilizedEffect(Entity entity, int turns) {
        super(entity);
        setName(name);
        description += "You can not move for " + turns + " turns";
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setStackable(false);
        setStackSize(0);
        setExpiring(true);
        setExpireTurns(turns);
        type = 8;
        entity.setImmobilized(true);
    }

    @Override
    public void onExpire() {
        super.onExpire();
        getEntity().setImmobilized(false);
    }
}
