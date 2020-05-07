package ru.myitschool.dcrawler.entities.skills;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.effects.CloudOfKnivesEffect;
import ru.myitschool.dcrawler.effects.FloorClearingEffect;
import ru.myitschool.dcrawler.effects.FloorEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.entities.skills.action.SimpleAttackAction;
import ru.myitschool.dcrawler.entities.skills.patterns.FloorSplashTargetPattern;
import ru.myitschool.dcrawler.entities.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.entities.skills.targeting.SplashDisplayer;
import ru.myitschool.dcrawler.entities.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 30.11.2017.
 */
public class CloudOfKnives extends Skill {

    private static final Color color = new Color(0xa020f0bf);

    MathAction attackAction = new DiceAction(6);

    public CloudOfKnives(Entity doer) {
        super(doer);
        addPlayContainer().getEntityPlay().addAction(new SimpleAttackAction(this, attackAction));
    }

    @Override
    public void maintainDisplayers(Array<TargetDisplayer> displayers) {
        displayers.add(new SplashDisplayer(true));
    }

    @Override
    public TargetPattern getPattern() {
        return new FloorSplashTargetPattern(this);
    }

    @Override
    public Texture getIcon() {
        return new Texture("cloud_knives.png");
    }

    @Override
    public String getName() {
        return "Cloud of Knives";
    }

    @Override
    public String getDescription() {
        return "You creates cloud of psi energy, that deal damage to all creatures in zone instantly " +
                "and every time creature starts turn in zone on enter the zone to the end of your next turn";
    }

    @Override
    public int getSkillAccuracyBonus() {
        return 0;
    }

    @Override
    public int getRange() {
        return 3;
    }

    @Override
    public int getDistanceMin() {
        return 3;
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
        return SKILL_TARGET_TYPE_FLOOR_SPLASH;
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
        return true;
    }

    @Override
    public void use() {
        FloorEffect effect = null;
        effect = new FloorEffect(getTargets(), new CloudOfKnivesEffect(effect, countAttackAction(attackAction)), color, true, true);
        getDoer().addEffect(new FloorClearingEffect(effect, 1));
        super.use();
    }
}
