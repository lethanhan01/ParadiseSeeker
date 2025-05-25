package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.CollisionSystem;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.monster.boss.Boss1;
import com.paradise_seeker.game.entity.monster.creep.*;
import com.paradise_seeker.game.entity.monster.elite.*;
import com.paradise_seeker.game.entity.npc.*;
import com.paradise_seeker.game.entity.object.*;
import com.paradise_seeker.game.entity.Monster;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap {
    private int MAP_WIDTH;      // In world units (tiles)
    private int MAP_HEIGHT;     // In world units (tiles)
    private int TILE_WIDTH;     // In pixels
    private int TILE_HEIGHT;    // In pixels
    public float getMapWidth() {
        return MAP_WIDTH;
    }
    public float getMapHeight() {
        return MAP_HEIGHT;
    }
    private Portal portal;
    private Texture backgroundTexture;
    private List<Collidable> collidables;
    private List<GameObject> gameObjects;
    private List<Rectangle> occupiedAreas;

    private List<Monster> monsters;
    private List<HPitem> hpItems = new ArrayList<>();
    private List<MPitem> mpItems = new ArrayList<>();
    private List<ATKitem> atkItems = new ArrayList<>();
    private List<Skill1item> skill1Items = new ArrayList<>();
    private List<Skill2item> skill2Items = new ArrayList<>();
	private List<NPC1> npcList = new ArrayList<>(); // Danh sách NPC
	private List<NPC2> npc2List = new ArrayList<>();

    private float itemSpawnTimer = 0f;
    private static final float ITEM_SPAWN_INTERVAL = 120f;

    public GameMap(Player player) {
        // --- Load Tiled map and get real size ---
        TiledMap tiledMap = new TmxMapLoader().load("tilemaps/TileMaps/maps/map1.tmx");
        MAP_WIDTH = tiledMap.getProperties().get("width", Integer.class);
        MAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class);
        TILE_WIDTH = tiledMap.getProperties().get("tilewidth", Integer.class);
        TILE_HEIGHT = tiledMap.getProperties().get("tileheight", Integer.class);

        // Load matching background PNG in world units
        backgroundTexture = new Texture("tilemaps/TileMaps/maps/test1.png");

        gameObjects = new ArrayList<>();
        occupiedAreas = new ArrayList<>();
        monsters = new ArrayList<>();
        collidables = new ArrayList<>();

        // Player starts at map center (world units)
        player.bounds.x = MAP_WIDTH / 2f;
        player.bounds.y = MAP_HEIGHT / 2f;
        occupiedAreas.add(new Rectangle(player.bounds));

        //generateObjects();
        generateMonsters(player);
        generateRandomItems(5, 5);
        generateNPCs(); // ✅ Thêm dòng này
        generateNPC2s(player);


        portal = new Portal(15f, 25f);
        // Add the portal to your collision system
        collidables.add(portal);
        // --- Load all "solid" rectangles, scale to world units, no Y flip ---
        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (obj instanceof RectangleMapObject) {
                    Object solidProp = obj.getProperties().get("solid");
                    if (solidProp instanceof Boolean && ((Boolean) solidProp)) {
                        Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                        float worldX = rect.x / (float) TILE_WIDTH;
                        float worldY = rect.y / (float) TILE_HEIGHT;
                        float worldWidth = rect.width / (float) TILE_WIDTH;
                        float worldHeight = rect.height / (float) TILE_HEIGHT;
                        Rectangle fixedRect = new Rectangle(worldX, worldY, worldWidth, worldHeight);
                        //System.out.println("Loaded solid (world units, no Y flip): " + fixedRect);
                        collidables.add(new SolidObject(fixedRect));
                    }
                }
            } 
        }
    }
    private void generateNPC2s(Player player) {
        Random random = new Random();
        int npc2Count = 2;
        for (int i = 0; i < npc2Count; i++) {
            Rectangle bounds = generateNonOverlappingBounds(3f, 3f);
            if (bounds != null) {
                NPC2 npc2 = new NPC2(bounds.x, bounds.y, player);
                npc2List.add(npc2);
                // Không add vào collidables để không cản Player
                occupiedAreas.add(new Rectangle(bounds));
            }
        }
    }

    private void generateNPCs() {
        // Place the NPC at the bottom-left corner of the map
        float npcX = 1f; // Bottom-left X + 1 coordinate
        float npcY = 1f; // Bottom-left Y + 1 coordinate
        NPC1 npc = new NPC1(npcX, npcY);
        npcList.add(npc);
        collidables.add(npc);
        occupiedAreas.add(npc.getBounds());
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

    public List<NPC1> getNPCs() {
        return npcList; // npcList là danh sách NPC bạn đã lưu trong map
    }

/*    private void generateMonsters(Player player) {
        int bossCount = 1;
        int normalMonsterCount = 3;
        for (int i = 0; i < bossCount; i++) {
            Rectangle b = generateNonOverlappingBounds(4, 4);
            if (b != null) spawnMonster(new Boss1(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new CyanBat(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new DevilCreep(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new EvilPlant(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new YellowBat(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new RatCreep(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new FlyingCreep(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(3, 3);
            if (b != null) spawnMonster(new FlyingDemon(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(4, 4);
            if (b != null) spawnMonster(new FirewormElite(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(4, 4);
            if (b != null) spawnMonster(new IceElite(b.x, b.y), player);
        }
        for (int i = 0; i < normalMonsterCount; i++) {
            Rectangle b = generateNonOverlappingBounds(4, 4);
            if (b != null) spawnMonster(new MinotaurElite(b.x, b.y), player);
        }
    	
    }*/
    
    private void generateMonsters(Player player) {
        Rectangle b;

        // Only 3 creeps of your choice
        b = generateNonOverlappingBounds(3, 3);
        if (b != null) spawnMonster(new CyanBat(b.x, b.y), player);

        b = generateNonOverlappingBounds(3, 3);
        if (b != null) spawnMonster(new DevilCreep(b.x, b.y), player);

        b = generateNonOverlappingBounds(3, 3);
        if (b != null) spawnMonster(new RatCreep(b.x, b.y), player);

        // No bosses, no other creeps
    }


    private void spawnMonster(Monster monster, Player player) {
        monster.player = player;
        monsters.add(monster);
        collidables.add(monster);
        occupiedAreas.add(monster.getBounds());
    }

    private Rectangle generateNonOverlappingBounds(float width, float height) {
        Random rand = new Random();
        for (int attempts = 0; attempts < 1000; attempts++) {
            float x = rand.nextFloat() * (MAP_WIDTH - width);
            float y = rand.nextFloat() * (MAP_HEIGHT - height);
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
        for (GameObject obj : gameObjects) obj.render(batch);
        for (HPitem item : hpItems) item.render(batch);
        for (MPitem item : mpItems) item.render(batch);
        for (ATKitem item : atkItems) item.render(batch);
        for (Skill1item item : skill1Items) item.render(batch);
        for (Skill2item item : skill2Items) item.render(batch);
        for (Monster m : monsters) m.render(batch);
        for (NPC1 npc : npcList) npc.render(batch);
        for (NPC2 npc2 : npc2List) npc2.render(batch);

        if (portal != null) portal.render(batch);

    }

    public void update(float deltaTime) {
    	for (NPC1 npc : npcList) {
    	    npc.update(deltaTime);
    	}
    	for (NPC2 npc2 : npc2List) npc2.update(deltaTime);

        for (Monster m : monsters) m.update(deltaTime);
        hpItems.removeIf(item -> !item.isActive());
        mpItems.removeIf(item -> !item.isActive());
        atkItems.removeIf(item -> !item.isActive());
        skill1Items.removeIf(item -> !item.isActive());
        skill2Items.removeIf(item -> !item.isActive());
        itemSpawnTimer += deltaTime;
        if (itemSpawnTimer >= ITEM_SPAWN_INTERVAL) {
            spawnRandomItem();
            itemSpawnTimer = 0f;
        }
    }

    public void checkCollisions(Player player) {
        CollisionSystem.checkCollisions(player, collidables);
        for (HPitem item : hpItems) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
        for (MPitem item : mpItems) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
        for (ATKitem item : atkItems) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
        for (Skill1item item : skill1Items) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
        for (Skill2item item : skill2Items) if (item.isActive() && item.getBounds().overlaps(player.getBounds())) item.onCollision(player);
    }
    public boolean isBlocked(Rectangle nextBounds) {
        for (Collidable c : collidables) {
            if (c.getBounds().overlaps(nextBounds)) {
                return true;
            }
        }
        return false;
    }
    public void renderSolids(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (Collidable c : collidables) {
            Rectangle r = c.getBounds();
            shapeRenderer.rect(r.x, r.y, r.width, r.height);
        }
        shapeRenderer.end();
    }
    public void dispose() {
        backgroundTexture.dispose();
        for (GameObject obj : gameObjects) obj.dispose();
    }

    private void generateRandomItems(int hpCount, int mpCount) {
        Random rand = new Random();
        String[] hpTextures = {"items/potion/potion3.png", "items/potion/potion4.png", "items/potion/potion5.png"};
        int[] hpValues = {20, 40, 60};
        String[] mpTextures = {"items/potion/potion9.png", "items/potion/potion10.png", "items/potion/potion11.png"};
        int[] mpValues = {15, 30, 50};
        String[] atkTextures = {"items/atkbuff_potion/potion14.png", "items/atkbuff_potion/potion15.png", "items/atkbuff_potion/potion16.png"};
        int[] atkValues = {5, 10, 15};
        for (int i = 0; i < hpCount; i++) {
            int idx = rand.nextInt(hpTextures.length);
            hpItems.add(new HPitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, hpTextures[idx], hpValues[idx]));
        }
        for (int i = 0; i < mpCount; i++) {
            int idx = rand.nextInt(mpTextures.length);
            mpItems.add(new MPitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, mpTextures[idx], mpValues[idx]));
        }
        for (int i = 0; i < 3; i++) {
            int idx = rand.nextInt(atkTextures.length);
            atkItems.add(new ATKitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, atkTextures[idx], atkValues[idx]));
            skill1Items.add(new Skill1item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion12.png"));
            skill2Items.add(new Skill2item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion13.png"));
        }
    }

    private void spawnRandomItem() {
        Random rand = new Random();
        int type = rand.nextInt(5); // 0 = HP, 1 = MP, 2 = ATK, 3 = Skill1, 4 = Skill2
        if (type == 0) {
            String[] textures = {"items/potion/potion3.png", "items/potion/potion4.png", "items/potion/potion5.png"};
            int[] values = {20, 40, 60};
            int idx = rand.nextInt(textures.length);
            hpItems.add(new HPitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, textures[idx], values[idx]));
        } else if (type == 1) {
            String[] textures = {"items/potion/potion9.png", "items/potion/potion10.png", "items/potion/potion11.png"};
            int[] values = {15, 30, 50};
            int idx = rand.nextInt(textures.length);
            mpItems.add(new MPitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, textures[idx], values[idx]));
        } else if (type == 2) {
            String[] textures = {"items/atkbuff_potion/potion14.png", "items/atkbuff_potion/potion15.png", "items/atkbuff_potion/potion16.png"};
            int[] values = {5, 10, 15};
            int idx = rand.nextInt(textures.length);
            atkItems.add(new ATKitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, textures[idx], values[idx]));
        } else if (type == 3) {
            skill1Items.add(new Skill1item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion12.png"));
        } else {
            skill2Items.add(new Skill2item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion13.png"));
        }
    }

    public void damageMonstersInRange(float x, float y, float radius, int damage) {
        for (Monster m : monsters) {
            if (!m.isDead() && isInRange(x, y, m.getBounds(), radius)) m.takeDamage(damage);
        }
    }

    private boolean isInRange(float x, float y, Rectangle bounds, float radius) {
        float centerX = bounds.x + bounds.width / 2;
        float centerY = bounds.y + bounds.height / 2;
        float dx = centerX - x;
        float dy = centerY - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public List<Monster> getMonsters() { return monsters; }
}
