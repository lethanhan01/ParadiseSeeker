package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.skill.*;

public class Player extends Character {
    private Texture texture;
    public PlayerSkill1 playerSkill1;


    public PlayerSkill2 playerSkill2;
    public Weapon weapon;
    
    public Player(Rectangle bounds, int hp, int mp, int atk, float speed) {
		this(bounds, hp, mp, atk, speed, new Texture("player.png")); // ví dụ load từ asset
	}
    public Player(Rectangle bounds, int hp, int mp, int atk, float speed, Texture texture) {
        super(bounds, hp, mp, atk, speed);
        this.texture = texture;
    }
    public Player(Rectangle bounds) {
        this(bounds, 100, 50, 10, 5f, new Texture("player.png")); // ví dụ load từ asset
    }
    
    // Nhặt vũ khí và cập nhât chỉ số
    public void pickWeapon(Weapon w) {
        weapon = w;
        atk+= (weapon != null ? weapon.getAttackBonus() : 0);
    	speed += (weapon != null ? weapon.getSpeedBonus() : 0);
    }
    
	
	
	// đánh thường
	public void attack(Character target) {
		if (target.isAlive()) {
			target.receiveDamage(atk);
		}
	}

	// ra skill vùng
	public void castSkill1( int x, int y) {
		if(mp>= playerSkill1.getManaCost()) {
			mp -= playerSkill1.getManaCost();
			playerSkill1.castSkill(atk,x, y);
		}
		else {
			System.out.println("not enough mana");
		}
	}

	// ra skill đơn
	public void castSkill2(Character target) {
		if(mp>= playerSkill2.getManaCost()) {
			mp -= playerSkill2.getManaCost();
			playerSkill2.castSkill(atk, target);
		}
		else {
			System.out.println("not enough mana");
		}
	}

	////////////////////////////////////// BỔ SUNG SAU ////////////////////////////////////////////////////
	
	// xử lý input, tương ứng khi aswd, mũi tên thì di chuyển
    // xử lý skill, vũ khí tương ứng chuột trái chuột phải
    // xử lý đánh thường, tương ứng phím chuột v.v.
	
	@Override
	public void move() {
		// xử lý di chuyển lên xuống trái phải
		
	}
	
	public void update(float deltaTime) {
        // ví dụ hồi mana, xử lý animation, input, v.v.
    }
	
	@Override
	public void onDeath() {
        // cập nhật lại các chỉ số
    	// cập nhật lại vị trí
    }
	
	@Override
	public void onCollision(Collidable other) {
		// xử lý va chạm với các đối tượng khác
		
	}
    
	@Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
