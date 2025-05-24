package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class SettingScreen implements Screen {

    final Main game;
    GlyphLayout layout;
    String[] menuItems = {"SETTINGS","Full Screen", "Music", "SE", "Control", "End Game", "Back"};
    int selectedIndex = 1; 
    Texture background;
    Texture[] buttonTextures;
    Texture[] selectedButtonTextures;
    public SettingScreen(Main game) {
        this.game = game;
        this.layout = new GlyphLayout();
        background = new Texture("menu/settings_menu/setting_main/menu_setting_c.png");

        buttonTextures = new Texture[] {
            new Texture("menu/settings_menu/setting_main/full_screen (1).png"),
            new Texture("menu/settings_menu/setting_main/music.png"),
            new Texture("menu/settings_menu/setting_main/SE.png"),
            new Texture("menu/settings_menu/setting_main/control.png"),
            new Texture("menu/settings_menu/setting_main/end_game.png"),
            new Texture("menu/settings_menu/setting_main/Back.png")
        };

        selectedButtonTextures = new Texture[] {
        		new Texture("menu/settings_menu/setting_main/full_screen_b (1).png"),
                new Texture("menu/settings_menu/setting_main/music_b.png"),
                new Texture("menu/settings_menu/setting_main/SE_b.png"),
                new Texture("menu/settings_menu/setting_main/control_b.png"),
                new Texture("menu/settings_menu/setting_main/end_game_b.png"),
                new Texture("menu/settings_menu/setting_main/Back_b.png")
        };
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        
        float viewportWidth = game.viewport.getWorldWidth();
        float viewportHeight = game.viewport.getWorldHeight();

        game.batch.begin();

     // 1. Vẽ nền
        game.batch.draw(background, 0, 0, viewportWidth, viewportHeight);

        float buttonWidth = 3.2f;    // 🔽 Giảm chiều ngang (từ 4f)
        float buttonHeight = 0.85f;  // 🔽 Giảm chiều cao (từ 1f)
        float xButton = 1.2f; 

        float yStart = viewportHeight - 2.5f; 
        float buttonSpacing = 0.55f;
        


        for (int i = 0; i < buttonTextures.length; i++) {
            float y = yStart - i * (buttonHeight + buttonSpacing);
            Texture tex = (i + 1 == selectedIndex) ? selectedButtonTextures[i] : buttonTextures[i];
            game.batch.draw(tex, xButton, y, buttonWidth, buttonHeight);
            if (i + 1 == selectedIndex) {
                // Vẽ mũi tên ở bên trái nút đang được chọn
                game.font.setColor(Color.WHITE); // hoặc màu khác nếu muốn
                game.font.draw(game.batch, ">", xButton - 0.5f, y + buttonHeight * 0.7f);
            }
        }

        game.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 1) selectedIndex = menuItems.length - 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= menuItems.length) selectedIndex = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            switch (selectedIndex) {
                case 1: 
                	toggleFullscreen();
                    break;
                case 2: // Setting
                    //
                    break;
                case 3: // Return to Main Menu
                	game.setScreen(new MainMenuScreen(game));
                    break;
                case 4: // Setting
                    //
                    break;
                case 5: // Setting
                	Gdx.app.exit();
                    break;
                case 6: // Setting
                	if (game.currentGame != null) {
                    game.setScreen(game.currentGame);
                	} else {	
                	game.setScreen(new MainMenuScreen(game));}
                    break;
            }
        }
    }
    
    private void toggleFullscreen() {
        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(800, 600);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    }

    @Override public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        background.dispose();
        for (Texture t : buttonTextures) t.dispose();
        for (Texture t : selectedButtonTextures) t.dispose();
    }
}