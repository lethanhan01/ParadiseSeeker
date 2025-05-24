package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class DeadScreen implements Screen{
	
	final Main game;
    GlyphLayout layout;
    
    int selectedIndex = 1; 
    Texture background;
    Texture[] buttonTextures;
    Texture[] selectedButtonTextures;
    public DeadScreen(Main game) {
        this.game = game;
        this.layout = new GlyphLayout();
        background = new Texture("menu/end_menu/main_death/bgdeath3.png");

        buttonTextures = new Texture[] {
            new Texture("menu/end_menu/main_death/newgame.png"),
            new Texture("menu/end_menu/main_death/backtomain.png")
        };

        selectedButtonTextures = new Texture[] {
            new Texture("menu/end_menu/main_death/newgame2.png"),
            new Texture("menu/end_menu/main_death/backtomain2.png")
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
        game.batch.draw(background, 0, 0, viewportWidth, viewportHeight);
        float buttonWidth = 5f;
        float buttonHeight = 1f;
        float xButton = (viewportWidth - buttonWidth) / 2f;
        float yStart = viewportHeight - 7f;
  
        for (int i = 0; i < buttonTextures.length; i++) {
            float yButton = yStart - i * (buttonHeight + 0.1f);
            if (i == selectedIndex) {
                game.font.setColor(Color.RED);
                // Draw ">" on the left
                game.font.draw(game.batch, ">", xButton - 0.5f, yButton + buttonHeight * 0.7f);
                // Draw "<" on the right
                game.font.draw(game.batch, "<", xButton + buttonWidth + 0.2f, yButton + buttonHeight * 0.7f);
            }
            Texture tex = (i == selectedIndex) ? selectedButtonTextures[i] : buttonTextures[i];
            game.batch.draw(tex, xButton, yButton, buttonWidth, buttonHeight);
        }


        game.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 0) selectedIndex = buttonTextures.length - 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= buttonTextures.length) selectedIndex = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        	switch (selectedIndex) {
            case 0: // NEW GAME
                game.currentGame = new GameScreen(game);
                game.setScreen(game.currentGame);
                break;
            case 1: // RETURN TO MAIN MENU
                game.setScreen(new MainMenuScreen(game));
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
