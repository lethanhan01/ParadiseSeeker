package com.paradise_seeker.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.skill.*;

public class Player extends Character {
    private Texture texture;
    public PlayerSkill1 playerSkill1;
    public PlayerSkill2 playerSkill2;
    public Weapon weapon;

    private boolean isDashing = false;
    private float dashCooldown = 1.0f;
    private float dashTimer = 0f;
    private float dashDistance = 50f;
    private boolean menuOpen = false;
    private boolean isPaused = false;

    public Player(Rectangle bounds) {
        this(bounds, 100, 50, 10, 5f, new Texture("player.png"));
    }

    public Player(Rectangle bounds, int hp, int mp, int atk, float speed, Texture texture) {
        super(bounds, hp, mp, atk, speed);
        this.texture = texture;
        this.playerSkill1 = new PlayerSkill1();
        this.playerSkill2 = new PlayerSkill2();
    }

    //hồi mana mỗi deltaTime thì + 5 mana
    public void regenMana(float deltaTime) {
		if (mp < 100) {
			mp += 5 * deltaTime; // Tăng mana mỗi giây
		}
	}
    
    public void update(float deltaTime) {
        handleInput(deltaTime);
        regenMana(deltaTime);
        dashTimer -= deltaTime;
    }

    private void handleInput(float deltaTime) {
        if (isPaused) return;

        float dx = 0, dy = 0;

        // Di chuyển cơ bản
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        // Di chuyển (bình thường)
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len > 0) {
            bounds.x += (dx / len) * speed * deltaTime;
            bounds.y += (dy / len) * speed * deltaTime;
        }

        // Dash né bằng Shift (không tốn MP)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashTimer <= 0) {
            if (len > 0) {
                bounds.x += (dx / len) * dashDistance;
                bounds.y += (dy / len) * dashDistance;
                dashTimer = dashCooldown;
            }
        }

        // Chuột trái - Tấn công cơ bản
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Giả sử tấn công kẻ địch gần nhất (chưa cài AI nên tạm thời in ra)
            System.out.println("Player tấn công cơ bản!");
        }

        // E - Skill 1
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            castSkill1((int) bounds.x, (int) bounds.y + 50);
        }

        // Q - Skill 2
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            castSkill2(this); // giả định tấn công chính mình (demo)
        }

        // F - Nhặt vật phẩm (giả lập)
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            System.out.println("Đã nhặt vật phẩm!");
        }

        // Tab - Mở menu trang bị
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            menuOpen = !menuOpen;
            System.out.println(menuOpen ? "Mở menu trang bị" : "Đóng menu trang bị");
        }

        // ESC - Pause Game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            System.out.println(isPaused ? "Game bị tạm dừng" : "Tiếp tục game");
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public void move() {
        // Di chuyển được xử lý trong update()
    }

    @Override
    public void onDeath() {
        System.out.println("Player đã chết!");
    }

    @Override
    public void onCollision(Collidable other) {
        // Xử lý va chạm nếu cần
    }
    // skill 1 đơn, nhận tọa độ x y và gọi hàm castSkill 
    public void castSkill1(int x, int y) {
        if (mp >= playerSkill1.getManaCost()) {
            mp -= playerSkill1.getManaCost();
            playerSkill1.castSkill(atk, x, y);
        } else {
            System.out.println("Không đủ mana cho skill 1");
        }
    }

    // skill 2 vùng, nhận đối tượng và gọi hàm castSkill
    public void castSkill2(Character target) {
        if (mp >= playerSkill2.getManaCost()) {
            mp -= playerSkill2.getManaCost();
            playerSkill2.castSkill(atk, target);
        } else {
            System.out.println("Không đủ mana cho skill 2");
        }
    }
    
    
    public boolean isPaused() {
        return isPaused;
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }
}
