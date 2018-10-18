package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;

/**
 * Created by Voyager on 26.11.2017.
 */
public class ArcaneShieldingEffect extends Effect {

    private String name = "God's protection";
    private String description = "";
    private Texture icon = new Texture("gods_protection.png");
    private boolean positive = true;

    public ArcaneShieldingEffect(Entity entity) {
        super(entity);
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
        return "God protects you from all damage for this turn";
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
        return "main.dcrawler.effect.arcane_shielding";
    }

    @Override
    public int onDamage(int damage) {
        return 0;
    }
}
