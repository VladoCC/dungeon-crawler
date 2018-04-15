package ru.myitschool.cubegame.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.ai.AI;
import ru.myitschool.cubegame.ai.GoblinAI;
import ru.myitschool.cubegame.entities.Enemy;

/**
 * Created by Voyager on 11.08.2017.
 */
public class GoblinWarrior extends Enemy {

    public GoblinWarrior(float x, float y) {
        super(x, y);
        setChallengeRating(1);
        setModel(new Texture("sprites/goblinwarrior.png"));
        setPortrait(new Texture("goblin.png"));
        setHp(10);
        setHpMax(10);
        setMp(6);
        setMpMax(6);
        setArmor(5);
    }

    @Override
    public AI createAI() {
        return new GoblinAI(this);
    }

}