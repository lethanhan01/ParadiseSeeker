package com.paradise_seeker.game.map;

public class GameMap5 extends GameMap {
    public GameMap5() {
        super();
        this.mapName = "Paradise King's Throne Room";
        // Example for custom objects, if you want:
        // this.portal = new Portal(...);
        // this.startPortal = new Portal(...);
        // this.chest = new Chest(...);
    }

    @Override
    protected String getMapTmxPath() {
        return "tilemaps/TileMaps/maps/map5.tmx";
    }

    @Override
    protected String getMapBackgroundPath() {
        return "tilemaps/TileMaps/maps/map5.png";
    }
}
