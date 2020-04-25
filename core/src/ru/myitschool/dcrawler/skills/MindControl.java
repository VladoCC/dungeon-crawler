package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.effects.ControlledEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.action.Action;
import ru.myitschool.dcrawler.skills.action.SimpleCombinedAction;
import ru.myitschool.dcrawler.skills.patterns.EnemyTargetPattern;
import ru.myitschool.dcrawler.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.skills.targeting.EnemyDisplayer;
import ru.myitschool.dcrawler.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 25.11.2017.
 */
public class MindControl extends Skill {

    MathAction attackAction = new DiceAction(4);

    public MindControl(Entity doer) {
        super(doer);
        addPlayContainer().getEnemyPlay().addAction(new SimpleCombinedAction(this, attackAction) {
            @Override
            public void successEffect(Target target, int damage, FloatingDamageMark mark) {
                target.getEntity().addEffect(new ControlledEffect(target.getEntity()));
                mark.addText("Success");
            }
        });
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
        return new Texture("mind_control.png");
    }

    @Override
    public String getName() {
        return "Mind control";
    }

    @Override
    public String getDescription() {
        return "You can control your target for one turn";
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
        return 4;
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
        return SKILL_TYPE_ENCOUNTER;
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
        return false;
    }

    @Override
    public boolean isWallTargets() {
        return false;
    }
}
