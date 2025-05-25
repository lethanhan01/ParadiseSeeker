package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Main game;
    Vector2 touchPos;
    Texture titleTexture;
    int selectedIndex = 0;
    Texture[] buttonTextures;
    Texture background;
    Texture[] selectedButtonTextures;
    Texture characterIcon;

    public MainMenuScreen(final Main game) {
        this.game = game;
        touchPos = new Vector2();

        // Use your title PNG here
        titleTexture = new Texture(Gdx.files.internal("menu/start_menu/main_menu/test.png"));
        titleTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        background = new Texture("menu/start_menu/main_menu/bg_7.png");

        characterIcon = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_up.png"));
        characterIcon.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        

        buttonTextures = new Texture[] {
            new Texture("menu/start_menu/main_menu/newgame1.png"),
            new Texture("menu/start_menu/main_menu/loadgame1.png"),
            new Texture("menu/start_menu/main_menu/settings1.png"),
            new Texture("menu/start_menu/main_menu/exit1.png")
        };
        selectedButtonTextures = new Texture[] {
            new Texture("menu/start_menu/main_menu/newgame_test.png"),
            new Texture("menu/start_menu/main_menu/loadgame2.png"),
            new Texture("menu/start_menu/main_menu/settings2.png"),
            new Texture("menu/start_menu/main_menu/exit2.png")
        };
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        float viewportWidth = game.viewport.getWorldWidth();
        float viewportHeight = game.viewport.getWorldHeight();

        game.batch.begin();

        // 1. Draw background
        game.batch.draw(background, 0, 0, viewportWidth, viewportHeight);

        // 2. Draw the title image at the top center
        float titleWidth = 10.5f;   // Adjust as needed for your image
        float titleHeight = 4.8f;  // Adjust as needed for your image
        float xTitle = (viewportWidth - titleWidth) / 2f;
        float yTitle = viewportHeight - titleHeight - 0.4f;
        game.batch.draw(titleTexture, xTitle, yTitle, titleWidth, titleHeight);

        // 3. Draw the menu buttons under the title
        float buttonWidth = 3f;
        float buttonHeight = 0.75f;
        float xButton = (viewportWidth - buttonWidth) / 2f;

        // Buttons start below the title image
        float yStart = yTitle - buttonHeight - 0.01f;

        for (int i = 0; i < buttonTextures.length; i++) {
            float yButton = yStart - i * (buttonHeight + 0.2f);
            Texture buttonTex = (i == selectedIndex) ? selectedButtonTextures[i] : buttonTextures[i];
            game.batch.draw(buttonTex, xButton, yButton, buttonWidth, buttonHeight);

            // Draw selector arrow 
            if (i == selectedIndex) {
                game.font.setColor(Color.WHITE);
                // Draw ">" on the left
                game.font.draw(game.batch, ">", xButton - 0.3f, yButton + buttonHeight * 0.7f);
                // Draw "<" on the right
                game.font.draw(game.batch, "<", xButton + buttonWidth + 0.1f, yButton + buttonHeight * 0.7f);
            }
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
           selectMenuItem();
        }

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);

            float viewportWidth = game.viewport.getWorldWidth();
            float viewportHeight = game.viewport.getWorldHeight();
            float titleHeight = 2f; // Should match above
            float yTitle = viewportHeight - titleHeight - 0.4f;
            float buttonHeight = 0.9f;
            float yStart = yTitle - buttonHeight - 0.8f;

            for (int i = 0; i < buttonTextures.length; i++) {
                float yButton = yStart - i * (buttonHeight + 0.2f);
                if (touchPos.y > yButton && touchPos.y < yButton + buttonHeight) {
                    selectedIndex = i;
                    selectMenuItem();
                    break;
                }
            }
        }
    }

    private void selectMenuItem() {
        switch (selectedIndex) {
            case 0:
                if (game.currentGame == null) {
                    game.currentGame = new GameScreen(game);
                } else {
                    game.currentGame = null;
                    game.inventoryScreen = null; // Reset inventory screengam
                    game.currentGame = new GameScreen(game);
                }
                game.setScreen(game.currentGame);
                break;
            case 1:
                if (game.currentGame == null) {
                    game.setScreen(new MainMenuScreen(game));
                } else {
                    game.setScreen(game.currentGame);
                }
                break;
            case 2:
                game.setScreen(game.settingMenu);
                break;
            case 3:
                Gdx.app.exit();
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
       titleTexture.dispose();
       for (Texture t : buttonTextures) t.dispose();
       for (Texture t : selectedButtonTextures) t.dispose();
    }
}
