package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.CollisionSystem;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.object.*;
import com.paradise_seeker.game.entity.monster.test.TestBoss;   // (tạm giữ nguyên boss nếu bạn chưa có boss thật)
import com.paradise_seeker.game.entity.monster.creep.*;
import com.paradise_seeker.game.entity.monster.elite.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap {
    private static final int MAP_WIDTH = 100;
    private static final int MAP_HEIGHT = 100;
    private List<Collidable> collidables;

    private Texture backgroundTexture;
    private List<GameObject> gameObjects;
    private List<Rectangle> occupiedAreas; // ✅ để kiểm tra trùng
    private List<TestBoss> bosses;
    private List<com.paradise_seeker.game.entity.Monster> elites;
    private List<com.paradise_seeker.game.entity.Monster> creeps; 

    public GameMap(Player player) {
        backgroundTexture = new Texture("images/map/grassland.png");
        gameObjects = new ArrayList<>();
        occupiedAreas = new ArrayList<>();
        bosses = new ArrayList<>();
        elites = new ArrayList<>();
        creeps = new ArrayList<>();
        collidables = new ArrayList<>();

        // Đặt player ở giữa map
        player.bounds.x = MAP_WIDTH / 2f;
        player.bounds.y = MAP_HEIGHT / 2f;
        occupiedAreas.add(new Rectangle(player.bounds));

        generateObjects();
        generateMonsters(player);
    }

    private void generateObjects() {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            Rectangle bounds = generateNonOverlappingBounds(2, 2);
            if (bounds != null) {
                GameObject obj;
                switch (i % 5) {
                    case 0: obj = new Tree(bounds.x, bounds.y); break;
                    case 1: obj = new Forest(bounds.x, bounds.y); break;
                    case 2: obj = new WaterLake(bounds.x, bounds.y); break;
                    case 3: obj = new LavaLake(bounds.x, bounds.y); break;
                    default: obj = new RockMountain(bounds.x, bounds.y); break;
                }
                gameObjects.add(obj);
                occupiedAreas.add(obj.getBounds());
                collidables.add(obj);
            }
        }
    }

    private void generateMonsters(Player player) {
    	for (int i = 0; i < 5; i++) {
    	    Rectangle bounds1 = generateNonOverlappingBounds(2, 1);
    	    Rectangle bounds2 = generateNonOverlappingBounds(2, 1);
    	    Rectangle bounds3 = generateNonOverlappingBounds(2, 1);

    	    if (bounds1 != null) spawnMonsterSafely(new DemonSoldier(bounds1), creeps, player);
    	    if (bounds2 != null) spawnMonsterSafely(new LittleDragon(bounds2), creeps, player);
    	    if (bounds3 != null) spawnMonsterSafely(new TitanSoldier(bounds3), creeps, player);
    	}

    	for (int i = 0; i < 3; i++) {
    	    Rectangle b1 = generateNonOverlappingBounds(3, 3);
    	    Rectangle b2 = generateNonOverlappingBounds(3, 3);
    	    Rectangle b3 = generateNonOverlappingBounds(3, 3);

    	    if (b1 != null) spawnMonsterSafely(new DemonKing(b1.x, b1.y), elites, player);
    	    if (b2 != null) spawnMonsterSafely(new GreatDragon(b2.x, b2.y), elites, player);
    	    if (b3 != null) spawnMonsterSafely(new TitanLeader(b3.x, b3.y), elites, player);
    	}


        for (int i = 0; i < 5; i++) {
            Rectangle bounds = generateNonOverlappingBounds(4, 4);
            if (bounds != null) {
                spawnMonsterSafely(new TestBoss(bounds.x, bounds.y), bosses, player);
            }
        }
    }

    private Rectangle generateNonOverlappingBounds(float width, float height) {
        Random rand = new Random();
        for (int attempts = 0; attempts < 1000; attempts++) {
            float x = rand.nextInt(MAP_WIDTH - (int)width);
            float y = rand.nextInt(MAP_HEIGHT - (int)height);
            Rectangle newBounds = new Rectangle(x, y, width, height);
            boolean overlaps = false;
            for (Rectangle occ : occupiedAreas) {
                if (occ.overlaps(newBounds)) {
                    overlaps = true;
                    break;
                }
            }
            if (!overlaps) {
                return newBounds;
            }
        }
        return null;
    }

    private <T extends com.paradise_seeker.game.entity.Monster> void spawnMonsterSafely(
            T monster, List<T> list, Player player) {
        Rectangle mBounds = monster.getBounds();
        monster.player = player;
        list.add(monster);
        collidables.add(monster);
        occupiedAreas.add(mBounds);
    }

    public void render(SpriteBatch batch) {
        batch.draw(backgroundTexture, 0, 0, MAP_WIDTH, MAP_HEIGHT);

        for (GameObject object : gameObjects) {
            object.render(batch);
        }

        for (TestBoss b : bosses) b.render(batch);
        for (com.paradise_seeker.game.entity.Monster e : elites) e.render(batch);
        for (com.paradise_seeker.game.entity.Monster c : creeps) c.render(batch);
    }

    public void update(float deltaTime) {
        for (TestBoss b : bosses) b.update(deltaTime);
        for (com.paradise_seeker.game.entity.Monster e : elites) e.update(deltaTime);
        for (com.paradise_seeker.game.entity.Monster c : creeps) c.update(deltaTime);
    }

    public void checkCollisions(Player player) {
        CollisionSystem.checkCollisions(player, collidables);
    }

    public void dispose() {
        backgroundTexture.dispose();
        for (GameObject object : gameObjects) object.dispose();
    }

    public void damageMonstersInRange(float x, float y, float radius, int damage) {
        for (TestBoss b : bosses) {
            if (!b.isDead() && isInRange(x, y, b.getBounds(), radius)) {
                b.takeDamage(damage);
            }
        }
        for (com.paradise_seeker.game.entity.Monster e : elites) {
            if (!e.isDead() && isInRange(x, y, e.getBounds(), radius)) {
                e.takeDamage(damage);
            }
        }

        for (com.paradise_seeker.game.entity.Monster c : creeps) {
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

    public List<com.paradise_seeker.game.entity.Monster> getCreeps() { return creeps; }
    public List<com.paradise_seeker.game.entity.Monster> getElites() {
        return elites;
    }
    public List<TestBoss> getBosses() { return bosses; }
}
