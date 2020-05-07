package ru.myitschool.dcrawler.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.ai.AI;
import ru.myitschool.dcrawler.entities.ai.DummyAI;
import ru.myitschool.dcrawler.entities.Enemy;
import ru.myitschool.dcrawler.ui.screens.DungeonScreen;

/**
 * Created by Voyager on 11.12.2017.
 */
public class Dummy extends Enemy {

    int damageLog;
    int damageTurnLog;
    int healLog;
    int healTurnLog;
    int damageTurns;
    int healTurns;
    float damage;
    float heal;

    public Dummy(float x, float y) {
        super(x, y);
        setChallengeRating(0);
        setModel(new Texture("sprites/goblinwarrior.png"));
        setPortrait(new Texture("goblin.png"));
        setHp(1000);
        setHpMax(2000);
        setMp(6);
        setMpMax(6);
        setArmor(0);
        damageLog = DungeonScreen.addLog("");
        damageTurnLog = DungeonScreen.addLog("");
        healLog = DungeonScreen.addLog("");
        healTurnLog = DungeonScreen.addLog("");
        damageTurns = 0;
        healTurns = 0;
        damage = 0;
        heal = 0;
    }

    @Override
    public AI createAI() {
        return new DummyAI(this);
    }

    @Override
    public int onDamage(int damage) {
        damageTurns++;
        this.damage += damage;
        DungeonScreen.changeLog("Damage: " + damage, damageLog);
        DungeonScreen.changeLog("Damage per turn: " + (this.damage / damageTurns), damageTurnLog);
        return damage;
    }

    @Override
    public int onHeal(int heal) {
        healTurns++;
        this.heal += heal;
        DungeonScreen.changeLog("Damage: " + heal, healLog);
        DungeonScreen.changeLog("Damage per turn: " + (this.heal / healTurns), healTurnLog);
        return heal;
    }
}