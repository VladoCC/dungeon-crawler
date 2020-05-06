package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.AIUtils;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.action.SimpleCombinedAction;
import ru.myitschool.dcrawler.skills.patterns.EntityTargetPattern;
import ru.myitschool.dcrawler.skills.patterns.TargetPattern;
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
        addPlayContainer().getEntityPlay().addAction(new SimpleCombinedAction(this, attackAction) {
            @Override
            public void successEffect(Target target, int damage, FloatingDamageMark mark) {
                Utils.pushEntity(getDoer(), target.getEntity(), distance);
                mark.addText("Success");
            }
        });
    }

    @Override
    public void maintainDisplayers(Array<TargetDisplayer> displayers) {
        displayers.add(new TargetDisplayer() {
            @Override
            public boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill) {
                AdvancedArray<Vector2> raytrace = AIUtils.getCellRaytrace(getDoer().getTileX(), getDoer().getTileY(), x, y, 2);
                raytrace.clip(raytrace.size - 2, raytrace.size - 1);
                Array<Integer> poses = AIUtils.getObstructorIndexes(raytrace, true);
                if (poses.size > 0){
                    raytrace.clip(0, poses.get(0) - 1);
                }
                for (Vector2 vector : raytrace){
                    array.add(new TilePos((int) vector.x, (int) vector.y, new ColorTile(Color.YELLOW, 0.4f, true)));
                }
                return true;
            }
        });
    }

    @Override
    public TargetPattern getPattern() {
        return new EntityTargetPattern(this);
    }

    @Override
    public Texture getIcon() {
        return new Texture("force_wave.png");
    }

    @Override
    public String getName() {
        return "Force wave";
    }

    @Override
    public String getDescription() {
        return "You repels your target for " + distance + "cells";
    }

    @Override
    public int getSkillAccuracyBonus() {
        return 0;
    }

    @Override
    public int getRange() {
        return 1;
    }

    @Override
    public int getDistanceMin() {
        return 1;
    }

    @Override
    public int getDistanceMax() {
        return 3;
    }

    @Override
    public int getTargetCountMax() {
        return 1;
    }

    @Override
    public int getCooldownMax() {
        return 0;
    }

    @Override
    public int getType() {
        return SKILL_TYPE_AT_WILL;
    }

    @Override
    public int getTargetType() {
        return SKILL_TARGET_TYPE_ENTITY;
    }

    @Override
    public boolean isCheckAllTargets() {
        return false;
    }

    @Override
    public boolean isMarkEverything() {
        return false;
    }

    @Override
    public boolean isMark() {
        return true;
    }

    @Override
    public boolean isObstruct() {
        return true;
    }

    @Override
    public boolean isWallTargets() {
        return false;
    }
}
