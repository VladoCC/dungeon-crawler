package ru.myitschool.dcrawler.skills;

import ru.myitschool.dcrawler.skills.check.EntityPlayCheck;
import ru.myitschool.dcrawler.skills.check.PlayCheck;

/**
 * Created by Voyager on 19.04.2018.
 */
public class PlayContainer extends Play {

    private Play floorPlay = this;
    private Play entityPlay;
    private Play playerPlay;
    private Play enemyPlay;
    public PlayContainer(Skill skill) {
        super(skill);
        entityPlay = new Play(skill) { // any entity
            @Override
            public boolean isValidTarget(Target target) {
                return target.getEntity() != null;
            }
        };
        entityPlay.setPlayCheck(new EntityPlayCheck(skill));
        addPlay(entityPlay);
        playerPlay = new Play(skill) {
            @Override
            public boolean isValidTarget(Target target) {
                return target.getEntity() != null && target.getEntity().isPlayer();
            }
        };
        playerPlay.setPlayCheck(new EntityPlayCheck(skill));
        entityPlay.addPlay(playerPlay);
        enemyPlay = new Play(skill) {
            @Override
            public boolean isValidTarget(Target target) {
                return target.getEntity() != null && target.getEntity().isEnemy();
            }
        };
        enemyPlay.setPlayCheck(new EntityPlayCheck(skill));
        entityPlay.addPlay(enemyPlay);
    }

    @Override
    public boolean isValidTarget(Target target) {
        return true;
    }

    public Play getFloorPlay() {
        return floorPlay;
    }

    public Play getEntityPlay() {
        return entityPlay;
    }

    public Play getPlayerPlay() {
        return playerPlay;
    }

    public Play getEnemyPlay() {
        return enemyPlay;
    }
}
