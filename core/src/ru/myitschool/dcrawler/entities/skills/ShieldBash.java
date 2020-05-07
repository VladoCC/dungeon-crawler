package ru.myitschool.dcrawler.entities.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.effects.ImmobilizedEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.entities.skills.action.SimpleCombinedAction;
import ru.myitschool.dcrawler.entities.skills.patterns.EnemyTargetPattern;
import ru.myitschool.dcrawler.entities.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.entities.skills.targeting.EnemyDisplayer;
import ru.myitschool.dcrawler.entities.skills.targeting.TargetDisplayer;
import ru.myitschool.dcrawler.utils.Utils;

/**
 * Created by Voyager on 02.12.2017.
 */
public class ShieldBash extends Skill {

    MathAction attackAction = new DiceAction(6);
    int turns = 1;
    int distance = 1;

    public ShieldBash(Entity doer) {
        super(doer);
        addPlayContainer().getEnemyPlay().addAction(new SimpleCombinedAction(this, attackAction) {
            @Override
            public void successEffect(Target target, int damage, FloatingDamageMark mark) {
                Entity entity = target.getEntity();
                Utils.pushEntity(getDoer(), target.getEntity(), distance);
                entity.addEffect(new ImmobilizedEffect(entity, turns));
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
        return new Texture("shield_bash.png");
    }

    @Override
    public String getName() {
        return "Shield Bash";
    }

    @Override
    public String getDescription() {
        return "You bashes target with your shield immobilizes it for " + turns + " turn(-s), trows it back for " + distance + " cell(-s) and deal " + attackAction.getDescription() + " damage";
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
        return 1;
    }

    @Override
    public int getTargetCountMax() {
        return 1;
    }

    @Override
    public int getCooldownMax() {
        return 3;
    }

    @Override
    public int getType() {
        return SKILL_TYPE_COOLDOWN_DICE;
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
