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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.paradise_seeker.game.map.AnotherGameMap;
import com.paradise_seeker.game.map.GameMap;
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
    private BitmapFont dialogueFont;
    private NPC1 currentTalkingNPC;
    private OrthographicCamera gameCamera;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;
    private AnotherGameMap anotherGameMap;
    private boolean isInGameMap = true;  // Bắt đầu ở GameMap

    public static List<LaserBeam> activeProjectiles = new ArrayList<>();

    // Camera will show 16x10 world units (tiles) by default
    private final float CAMERA_VIEW_WIDTH = 16f;
    private final float CAMERA_VIEW_HEIGHT = 10f;
    private float zoom = 1.0f;
    private int selectedOptionIndex = 0;
    private final String[] options = {"HP potion", "MP potion", "ATK potion"};
    private boolean showDialogueOptions = false; // bật khi NPC được nói

    public GameScreen(final Main game) {
        this.game = game;
        Rectangle playerBounds = new Rectangle(5, 5, 1, 1);
        player = new Player(playerBounds, game.font);
        this.gameMap = new GameMap(player);
        this.player.setGameMap(gameMap);
        this.hud = new HUD(player, game.font);
        this.shapeRenderer = new ShapeRenderer();
        dialogueBg = new Texture(Gdx.files.internal("ui/dialog/dlg_box_bg/dialogboxc.png"));
        System.out.println("✔ Đã load ảnh hộp thoại: " + dialogueBg.getWidth() + "x" + dialogueBg.getHeight());
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/Roboto-VariableFont_wdth,wght.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 28;
        parameter.characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            + "ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝĂĐĨŨƠƯ"
            + "àáâãèéêìíòóôõùúýăđĩũơư"
            + "ẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưưỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪỬỮỰỲỴỶỸ"
            + "ạảấầẩẫậắằẳẵặẹẻẽềềểễệỉịọỏốồổỗộớờởỡợ"
            + "ụủứừửữựỳỵỷỹ"
            + "0123456789.,:;!?()[]{}<>+-*/=|\\\"' \n";
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;

         dialogueFont = generator.generateFont(parameter);
        generator.dispose();

        dialogueFont.getData().setScale(1f);  
        float margin = 0f; // khoảng cách cách lề
        float boxHeight = 180f; // hoặc 150 tùy font bạn dùng

        float dialogX = 0;
        float dialogY = margin;
        float dialogWidth = Gdx.graphics.getWidth();
        float dialogHeight = boxHeight;

        dialogueBox = new DialogueBox(
            "", 
            dialogueBg, 
            dialogueFont, 
            dialogX, 
            dialogY, 
            dialogWidth, 
            dialogHeight
        );
        currentTalkingNPC = null;

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
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
        	if (showDialogueOptions) {
        	    // xử lý chọn và next luôn như ENTER
        	    if (currentTalkingNPC != null) {
        	        currentTalkingNPC.setHasTalked(true);
        	    }

        	    System.out.println("🎯 Đã chọn (qua F): " + options[selectedOptionIndex]);

        	    showDialogueOptions = false;
        	    selectedOptionIndex = 0;

        	    // 👉 Chuyển sang dòng thoại tiếp theo nếu còn
        	    if (currentTalkingNPC != null && currentTalkingNPC.hasNextLine()) {
        	        currentTalkingNPC.nextLine();
        	        dialogueBox.show(currentTalkingNPC.getCurrentLine());
        	    } else {
        	        dialogueBox.hide();
        	        currentTalkingNPC = null;
        	    }

        	    return;
        	}

            if (dialogueBox.isVisible() && currentTalkingNPC != null) {
                if (currentTalkingNPC.hasNextLine()) {
                    currentTalkingNPC.nextLine();
                    dialogueBox.show(currentTalkingNPC.getCurrentLine());
                } else {
                	// Đừng đóng hộp thoại vội – chỉ bật lựa chọn
                	if (currentTalkingNPC.shouldShowOptions()) {
                	    showDialogueOptions = true;
                	    return;
                	} else {
                	    // kết thúc luôn nếu không có lựa chọn
                	    dialogueBox.hide();
                	    currentTalkingNPC.setTalking(false);
                	    currentTalkingNPC = null;
                	}
                	// Vẫn để currentTalkingNPC để giữ được câu cuối trong hộp thoại
                }
            } else {
                for (NPC1 npc : gameMap.getNPCs()) {
                    float dx = Math.abs(player.getBounds().x - npc.getBounds().x);
                    float dy = Math.abs(player.getBounds().y - npc.getBounds().y);
                    if (dx < 2.5f && dy < 2.5f) {
                        currentTalkingNPC = npc;
                        if (!npc.isChestOpened()) {
                            npc.openChest();
                        } else if (!npc.hasTalked()) {
                            npc.resetDialogue();
                            npc.setTalking(true);
                            dialogueBox.show(npc.getCurrentLine());
                        }
                        break;
                    }
                }
            }
        }

        if (!dialogueBox.isVisible()) {
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
        }

        // Cập nhật camera
        Vector2 playerCenter = new Vector2(player.bounds.x + player.bounds.width / 2, player.bounds.y + player.bounds.height / 2);
        Vector2 currentCameraPos = new Vector2(gameCamera.position.x, gameCamera.position.y);
        Vector2 newCameraPos = currentCameraPos.lerp(playerCenter, cameraLerp);
        gameCamera.position.set(newCameraPos.x, newCameraPos.y, 0);
        gameCamera.viewportWidth = CAMERA_VIEW_WIDTH * zoom;
        gameCamera.viewportHeight = CAMERA_VIEW_HEIGHT * zoom;
        gameCamera.update();

        // Vẽ thế giới
        ScreenUtils.clear(Color.BLACK);
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        gameMap.render(game.batch);
        player.render(game.batch);
        player.playerSkill1.render(game.batch);
        player.playerSkill2.render(game.batch);
        for (LaserBeam projectile : activeProjectiles) projectile.render(game.batch);
        game.batch.end();

        // Vẽ hộp thoại bằng camera UI
        hudCamera.update();
        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        dialogueBox.render(game.batch);
        game.batch.end();

        // Vẽ HUD khác
        hud.shapeRenderer.setProjectionMatrix(hudCamera.combined);
        hud.spriteBatch.setProjectionMatrix(hudCamera.combined);
        hud.render(hudCamera.viewportHeight);
        
     // Hiển thị lựa chọn bên dưới hộp thoại hoặc ngay cả khi câu cuối
     // ✅ Kiểm tra xem có nên hiển thị lựa chọn không
        boolean shouldShowChoicesNow = dialogueBox.isVisible()
                && currentTalkingNPC != null
                && currentTalkingNPC.shouldShowOptions();

        // ✅ Vẽ lựa chọn nếu đang trong chế độ show hoặc đang ở dòng trigger
        if ((shouldShowChoicesNow || showDialogueOptions) && dialogueFont != null) {
        	if (shouldShowChoicesNow && !showDialogueOptions) {
        	    showDialogueOptions = true;
        	}
        	hud.spriteBatch.begin();

        	float screenWidth = Gdx.graphics.getWidth();
        	float screenHeight = Gdx.graphics.getHeight();
        	float dialogueBoxHeight = 222f;

        	// 👇 Hạ xuống gần đáy hộp thoại hơn
        	float startY = 70f;

        	// 👇 Dịch sang phải nhẹ để tránh đè icon / dòng bên trái
        	float optionSpacing = 220;
        	float totalWidth = optionSpacing * options.length;
        	float startX = (screenWidth - totalWidth) / 2f + 20f;

        	for (int i = 0; i < options.length; i++) {
        	    String prefix = (i == selectedOptionIndex) ? "> " : "  ";
        	    dialogueFont.draw(hud.spriteBatch, prefix + (i + 1) + ". " + options[i], startX + i * optionSpacing, startY);
        	}

        	hud.spriteBatch.end();





        }

        // 👉 Sau block render lựa chọn, đặt khối này riêng biệt
        if (showDialogueOptions) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            	showDialogueOptions = true; // 👈 QUAN TRỌNG
                selectedOptionIndex = (selectedOptionIndex - 1 + options.length) % options.length;
                System.out.println("← Moved to: " + selectedOptionIndex);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                selectedOptionIndex = (selectedOptionIndex + 1) % options.length;
                System.out.println("→ Moved to: " + selectedOptionIndex);
            }
            
        }

        if (isInGameMap) {
            gameMap.update(delta);
            gameMap.checkCollisions(player);
        } else {
            anotherGameMap.update(delta);
            anotherGameMap.checkCollisions(player);
        }
        if (isInGameMap && gameMap.portal != null && gameMap.portal.getBounds().overlaps(player.getBounds())) {
            gameMap.portal.onCollision(player);
            switchToAnotherGameMap();
        } else if (!isInGameMap && anotherGameMap.portal != null && anotherGameMap.portal.getBounds().overlaps(player.getBounds())) {
            anotherGameMap.portal.onCollision(player);
            switchToGameMap();
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

    @Override public void pause() { game.setScreen(new PauseScreen(game)); music.pause(); }
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        music.dispose();
        hud.dispose();
        gameMap.dispose();
    }
    private void switchToAnotherGameMap() {
        anotherGameMap = new AnotherGameMap(player);
        isInGameMap = false;
        System.out.println("➡️ Chuyển sang AnotherGameMap");
    }

    private void switchToGameMap() {
        System.out.println("⬅️ Quay lại GameMap");
    }

}
