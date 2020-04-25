package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.action.Action;
import ru.myitschool.dcrawler.skills.action.SimpleAttackAction;
import ru.myitschool.dcrawler.skills.patterns.FloorSplashTargetPattern;
import ru.myitschool.dcrawler.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.skills.targeting.SplashDisplayer;
import ru.myitschool.dcrawler.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 25.11.2017.
 */
public class Bomb extends Skill {

    MathAction attackAction = new DiceAction(4);

    public Bomb(Entity doer) {
        super(doer);
        addPlayContainer().getEntityPlay().addAction(new SimpleAttackAction(this, attackAction));
    }

    @Override
    public void maintainDisplayers(Array<TargetDisplayer> displayers) {
        displayers.add(new SplashDisplayer(true));
    }

    @Override
    public TargetPattern getPattern() {
        return new FloorSplashTargetPattern(this);
    }

    @Override
    public Texture getIcon() {
        return new Texture("bomb.png");
    }

    @Override
    public String getName() {
        return "Bomb";
    }

    @Override
    public String getDescription() {
        return "Deal " + attackAction.getDescription() + " damage to all creatures in small zone";
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
        return 2;
    }

    @Override
    public int getDistanceMax() {
        return 8;
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
        return SKILL_TARGET_TYPE_FLOOR_SPLASH;
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
        return true;
    }
}
