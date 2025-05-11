package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderable {
    /**
     * Giao diện cho các đối tượng có thể vẽ lên màn hình.
     * @param batch SpriteBatch được sử dụng để vẽ.
     */
    void render(SpriteBatch batch);
}
