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
 * Created by Voyager on 02.12.2017.
 */
public class Vampirism extends Skill {

    MathAction attackAction = new DiceAction(6);
    int damage = 3;
    int turns = 3;

    public Vampirism(final Entity doer) {
        super(doer);
        addPlayContainer().getEnemyPlay().addAction(new SimpleCombinedAction(this, attackAction) {
            @Override
            public void successEffect(Target target, int damage, FloatingDamageMark mark) {
                doer.addHp(damage);
                if (target.getEntity().getHp() <= target.getEntity().getHpMax() / 4){
                    target.getEntity().addEffect(new BloodiedEffect(target.getEntity(), new NumberAction(damage), turns));
                }
                mark.addText(damage + "");
                FloatingDamageMark damageMark = new FloatingDamageMark(doer.getTileX(), doer.getTileY(), -damage + "");
                damageMark.show();
            }
        });
        addPlayContainer();
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
        return new Texture("vampirism.png");
    }

    @Override
    public String getName() {
        return "Vampirism";
    }

    @Override
    public String getDescription() {
        return "You deal damage to your target and restore as many hp as it loses. If target will be lower then quarter of max hp, it will bleed " + turns +" turn(-s) for " + damage + " damage";
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
