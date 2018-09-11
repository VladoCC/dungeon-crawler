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

    public BloodiedEffect(Entity entity, MathAction damage, int rounds) {
        super(entity);
        setName(name);
        description += "Deal " + damage.getDescription() + " damage on start of your turn for " + rounds + " turns.";
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setStackable(false);
        setStackSize(1);
        setExpiring(true);
        setExpireTurns(rounds);
        this.damage = damage;
        type = 3;
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
