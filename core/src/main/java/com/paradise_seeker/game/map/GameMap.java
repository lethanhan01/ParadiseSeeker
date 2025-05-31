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
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.collision.CollisionSystem;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.MonsterFactory;
import com.paradise_seeker.game.entity.Monster;
import com.paradise_seeker.game.entity.npc.NPC1;
import com.paradise_seeker.game.entity.object.*;
import com.paradise_seeker.game.entity.object.item.ATKitem;
import com.paradise_seeker.game.entity.object.item.Fragment;
import com.paradise_seeker.game.entity.object.item.HPitem;
import com.paradise_seeker.game.entity.object.item.Item;
import com.paradise_seeker.game.entity.object.item.MPitem;
import com.paradise_seeker.game.entity.object.item.Skill1item;
import com.paradise_seeker.game.entity.object.item.Skill2item;
import com.paradise_seeker.game.ui.HUD;
import java.util.*;

public abstract class GameMap {
    public int MAP_WIDTH;
    public int MAP_HEIGHT;
    protected int TILE_WIDTH;
    protected int TILE_HEIGHT;
    protected String mapName = "Unknown Map";
    public String getMapName() { return mapName; }

    protected TiledMap tiledMap;
    protected Texture backgroundTexture;

    public Portal portal, startPortal;
    public Chest chest;
    public List<Collidable> collidables = new ArrayList<>();
    public List<NPC1> npcList = new ArrayList<>();
    public List<Monster> monsters = new ArrayList<>();
    private List<GameObject> gameObjects = new ArrayList<>();
    private List<Rectangle> occupiedAreas = new ArrayList<>();

    // Items
    private List<HPitem> hpItems = new ArrayList<>();
    private List<MPitem> mpItems = new ArrayList<>();
    private List<ATKitem> atkItems = new ArrayList<>();
    private List<Skill1item> skill1Items = new ArrayList<>();
    private List<Skill2item> skill2Items = new ArrayList<>();

    private float itemSpawnTimer = 0f;
    private static final float ITEM_SPAWN_INTERVAL = 120f;
    

    // Subclass must provide these
    protected abstract String getMapTmxPath();
    protected abstract String getMapBackgroundPath();

    public GameMap() {
        // 1. Load map and background
        tiledMap = new TmxMapLoader().load(getMapTmxPath());
        backgroundTexture = new Texture(getMapBackgroundPath());

        MAP_WIDTH = tiledMap.getProperties().get("width", Integer.class);
        MAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class);
        TILE_WIDTH = tiledMap.getProperties().get("tilewidth", Integer.class);
        TILE_HEIGHT = tiledMap.getProperties().get("tileheight", Integer.class);

        // Do NOT call loadSpawnPoints here!
        loadCollidables(tiledMap);
    }

    public void loadSpawnPoints(Player player) {
        MapLayer spawnsLayer = tiledMap.getLayers().get("Spawns");
        if (spawnsLayer == null) return;

        for (MapObject obj : spawnsLayer.getObjects()) {
            String type = (String) obj.getProperties().get("type");
            if (type == null) continue;
            Float px = obj.getProperties().get("x", Float.class);
            Float py = obj.getProperties().get("y", Float.class);
            if (px == null || py == null) continue;
            float worldX = px / TILE_WIDTH;
            float worldY = py / TILE_HEIGHT;

            switch (type) {
                case "player":
                    player.bounds.x = worldX;
                    player.bounds.y = worldY;
                    break;

                case "monster": {
                    String className = (String) obj.getProperties().get("class");
                    if (className != null) {
                        Monster monster = MonsterFactory.create(className, worldX, worldY, player);
                        if (monster != null) {
                            monsters.add(monster);
                            collidables.add(monster);
                        }
                    }
                    break;
                }

                case "portal":
                    // Main portal (exit/entrance)
                    portal = new Portal(worldX, worldY);
                    break;

                case "portal_start":
                    // For maps with a "start" portal (e.g., two-way portal)
                    startPortal = new Portal(worldX, worldY);
                    break;

                case "chest":
                	//test chest
                    chest = new Chest(worldX, worldY);
                    chest.addItem(new Fragment(worldX, worldY, 1f, "items/fragment/frag1.png", 1));
      //              chest.addItem(new Fragment(worldX, worldY, 1f, "items/fragment/frag2.png", 2));
                    chest.addItem(new Fragment(worldX, worldY, 1f, "items/fragment/frag3.png", 3));

                    break;

                case "npc":
                    String npcClass = (String) obj.getProperties().get("class");
                    NPC1 npc;
                    if (npcClass != null && npcClass.equals("npc1")) {
                        // If you have different NPC subclasses, handle here.
                        // npc = new SpecialNPC(worldX, worldY);
                        npc = new NPC1(worldX, worldY); // Fallback
                    } else {
                        npc = new NPC1(worldX, worldY);
                    }
                    npcList.add(npc);
                    collidables.add(npc);
                    break;

                // You can add more types as needed!
                // case "item": ...
            }
        }
    }


    protected void loadCollidables(TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (obj instanceof RectangleMapObject) {
                    Object solidProp = obj.getProperties().get("solid");
                    if (solidProp instanceof Boolean && (Boolean) solidProp) {
                        Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                        float worldX = rect.x / TILE_WIDTH;
                        float worldY = rect.y / TILE_HEIGHT;
                        float worldWidth = rect.width / TILE_WIDTH;
                        float worldHeight = rect.height / TILE_HEIGHT;
                        collidables.add(new SolidObject(new Rectangle(worldX, worldY, worldWidth, worldHeight)));
                    }
                }
            }
        }
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
        if (portal != null) portal.render(batch);
        if (startPortal != null) startPortal.render(batch);
        if (chest != null) chest.render(batch);
    }

    public void update(float deltaTime) {
        for (NPC1 npc : npcList) npc.update(deltaTime);
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
        if (chest != null) chest.update(deltaTime);
    }

    public void checkCollisions(Player player, HUD hud) {
        CollisionSystem.checkCollisions(player, collidables);
        List<List<? extends Item>> allItemLists = Arrays.asList(
            hpItems, mpItems, atkItems, skill1Items, skill2Items
        );
        for (List<? extends Item> itemList : allItemLists) {
            for (Item item : itemList) {
                if (item.isActive() && item.getBounds().overlaps(player.getBounds())) {
                    boolean canStack = false;
                    boolean hasStackWithSpace = false;
                    if (item.isStackable()) {
                        for (Item invItem : player.inventory) {
                            if (invItem.canStackWith(item) && invItem.getCount() < invItem.getMaxStackSize()) {
                                hasStackWithSpace = true;
                                break;
                            }
                        }
                        canStack = hasStackWithSpace;
                    }
                    boolean isFull = player.inventory.size() >= 18;
                    if (!canStack && isFull) {
                        if (hud != null) hud.showNotification("> Inventory is full!");
                    } else {
                        item.onCollision(player);
                        if (hud != null) hud.showNotification("> Picked up: " + item.getName());
                    }
                }
            }
        }
    }

    public boolean isBlocked(Rectangle nextBounds) {
        for (Collidable c : collidables) {
            if (c.getBounds().overlaps(nextBounds)) return true;
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

    public void dropItem(Item item) {
        if (item instanceof HPitem) hpItems.add((HPitem) item);
        else if (item instanceof MPitem) mpItems.add((MPitem) item);
        else if (item instanceof ATKitem) atkItems.add((ATKitem) item);
        // Extend for other item types as needed
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

    public List<NPC1> getNPCs() { return npcList; }
    public List<Monster> getMonsters() { return monsters; }
    public float getMapWidth() { return MAP_WIDTH; }
    public float getMapHeight() { return MAP_HEIGHT; }
    public Portal getStartPortal() { return startPortal; }
    public Portal getPortal() { return portal; }
    public Chest getChest() { return chest; }

    // === Random item generator (if you want to keep it) ===
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
}
