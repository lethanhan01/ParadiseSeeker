package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Main game;
    Vector2 touchPos;
    Texture characterIcon;
    int selectedIndex = 0;
    String[] menuItems = {"NEW GAME", "LOAD GAME", "SETTINGS", "EXIT"};

    public MainMenuScreen(final Main game) {
        this.game = game;
        touchPos = new Vector2();
        characterIcon = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_up.png"));
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        float viewportWidth = game.viewport.getWorldWidth();
        float viewportHeight = game.viewport.getWorldHeight();

        game.batch.begin();

        // Tiêu đề
        game.font.setColor(Color.RED);
        GlyphLayout layout = new GlyphLayout();
        String title = "PARADISE SEEKER";
        layout.setText(game.font, title);
        float xTitle = (viewportWidth - layout.width) / 2f;
        float yTitle = viewportHeight - 1f;
        
        game.font.draw(game.batch, layout, xTitle, yTitle);

        // Icon nhân vật
        float iconWidth = 1.5f;
        float iconHeight = 1.5f;
        float xIcon = (viewportWidth - iconWidth) / 2f;
        float yIcon = yTitle - 3f;
        
        game.batch.draw(characterIcon, xIcon, yIcon, iconWidth, iconHeight);

        // Menu
        game.font.setColor(Color.WHITE);
        for (int i = 0; i < menuItems.length; i++) {
            String item = menuItems[i];
            layout.setText(game.font, item);
            float xItem = (viewportWidth - layout.width) / 2f;
            float yItem = yIcon - 1f - i * 1f;
            game.font.draw(game.batch, item, xItem, yItem);

            // Con trỏ >
            if (i == selectedIndex) {
                game.font.draw(game.batch, ">", xItem - 1f, yItem);
            }
        }

        game.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 0) selectedIndex = menuItems.length - 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= menuItems.length) selectedIndex = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
           selectMenuItem();
        }
        
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);

            float viewportWidth = game.viewport.getWorldWidth();
            float viewportHeight = game.viewport.getWorldHeight();
            float yTitle = viewportHeight - 1f;
            float iconHeight = 1.5f;
            float yIcon = yTitle - 3f;

            for (int i = 0; i < menuItems.length; i++) {
                float yItem = yIcon - 1f - i * 1.5f;
                float itemHeight = 1f;  // chiều cao ước lượng dòng menu

                if (touchPos.y > yItem - itemHeight && touchPos.y < yItem + 0.5f) {
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
                game.setScreen(new GameScreen(game));
                dispose();
                break;
            case 1:
                game.setScreen(game.gameScreen);
                break;
            case 2:
                game.setScreen(new SettingScreen(game));
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
       characterIcon.dispose();
    }
}
