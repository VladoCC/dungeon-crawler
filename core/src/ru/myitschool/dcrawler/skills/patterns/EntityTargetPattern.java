package ru.myitschool.dcrawler.skills.patterns;

import ru.myitschool.dcrawler.dungeon.DungeonCell;
import ru.myitschool.dcrawler.dungeon.DungeonMap;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;

public class EntityTargetPattern extends TargetPattern {

    public EntityTargetPattern(Skill skill) {
        super(skill);
    }

    @Override
    public Target createTarget(Target target) {
        DungeonCell cell = DungeonMap.getCell(target.getX(), target.getY());
        if (cell.hasEntity()){
            return target;
        }
        return null;
    }
}
