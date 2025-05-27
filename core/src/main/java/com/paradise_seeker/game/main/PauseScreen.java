package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class PauseScreen implements Screen {

    final Main game;
    GlyphLayout layout;
    String[] menuItems = {"- Pausing -", "Continue Game", "Settinsgs", "Return to Main Menu"};
    int selectedIndex = 1; 
    public PauseScreen(Main game) {
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
        
        game.font.setColor(Color.RED);
        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i];
            layout.setText(game.font, text);
            float x = (viewportWidth - layout.width) / 2f;
            float y = viewportHeight - 2f - i * 1.5f;

          
                game.font.setColor(Color.WHITE);
                if (i == selectedIndex) {
                    game.font.draw(game.batch, ">", x - 1.2f, y);
            }

            game.font.draw(game.batch, layout, x, y);
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
                	game.setScreen(game.currentGame);
                    break;
                case 2: // Setting
            		game.setScreen(game.settingMenu);
                    break;
                case 3: // Return to Main Menu
                	game.setScreen(game.mainMenu);
                    break;
            }
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