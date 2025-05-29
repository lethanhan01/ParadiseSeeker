package com.paradise_seeker.game.map;

import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.object.Portal;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class GameMap3 extends GameMap {
	public Portal startPortal;  // Biến thành viên, dùng chung trong class

    public GameMap3(Player player) {
        super(player);
        this.mapName = "Titans' Plains";
        // Load TiledMap cho GameMap2
        TiledMap tiledMap = new TmxMapLoader().load("tilemaps/TileMaps/maps/map3.tmx");
        MAP_WIDTH = tiledMap.getProperties().get("width", Integer.class);
        MAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class);
        TILE_WIDTH = tiledMap.getProperties().get("tilewidth", Integer.class);
        TILE_HEIGHT = tiledMap.getProperties().get("tileheight", Integer.class);

        backgroundTexture = new Texture("tilemaps/TileMaps/maps/map3.png");
        loadCollidables(tiledMap);

        // Đặt portal cho map2
        startPortal = new Portal(MAP_WIDTH / 2f-1f, MAP_HEIGHT / 2f-1f);
        portal = new Portal(42f, 1f);

    }
    @Override
    public Portal getStartPortal() {
        return startPortal;
    }
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch); // Vẽ map và các portal mặc định
        if (startPortal != null) {
            startPortal.render(batch); // Thêm vẽ startPortal
        }
    }

}
