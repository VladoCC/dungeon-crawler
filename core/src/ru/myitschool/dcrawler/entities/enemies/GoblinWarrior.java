package ru.myitschool.dcrawler.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.ai.AI;
import ru.myitschool.dcrawler.ai.GoblinAI;
import ru.myitschool.dcrawler.entities.Enemy;

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