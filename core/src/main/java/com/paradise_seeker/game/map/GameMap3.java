package com.paradise_seeker.game.map;

public class GameMap3 extends GameMap {
    public GameMap3() {
        super();
        this.mapName = "Fiery Maze";
        // Example for custom objects, if you want:
        // this.portal = new Portal(...);
        // this.startPortal = new Portal(...);
        // this.chest = new Chest(...);
    }

    @Override
    protected String getMapTmxPath() {
        return "tilemaps/TileMaps/maps/map3.tmx";
    }

    @Override
    protected String getMapBackgroundPath() {
        return "tilemaps/TileMaps/maps/map3.png";
    }
}
