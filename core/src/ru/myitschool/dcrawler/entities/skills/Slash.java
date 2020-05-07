package ru.myitschool.dcrawler.entities.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.entities.skills.action.SimpleAttackAction;
import ru.myitschool.dcrawler.entities.skills.patterns.FloorSwingTargetPattern;
import ru.myitschool.dcrawler.entities.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.entities.skills.targeting.SwingDisplayer;
import ru.myitschool.dcrawler.entities.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 16.11.2017.
 */
public class Slash extends Skill {

    MathAction attackAction = new DiceAction(8);

    public Slash(Entity doer) {
        super(doer);
        addPlayContainer().getEntityPlay().addAction(new SimpleAttackAction(this, attackAction));
    }

    @Override
    public void maintainDisplayers(Array<TargetDisplayer> displayers) {
        displayers.add(new SwingDisplayer());
    }

    @Override
    public TargetPattern getPattern() {
        return new FloorSwingTargetPattern(this);
    }

    @Override
    public Texture getIcon() {
        return new Texture("slash.png");
    }

    @Override
    public String getName() {
        return "Slash";
    }

    @Override
    public String getDescription() {
        return "You swings with your halberd and deal damage to all enemies in front of you";
    }

    @Override
    public int getSkillAccuracyBonus() {
        return 2;
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
        return 1;
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
        return SKILL_TARGET_TYPE_FLOOR_SWING;
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
