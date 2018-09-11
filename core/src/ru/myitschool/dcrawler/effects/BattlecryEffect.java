package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.SumAction;

/**
 * Created by Voyager on 19.11.2017.
 */
public class BattlecryEffect extends Effect {

    private String name = "Battle cry";
    private String description = "";
    private Texture icon = new Texture("battle_cry.png");
    private boolean used = false;
    private boolean positive = true;
    private MathAction damage;
    private int accuracyBonus;

    public BattlecryEffect(Entity entity, int accuracyBonus, MathAction damage, int turns) {
        super(entity);
        setName(name);
        description += "Increases accuracy for " + accuracyBonus + " and damage for " + damage.getDescription() + " for " + turns + " turns.";
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setExpiring(true);
        setExpireTurns(turns);
        this.damage = damage;
        this.accuracyBonus = accuracyBonus;
        entity.setAccuracyBonus(entity.getAccuracyBonus() + accuracyBonus);
        type = 4;
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        action = super.attackBonus(action);
        MathAction resultAction = new SumAction(action, damage);
        return resultAction;
    }

    @Override
    public void startTurn() {
        super.startTurn();
        if (getExpireTurns() == 0){
            getEntity().setAccuracyBonus(getEntity().getAccuracyBonus() - accuracyBonus);
        }
    }
}
