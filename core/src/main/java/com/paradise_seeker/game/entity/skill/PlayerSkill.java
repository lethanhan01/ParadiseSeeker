package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Character;
import com.paradise_seeker.game.main.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

public class PlayerSkill implements Skill {
    private int manaCost;
    private long cooldown;
    private long lastUsedTime;
    private Map<String, Animation<TextureRegion>> skillAnimations;
    private float stateTime = 0f;
    private boolean isActive = false;
    private float x, y;
    private String direction;
    private boolean isSkill1;

    private float damageMultiplier = 1.0f; // ✅ Mặc định: không buff

    public PlayerSkill(boolean isSkill1) {
        this.manaCost = 10;
        this.cooldown = 0;
        this.lastUsedTime = 0;
        this.isSkill1 = isSkill1;
        this.skillAnimations = new HashMap<>();
        loadSkillAnimations();
    }

    public void setDamageMultiplier(float multiplier) {
        this.damageMultiplier +=  multiplier ;
        System.out.println("Skill " + (isSkill1 ? "1" : "2") + " damage multiplier set to " + multiplier);
    }

    private void loadSkillAnimations() {
        String[] directions = {"up", "down", "left", "right"};
        int FRAME_ROWS = 5;
        int FRAME_COLS = 5;
        for (String dir : directions) {
            String path = isSkill1
                    ? "images/Entity/skills/PlayerSkills/Skill1/Skill1_" + dir + ".png"
                    : "images/Entity/skills/PlayerSkills/Skill2/Skill2_" + dir + ".png";
            try {
                Texture sheet = new Texture(Gdx.files.internal(path));
                TextureRegion[] frames;
                if (dir.equals("left") || dir.equals("right")) {
                    TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / FRAME_COLS, sheet.getHeight());
                    frames = new TextureRegion[FRAME_COLS];
                    for (int i = 0; i < FRAME_COLS; i++) {
                        frames[i] = tmp[0][i];
                    }
                } else {
                    TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth(), sheet.getHeight() / FRAME_ROWS);
                    frames = new TextureRegion[FRAME_ROWS];
                    for (int i = 0; i < FRAME_ROWS; i++) {
                        frames[i] = tmp[i][0];
                    }
                }
                skillAnimations.put(dir, new Animation<>(0.1f, frames));
            } catch (Exception e) {
                // Ignore load errors
            }
        }
    }

    @Override
    public boolean canUse(long now) {
        return (now - lastUsedTime) >= cooldown;
    }

    @Override
    public void execute(Character target) {
        if (canUse(System.currentTimeMillis()) && target != null) {
            int damage = (int) (1000 * damageMultiplier);
            target.receiveDamage(damage);
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void castSkill(int atk, Character target) {
        if (canUse(System.currentTimeMillis()) && target != null) {
            int damage = (int) ((atk * 2) * damageMultiplier);
            target.receiveDamage(damage);
            lastUsedTime = System.currentTimeMillis();
        }
    }

    public void castSkill(int atk, int x, int y, String direction) {
        if (canUse(System.currentTimeMillis())) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            isActive = true;
            stateTime = 0f;

            float offset = 0.5f;
            float startX = x, startY = y;
            switch (direction) {
                case "up": startY += offset; break;
                case "down": startY -= offset; break;
                case "left": startX -= offset; break;
                case "right": startX += offset; break;
            }

            Animation<TextureRegion> anim = skillAnimations.get(direction);
            Animation<TextureRegion> animDown = skillAnimations.get("down");
            int damage = (int) ((atk * 2) * damageMultiplier);
            LaserBeam laser = new LaserBeam(startX, startY, damage, direction, anim);
            if (anim == null && animDown != null) {
                laser.setAnimDown(animDown);
            }
            GameScreen.activeProjectiles.add(laser);
            lastUsedTime = System.currentTimeMillis();
        }
    }

    public void castSkill(int atk, Rectangle playerBounds, String direction) {
        if (canUse(System.currentTimeMillis())) {
            float centerX = playerBounds.x + playerBounds.width / 2f;
            float centerY = playerBounds.y + playerBounds.height / 2f;
            this.direction = direction;
            isActive = true;
            stateTime = 0f;

            float offset = 0.5f;
            float startX = centerX, startY = centerY;
            switch (direction) {
                case "up": startY += offset; break;
                case "down": startY -= offset; break;
                case "left": startX -= offset; break;
                case "right": startX += offset; break;
            }

            float MIN_X = 0f, MAX_X = 100f, MIN_Y = 0f, MAX_Y = 100f;
            startX = Math.max(MIN_X, Math.min(MAX_X, startX));
            startY = Math.max(MIN_Y, Math.min(MAX_Y, startY));

            Animation<TextureRegion> anim = skillAnimations.get(direction);
            Animation<TextureRegion> animDown = skillAnimations.get("down");
            int damage = (int) ((atk * 2) * damageMultiplier);
            LaserBeam laser = new LaserBeam(startX, startY, damage, direction, anim);
            if (anim == null && animDown != null) {
                laser.setAnimDown(animDown);
            }
            GameScreen.activeProjectiles.add(laser);
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void update(long now) {
        if (isActive) {
            stateTime += Gdx.graphics.getDeltaTime();
            Animation<TextureRegion> currentAnimation = skillAnimations.get(direction);
            if (currentAnimation != null && currentAnimation.isAnimationFinished(stateTime)) {
                isActive = false;
                stateTime = 0f;
            }
        }
    }

    public void render(SpriteBatch batch) {
        // Tạm thời không cần vẽ ở đây
    }

    @Override
    public void castSkill(int atk, int x, int y) {
        // Không dùng
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public long getCooldown() {
        return cooldown;
    }

    public long getLastUsedTime() {
        return lastUsedTime;
    }
    public float getdamageMultiplier() {
		return damageMultiplier;
	}
}
