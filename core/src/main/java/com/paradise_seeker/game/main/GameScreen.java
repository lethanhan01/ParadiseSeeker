package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.skill.LaserBeam;

public class GameScreen implements Screen {
    final Main game;
    Player player;
    Music music;

    // Danh sách tia laser đang hoạt động
    public static Array<LaserBeam> activeProjectiles = new Array<>();

    public GameScreen(final Main game) {
        this.game = game;
        this.player = new Player(new Rectangle(5, 5, 1, 1));

        // Nhạc nền
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
        // Cập nhật logic người chơi
        player.update(delta);

        // Cập nhật các laser và loại bỏ nếu đã hết hiệu lực
        for (int i = 0; i < activeProjectiles.size; i++) {
            LaserBeam laser = activeProjectiles.get(i);
            laser.update();
            if (!laser.isActive()) {
                activeProjectiles.removeIndex(i);
                i--;
            }
        }

        // Xóa màn hình và vẽ
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        // Vẽ người chơi
        player.render(game.batch);

        // Vẽ laser
        for (LaserBeam laser : activeProjectiles) {
            laser.render(game.batch);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        music.dispose();
    }
}
