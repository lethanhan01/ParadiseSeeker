package com.paradise_seeker.game.map;

import com.paradise_seeker.game.entity.object.Chest;
import com.paradise_seeker.game.entity.object.item.Fragment;

public class GameMap4 extends GameMap {
    public GameMap4() {
        super();
        this.mapName = "Old Castle";
        // If you have portals, chest, etc, add them here!
        // Example:
        // this.portal = new Portal(15f, 25f);	
    }

    @Override
    protected String getMapTmxPath() {
        return "tilemaps/TileMaps/maps/map4.tmx";
    }

    @Override
    protected String getMapBackgroundPath() {
        return "tilemaps/TileMaps/maps/map4.png";
    }
}