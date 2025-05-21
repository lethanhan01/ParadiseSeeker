package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.object.*;
import com.paradise_seeker.game.entity.monster.test.*;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private static final int MAP_WIDTH = 30;
    private static final int MAP_HEIGHT = 30;

    private Texture backgroundTexture;
    private List<GameObject> gameObjects;
    private List<Rectangle> occupiedAreas; // ✅ để kiểm tra trùng
    private List<TestBoss> bosses;
    private List<TestElite> elites;
    private List<TestCreep> creeps;

    public GameMap(Player player) {
        backgroundTexture = new Texture("images/map/grassland.png");
        gameObjects = new ArrayList<>();
        occupiedAreas = new ArrayList<>();
        bosses = new ArrayList<>();
        elites = new ArrayList<>();
        creeps = new ArrayList<>();

        generateObjects();
        generateMonsters(player);
    }

    private void generateObjects() {
        // Object và vùng chiếm chỗ
        GameObject t1 = new Tree(5, 8);
        GameObject f1 = new Forest(7, 7);
        GameObject w1 = new WaterLake(10, 10);
        GameObject l1 = new LavaLake(12, 4);
        GameObject r1 = new RockMountain(3, 12);

        gameObjects.add(t1);
        gameObjects.add(f1);
        gameObjects.add(w1);
        gameObjects.add(l1);
        gameObjects.add(r1);

        for (GameObject obj : gameObjects) {
            occupiedAreas.add(obj.getBounds());
        }
    }

    private void generateMonsters(Player player) {
        // Tạo quái nếu vị trí không trùng object
        spawnMonsterSafely(new TestBoss(15, 15), bosses, player);
        spawnMonsterSafely(new TestElite(6, 2), elites, player);
        spawnMonsterSafely(new TestCreep(2, 5), creeps, player);
        spawnMonsterSafely(new TestCreep(8, 13), creeps, player);
        spawnMonsterSafely(new TestElite(13, 8), elites, player);
    }

    private <T extends com.paradise_seeker.game.entity.Monster> void spawnMonsterSafely(
            T monster, List<T> list, Player player) {
        Rectangle mBounds = monster.getBounds();
        for (Rectangle occ : occupiedAreas) {
            if (occ.overlaps(mBounds)) return; // Bỏ qua nếu trùng
        }
        monster.player = player;
        list.add(monster);
        occupiedAreas.add(mBounds); // Đánh dấu đã chiếm chỗ
    }

    public void render(SpriteBatch batch) {
        batch.draw(backgroundTexture, 0, 0, MAP_WIDTH, MAP_HEIGHT);

        for (GameObject object : gameObjects) {
            object.render(batch);
        }

        // Render quái vật
        for (TestBoss b : bosses) b.render(batch);
        for (TestElite e : elites) e.render(batch);
        for (TestCreep c : creeps) c.render(batch);
    }

    public void update(float deltaTime) {
        for (TestBoss b : bosses) b.update(deltaTime);
        for (TestElite e : elites) e.update(deltaTime);
        for (TestCreep c : creeps) c.update(deltaTime);
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
        for (GameObject object : gameObjects) object.dispose();
        // Bạn có thể dispose thêm nếu monster có assets riêng
    }
    public void damageMonstersInRange(float x, float y, float radius, int damage) {
        for (TestBoss b : bosses) {
            if (!b.isDead() && isInRange(x, y, b.getBounds(), radius)) {
                b.takeDamage(damage);
            }
        }
        for (TestElite e : elites) {
            if (!e.isDead() && isInRange(x, y, e.getBounds(), radius)) {
                e.takeDamage(damage);
            }
        }
        for (TestCreep c : creeps) {
            if (!c.isDead() && isInRange(x, y, c.getBounds(), radius)) {
                c.takeDamage(damage);
            }
        }
    }

    private boolean isInRange(float x, float y, Rectangle bounds, float radius) {
        float centerX = bounds.x + bounds.width / 2;
        float centerY = bounds.y + bounds.height / 2;
        float dx = centerX - x;
        float dy = centerY - y;
        return dx * dx + dy * dy <= radius * radius;
    }

}
