package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NPC1 implements Collidable {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> talkAnimation;
    private Animation<TextureRegion> openChestAnimation;
    private Animation<TextureRegion> chestOpenedAnimation;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion currentFrame;

    private float stateTime;
    private Rectangle bounds;
    private float spriteWidth = 3f;
    private float spriteHeight = 3f;

    // --- State flags ---
    private boolean isChestOpened = false;
    private boolean isOpeningChest = false;
    private boolean isTalking = false;
    private boolean hasTalked = false;

    // Getters and setters for state
    public boolean hasTalked() { return hasTalked; }
    public void setHasTalked(boolean value) { 
        this.hasTalked = value; 
    }
    public boolean isChestOpened() { return isChestOpened; }
    public boolean isOpeningChest() { return isOpeningChest; }

    public NPC1(float x, float y) {
        loadIdleAnimation();
        loadTalkAnimation();
        loadOpenChestAnimation();
        loadChestOpenedAnimation();

        this.spriteWidth = 3f;
        this.spriteHeight = 3f;

        this.bounds = new Rectangle(x, y, spriteWidth, spriteHeight); // Bổ sung ngay đây

        currentAnimation = idleAnimation;
        currentFrame = currentAnimation.getKeyFrame(0f);
        stateTime = 0f;
    }


    private void loadIdleAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 120; i <= 130; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act3/npc" + i + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(path));
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Handle missing textures silently
            }
        }
        idleAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void loadTalkAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 10; i <= 19; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act1/npc" + i + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(path));
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Handle missing textures silently
            }
        }
        talkAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        talkAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void loadOpenChestAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 131; i <= 137; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act4/npc" + i + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(path));
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Handle missing textures silently
            }
        }
        openChestAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        openChestAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }
    public void updateBounds() {
        if (bounds != null) {
            bounds.setSize(spriteWidth, spriteHeight);
        }
    }


    private void loadChestOpenedAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 140; i <= 145; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act5/npc" + i + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(path));
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Handle missing textures silently
            }
        }
        chestOpenedAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        chestOpenedAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        currentFrame = currentAnimation.getKeyFrame(stateTime);

        // Handle chest opening animation completion
        if (isOpeningChest && openChestAnimation.isAnimationFinished(stateTime)) {
            currentAnimation = chestOpenedAnimation;
            stateTime = 0f;
            isChestOpened = true;
            isOpeningChest = false;
            currentFrame = chestOpenedAnimation.getKeyFrame(0f);
        }
    }

    public void setTalking(boolean talking) {
        if (isTalking != talking) {
            isTalking = talking;
            
            // Don't change animation if currently opening chest
            if (!isOpeningChest) {
                if (talking) {
                    currentAnimation = talkAnimation;
                } else if (isChestOpened) {
                    currentAnimation = chestOpenedAnimation;
                } else {
                    currentAnimation = idleAnimation;
                }
                stateTime = 0f;
            }
        }
    }

    public void openChest() {
        if (isChestOpened || isOpeningChest) {
            return;
        }
        
        currentAnimation = openChestAnimation;
        stateTime = 0f;
        isOpeningChest = true;
        isTalking = false; // Stop talking when opening chest
    }

    public boolean isChestOpenAndFinished() {
        return isChestOpened && !isOpeningChest;
    }

    public void render(SpriteBatch batch) {
        if (currentFrame != null) {
            batch.draw(currentFrame, bounds.x, bounds.y, spriteWidth, spriteHeight);
        }
    }

    public Rectangle getBounds() { 
        return bounds; 
    }

    // ======= Dialogue Logic =======
    private List<String> dialogueLines = new ArrayList<>();
    private int currentLineIndex = 0;

    public void setDialogue(List<String> lines) {
        this.dialogueLines = new ArrayList<>(lines);
        this.currentLineIndex = 0;
    }

    public String getCurrentLine() {
        if (dialogueLines.isEmpty()) {
            return "";
        }
        return dialogueLines.get(currentLineIndex);
    }

    public boolean hasNextLine() {
        return currentLineIndex < dialogueLines.size() - 1;
    }

    public void nextLine() {
        if (hasNextLine()) {
            currentLineIndex++;
        }
    }

    public void resetDialogue() {
        currentLineIndex = 0;
    }

    public int getCurrentLineIndex() { 
        return currentLineIndex; 
    }

    public boolean shouldShowOptions() {
        return getCurrentLineIndex() == 1 && !hasTalked;
    }

    @Override
    public void onCollision(Player player) {
        // Interaction handled in GameScreen
    }
}