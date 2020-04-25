package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.effects.DisarmedEffect;
import ru.myitschool.dcrawler.effects.ImmobilizedEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.skills.action.Action;
import ru.myitschool.dcrawler.skills.action.ComplexEffectAction;
import ru.myitschool.dcrawler.skills.action.SimpleEffectAction;
import ru.myitschool.dcrawler.skills.check.EntityPlayCheck;
import ru.myitschool.dcrawler.skills.patterns.EnemyTargetPattern;
import ru.myitschool.dcrawler.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.skills.targeting.EnemyDisplayer;
import ru.myitschool.dcrawler.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Immobilize extends Skill {

    int normalTurns = 1;
    int criticalTurns = 2;

    public Immobilize(Entity doer) {
        super(doer);
        PlayContainer container = addPlayContainer();
        container.getEnemyPlay().setPlayCheck(new EntityPlayCheck(this));
        container.getEnemyPlay().addAction(new ComplexEffectAction(this) {
            int turns = 0;

            @Override
            protected void beforeEffect(Target target, int damage, FloatingDamageMark mark) {

            }

            @Override
            public void successEffect(Target target, int damage, FloatingDamageMark mark) {
                turns = normalTurns;
            }

            @Override
            protected void critSuccessEffect(Target target, int damage, FloatingDamageMark mark) {
                turns = criticalTurns;
            }

            @Override
            protected void failEffect(Target target, int damage, FloatingDamageMark mark) {

            }

            @Override
            protected void critFailEffect(Target target, int damage, FloatingDamageMark mark) {

            }

            @Override
            protected void afterEffect(Target target, int damage, FloatingDamageMark mark) {
                if (turns > 0) {
                    target.getEntity().addEffect(new ImmobilizedEffect(target.getEntity(), turns));
                    target.getEntity().addEffect(new DisarmedEffect(target.getEntity(), turns));
                }
                if (turns == normalTurns) {
                    mark.addText("Success");
                } else if (turns == criticalTurns) {
                    mark.addText("Critical success");
                }
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
        return new Texture("immobilization.png");
    }

    @Override
    public String getName() {
        return "Immobilize";
    }

    @Override
    public String getDescription() {
        return "You immobilizes your target for " + normalTurns + "(" + criticalTurns + ")";
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
        return 2;
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
        return false;
    }

    @Override
    public boolean isWallTargets() {
        return false;
    }
}
