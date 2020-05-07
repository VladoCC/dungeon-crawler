package ru.myitschool.dcrawler.ui.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import ru.myitschool.dcrawler.Dungeon;
import com.devon.dungeon.DungeonGenerator;
import com.devon.dungeon.InputHandler;
import com.devon.dungeon.MyGdxGame;
import com.devon.dungeon.Character;
import com.devon.dungeon.tiles.Tile;
import com.devon.dungeon.ui.PlayerActionMenu;
import com.devon.pathfinding.DFS;
import com.devon.pathfinding.Node;

public class GameScreen extends AbstractScreen
{
	private Dungeon dungeon;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	public  Character player;
	private InputHandler input;
	private PlayerActionMenu actionMenu;
	private InputMultiplexer inputMultiplexor;
	
	public GameScreen(MyGdxGame game) 
	{
		super(game);
		
	}

	@Override
	public void render(float delta) 
	{
		super.render(delta);
		camera.update();

		renderer.setView(camera);
		
		//render tiles
		renderer.render();
	
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); 
		font.draw(batch, "camera: " + camera.position, 10, 50); 
		font.draw(batch, "Stage:" + this.stage.getCamera().position, 10, 80);
		batch.end();
		
		//update and render stage actors(UI/players/AI/objects)
		stage.act(delta);
        stage.draw();
        
        
		
	}

	@Override
	public void resize(int width, int height)
	{
		
		
	}

	@Override
	public void show() 
	{
		//Gdx.graphics.setDisplayMode(1700, 960, false);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		this.inputMultiplexor = new InputMultiplexer();
	
		
		
		

		this.camera = new OrthographicCamera(w, h);
		
		this.camera.setToOrtho(false, w, h);
		this.stage.setCamera(this.camera);
		
		this.input = new InputHandler(this.camera);
		//Gdx.input.setInputProcessor(this.input);

		dungeon = new Dungeon(DungeonGenerator.generateDungeonMap());

		renderer = new OrthogonalTiledMapRenderer(dungeon);
		
		this.player = new Character(dungeon);
		this.actionMenu = new PlayerActionMenu(this, this.player);
		this.stage.addActor(this.player);
		this.stage.addActor(actionMenu);
		
		this.inputMultiplexor.addProcessor(stage);
		this.inputMultiplexor.addProcessor(input);
		Gdx.input.setInputProcessor(this.inputMultiplexor);
		
		camera.position.set(player.getX(), player.getY(), 0f);
		//camera.position.set(0,0, 0);
			
	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
		
	}

	
	
}
