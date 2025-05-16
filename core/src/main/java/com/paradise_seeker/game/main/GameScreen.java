package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Player;

public class GameScreen implements Screen {
    final Main game;
    Player player;
    Music music;

    public GameScreen(final Main game) {
        this.game = game;
        this.player = new Player(new Rectangle(5, 5, 1, 1));

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

        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
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
    }
}
