package com.paradise_seeker.game.map;

public class GameMap1 extends GameMap {
    public GameMap1() {
        super();
        this.mapName = "Forgotten Ruins";
        // If you have portals, chest, etc, add them here!
        // Example:
        // this.portal = new Portal(15f, 25f);
        // this.chest = new Chest(13f, 25f);
    }

    @Override
    protected String getMapTmxPath() {
        return "tilemaps/TileMaps/maps/map1.tmx";
    }

    @Override
    protected String getMapBackgroundPath() {
        return "tilemaps/TileMaps/maps/map1.png";
    }
}
