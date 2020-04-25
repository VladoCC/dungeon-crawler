package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.effects.ProtectionEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.action.Action;
import ru.myitschool.dcrawler.skills.action.SimpleCombinedAction;
import ru.myitschool.dcrawler.skills.patterns.EntityTargetPattern;
import ru.myitschool.dcrawler.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.skills.targeting.EntityDisplayer;
import ru.myitschool.dcrawler.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 05.11.2017.
 */
public class ProtectiveStance extends Skill {

    MathAction attackAction = new DiceAction(4);
    int armor = 3;

    public ProtectiveStance(final Entity doer) {
        super(doer);
        addPlayContainer().getPlayerPlay().addAction(new SimpleCombinedAction(this, attackAction) {
            @Override
            public void successEffect(Target target, int damage, FloatingDamageMark mark) {

            }

            @Override
            protected void afterEffect(Target target, int damage, FloatingDamageMark mark) {
                doer.addEffect(new ProtectionEffect(doer, armor));
            }
        });
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
        return new Texture("protectivestance.png");
    }

    @Override
    public String getName() {
        return "Protective stance";
    }

    @Override
    public String getDescription() {
        return "You deal " + attackAction.getDescription() +
                " damage and anyway take up a protective stance that gives you 5 additional armor";
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
