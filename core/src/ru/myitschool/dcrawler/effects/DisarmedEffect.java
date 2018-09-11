package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;

public class DisarmedEffect extends Effect {

    private String name = "Disarmed";
    private String description = "";
    private Texture icon = new Texture("disarm.png");
    private boolean positive = false;

    public DisarmedEffect(Entity entity, int turns) {
        super(entity);
        setName(name);
        description += "You can not attack for " + turns + "turn(-s)";
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setStackable(false);
        setStackSize(1);
        setExpiring(true);
        setExpireTurns(turns);
        setSkillUse(true);
        type = 9;
    }

    @Override
    public boolean canUseSkill() {
        return false;
    }
}
