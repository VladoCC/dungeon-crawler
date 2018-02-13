package ru.myitschool.cubegame.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.MathAction;
import ru.myitschool.cubegame.skills.Target;

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
        setName(name);
        description += "";
        setDescription(description);
        setIcon(icon);
        setPositive(positive);
        setStackable(false);
        setStackSize(0);
        setExpiring(false);
        setExpireTurns(0);
        this.damage = damage;
        type = -2;
    }

    @Override
    protected void stepToAction(Entity entity) {
        super.stepToAction(entity);
        entity.addHp(-damage.act());
    }

    @Override
    public void startTurn() {
        super.startTurn();
        int dmg = -damage.act();
        System.out.println("DAMAGE!!! - " + dmg);
        getEntity().addHp(dmg);
    }
}
