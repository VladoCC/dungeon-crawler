package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.effects.ArcaneShieldingEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.action.Action;
import ru.myitschool.dcrawler.skills.action.SimpleAttackAction;
import ru.myitschool.dcrawler.skills.action.SimpleEffectAction;
import ru.myitschool.dcrawler.skills.patterns.CharacterTargetPattern;
import ru.myitschool.dcrawler.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.skills.targeting.CharacterDisplayer;
import ru.myitschool.dcrawler.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 26.11.2017.
 */
public class ArcaneShielding extends Skill {

    MathAction rollAction = new DiceAction(20);

    public ArcaneShielding(Entity doer) {
        super(doer);
        addPlayContainer().getPlayerPlay().addAction(new SimpleEffectAction(this) {
            @Override
            public void successEffect(Target target, int damage, FloatingDamageMark mark) {
                target.getEntity().addEffect(new ArcaneShieldingEffect(getDoer()));
            }
        });
    }

    @Override
    public void maintainDisplayers(Array<TargetDisplayer> displayers) {
        displayers.add(new CharacterDisplayer());
    }

    @Override
    public TargetPattern getPattern() {
        return new CharacterTargetPattern(this);
    }

    @Override
    public Texture getIcon() {
        return new Texture("gods_protection.png");
    }

    @Override
    public String getName() {
        return "Arcane shielding";
    }

    @Override
    public String getDescription() {
        return "This shield will protect its target from any damage for one turn";
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
        return 0;
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
        return SKILL_TARGET_TYPE_CHARACTER;
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
