package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 16.11.2017.
 */
public class BloodiedEffect extends Effect {

    private String name = "Bloodied";
    private String description = "";
    private Texture icon = new Texture("blood.png");//TODO change to better icon
    private boolean used = false;
    private boolean positive = false;
    private int damage;

    public BloodiedEffect(Entity entity, int damage, int rounds) {
        super(entity);
        setName(name);
        description += "Deal " + damage + " damage on start of your turn for " + rounds + " turns.";
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setStackable(true);
        setStackSize(100);//TODO change to 3
        setExpiring(true);
        setExpireTurns(rounds);
        this.damage = damage;
        type = 3;
    }

    @Override
    public void startTurn() {
        super.startTurn();
        if (!isDelete()){
            description += "Deal " + damage + " damage on start of your turn for " + getExpireTurns() + " turns.";
            getEntity().addHp(-damage); //TODO CHANGE DAMAGE TO MathAction
        }
    }
}
