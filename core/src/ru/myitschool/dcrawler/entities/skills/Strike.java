package ru.myitschool.dcrawler.entities.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.RepeatAction;
import ru.myitschool.dcrawler.math.SumAction;
import ru.myitschool.dcrawler.entities.skills.action.SimpleAttackAction;
import ru.myitschool.dcrawler.entities.skills.patterns.EnemyTargetPattern;
import ru.myitschool.dcrawler.entities.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.entities.skills.targeting.EnemyDisplayer;
import ru.myitschool.dcrawler.entities.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 15.11.2017.
 */
public class Strike extends Skill {
    
    MathAction attackAction = new SumAction(new RepeatAction(new DiceAction(6), 2));

    public Strike(Entity doer) {
        super(doer);
        addPlayContainer().getEnemyPlay().addAction(new SimpleAttackAction(this, attackAction));
    }

    @Override
    public void maintainDisplayers(Array<TargetDisplayer> displayers) {
        displayers.add(new EnemyDisplayer());
    }

    @Override
    public TargetPattern getPattern() {
        return new EnemyTargetPattern(this);
    }

    @Override
    public Texture getIcon() {
        return new Texture("punch.png");
    }

    @Override
    public String getName() {
        return "Strike";
    }

    @Override
    public String getDescription() {
        return "Strike that deals " + attackAction.getDescription() + " damage";
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
        return 2;
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
        return SKILL_TARGET_TYPE_ENEMY;
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
