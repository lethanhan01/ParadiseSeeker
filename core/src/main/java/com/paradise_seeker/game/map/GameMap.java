package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.CollisionSystem;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.object.*;
import com.paradise_seeker.game.entity.monster.boss.TitanKing;
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
    private List<Rectangle> occupiedAreas;
    private List<TitanKing> bosses;
    private List<com.paradise_seeker.game.entity.Monster> elites;
    private List<com.paradise_seeker.game.entity.Monster> creeps;
    private List<HPitem> hpItems = new ArrayList<>();
    private List<MPitem> mpItems = new ArrayList<>();
    private float itemSpawnTimer = 0f;
    private static final float ITEM_SPAWN_INTERVAL = 120f;

    public GameMap(Player player) {
        backgroundTexture = new Texture("images/map/test.png");
        gameObjects = new ArrayList<>();
        occupiedAreas = new ArrayList<>();
        bosses = new ArrayList<>();
        elites = new ArrayList<>();
        creeps = new ArrayList<>();
        collidables = new ArrayList<>();

        player.bounds.x = MAP_WIDTH / 2f;
        player.bounds.y = MAP_HEIGHT / 2f;
        occupiedAreas.add(new Rectangle(player.bounds));

        generateObjects();
        generateMonsters(player);
        generateRandomItems(5, 5);
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
        for (int i = 0; i < 30; i++) {
            Rectangle bounds = generateNonOverlappingBounds(4, 4);
            if (bounds != null) {
                TitanKing boss = new TitanKing(bounds.x, bounds.y);
                boss.player = player;
                bosses.add(boss);
                collidables.add(boss);
                occupiedAreas.add(boss.getBounds());
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

    public void render(SpriteBatch batch) {
        batch.draw(backgroundTexture, 0, 0, MAP_WIDTH, MAP_HEIGHT);

        for (GameObject object : gameObjects) {
            object.render(batch);
        }

        for (HPitem item : hpItems) item.render(batch);
        for (MPitem item : mpItems) item.render(batch);
        for (TitanKing b : bosses) b.render(batch);
        for (com.paradise_seeker.game.entity.Monster e : elites) e.render(batch);
        for (com.paradise_seeker.game.entity.Monster c : creeps) c.render(batch);
    }

    public void update(float deltaTime) {
        for (TitanKing b : bosses) b.update(deltaTime);
        for (com.paradise_seeker.game.entity.Monster e : elites) e.update(deltaTime);
        for (com.paradise_seeker.game.entity.Monster c : creeps) c.update(deltaTime);
        hpItems.removeIf(item -> !item.isActive());
        mpItems.removeIf(item -> !item.isActive());
        itemSpawnTimer += deltaTime;
        if (itemSpawnTimer >= ITEM_SPAWN_INTERVAL) {
            spawnRandomItem();
            itemSpawnTimer = 0f;
        }
    }

    public void checkCollisions(Player player) {
        CollisionSystem.checkCollisions(player, collidables);
        for (HPitem item : hpItems) {
            if (item.isActive() && item.getBounds().overlaps(player.getBounds())) {
                item.onCollision(player);
            }
        }
        for (MPitem item : mpItems) {
            if (item.isActive() && item.getBounds().overlaps(player.getBounds())) {
                item.onCollision(player);
            }
        }
    }

    public void dispose() {
        backgroundTexture.dispose();
        for (GameObject object : gameObjects) object.dispose();
    }

    public void damageMonstersInRange(float x, float y, float radius, int damage) {
        for (TitanKing b : bosses) {
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
    public List<com.paradise_seeker.game.entity.Monster> getElites() { return elites; }
    public List<TitanKing> getBosses() { return bosses; }

    private void generateRandomItems(int hpCount, int mpCount) {
        Random rand = new Random();
        String[] hpTextures = {"items/potion/potion3.png", "items/potion/potion4.png", "items/potion/potion5.png"};
        int[] hpValues = {20, 40, 60};
        String[] mpTextures = {"items/potion/potion9.png", "items/potion/potion10.png", "items/potion/potion11.png"};
        int[] mpValues = {15, 30, 50};
        for (int i = 0; i < hpCount; i++) {
            int idx = rand.nextInt(hpTextures.length);
            float x = rand.nextFloat() * (MAP_WIDTH - 1);
            float y = rand.nextFloat() * (MAP_HEIGHT - 1);
            hpItems.add(new HPitem(x, y, 1, hpTextures[idx], hpValues[idx]));
        }
        for (int i = 0; i < mpCount; i++) {
            int idx = rand.nextInt(mpTextures.length);
            float x = rand.nextFloat() * (MAP_WIDTH - 1);
            float y = rand.nextFloat() * (MAP_HEIGHT - 1);
            mpItems.add(new MPitem(x, y, 1, mpTextures[idx], mpValues[idx]));
        }
    }

    private void spawnRandomItem() {
        Random rand = new Random();
        boolean spawnHP = rand.nextBoolean();
        if (spawnHP) {
            String[] hpTextures = {"items/potion/potion3.png", "items/potion/potion4.png", "items/potion/potion5.png"};
            int[] hpValues = {20, 40, 60};
            int idx = rand.nextInt(hpTextures.length);
            float x = rand.nextFloat() * (MAP_WIDTH - 1);
            float y = rand.nextFloat() * (MAP_HEIGHT - 1);
            hpItems.add(new HPitem(x, y, 1, hpTextures[idx], hpValues[idx]));
        } else {
            String[] mpTextures = {"items/potion/potion9.png", "items/potion/potion10.png", "items/potion/potion11.png"};
            int[] mpValues = {15, 30, 50};
            int idx = rand.nextInt(mpTextures.length);
            float x = rand.nextFloat() * (MAP_WIDTH - 1);
            float y = rand.nextFloat() * (MAP_HEIGHT - 1);
            mpItems.add(new MPitem(x, y, 1, mpTextures[idx], mpValues[idx]));
        }
    }
}
