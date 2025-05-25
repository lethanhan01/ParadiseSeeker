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
        this.bounds = new Rectangle(x, y, 4f, 4f);
        this.player = player;
        this.speed = player.speed;

        loadAnimations();
    }

    private void loadAnimations() {
        String basePathRight = "images/Entity/characters/NPCs/npc2 - Copy/phai/";
        String basePathLeft = "images/Entity/characters/NPCs/npc2 - Copy/trai/";

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

    public void update(float deltaTime) {
        stateTime += deltaTime;
        float playerX = player.getBounds().x;
        float playerY = player.getBounds().y;
        float dx = playerX - bounds.x;
        float dy = playerY - bounds.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        isMoving = distance > 2f;

        if (isMoving) {
            float moveX = (dx / distance) * speed * deltaTime;
            float moveY = (dy / distance) * speed * deltaTime;
            bounds.x += moveX;
            bounds.y += moveY;
            facingRight = dx >= 0;
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
