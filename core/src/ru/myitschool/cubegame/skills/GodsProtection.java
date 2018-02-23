package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.GodsProtectionEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 26.11.2017.
 */
public class GodsProtection extends Skill {

    MathAction rollAction = new DiceAction(1, 20);

    public GodsProtection(Entity doer) {
        super(doer);
        setIcon(new Texture("gods_protection.png"));
        setName("God's protection");
        setDescription("God protects you from getting damage");
        setTargetCountMax(0);
        setDistanceMax(0);
        setDistanceMin(0);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_SELF);
        setType(SKILL_TYPE_ENCOUNTER);
        setMark(false);
    }

    @Override
    public void use() {
        getDoer().addEffect(new GodsProtectionEffect(getDoer()));
    }
}
