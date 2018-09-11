package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.AITweaks;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.targeting.TargetDisplayer;
import ru.myitschool.dcrawler.skills.targeting.TilePos;
import ru.myitschool.dcrawler.tiles.ColorTile;
import ru.myitschool.dcrawler.utils.AdvancedArray;
import ru.myitschool.dcrawler.utils.Utils;

/**
 * Created by Voyager on 06.12.2017.
 */
public class ForceWave extends Skill {

    MathAction rollAction = new DiceAction(20);
    MathAction attackAction = new DiceAction(6);
    int distance = 2;

    public ForceWave(final Entity doer) {
        super(doer);
        setIcon(new Texture("force_wave.png"));
        setName("Force wave");
        setDescription("You repels your target for " + distance + "cells");
        setTargetCountMax(1);
        setDistanceMax(3);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENTITY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENTITY);
        addDisplayer(new TargetDisplayer() {
            @Override
            public boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill) {
                AdvancedArray<Vector2> raytrace = AITweaks.getCellRaytrace(doer.getTileX(), doer.getTileY(), x, y, 2);
                raytrace.clip(raytrace.size - 2, raytrace.size - 1);
                Array<Integer> poses = AITweaks.getObstructorIndexes(raytrace, true);
                if (poses.size > 0){
                    raytrace.clip(0, poses.get(0) - 1);
                }
                for (Vector2 vector : raytrace){
                    array.add(new TilePos((int) vector.x, (int) vector.y, new ColorTile(Color.YELLOW, 0.4f, true)));
                }
                return true;
            }
        });
        setObstruct(true);
        setWallTargets(false);
        PlayContainer container = addPlayContainer();
        container.getEntityPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                    Utils.pushEntity(getDoer(), target.getEntity(), 2);
                    mark.addText("Success");
                }
            }
        }.setAttack(attackAction));
        addPlayContainer();
    }
}
