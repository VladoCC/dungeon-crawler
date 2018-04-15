package ru.myitschool.cubegame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ru.myitschool.cubegame.entities.Entity;


public class MainMenuScreen extends AdvancedScreen {
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
	
	public MainMenuScreen() {
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
	        	getInput().removeProcessor(stage);
                Entity.getPlayingEntities().removeIf(entity -> entity.isEnemy());
                ((Game) Gdx.app.getApplicationListener()).setScreen(new CharGenScreen());
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

        getInput().addProcessor(stage);
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
