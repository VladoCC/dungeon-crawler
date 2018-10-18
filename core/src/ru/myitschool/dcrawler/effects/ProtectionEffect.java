package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;

/**
 * Created by Voyager on 05.11.2017.
 */
public class ProtectionEffect extends Effect {

    private String name = "Armor Improvement";
    private String description = "Increases armor for ";
    private String descriptionEnd = " until end of next turn.";
    private Texture icon = new Texture("sword.png");
    private boolean used = false;
    private boolean positive = true;
    private int armor = 0;
    private int turn = 1;

    public ProtectionEffect(Entity entity, int armor) {
        super(entity);
        entity.setArmor(entity.getArmor() + armor);
        this.armor = armor;
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
        return description + armor + descriptionEnd;
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
        return false;
    }

    @Override
    public int getExpireTurns() {
        return 0;
    }

    @Override
    public String getType() {
        return "main.dcrawler.effect.protection";
    }

    @Override
    public void endTurn() {
        super.endTurn();
        if (getExpireTurns() == 0){
            getEntity().setArmor(getEntity().getArmor() - armor);
        }
    }
}
