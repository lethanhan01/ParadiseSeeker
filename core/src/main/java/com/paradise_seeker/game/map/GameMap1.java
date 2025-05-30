package com.paradise_seeker.game.map;

import com.paradise_seeker.game.entity.object.Chest;
import com.paradise_seeker.game.entity.object.Fragment;

public class GameMap1 extends GameMap {
    public GameMap1() {
        super();
        this.mapName = "Forgotten Ruins";
        // If you have portals, chest, etc, add them here!
        // Example:
        // this.portal = new Portal(15f, 25f);
         this.chest = new Chest(13f, 25f);
         chest.addItem(new Fragment(13f, 25f, 1f, "items/fragment/frag1.png", 1));	
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
