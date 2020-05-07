package ru.myitschool.dcrawler.entities.skills.patterns;

import ru.myitschool.dcrawler.entities.skills.Skill;
import ru.myitschool.dcrawler.entities.skills.Target;

public class FloorSwingTargetPattern extends TargetPattern {
    public FloorSwingTargetPattern(Skill skill) {
        super(skill);
    }

    @Override
    public Target createTarget(Target target) {
        int range = getSkill().getRange();
        int cellX = target.getX();
        int cellY = target.getY();
        int charX = getSkill().getDoer().getTileX();
        int charY = getSkill().getDoer().getTileY();
        if (cellY == charY && cellX > charX){
            for (int i = 0; i < range; i++) {
                Target linked1 = new Target(cellX, cellY + 1 + i);
                linked1.setMain(target);
                linked1.setLinked(true);
                Target linked2 = new Target(cellX - 1, cellY + 1 + i);
                linked2.setMain(target);
                linked2.setLinked(true);
                Target linked3 = new Target(cellX, cellY - 1 - i);
                linked3.setMain(target);
                linked3.setLinked(true);
                Target linked4 = new Target(cellX - 1, cellY - 1 - i);
                linked4.setMain(target);
                linked4.setLinked(true);
                target.addLinkedTarget(linked1);
                target.addLinkedTarget(linked2);
                target.addLinkedTarget(linked3);
                target.addLinkedTarget(linked4);
            }
            for (int i = 1; i < range; i++) {
                for (int j = 0; j < 2 * range + 1; j++) {
                    Target linked = new Target(cellX + i, cellY - range + j);
                    linked.setMain(target);
                    linked.setLinked(true);
                    target.addLinkedTarget(linked);
                }
            }
        } else if (cellY == charY && cellX < charX){
            for (int i = 0; i < range; i++) {
                Target linked1 = new Target(cellX, cellY + 1 + i);
                linked1.setMain(target);
                linked1.setLinked(true);
                Target linked2 = new Target(cellX + 1, cellY + 1 + i);
                linked2.setMain(target);
                linked2.setLinked(true);
                Target linked3 = new Target(cellX, cellY - 1 - i);
                linked3.setMain(target);
                linked3.setLinked(true);
                Target linked4 = new Target(cellX + 1, cellY - 1 - i);
                linked4.setMain(target);
                linked4.setLinked(true);
                target.addLinkedTarget(linked1);
                target.addLinkedTarget(linked2);
                target.addLinkedTarget(linked3);
                target.addLinkedTarget(linked4);
            }
            for (int i = 1; i < range; i++) {
                for (int j = 0; j < 2 * range + 1; j++) {
                    Target linked = new Target(cellX - i, cellY - range + j);
                    linked.setMain(target);
                    linked.setLinked(true);
                    target.addLinkedTarget(linked);
                }
            }
        } else if (cellX == charX && cellY > charY){
            for (int i = 0; i < range; i++) {
                Target linked1 = new Target(cellX + 1 + i, cellY);
                linked1.setMain(target);
                linked1.setLinked(true);
                Target linked2 = new Target(cellX + 1 + i, cellY - 1);
                linked2.setMain(target);
                linked2.setLinked(true);
                Target linked3 = new Target(cellX - 1 - i, cellY);
                linked3.setMain(target);
                linked3.setLinked(true);
                Target linked4 = new Target(cellX - 1 - i, cellY - 1);
                linked4.setMain(target);
                linked4.setLinked(true);
                target.addLinkedTarget(linked1);
                target.addLinkedTarget(linked2);
                target.addLinkedTarget(linked3);
                target.addLinkedTarget(linked4);
            }
            for (int i = 1; i < range; i++) {
                for (int j = 0; j < 2 * range + 1; j++) {
                    Target linked = new Target(cellX - range + j, cellY + i);
                    linked.setMain(target);
                    linked.setLinked(true);
                    target.addLinkedTarget(linked);
                }
            }
        } else if (cellX == charX && cellY < charY){
            for (int i = 0; i < range; i++) {
                Target linked1 = new Target(cellX + 1 + i, cellY);
                linked1.setMain(target);
                linked1.setLinked(true);
                Target linked2 = new Target(cellX + 1 + i, cellY + 1);
                linked2.setMain(target);
                linked2.setLinked(true);
                Target linked3 = new Target(cellX - 1 - i, cellY);
                linked3.setMain(target);
                linked3.setLinked(true);
                Target linked4 = new Target(cellX - 1 - i, cellY + 1);
                linked4.setMain(target);
                linked4.setLinked(true);
                target.addLinkedTarget(linked1);
                target.addLinkedTarget(linked2);
                target.addLinkedTarget(linked3);
                target.addLinkedTarget(linked4);
            }
            for (int i = 1; i < range; i++) {
                for (int j = 0; j < 2 * range + 1; j++) {
                    Target linked = new Target(cellX - range + j, cellY - i);
                    linked.setMain(target);
                    linked.setLinked(true);
                    target.addLinkedTarget(linked);
                }
            }
        }
        return target;
    }
}
