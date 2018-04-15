package ru.myitschool.cubegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.myitschool.cubegame.encounters.Encounter;
import ru.myitschool.cubegame.entities.Enemy;
import ru.myitschool.cubegame.screens.MainMenuScreen;
import ru.myitschool.cubegame.tiles.DungeonTile;

public class MyGdxGame extends Game
{
    private MainMenuScreen mainMenuScreen;
    private boolean debugInfo = false;

    protected BitmapFont font;
    protected SpriteBatch batch;

    private static InputMultiplexer input;

    @Override
    public void create() {
        font = new BitmapFont();
        batch = new SpriteBatch();
        DungeonTile.initTiles();
        Encounter.initEncouters();
        Enemy.createCRTable();
        input = new InputMultiplexer();
        input.addProcessor(new InputAdapter());

        //this.gameScreen = new GameScreen(this);
        this.mainMenuScreen = new MainMenuScreen();
        this.setScreen(this.mainMenuScreen);
        input.addProcessor(new InputAdapter(){
            @Override
            public boolean keyUp(int keycode) {
                System.out.println(keycode);
                if (keycode == Input.Keys.GRAVE){
                    debugInfo = !debugInfo;
                }
                if (keycode == Input.Keys.R){
                    resize(1920, 1080);
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(input);
    }

    public static void setScreenInput(InputProcessor inputProcessor){
        input.getProcessors().set(0, inputProcessor);
    }

    @Override
    public void render() {
        super.render();
        if (debugInfo) {
            float h = Gdx.graphics.getHeight();
            batch.begin();
            font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, h - 20);
            //font.draw(charBatch, "camera: " + camera.position, 10, h - 50); TODO draw in screen class
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
    }
}