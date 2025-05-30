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
import com.paradise_seeker.game.entity.npc.NPC1;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.paradise_seeker.game.entity.object.*;
import com.paradise_seeker.game.ui.DialogueBox;
import com.paradise_seeker.game.ui.HUD;
import com.paradise_seeker.game.entity.skill.LaserBeam;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;
import com.paradise_seeker.game.main.GameMapManager;
import com.paradise_seeker.game.map.GameMap;

public class GameScreen implements Screen {
    final Main game;
    Player player;
    Music music;
    private float cameraLerp = 0.1f;
    private GameMapManager mapManager;
    private HUD hud;
    private DialogueBox dialogueBox;
    private Texture dialogueBg;
    private NPC1 currentTalkingNPC;
    private OrthographicCamera gameCamera;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;
    private boolean isInGameMap = true;

    public static List<LaserBeam> activeProjectiles = new ArrayList<>();

    private final float CAMERA_VIEW_WIDTH = 16f;
    private final float CAMERA_VIEW_HEIGHT = 10f;
    private float zoom = 1.0f;

    // Dialogue choices
    private int selectedOptionIndex = 0;
    private final String[] options = {"HP potion", "MP potion", "ATK potion"};
    private boolean showDialogueOptions = false;
    private String pendingPotionToDrop = null;
    private boolean waitingForChestToOpen = false;

    public GameScreen(final Main game) {
        this.game = game;

        // Create player, initial position will be set from Tiled data by mapManager!
        Rectangle playerBounds = new Rectangle(0, 0, 1, 1); // Temporary
        player = new Player(playerBounds, game.font);

        this.mapManager = new GameMapManager(player);
        player.setGameMap(mapManager.getCurrentMap());
        this.hud = new HUD(player, game.font);
        this.shapeRenderer = new ShapeRenderer();

        dialogueBg = new Texture(Gdx.files.internal("ui/dialog/dlg_box_bg/dialogboxc.png"));
        float boxHeight = 180f;
        float dialogX = 0;
        float dialogY = 0f;
        float dialogWidth = Gdx.graphics.getWidth();
        float dialogHeight = boxHeight;

        dialogueBox = new DialogueBox(
            "",
            dialogueBg,
            game.font,
            dialogX,
            dialogY,
            dialogWidth,
            dialogHeight
        );
        currentTalkingNPC = null;

        this.gameCamera = new OrthographicCamera(CAMERA_VIEW_WIDTH, CAMERA_VIEW_HEIGHT);
        // Initial camera will be positioned on first render/update
        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        if (music != null) music.stop();
        String musicPath = mapManager.getCurrentMapMusic();
        music = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
        music.setLooping(true);
        music.setVolume(game.settingMenu.setVolume);
        music.play();
        hud.showMapNotification(mapManager.getCurrentMap().getMapName());
    }

    @Override
    public void render(float delta) {
        // Dialogue logic (unchanged)
        handleDialogue();

        // Zoom logic
        handleZoomInput();

        // Game update logic (outside dialogue)
        if (!dialogueBox.isVisible() && !showDialogueOptions && !waitingForChestToOpen) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new PauseScreen(game));
                music.pause();
            }
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

            player.update(delta);
            mapManager.update(delta);

            Chest chest = mapManager.getCurrentMap().getChest();
            if (chest != null) {
				handleChest();
			}
            
            mapManager.getCurrentMap().checkCollisions(player, hud);

            // Update projectiles
            for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
                LaserBeam projectile = activeProjectiles.get(i);
                projectile.update();
                for (Monster monster : mapManager.getCurrentMap().getMonsters()) {
                    if (projectile.isActive() && !monster.isDead() && monster.getBounds().overlaps(projectile.getHitbox())) {
                        monster.takeDamage(projectile.getDamage());
                        projectile.setInactive();
                    }
                }
                if (!projectile.isActive()) activeProjectiles.remove(i);
            }
        } else {
            mapManager.update(delta);
        }

        if (waitingForChestToOpen && currentTalkingNPC != null) {
            if (currentTalkingNPC.isChestOpenAndFinished()) {
                waitingForChestToOpen = false;
                finishNpcInteraction();
            }
        }

        // Camera follows player
        Vector2 playerCenter = new Vector2(player.bounds.x + player.bounds.width / 2, player.bounds.y + player.bounds.height / 2);
        Vector2 currentCameraPos = new Vector2(gameCamera.position.x, gameCamera.position.y);
        Vector2 newCameraPos = currentCameraPos.lerp(playerCenter, cameraLerp);
        gameCamera.position.set(newCameraPos.x, newCameraPos.y, 0);
        gameCamera.viewportWidth = CAMERA_VIEW_WIDTH * zoom;
        gameCamera.viewportHeight = CAMERA_VIEW_HEIGHT * zoom;
        gameCamera.update();

        // --- RENDER ---
        ScreenUtils.clear(Color.BLACK);
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        mapManager.render(game.batch);
        player.render(game.batch);
        player.playerSkill1.render(game.batch);
        player.playerSkill2.render(game.batch);
        for (LaserBeam projectile : activeProjectiles) projectile.render(game.batch);
        game.batch.end();

        hudCamera.update();
        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        float baseHeight = 720f;
        float fontScale = Math.max(Gdx.graphics.getHeight() / baseHeight, 0.05f);
        dialogueBox.render(game.batch, fontScale);
        game.batch.end();

        hud.shapeRenderer.setProjectionMatrix(hudCamera.combined);
        hud.spriteBatch.setProjectionMatrix(hudCamera.combined);
        hud.render(hudCamera.viewportHeight);

        renderDialogueOptions(fontScale);

        // --- PORTAL & MAP SWITCH ---
        handlePortals();
    }

    /** All portal/map transition logic, always uses Tiled player spawn! */
    private void handlePortals() {
        GameMap currentMap = mapManager.getCurrentMap();
        if (currentMap.portal != null && player.getBounds().overlaps(currentMap.portal.getBounds())) {
            currentMap.portal.onCollision(player);
            mapManager.switchToNextMap();
            player.setGameMap(mapManager.getCurrentMap());
            switchMusicAndShowMap();
        }
        if (currentMap.getStartPortal() != null && player.getBounds().overlaps(currentMap.getStartPortal().getBounds())) {
            currentMap.getStartPortal().onCollision(player);
            mapManager.switchToPreviousMap();
            player.setGameMap(mapManager.getCurrentMap());
            switchMusicAndShowMap();
        }
    }

    private void handleChest() {
		Chest chest = mapManager.getCurrentMap().getChest();
		if (chest != null && player.getBounds().overlaps(chest.getBounds())) {
			player.showInteractMessage = true;
			
			if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
				if (!chest.isOpened()) {
					player.showInteractMessage = false;
					chest.onPlayerCollision(player);
					hud.showNotification("You opened the chest!");
				} else {
					hud.showNotification("The chest is already opened!");
				}
			}
		}
	}

    private void handleDialogue() {
        // Handle F key for dialogue interaction
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (showDialogueOptions) {
                if (currentTalkingNPC != null) {
                    currentTalkingNPC.setHasTalked(true);
                    pendingPotionToDrop = options[selectedOptionIndex];
                    showDialogueOptions = false;
                    selectedOptionIndex = 0;

                    if (currentTalkingNPC.hasNextLine()) {
                        currentTalkingNPC.nextLine();
                        dialogueBox.show(currentTalkingNPC.getCurrentLine());
                    } else {
                        dialogueBox.hide();
                        currentTalkingNPC.setTalking(false);
                        currentTalkingNPC.openChest();
                        waitingForChestToOpen = true;
                    }
                }
            } else if (dialogueBox.isVisible() && currentTalkingNPC != null) {
                if (currentTalkingNPC.shouldShowOptions() && !showDialogueOptions) {
                    showDialogueOptions = true;
                } else {
                    if (currentTalkingNPC.hasNextLine()) {
                        currentTalkingNPC.nextLine();
                        dialogueBox.show(currentTalkingNPC.getCurrentLine());
                    } else {
                        dialogueBox.hide();
                        currentTalkingNPC.setTalking(false);
                        if (!currentTalkingNPC.isChestOpened()) {
                            currentTalkingNPC.openChest();
                            waitingForChestToOpen = true;
                        } else {
                            finishNpcInteraction();
                        }
                    }
                }
            } else {
                for (NPC1 npc : mapManager.getCurrentMap().getNPCs()) {
                    float dx = Math.abs(player.getBounds().x - npc.getBounds().x);
                    float dy = Math.abs(player.getBounds().y - npc.getBounds().y);
                    if (dx < 2.5f && dy < 2.5f) {
                        currentTalkingNPC = npc;
                        if (!npc.hasTalked()) {
                            npc.resetDialogue();
                            npc.setTalking(true);
                            dialogueBox.show(npc.getCurrentLine());
                        } else if (npc.isChestOpened()) {
                            npc.setTalking(true);
                            dialogueBox.show("<You've already chosen a potion.>");
                        }
                        break;
                    }
                }
            }
        }

        // Handle left/right input for options
        if (showDialogueOptions) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                selectedOptionIndex = (selectedOptionIndex - 1 + options.length) % options.length;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                selectedOptionIndex = (selectedOptionIndex + 1) % options.length;
            }
        }
    }

    private void finishNpcInteraction() {
        if (pendingPotionToDrop != null) {
            dropPotionNextToPlayer(pendingPotionToDrop);
            pendingPotionToDrop = null;
        }
        if (currentTalkingNPC != null) {
            currentTalkingNPC.setTalking(false);
        }
        currentTalkingNPC = null;
    }

    private void dropPotionNextToPlayer(String potionType) {
        float dropX = player.getBounds().x + player.getBounds().width + 0.2f;
        float dropY = player.getBounds().y;
        Item dropped = null;

        switch (potionType) {
            case "HP potion":
                dropped = new HPitem(dropX, dropY, 1f, "items/potion/potion3.png", 20);
                break;
            case "MP potion":
                dropped = new MPitem(dropX, dropY, 1f, "items/potion/potion9.png", 15);
                break;
            case "ATK potion":
                dropped = new ATKitem(dropX, dropY, 1f, "items/atkbuff_potion/potion14.png", 5);
                break;
        }

        if (dropped != null) {
            mapManager.getCurrentMap().dropItem(dropped);
        }
    }

    private void handleZoomInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) zoom = Math.min(3.0f, zoom + 0.1f);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS) || Gdx.input.isKeyJustPressed(Input.Keys.PLUS))
            zoom = Math.max(0.5f, zoom - 0.1f);
    }

    private void renderDialogueOptions(float fontScale) {
        boolean shouldShowChoicesNow = dialogueBox.isVisible() && currentTalkingNPC != null && currentTalkingNPC.shouldShowOptions();
        if ((shouldShowChoicesNow || showDialogueOptions) && game.font != null) {
            if (shouldShowChoicesNow && !showDialogueOptions) showDialogueOptions = true;
            hud.spriteBatch.begin();
            float screenWidth = Gdx.graphics.getWidth();
            float startY = 60 * fontScale;
            float optionSpacing = 220 * fontScale;
            float totalWidth = optionSpacing * options.length;
            float startX = (screenWidth - totalWidth) / 2f + 20f * fontScale;

            float oldScaleX = game.font.getData().scaleX;
            float oldScaleY = game.font.getData().scaleY;
            game.font.getData().setScale(fontScale);

            for (int i = 0; i < options.length; i++) {
                String prefix = (i == selectedOptionIndex) ? "> " : "  ";
                game.font.draw(hud.spriteBatch, prefix + (i + 1) + ". " + options[i], startX + i * optionSpacing, startY);
            }
            game.font.getData().setScale(oldScaleX, oldScaleY);
            hud.spriteBatch.end();
        }
    }

    @Override public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        hudCamera.setToOrtho(false, width, height);
        hudCamera.update();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        if (music != null) music.dispose();
        hud.dispose();
        dialogueBg.dispose();
    }

    private void switchMusicAndShowMap() {
        if (music != null) {
            music.stop();
            music.dispose();
        }
        String musicPath = mapManager.getCurrentMapMusic();
        music = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
        music.setLooping(true);
        music.setVolume(game.settingMenu.setVolume);
        music.play();
        hud.showMapNotification(mapManager.getCurrentMap().getMapName());
    }
}
