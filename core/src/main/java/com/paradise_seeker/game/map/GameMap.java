package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.object.*;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private static final int MAP_WIDTH = 20;
    private static final int MAP_HEIGHT = 20;

    private Texture backgroundTexture;
    private List<GameObject> gameObjects;

    public GameMap() {
        backgroundTexture = new Texture("images/map/grassland.png"); // Đảm bảo path đúng
        gameObjects = new ArrayList<>();
        generateObjects();
    }

    private void generateObjects() {
        // Vị trí các object có thể được sinh ngẫu nhiên hoặc theo layout định sẵn
        gameObjects.add(new Tree(5, 8));
        gameObjects.add(new Forest(7, 7));
        gameObjects.add(new WaterLake(10, 10));
        gameObjects.add(new LavaLake(12, 4));
        gameObjects.add(new RockMountain(3, 12));
    }

    public void render(SpriteBatch batch) {
        batch.draw(backgroundTexture, 0, 0, MAP_WIDTH, MAP_HEIGHT);

        for (GameObject object : gameObjects) {
            object.render(batch);
        }
    }

    public void checkCollisions(Player player) {
        for (GameObject object : gameObjects) {
            if (player.getBounds().overlaps(object.getBounds())) {
                object.onPlayerCollision(player);
            }
        }
    }

    public void dispose() {
        backgroundTexture.dispose();
        for (GameObject object : gameObjects) {
            object.dispose();
        }
    }
}
