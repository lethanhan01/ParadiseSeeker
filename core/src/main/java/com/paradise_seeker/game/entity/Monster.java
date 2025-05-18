package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Monster extends Character  implements Renderable, Collidable {
    private Texture texture;
    public Player player; // Tham chiếu đến người chơi
    public Monster(Rectangle bounds, Texture texture) {
        super(bounds, 50, 0, 8, 3f);
        this.texture = texture;
    }

    
    @Override
    public void receiveDamage(int dmg) {
		hp = Math.max(0, hp - dmg);
		if (hp == 0) onDeath();
		approachPlayer(); // Gọi hàm này để di chuyển về phía người chơi
	}
    
    // Di chuyển về phía người chơi
    void approachPlayer() {
		// Tính toán khoảng cách và di chuyển về phía người chơi
		float deltaX = player.bounds.x - bounds.x;
		float deltaY = player.bounds.y - bounds.y;
		float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		if (distance > 0) {
			bounds.x += (deltaX / distance) * speed;
			bounds.y += (deltaY / distance) * speed;
		}
	}
    
    // Di chuyển ngẫu nhiên
    @Override
    public void move() {
        // Di chuyển ngẫu nhiên trong một khoảng cách nhất định
        float randomX = (float) (Math.random() * 2 - 1); // Giá trị ngẫu nhiên từ -1 đến 1
        float randomY = (float) (Math.random() * 2 - 1);
        bounds.x += randomX * speed;
        bounds.y += randomY * speed;
    }
    
    //////////////////////////////////// BỔ SUNG SAU ////////////////////////////////////
    
    @Override
    public void onDeath() {
        //
    }
    
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void update(float deltaTime) {
        // AI di chuyển, tấn công, tuần tra
    }

}
