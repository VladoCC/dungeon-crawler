package ru.myitschool.dcrawler.effects;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 29.11.2017.
 */
public class CloudOfKnivesEffect extends CellEffect {

    private String name = "Cloud of knives";
    private String description = "";
    private Texture icon = new Texture("cloud_knives.png");
    private boolean positive = false;
    private MathAction damage;

    public CloudOfKnivesEffect(FloorEffect floorEffect, MathAction damage) {
        super(floorEffect);
        this.damage = damage;
    }

    @Override
    protected void onStepTo(Entity entity) {
        super.onStepTo(entity);
        entity.addHp(-damage.act());
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
        return description;
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
        return "main.dcrawler.effect.cloud_of_knives";
    }

    @Override
    public void startTurn() {
        super.startTurn();
        int dmg = -damage.act();
        System.out.println("DAMAGE!!! - " + dmg);
        getEntity().addHp(dmg);
    }
}
