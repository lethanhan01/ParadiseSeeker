package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class SettingScreen implements Screen {

    final Main game;
    GlyphLayout layout;
    String[] menuItems = {"SETTINGS","Full Screen", "Music", "SE", "Control", "End Game", "Back"};
    int selectedIndex = 1; 
    public SettingScreen(Main game) {
        this.game = game;
        this.layout = new GlyphLayout();
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

        // 1. Title "SETTINGS" (căn giữa)
        layout.setText(game.font, menuItems[0], Color.RED, 
                     viewportWidth, Align.center, true);
        game.font.draw(game.batch, layout, 0, viewportHeight - 2f);

        // 2. Menu items (căn trái)
        float leftMargin = 2f; // Khoảng cách từ lề trái
        for (int i = 1; i < menuItems.length; i++) {
            layout.setText(game.font, menuItems[i], Color.WHITE, 
                         viewportWidth - leftMargin, Align.left, true);
            
            float y = viewportHeight - 2f - i * 1f;
            if (i == selectedIndex) {
                game.font.draw(game.batch, ">", leftMargin - 1.2f, y);
            }
            game.font.draw(game.batch, layout, leftMargin, y);
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
    @Override public void dispose() {}
}