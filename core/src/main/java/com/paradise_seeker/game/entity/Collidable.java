package com.paradise_seeker.game.entity;

import com.badlogic.gdx.math.Rectangle;

public interface Collidable {
    /**
     * Lấy vùng va chạm (bounding box) của đối tượng
     */
    Rectangle getBounds();

    /**
     * Lấy đối tượng Character thực thi va chạm (nếu có)
     */
    default Character getOwner() {
        return null; // subclass override trả về this nếu muốn
    }

    void onCollision(Collidable other);
    
}
