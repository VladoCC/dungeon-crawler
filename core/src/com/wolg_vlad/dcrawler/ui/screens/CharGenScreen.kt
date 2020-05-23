package com.wolg_vlad.dcrawler.ui.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.dungeon.Dungeon
import com.wolg_vlad.dcrawler.dungeon.Room
import com.wolg_vlad.dcrawler.entities.Character
import com.wolg_vlad.dcrawler.entities.skills.*
import com.wolg_vlad.dcrawler.story.quest.Quest
import com.wolg_vlad.dcrawler.story.quest.StayHereQuest
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile

/**
 * Created by Voyager on 15.05.2017.
 */
class CharGenScreen : AdvancedScreen() {
    override fun show() {
        if (!Character.created) {
            genChars(4)
        }
        val quest: Quest = StayHereQuest(12, 3, 25)
        quest.setRoom(Room("rooms/default.room"))
        (Gdx.app.applicationListener as Game).screen = DungeonScreen(Dungeon(quest))
    }

    private fun genChars(count: Int) {
        var count = count
        var x = Dungeon.ROOM_WIDTH / 2 - 1
        var y = Dungeon.ROOM_HEIGHT / 2 - 1
        if (count > 0) {
            x *= DungeonTile.TILE_WIDTH
            y *= DungeonTile.TILE_HEIGHT
            println("$x; $y")
            val warrior = Character(Texture("sprites/Char1.png"), Texture("warrior.png"), x.toFloat(), y.toFloat(), 22, 22, 5, 5, 10)
            val skills = Array<Skill>()
            skills.add(SpearSting(warrior))
            skills.add(Slash(warrior))
            skills.add(ShieldBash(warrior))
            skills.add(Mark(warrior))
            warrior.skills = skills
            count--
        }
        if (count > 0) {
            x += DungeonTile.TILE_WIDTH
            val mage = Character(Texture("sprites/Char1.png"), Texture("mage.png"), x.toFloat(), y.toFloat(), 12, 12, 100, 100, 4)
            val skills = Array<Skill>()
            skills.add(MindControl(mage))
            skills.add(CloudOfKnives(mage))
            skills.add(ForceWave(mage))
            skills.add(Immobilize(mage))
            mage.skills = skills
            count--
        }
        if (count > 0) {
            y += DungeonTile.TILE_HEIGHT
            val rogue = Character(Texture("sprites/Char1.png"), Texture("rogue.png"), x.toFloat(), y.toFloat(), 17, 17, 8, 8, 6)
            val skills = Array<Skill>()
            skills.add(Strike(rogue))
            skills.add(JaggedSword(rogue))
            skills.add(Shoot(rogue))
            skills.add(Battlecry(rogue))
            rogue.skills = skills
            count--
        }
        if (count > 0) {
            x -= DungeonTile.TILE_WIDTH
            val cleric = Character(Texture("sprites/Char1.png"), Texture("cleric.png"), x.toFloat(), y.toFloat(), 14, 14, 6, 6, 6)
            val skills = Array<Skill>()
            skills.add(HealOrKill(cleric))
            skills.add(ArcaneShielding(cleric))
            skills.add(Vampirism(cleric))
            skills.add(Bomb(cleric))
            cleric.skills = skills
            count--
        }
        Character.created = true
    }

    override fun render(delta: Float) {}
    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}