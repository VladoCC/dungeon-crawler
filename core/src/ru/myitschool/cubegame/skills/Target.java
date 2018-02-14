package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 05.07.2017.
 */
public class Target {

    private int x;
    private int y;
    private boolean linked = false;

    private Target main;
    private Array<Target> linkedTargets = new Array<Target>();

    public Target(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addLinkedTarget(Target target){
        linkedTargets.add(target);
    }

    public Array<Target> getLinkedTargets() {
        return linkedTargets;
    }

    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    public Target getMain() {
        return main;
    }

    public void setMain(Target main) {
        this.main = main;
    }

    public Entity getEntity(){ //TODO change to DungeonCell checks
        /*for (Entity entity : Entity.getPlayingEntities()){
            if (entity.getTileX() == getX() && entity.getTileY() == getY()){
                return entity;
            }
        }
        return null;*/
        return DungeonMap.getCell(x, y).getEntity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Target target = (Target) o;

        if (x != target.x) return false;
        return y == target.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
