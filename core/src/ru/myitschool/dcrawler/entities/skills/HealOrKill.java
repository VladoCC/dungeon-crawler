package ru.myitschool.dcrawler.entities.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.NegativeAction;
import ru.myitschool.dcrawler.entities.skills.action.SimpleAttackAction;
import ru.myitschool.dcrawler.entities.skills.check.PlayCheck;
import ru.myitschool.dcrawler.entities.skills.patterns.EntityTargetPattern;
import ru.myitschool.dcrawler.entities.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.entities.skills.targeting.EntityDisplayer;
import ru.myitschool.dcrawler.entities.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 26.11.2017.
 */
public class HealOrKill extends Skill {

    MathAction attackAction = new DiceAction(6);
    MathAction healAction = new NegativeAction(attackAction);

    public HealOrKill(Entity doer) {
        super(doer);
        PlayContainer container = addPlayContainer();
        container.getPlayerPlay().setPlayCheck(new PlayCheck(this) {
            @Override
            public int check(Target target) {
                return Play.TARGETING_HIT;
            }
        });
        container.getEnemyPlay().addAction(new SimpleAttackAction(this, attackAction));
        container.getPlayerPlay().addAction(new SimpleAttackAction(this, healAction));
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
        return new Texture("heal.png");
    }

    @Override
    public String getName() {
        return "Heal or Kill";
    }

    @Override
    public String getDescription() {
        return "You can heal your ally or damage enemy for " + healAction.getDescription();
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
        return false;
    }

    @Override
    public boolean isWallTargets() {
        return false;
    }
}
