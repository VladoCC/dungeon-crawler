package ru.myitschool.dcrawler.entities.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.effects.BloodiedEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.NumberAction;
import ru.myitschool.dcrawler.entities.skills.action.SimpleCombinedAction;
import ru.myitschool.dcrawler.entities.skills.patterns.EntityTargetPattern;
import ru.myitschool.dcrawler.entities.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.entities.skills.targeting.EntityDisplayer;
import ru.myitschool.dcrawler.entities.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 18.11.2017.
 */
public class JaggedSword extends Skill {

    MathAction attackAction = new DiceAction(6);

    int normalDamage = 3;
    int criticalDamage = 5;

    int rounds = 3;

    public JaggedSword(Entity doer) {
        super(doer);
        addPlayContainer().getEnemyPlay().addAction(new SimpleCombinedAction(this, attackAction) {
            int bleedDamage = 0;

            @Override
            protected void successEffect(Target target, int damage, FloatingDamageMark mark) {
                bleedDamage = normalDamage;
            }

            @Override
            protected void critSuccessEffect(Target target, int damage, FloatingDamageMark mark) {
                bleedDamage = criticalDamage;
            }

            @Override
            protected void afterEffect(Target target, int damage, FloatingDamageMark mark) {
                if (bleedDamage > 0) {
                    Entity entity = target.getEntity();
                    entity.addEffect(new BloodiedEffect(entity, new NumberAction(bleedDamage), rounds));
                }
                if (bleedDamage == normalDamage) {
                    mark.addText("Success");
                } else if (bleedDamage == criticalDamage) {
                    mark.addText("Critical success");
                }
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
        return new Texture("blood.png");
    }

    @Override
    public String getName() {
        return "Jagged sword";
    }

    @Override
    public String getDescription() {
        return "Deal some damage and adds bleed to target";
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
        return 4;
    }

    @Override
    public int getType() {
        return SKILL_TYPE_COOLDOWN;
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
