package com.paradise_seeker.game.map;

import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.object.Portal;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class GameMap5 extends GameMap {

    public GameMap5(Player player) {
        super(player);

        // Load TiledMap cho GameMap2
        TiledMap tiledMap = new TmxMapLoader().load("tilemaps/TileMaps/maps/map5.tmx");
        MAP_WIDTH = tiledMap.getProperties().get("width", Integer.class);
        MAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class);
        TILE_WIDTH = tiledMap.getProperties().get("tilewidth", Integer.class);
        TILE_HEIGHT = tiledMap.getProperties().get("tileheight", Integer.class);

        backgroundTexture = new Texture("tilemaps/TileMaps/maps/map5.png");
        loadCollidables(tiledMap);

        // Đặt portal cho map2
        portal = new Portal(20f, 10f);
    }
}
