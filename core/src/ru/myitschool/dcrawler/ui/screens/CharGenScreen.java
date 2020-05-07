package ru.myitschool.dcrawler.ui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.dungeon.Dungeon;
import ru.myitschool.dcrawler.dungeon.Room;
import ru.myitschool.dcrawler.entities.Character;
import ru.myitschool.dcrawler.entities.skills.*;
import ru.myitschool.dcrawler.story.quest.Quest;
import ru.myitschool.dcrawler.story.quest.StayHereQuest;
import ru.myitschool.dcrawler.ui.tiles.DungeonTile;

/**
 * Created by Voyager on 15.05.2017.
 */
public class CharGenScreen extends AdvancedScreen {
    @Override
    public void show() {
        if (!Character.isCreated()){
            genChars(4);
        }

        Quest quest = new StayHereQuest(12, 3, 25);
        quest.setRoom(new Room("rooms/default.room"));

        ((Game) Gdx.app.getApplicationListener()).setScreen(new DungeonScreen(new Dungeon(quest)));
    }

    private void genChars(int count){
        int x = Dungeon.ROOM_WIDTH / 2 - 1;
        int y = Dungeon.ROOM_HEIGHT / 2 - 1;
        if (count  > 0){
            x *= DungeonTile.TILE_WIDTH;
            y *= DungeonTile.TILE_HEIGHT;
            System.out.println(x + "; " + y);
            Character warrior = new Character(new Texture("sprites/Char1.png"), new Texture("warrior.png"), x, y, 22, 22, 5, 5,10);
            Array<Skill> skills = new Array<Skill>();
            skills.add(new SpearSting(warrior));
            skills.add(new Slash(warrior));
            skills.add(new ShieldBash(warrior));
            skills.add(new Mark(warrior));
            warrior.setSkills(skills);
            count--;
        }
        if (count > 0){
            x += DungeonTile.TILE_WIDTH;
            Character mage = new Character(new Texture("sprites/Char1.png"), new Texture("mage.png"), x, y, 12, 12, 100, 100,4);
            Array<Skill> skills = new Array<Skill>();
            skills.add(new MindControl(mage));
            skills.add(new CloudOfKnives(mage));
            skills.add(new ForceWave(mage));
            skills.add(new Immobilize(mage));
            mage.setSkills(skills);
            count--;
        }
        if (count > 0){
            y += DungeonTile.TILE_HEIGHT;
            Character rogue = new Character(new Texture("sprites/Char1.png"), new Texture("rogue.png"), x, y, 17, 17, 8,8,6);
            Array<Skill> skills = new Array<Skill>();
            skills.add(new Strike(rogue));
            skills.add(new JaggedSword(rogue));
            skills.add(new Shoot(rogue));
            skills.add(new Battlecry(rogue));
            rogue.setSkills(skills);
            count--;
        }
        if (count > 0){
            x -= DungeonTile.TILE_WIDTH;
            Character cleric = new Character(new Texture("sprites/Char1.png"), new Texture("cleric.png"), x, y, 14, 14, 6, 6,6);
            Array<Skill> skills = new Array<Skill>();
            skills.add(new HealOrKill(cleric));
            skills.add(new ArcaneShielding(cleric));
            skills.add(new Vampirism(cleric));
            skills.add(new Bomb(cleric));
            cleric.setSkills(skills);
            count--;
        }
        Character.setCreated(true);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
