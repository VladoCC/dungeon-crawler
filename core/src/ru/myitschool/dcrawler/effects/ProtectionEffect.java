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
        setName(name);
        description += armor  + descriptionEnd;
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        entity.setArmor(entity.getArmor() + armor);
        this.armor = armor;
        type = 2;
    }

    @Override
    public void endTurn() {
        super.endTurn();
        if (getExpireTurns() == 0){
            getEntity().setArmor(getEntity().getArmor() - armor);
        }
    }
}
