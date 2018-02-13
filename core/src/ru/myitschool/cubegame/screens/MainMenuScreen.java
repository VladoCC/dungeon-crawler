package ru.myitschool.cubegame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Character;
import ru.myitschool.cubegame.skills.*;
import ru.myitschool.cubegame.tiles.DungeonTile;


public class MainMenuScreen implements Screen
{
	private TextureAtlas atlas;
	private Skin skin;
	//private Table table;
	private TextButton playButton;
	private TextButton exitButton;
	private TextButtonStyle buttonStyle;
	private BitmapFont font;
	private DungeonScreen gameScreen;
	private Table table;
	private Stage stage;
	private InputMultiplexer input;

	
	public MainMenuScreen(InputMultiplexer input)
	{
	    this.input = input;
		this.stage = new Stage();
		this.atlas = new TextureAtlas("data/ui/ui.pack");
		this.font = new BitmapFont();
		this.table = new Table();
		table.debug();
		this.skin = new Skin(this.atlas);		
		table.setSkin(this.skin);
		
		this.buttonStyle = new TextButtonStyle();
		this.buttonStyle.up = this.skin.getDrawable("button_up");
		this.buttonStyle.down = this.skin.getDrawable("button_up");
		this.buttonStyle.font = this.font;
		
		this.skin.add("default", this.buttonStyle);
		
		
		this.playButton = new TextButton("Play", this.skin);
		this.exitButton = new TextButton("Exit", this.skin);
	}

	private void genChars(int count){
	    int x = DungeonMap.ROOM_WIDTH / 2 - 1;
	    int y = DungeonMap.ROOM_HEIGHT / 2 - 1;
        if (count  > 0){
            x *= DungeonTile.TILE_WIDTH;
            y *= DungeonTile.TILE_HEIGHT;
            System.out.println(x + "; " + y);
            Character warrior = new Character(new Texture("sprites/Char1.png"), new Texture("warrior.png"), x, y, 20, 20, 6, 6,15);
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
            Character mage = new Character(new Texture("sprites/Char1.png"), new Texture("mage.png"), x, y, 20, 20, 6, 6,15);
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
            Character rogue = new Character(new Texture("sprites/Char1.png"), new Texture("rogue.png"), x, y, 15, 20, 6,6,15);
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
            Character cleric = new Character(new Texture("sprites/Char1.png"), new Texture("cleric.png"), x, y, 20, 20, 6, 6,15);
            Array<Skill> skills = new Array<Skill>();
            skills.add(new Heal(cleric));
            skills.add(new GodsProtection(cleric));
            skills.add(new Vampirism(cleric));
            skills.add(new Bomb(cleric));
            cleric.setSkills(skills);
            count--;
        }
    }

	@Override
	public void render(float delta) 
	{
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() 
	{
		playButton.addListener(new ClickListener(){
			@Override
	        public void clicked(InputEvent event, float x, float y) 
	        {
	        	input.removeProcessor(stage );
	            ((Game)Gdx.app.getApplicationListener()).setScreen(new DungeonScreen(input));
	            //Gdx.graphics.setWindowedMode(1280, 720);
	        }
	    });
	    exitButton.addListener(new ClickListener(){
	        @Override
	        public void clicked(InputEvent event, float x, float y) {
	            Gdx.app.exit();
	        }
	    });
			
        table.add(playButton).row();
        table.add(exitButton).row();

        table.setFillParent(true);
        stage.addActor(table);

        input.addProcessor(stage);
        genChars(4);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		dispose();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
        skin.dispose();
	}
	
	

}
