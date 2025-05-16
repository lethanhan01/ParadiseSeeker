package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.paradise_seeker.game.entity.Character;

public class PlayerSkill1 implements Skill {
    private int manaCost;
    private long cooldown;
    private long lastUsedTime;

    private Texture texture;
    private Vector2 position;
    private Vector2 direction;
    private float speed = 300f; // tốc độ di chuyển của kỹ năng (px/s)
    private boolean active = false;

    public PlayerSkill1(int manaCost, long cooldown) {
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.lastUsedTime = 0;
        this.texture = new Texture("projectile.png"); // hình ảnh đạn bay, phải nằm trong assets/
        this.position = new Vector2();
        this.direction = new Vector2();
    }

    @Override
    public boolean canUse(long now) {
        return (now - lastUsedTime) >= cooldown;
    }

    @Override
    public void execute(Character target) {
        // Skill không định hướng nên không làm gì ở đây
    }

    public void castSkill(int atk, float x, float y, float dirX, float dirY) {
        if (!active && canUse(System.currentTimeMillis())) {
            lastUsedTime = System.currentTimeMillis();
            position.set(x, y);
            direction.set(dirX, dirY).nor(); // chuẩn hóa hướng
            active = true;

            System.out.println("Skill casted at direction (" + dirX + ", " + dirY + ")");
        }
    }

    @Override
    public void update(float deltaTime) {
        if (active) {
            // Di chuyển kỹ năng
            position.x += direction.x * speed * deltaTime;
            position.y += direction.y * speed * deltaTime;

            // Vô hiệu hóa nếu ra khỏi màn hình (giả sử kích thước màn hình 1920x1080)
            if (position.x < 0 || position.x > 1920 || position.y < 0 || position.y > 1080) {
                active = false;
            }
        }
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, position.x, position.y, 16, 16); // vẽ đạn
        }
    }

    public boolean isActive() {
        return active;
    }
}
