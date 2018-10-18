package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;

/**
 * Created by Voyager on 28.11.2017.
 */
public class MarkedEffect extends Effect {

    private String name = "Marked";
    private String description = "";
    private Texture icon = new Texture("mark.png");
    private boolean positive = false;
    private int penalty; //penalty is negative number
    private Entity marker;

    public MarkedEffect(Entity entity, Entity marker, int penalty) {
        super(entity);
        this.penalty = penalty;
        this.marker = marker;
    }

    @Override
    public int accuracyBonus(int accuracy, Entity target) {
        if (target != marker){
            accuracy += penalty;
        }
        return accuracy;
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
        return "All your attacks except of attacks to creature that marked you gets " + penalty + " accuracy penalty.";
    }

    @Override
    public boolean isSkillUse() {
        return false;
    }

    @Override
    public boolean isPositive() {
        return false;
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
        return "main.dcrawler.effect.marked";
    }
}
