package ru.myitschool.dcrawler.entities.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.effects.MarkedEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.entities.skills.action.SimpleEffectAction;
import ru.myitschool.dcrawler.entities.skills.patterns.EnemyTargetPattern;
import ru.myitschool.dcrawler.entities.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.entities.skills.targeting.EnemyDisplayer;
import ru.myitschool.dcrawler.entities.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 28.11.2017.
 */
public class Mark extends Skill {

    MarkedEffect effect = null;
    int penalty = -5;

    public Mark(Entity doer) {
        super(doer);
        addPlayContainer().getEnemyPlay().addAction(new SimpleEffectAction(this) {
            @Override
            public void successEffect(Target target, int damage, FloatingDamageMark mark) {
                if (effect != null){
                    effect.getEntity().removeEffect(effect);
                }
                effect = new MarkedEffect(target.getEntity(), getDoer(), penalty);
                target.getEntity().addEffect(effect);
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
        return new Texture("mark.png");
    }

    @Override
    public String getName() {
        return "Mark";
    }

    @Override
    public String getDescription() {
        return "You mark target and it gets" + penalty +
                " accuracy penalty for all attacks, except attacks targeted" +
                " to you, but you can place only one mark at the time";
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
