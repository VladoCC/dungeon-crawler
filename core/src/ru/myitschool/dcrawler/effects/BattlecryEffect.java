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
    private int turns;

    public BattlecryEffect(Entity entity, int accuracyBonus, MathAction damage, int turns) {
        super(entity);
        this.turns = turns;
        this.damage = damage;
        this.accuracyBonus = accuracyBonus;
        entity.setAccuracyBonus(entity.getAccuracyBonus() + accuracyBonus);
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        action = super.attackBonus(action);
        MathAction resultAction = new SumAction(action, damage);
        return resultAction;
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
        return "Increases accuracy for " + accuracyBonus + " and damage for " + damage.getDescription() + " for " + turns + " turns.";
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
        return turns;
    }

    @Override
    public String getType() {
        return "main.dcrawler.effect.battlecry";
    }

    @Override
    public void startTurn() {
        super.startTurn();
        if (getExpireTurns() == 0){
            getEntity().setAccuracyBonus(getEntity().getAccuracyBonus() - accuracyBonus);
        }
    }
}
