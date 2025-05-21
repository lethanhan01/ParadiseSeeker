package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Player;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.ui.HUD;
import com.paradise_seeker.game.entity.skill.LaserBeam;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    final Main game;
    Player player;
    Music music;
    private float cameraLerp = 0.1f;
    private GameMap gameMap;
    private HUD hud;
    private OrthographicCamera gameCamera;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;
    public static List<LaserBeam> activeProjectiles = new ArrayList<>();

    public GameScreen(final Main game) {
        this.game = game;
        this.player = new Player(new Rectangle(5, 5, 1, 1));
        this.gameMap = new GameMap();
        this.hud = new HUD(player);
        this.shapeRenderer = new ShapeRenderer();

        this.gameCamera = new OrthographicCamera();
        this.gameCamera.setToOrtho(false, 16, 10);
        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
        player.update(delta);             // ✅ 1. Di chuyển nhân vật
        gameMap.checkCollisions(player);  // ✅ 2. Kiểm tra va chạm và rollback nếu cần

        // Cập nhật đạn
        for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
            LaserBeam projectile = activeProjectiles.get(i);
            projectile.update();
            if (!projectile.isActive()) {
                activeProjectiles.remove(i);
            }
        }

        // Cập nhật camera
        Vector2 playerCenter = new Vector2(
            player.bounds.x + player.bounds.width / 2,
            player.bounds.y + player.bounds.height / 2
        );

        Vector2 currentCameraPos = new Vector2(gameCamera.position.x, gameCamera.position.y);
        Vector2 newCameraPos = new Vector2(
            currentCameraPos.x + (playerCenter.x - currentCameraPos.x) * cameraLerp,
            currentCameraPos.y + (playerCenter.y - currentCameraPos.y) * cameraLerp
        );

        gameCamera.position.set(newCameraPos.x, newCameraPos.y, 0);
        gameCamera.update();

        // Render
        ScreenUtils.clear(Color.BLACK);
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        gameMap.render(game.batch);
        player.render(game.batch);
        
        // Render skills
        player.playerSkill1.render(game.batch);
        player.playerSkill2.render(game.batch);
        
        for (LaserBeam projectile : activeProjectiles) {
            projectile.render(game.batch);
        }
        game.batch.end();

        hudCamera.update();
        hud.shapeRenderer.setProjectionMatrix(hudCamera.combined);
        hud.render();
    }


    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        hudCamera.setToOrtho(false, width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        music.dispose();
        hud.dispose();
        gameMap.dispose();
    }
}
