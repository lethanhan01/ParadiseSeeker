package com.paradise_seeker.game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends Game {

    public static final float WORLD_WIDTH = 16;
    public static final float WORLD_HEIGHT = 9;

    public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public FitViewport viewport;
    public GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        // Khởi tạo camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // FitViewport gắn với camera
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        // Font scaling theo WORLD_HEIGHT
        font.setUseIntegerPositions(false);
        font.getData().setScale(WORLD_HEIGHT / Gdx.graphics.getHeight());

        // Khởi tạo màn hình game
        this.gameScreen = new GameScreen(this);
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // quan trọng để gọi render() của current screen
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}