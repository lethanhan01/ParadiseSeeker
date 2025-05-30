package com.paradise_seeker.game.map;

public class GameMap2 extends GameMap {
    public GameMap2() {
        super();
        this.mapName = "Titans' Plains";
        // Example for custom portal/chest setup:
        // this.portal = new Portal(44f, 31f);
        // this.startPortal = new Portal(2f, 10f);
        // this.chest = new Chest(30f, 12f);
    }

    @Override
    protected String getMapTmxPath() {
        return "tilemaps/TileMaps/maps/map2.tmx";
    }

    @Override
    protected String getMapBackgroundPath() {
        return "tilemaps/TileMaps/maps/map2.png";
    }
}
