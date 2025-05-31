package com.paradise_seeker.game.entity;

import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.monster.boss.*;
import com.paradise_seeker.game.entity.monster.creep.*;
import com.paradise_seeker.game.entity.monster.elite.*;

public class MonsterFactory {

    /**
     * Creates a Monster instance of the given class name, at (x, y), linked to the player.
     * The className string must match the "class" property in your Tiled map.
     */
    public static Monster create(String className, float x, float y, Player player) {
        Monster m = null;
        switch (className) {
            case "CyanBat":
                m = new CyanBat(x, y);
                break;
            case "DevilCreep":
                m = new DevilCreep(x, y);
                break;
            case "RatCreep":
                m = new RatCreep(x, y);
                break;
            case "Boss1":
                m = new Boss1(x, y);
                break;
            case "FlyingCreep":
                m = new FlyingCreep(x, y);
                break;
            case "MinotaurElite":
                m = new MinotaurElite(x, y);
                break;
            case "EvilPlant":
                m = new EvilPlant(x, y);
                break;
            case "YellowBat":
                m = new YellowBat(x, y);
                break;
            case "FirewormElite":
                m = new FirewormElite(x, y);
                break;
            case "FlyingDemon":
                m = new FlyingDemon(x, y);
                break;
            case "IceElite":
                m = new IceElite(x, y);
                break;

            // --- Add new monsters below ---
            // case "MyOtherMonster":
            //     m = new MyOtherMonster(x, y);
            //     break;

            default:
                System.err.println("Unknown monster class: " + className);
        }
        if (m != null) m.player = player;
        return m;
    }
}

