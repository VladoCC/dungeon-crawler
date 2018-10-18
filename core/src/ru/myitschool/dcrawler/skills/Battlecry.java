package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.effects.BattlecryEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 18.11.2017.
 */
public class Battlecry extends Skill {

    int accuracyBonus = 5;
    MathAction action = new DiceAction(6);

    public Battlecry(Entity doer) {
        super(doer);
        setName("Battle cry");
        setDescription("You gets additional +" + accuracyBonus + " accuracy and additional " + action.getDescription() + " damage for all attack on your next turn");
        setIcon(new Texture("battle_cry.png"));
        setMark(false);
        setTargetType(SKILL_TARGET_TYPE_SELF);
        setTypeDisplayer(SKILL_TARGET_TYPE_SELF);
    }

    @Override
    public void use() {
        getDoer().addEffect(new BattlecryEffect(getDoer(), accuracyBonus, action, 1));
    }
}