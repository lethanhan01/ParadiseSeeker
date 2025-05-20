package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.paradise_seeker.game.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap {
    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;
    private static final float CIRCLE_RADIUS = 0.5f;

    private Texture backgroundTexture;
    private List<Circle> obstacles;
    private Random random;

    public GameMap() {
        backgroundTexture = new Texture("images/map/grassland.png"); // Đảm bảo file nằm đúng path
        obstacles = new ArrayList<>();
        random = new Random();
        generateMap();
    }

    private void generateMap() {
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                if (random.nextFloat() < 0.7f) {
                    float centerX = x * 2 + 1;
                    float centerY = y * 2 + 1;
                    obstacles.add(new Circle(centerX, centerY, CIRCLE_RADIUS));
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(backgroundTexture, 0, 0); // Vẽ ảnh nền tại vị trí (0,0)
    }

    public void renderObstacles(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < obstacles.size(); i++) {
            Circle circle = obstacles.get(i);
            if (isDangerous(i)) {
                shapeRenderer.setColor(1, 0, 0, 1);
            } else {
                shapeRenderer.setColor(1, 1, 1, 1);
            }
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
        }
        shapeRenderer.end();
    }

    public void checkCollisions(Player player) {
        Rectangle playerBounds = player.getBounds();
        for (int i = 0; i < obstacles.size(); i++) {
            Circle circle = obstacles.get(i);
            if (intersects(playerBounds, circle)) {
                if (isDangerous(i)) {
                    player.takeDamage(10);
                }
            }
        }
    }

    private boolean isDangerous(int index) {
        return index % 3 == 0;
    }

    private boolean intersects(Rectangle rect, Circle circle) {
        float closestX = Math.max(rect.x, Math.min(circle.x, rect.x + rect.width));
        float closestY = Math.max(rect.y, Math.min(circle.y, rect.y + rect.height));

        float distanceX = circle.x - closestX;
        float distanceY = circle.y - closestY;

        return (distanceX * distanceX + distanceY * distanceY) <= (circle.radius * circle.radius);
    }

    public void dispose() {
        backgroundTexture.dispose();
    }
}
