package com.paradise_seeker.game.main;

import java.util.List;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.map.GameMap2;
import com.paradise_seeker.game.map.GameMap3;
import com.paradise_seeker.game.map.GameMap5;
import com.paradise_seeker.game.ui.HUD;

public class GameMapManager {
    private GameMap currentMap;
    private final Player player;
    private final List<GameMap> maps = new ArrayList<>();
    private int currentMapIndex = 0;

    public GameMapManager(Player player) {
        this.player = player;

        // Khởi tạo các map và thêm vào danh sách
        maps.add(new GameMap(player));   // Map 1
        maps.add(new GameMap2(player));  // Map 2
        maps.add(new GameMap3(player));  // Map 4
        maps.add(new GameMap5(player));  // Map 5

        // Bắt đầu với map đầu tiên
        currentMap = maps.get(0);
        player.setGameMap(currentMap); // Gán map đầu tiên cho player
    }
    
    // Danh sách các đường dẫn nhạc tương ứng với từng map
    private final String[] mapMusicPaths = {
            "music/map1.mp3", // for GameMap
            "music/map2.mp3", // for GameMap2
            "music/map5.mp3",  // for GameMap5 
            "music/map5.mp3"  // for GameMap5 
        };

    public String getCurrentMapMusic() {
        return mapMusicPaths[currentMapIndex];
        }
    
        
    public GameMap getCurrentMap() {
        return currentMap;
    }

    public void update(float delta) {
        currentMap.update(delta);
    }

    public void render(SpriteBatch batch) {
        currentMap.render(batch);
    }

    public void checkCollisions(Player player, HUD hud) {
        currentMap.checkCollisions(player, hud);
    }

    public void switchToNextMap() {
        currentMapIndex = (currentMapIndex + 1) % maps.size();
        currentMap = maps.get(currentMapIndex);
        player.setGameMap(currentMap); // Cập nhật map mới cho player
        resetPlayerPosition();         // Reset vị trí player nếu cần
    }

    public void switchToPreviousMap() {
        currentMapIndex = (currentMapIndex - 1 + maps.size()) % maps.size();
        currentMap = maps.get(currentMapIndex);
        player.setGameMap(currentMap); // Cập nhật map mới cho player
        resetPlayerPosition();
    }

    public void switchToSpecificMap(int index) {
        if (index >= 0 && index < maps.size()) {
            currentMapIndex = index;
            currentMap = maps.get(currentMapIndex);
            player.setGameMap(currentMap);
            resetPlayerPosition();
        }
    }

    // Hàm optional: Reset vị trí player về giữa map mới (hoặc vị trí mong muốn)
    private void resetPlayerPosition() {
        player.bounds.x = currentMap.getMapWidth() / 2f;
        player.bounds.y = currentMap.getMapHeight() / 2f;
    }
}
