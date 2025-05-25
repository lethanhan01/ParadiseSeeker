package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class NPC2 {
    private Rectangle bounds;
    private Player player;
    private float speed;

    private float stateTime = 0f;
    private boolean facingRight = true;
    private boolean isMoving = false;

    private Map<String, Animation<TextureRegion>> rightAnimations = new HashMap<>();
    private Map<String, Animation<TextureRegion>> leftAnimations = new HashMap<>();

    public NPC2(float x, float y, Player player) {
        this.bounds = new Rectangle(x, y, 5.5f, 5.5f);
        this.player = player;
        this.speed = player.speed;

        loadAnimations();
    }

    private void loadAnimations() {
        String basePathRight = "images/Entity/characters/NPCs/npc2 - Copy/trai/";
        String basePathLeft = "images/Entity/characters/NPCs/npc2 - Copy/phai/";

        String[] folders = {"idle", "walk", "agree", "cleave", "death", "jump", "takehit"};

        for (String folder : folders) {
            rightAnimations.put(folder, loadAnimation(basePathRight + folder));
            leftAnimations.put(folder, loadAnimation(basePathLeft + folder));
        }
    }

    private Animation<TextureRegion> loadAnimation(String folderPath) {
        List<TextureRegion> frames = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));
        if (files == null) {
            System.out.println("Error: Folder not found -> " + folderPath);
            return null;
        }
        java.util.Arrays.sort(files);
        for (File file : files) {
            Texture texture = new Texture(Gdx.files.absolute(file.getAbsolutePath()));
            frames.add(new TextureRegion(texture));
        }
        return new Animation<>(0.1f, frames.toArray(new TextureRegion[0]));
    }

    
    // Track if player is within 2f radius
    private boolean playerInRange = false; // tracks if player is within 2f radius last frame
    public void update(float deltaTime) {
        stateTime += deltaTime;

        float playerX = player.getBounds().x + player.getBounds().width / 2f;
        float playerY = player.getBounds().y + player.getBounds().height / 2f;
        float npcX = bounds.x + bounds.width / 2f;
        float npcY = bounds.y + bounds.height / 2f;

        float dx = playerX - npcX;
        float dy = playerY - npcY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        boolean wasInRange = playerInRange;
        playerInRange = distance <= 2f;

        if (playerInRange) {
            // If just entered range, do NOT update facing
            isMoving = false;
            // Facing remains as it was
        } else {
            // Only update facing & move when leaving range
            isMoving = true;
            // Left or right only (because you only have left/right anims)
            facingRight = dx >= 0;

            // Move towards player
            float moveX = (dx / distance) * speed * deltaTime;
            float moveY = (dy / distance) * speed * deltaTime;
            bounds.x += moveX;
            bounds.y += moveY;
        }
    }




    public void render(SpriteBatch batch) {
    	Animation<TextureRegion> currentAnim;
    	if (isMoving) {
    	    currentAnim = facingRight ? rightAnimations.get("walk") : leftAnimations.get("walk");
    	} else {
    	    currentAnim = facingRight ? rightAnimations.get("idle") : leftAnimations.get("idle");
    	}
    	if (currentAnim != null) {
    	    TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);
    	    batch.draw(frame, bounds.x, bounds.y, bounds.width, bounds.height);
    	}

    }

    public Rectangle getBounds() {
        return bounds;
    }
}
