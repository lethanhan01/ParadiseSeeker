package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Player;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Interpolation;
import com.paradise_seeker.game.map.GameMap;

public class GameScreen implements Screen {
    final Main game;
    Player player;
    Music music;
    private float cameraLerp = 0.1f; // Hệ số làm mượt chuyển động camera
    private GameMap gameMap;

    public GameScreen(final Main game) {
        this.game = game;
        this.player = new Player(new Rectangle(5, 5, 1, 1));
        this.gameMap = new GameMap();

        music = Gdx.audio.newMusic(Gdx.files.internal("music/music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
    }

    @Override
    public void show() {
        music.play();
    }

    @Override
    public void render(float delta) {
        player.update(delta);

        // Tính toán vị trí trung tâm của người chơi
        Vector2 playerCenter = new Vector2(
            player.bounds.x + player.bounds.width / 2,
            player.bounds.y + player.bounds.height / 2
        );

        // Lấy vị trí hiện tại của camera
        Vector2 currentCameraPos = new Vector2(
            game.viewport.getCamera().position.x,
            game.viewport.getCamera().position.y
        );

        // Tính toán vị trí mới của camera với hiệu ứng làm mượt
        Vector2 newCameraPos = new Vector2(
            currentCameraPos.x + (playerCenter.x - currentCameraPos.x) * cameraLerp,
            currentCameraPos.y + (playerCenter.y - currentCameraPos.y) * cameraLerp
        );

        // Cập nhật vị trí camera
        game.viewport.getCamera().position.set(newCameraPos.x, newCameraPos.y, 0);
        game.viewport.getCamera().update();

        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        
        // Vẽ map với camera hiện tại
        gameMap.render(game.viewport.getCamera());
        
        // Vẽ người chơi
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        player.render(game.batch);
        game.batch.end();
    }

    @Override public void resize(int width, int height) { game.viewport.update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        music.dispose();
        gameMap.dispose();
    }
}