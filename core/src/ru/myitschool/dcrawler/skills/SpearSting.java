package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.action.Action;
import ru.myitschool.dcrawler.skills.action.SimpleAttackAction;
import ru.myitschool.dcrawler.skills.patterns.FloorWaveTargetPattern;
import ru.myitschool.dcrawler.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.skills.targeting.TargetDisplayer;
import ru.myitschool.dcrawler.skills.targeting.WaveDisplayer;

/**
 * Created by Voyager on 02.12.2017.
 */
public class SpearSting extends Skill {

    MathAction attackAction = new DiceAction(10);

    public SpearSting(Entity doer) {
        super(doer);
        addPlayContainer().getEntityPlay().addAction(new SimpleAttackAction(this, attackAction));
        addPlayContainer();
    }

    @Override
    public void maintainDisplayers(Array<TargetDisplayer> displayers) {
        displayers.add(new WaveDisplayer());
    }

    @Override
    public TargetPattern getPattern() {
        return new FloorWaveTargetPattern(this);
    }

    @Override
    public Texture getIcon() {
        return new Texture("spear_sting.png");
    }

    @Override
    public String getName() {
        return "Spear sting";
    }

    @Override
    public String getDescription() {
        return "You attacks all enemies that stands in two cells in front of you and deal " + attackAction.getDescription() + " damage";
    }

    @Override
    public int getSkillAccuracyBonus() {
        return 0;
    }

    @Override
    public int getRange() {
        return 2;
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
        return SKILL_TARGET_TYPE_FLOOR_WAVE;
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
