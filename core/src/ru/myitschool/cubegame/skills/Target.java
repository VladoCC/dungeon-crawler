package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.DungeonCell;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 05.07.2017.
 */
public class Target {

    private int x;
    private int y;

    private int checkX;
    private int checkY;

    private boolean linked = false;

    private Target main;
    private Array<Target> linkedTargets = new Array<Target>();

    public Target(int x, int y) {
        this.x = x;
        this.y = y;
        this.checkX = x;
        this.checkY = y;
        this.main = this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCheckX() {
        return checkX;
    }

    public void setCheckX(int checkX) {
        this.checkX = checkX;
    }

    public int getCheckY() {
        return checkY;
    }

    public void setCheckY(int checkY) {
        this.checkY = checkY;
    }

    public void setCheckCoords(int checkX, int checkY){
        setCheckX(checkX);
        setCheckY(checkY);
    }

    public void move(Vector2 movement){
        x += movement.x;
        y += movement.y;
    }

    public void addLinkedTarget(Target target){
        linkedTargets.add(target);
        target.setLinked(true);
        target.setMain(this);
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
        DungeonCell cell = DungeonMap.getCell(x, y);
        if (cell != null){
            return cell.getEntity();
        }
        return null;
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
