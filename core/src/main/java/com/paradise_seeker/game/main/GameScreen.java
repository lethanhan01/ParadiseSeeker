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
import com.paradise_seeker.game.map.AnotherGameMap;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.entity.object.*;
import com.paradise_seeker.game.ui.DialogueBox;
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
    private DialogueBox dialogueBox;
    private Texture dialogueBg;
    private NPC1 currentTalkingNPC;
    private OrthographicCamera gameCamera;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;
    private AnotherGameMap anotherGameMap;
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
        Rectangle playerBounds = new Rectangle(5, 5, 1, 1);
        player = new Player(playerBounds, game.font);
        this.gameMap = new GameMap(player);
        this.player.setGameMap(gameMap);
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
        this.gameCamera.position.set(
            player.bounds.x + player.bounds.width / 2f,
            player.bounds.y + player.bounds.height / 2f,
            0
        );
        this.gameCamera.update();

        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        music = Gdx.audio.newMusic(Gdx.files.internal("music/map2.mp3"));
        music.setLooping(true);
        music.setVolume(game.settingMenu.setVolume);
    }

    @Override
    public void show() {
        music.play();
    }

    @Override
    public void render(float delta) {
        // Handle F key for dialogue interaction
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            // Handle dialogue options selection
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
                        // End dialogue and start chest opening
                        dialogueBox.hide();
                        currentTalkingNPC.setTalking(false);
                        currentTalkingNPC.openChest();
                        waitingForChestToOpen = true;
                    }
                }
            }
            // Handle normal dialogue progression
            else if (dialogueBox.isVisible() && currentTalkingNPC != null) {
                // Check if we should show options at THIS line
                if (currentTalkingNPC.shouldShowOptions() && !showDialogueOptions) {
                    showDialogueOptions = true;
                } else {
                    // Normal dialogue advancement
                    if (currentTalkingNPC.hasNextLine()) {
                        currentTalkingNPC.nextLine();
                        dialogueBox.show(currentTalkingNPC.getCurrentLine());
                    } else {
                        // End of dialogue
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
            }
            // Start new conversation
            else {
                for (NPC1 npc : gameMap.getNPCs()) {
                    float dx = Math.abs(player.getBounds().x - npc.getBounds().x);
                    float dy = Math.abs(player.getBounds().y - npc.getBounds().y);
                    if (dx < 2.5f && dy < 2.5f) {
                        currentTalkingNPC = npc;
                        if (!npc.hasTalked()) {
                            npc.resetDialogue();
                            npc.setTalking(true);
                            dialogueBox.show(npc.getCurrentLine());
                        } else if (npc.isChestOpened()) {
                            // Simple post-chest message
                            npc.setTalking(true);
                            dialogueBox.show("<You've already chosen a potion.>");
                        }
                        break;
                    }
                }
            }
        }

        // Options navigation
        if (showDialogueOptions) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                selectedOptionIndex = (selectedOptionIndex - 1 + options.length) % options.length;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                selectedOptionIndex = (selectedOptionIndex + 1) % options.length;
            }
        }

        // Game logic - only when not in dialogue or waiting for chest
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

            handleZoomInput();
            player.update(delta);
            gameMap.update(delta);
            gameMap.checkCollisions(player, hud);

            // Update projectiles
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
        } else {
            // Still update NPCs even when in dialogue to handle animations
            gameMap.update(delta);
        }

        // Handle chest opening completion
        if (waitingForChestToOpen && currentTalkingNPC != null) {
            if (currentTalkingNPC.isChestOpenAndFinished()) {
                waitingForChestToOpen = false;
                finishNpcInteraction();
            }
        }

        // Camera update
        Vector2 playerCenter = new Vector2(player.bounds.x + player.bounds.width / 2, player.bounds.y + player.bounds.height / 2);
        Vector2 currentCameraPos = new Vector2(gameCamera.position.x, gameCamera.position.y);
        Vector2 newCameraPos = currentCameraPos.lerp(playerCenter, cameraLerp);
        gameCamera.position.set(newCameraPos.x, newCameraPos.y, 0);
        gameCamera.viewportWidth = CAMERA_VIEW_WIDTH * zoom;
        gameCamera.viewportHeight = CAMERA_VIEW_HEIGHT * zoom;
        gameCamera.update();

        // Render world
        ScreenUtils.clear(Color.BLACK);
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        gameMap.render(game.batch);
        player.render(game.batch);
        player.playerSkill1.render(game.batch);
        player.playerSkill2.render(game.batch);
        for (LaserBeam projectile : activeProjectiles) projectile.render(game.batch);
        game.batch.end();

        // Render dialogue box
        hudCamera.update();
        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        float baseHeight = 720f;
        float fontScale = Gdx.graphics.getHeight() / baseHeight;
        dialogueBox.render(game.batch, fontScale);
        game.batch.end();

        // Render HUD
        hud.shapeRenderer.setProjectionMatrix(hudCamera.combined);
        hud.spriteBatch.setProjectionMatrix(hudCamera.combined);
        hud.render(hudCamera.viewportHeight);

        // Render dialogue choices
        boolean shouldShowChoicesNow = dialogueBox.isVisible()
                && currentTalkingNPC != null
                && currentTalkingNPC.shouldShowOptions();

        if ((shouldShowChoicesNow || showDialogueOptions) && game.font != null) {
            if (shouldShowChoicesNow && !showDialogueOptions) showDialogueOptions = true;
            hud.spriteBatch.begin();
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            float bottomPadding = 60 * fontScale;
            float startY = bottomPadding;
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

        // Handle portal transitions
        if (isInGameMap && gameMap.portal != null && gameMap.portal.getBounds().overlaps(player.getBounds())) {
            gameMap.portal.onCollision(player);
            switchToAnotherGameMap();
        } else if (!isInGameMap && anotherGameMap != null && anotherGameMap.portal != null && anotherGameMap.portal.getBounds().overlaps(player.getBounds())) {
            anotherGameMap.portal.onCollision(player);
            switchToGameMap();
        }
    }

    private void finishNpcInteraction() {
        // Drop the pending potion (if any)
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
            gameMap.dropItem(dropped);
        }
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

    @Override 
    public void pause() { 
        
    }
    
    @Override 
    public void resume() {}
    
    @Override 
    public void hide() {}
    
    @Override
    public void dispose() {
        music.dispose();
        hud.dispose();
        gameMap.dispose();
        dialogueBg.dispose();
    }
    
    private void switchToAnotherGameMap() {
        anotherGameMap = new AnotherGameMap(player);
        isInGameMap = false;
    }
    
    private void switchToGameMap() {
        isInGameMap = true;
    }
}