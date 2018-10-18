package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 16.11.2017.
 */
public class BloodiedEffect extends Effect {

    private String name = "Bloodied";
    private String description = "";
    private Texture icon = new Texture("blood.png");//TODO change to better icon
    private boolean used = false;
    private boolean positive = false;
    private MathAction damage;
    private int rounds;

    public BloodiedEffect(Entity entity, MathAction damage, int rounds) {
        super(entity);
        this.rounds = rounds;
        this.damage = damage;
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
        return "Deal " + damage.getDescription() + " damage on start of your turn for " + rounds + " turns.";
    }

    @Override
    public boolean isSkillUse() {
        return false;
    }

    @Override
    public boolean isPositive() {
        return positive;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public int getStackSize() {
        return 1;
    }

    @Override
    public boolean isExpiring() {
        return true;
    }

    @Override
    public int getExpireTurns() {
        return rounds;
    }

    @Override
    public String getType() {
        return "main.dcrawler.effect.bloodied";
    }

    @Override
    public void startTurn() {
        super.startTurn();
        if (!isDelete()){
            description += "Deal " + damage.getDescription() + " damage on start of your turn for " + getExpireTurns() + " turns.";
            getEntity().addHp(-damage.act());
        }
    }
}
