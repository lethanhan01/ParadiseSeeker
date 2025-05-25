package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;
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

    // Camera will show 16x10 world units (tiles) by default
    private final float CAMERA_VIEW_WIDTH = 16f;
    private final float CAMERA_VIEW_HEIGHT = 10f;
    private float zoom = 1.0f;

    public GameScreen(final Main game) {
        this.game = game;
        this.player = new Player(new Rectangle(5, 5, 1, 1)); // world units (tiles)
        this.gameMap = new GameMap(player);
        this.player.setGameMap(gameMap);
        this.hud = new HUD(player);
        this.shapeRenderer = new ShapeRenderer();

        // World-unit based camera
        this.gameCamera = new OrthographicCamera(CAMERA_VIEW_WIDTH, CAMERA_VIEW_HEIGHT);
        this.gameCamera.position.set(
            player.bounds.x + player.bounds.width / 2f,
            player.bounds.y + player.bounds.height / 2f,
            0
        );
        this.gameCamera.update();

        // HUD camera can stay in screen pixels
        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        music = Gdx.audio.newMusic(Gdx.files.internal("music/map2.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
    }

    @Override
    public void show() {
        music.play();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pause();
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            if (game.inventoryScreen == null) game.inventoryScreen = new InventoryScreen(game, player);
            game.setScreen(game.inventoryScreen);
            music.pause();
        }
        if (player.hp == 0) {
            game.setScreen(new DeadScreen(game));
            music.stop();
            game.currentGame = null;
        }
        handleZoomInput();

        player.update(delta);
        gameMap.update(delta);
        gameMap.checkCollisions(player);

        for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
            LaserBeam projectile = activeProjectiles.get(i);
            projectile.update();
            for (Monster monster : gameMap.getMonsters()) {
                if (projectile.isActive() && !monster.isDead() && monster.getBounds().overlaps(projectile.getHitbox())) {
                    monster.takeDamage(projectile.getDamage());
                    projectile.setInactive();
                }
            }
            if (!projectile.isActive()) activeProjectiles.remove(i);
        }

        // --- Camera follows player (in world units)
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
        gameCamera.viewportWidth = CAMERA_VIEW_WIDTH * zoom;
        gameCamera.viewportHeight = CAMERA_VIEW_HEIGHT * zoom;
        gameCamera.update();

        ScreenUtils.clear(Color.BLACK);
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        gameMap.render(game.batch);
        player.render(game.batch);
        player.playerSkill1.render(game.batch);
        player.playerSkill2.render(game.batch);
        for (LaserBeam projectile : activeProjectiles) projectile.render(game.batch);
        game.batch.end();

        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        //gameMap.renderSolids(shapeRenderer);  // Uncomment to see solid tiles

        hudCamera.update();
        hud.shapeRenderer.setProjectionMatrix(hudCamera.combined);
        hud.spriteBatch.setProjectionMatrix(hudCamera.combined);
        hud.render(hudCamera.viewportHeight);
    }

    private void handleZoomInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) zoom = Math.min(3.0f, zoom + 0.1f);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS) || Gdx.input.isKeyJustPressed(Input.Keys.PLUS))
            zoom = Math.max(0.5f, zoom - 0.1f);
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        hudCamera.setToOrtho(false, width, height);
        hudCamera.update();
    }

    @Override public void pause() { game.setScreen(new PauseScreen(game)); music.pause(); }
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        music.dispose();
        hud.dispose();
        gameMap.dispose();
    }
}
