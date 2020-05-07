package ru.myitschool.dcrawler.entities.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.effects.BattlecryEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.entities.skills.patterns.CharacterTargetPattern;
import ru.myitschool.dcrawler.entities.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.entities.skills.targeting.TargetDisplayer;

/**
 * Created by Voyager on 18.11.2017.
 */
public class Battlecry extends Skill {

    int accuracyBonus = 5;
    MathAction action = new DiceAction(6);

    public Battlecry(Entity doer) {
        super(doer);
    }

    @Override
    public void maintainDisplayers(Array<TargetDisplayer> displayers) {

    }

    @Override
    public TargetPattern getPattern() {
        return new CharacterTargetPattern(this);
    }

    @Override
    public Texture getIcon() {
        return new Texture("battle_cry.png");
    }

    @Override
    public String getName() {
        return "Battle cry";
    }

    @Override
    public String getDescription() {
        return "You gets additional +" + accuracyBonus + " accuracy and additional "
                + action.getDescription() + " damage for all attack on your next turn";
    }

    @Override
    public int getSkillAccuracyBonus() {
        return 0;
    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    public int getDistanceMin() {
        return 0;
    }

    @Override
    public int getDistanceMax() {
        return 0;
    }

    @Override
    public int getTargetCountMax() {
        return 0;
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
        return SKILL_TARGET_TYPE_SELF;
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
        return false;
    }

    @Override
    public boolean isObstruct() {
        return false;
    }

    @Override
    public boolean isWallTargets() {
        return false;
    }

    @Override
    public void use() {
        getDoer().addEffect(new BattlecryEffect(getDoer(), accuracyBonus, action, 1));
    }
}
