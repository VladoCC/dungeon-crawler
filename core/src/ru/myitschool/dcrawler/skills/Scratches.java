package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.action.Action;
import ru.myitschool.dcrawler.skills.action.ComplexAttackAction;
import ru.myitschool.dcrawler.skills.patterns.EntityTargetPattern;
import ru.myitschool.dcrawler.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.skills.targeting.EntityDisplayer;
import ru.myitschool.dcrawler.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 26.10.2017.
 */
public class Scratches extends Skill {

    MathAction attackAction = new DiceAction(6);
    MathAction critAction = new DiceAction(8);
    int accuracyBonus = 1;

    public Scratches(final Entity doer) {
        super(doer);
        addPlayContainer().getPlayerPlay().addAction(new ComplexAttackAction(this, attackAction, critAction, null, null));
    }

    @Override
    public void maintainDisplayers(Array<TargetDisplayer> displayers) {
        displayers.add(new EntityDisplayer());
    }

    @Override
    public TargetPattern getPattern() {
        return new EntityTargetPattern(this);
    }

    @Override
    public Texture getIcon() {
        return new Texture("scratches.png");
    }

    @Override
    public String getName() {
        return "Scratches";
    }

    @Override
    public String getDescription() {
        return "You scratch body of your enemy with your claws. It is not as effective as weapons, " +
                "but this can be done faster and more accurately. It gives you +" + getSkillAccuracyBonus() +
                " bonus accuracy. And deals " + attackAction.getDescription() + "(" + critAction.getDescription() +
                ") damage";
    }

    @Override
    public int getSkillAccuracyBonus() {
        return accuracyBonus;
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