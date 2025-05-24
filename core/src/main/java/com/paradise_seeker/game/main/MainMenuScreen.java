package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MainMenuScreen implements Screen {

    final Main game;
    Vector2 touchPos;
    Texture characterIcon;
    int selectedIndex = 0;
    Texture[] buttonTextures;
    Texture background;
    Texture[] selectedButtonTextures;
   

    public MainMenuScreen(final Main game) {
        this.game = game;
        touchPos = new Vector2();
        characterIcon = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_up.png"));
        characterIcon.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        background = new Texture("menu/start_menu/main_menu/backgroundColorForest.png");
        buttonTextures = new Texture[] {
        	    new Texture("menu/start_menu/main_menu/new_game.png"),
        	    new Texture("menu/start_menu/main_menu/load_game.png"),
        	    new Texture("menu/start_menu/main_menu/settings.png"),
        	    new Texture("menu/start_menu/main_menu/exit.png")
        	};
        selectedButtonTextures = new Texture[] {
        	    new Texture("menu/start_menu/main_menu/new_game_b.png"),
        	    new Texture("menu/start_menu/main_menu/load_game_b.png"),
        	    new Texture("menu/start_menu/main_menu/settings_b.png"),
        	    new Texture("menu/start_menu/main_menu/exit_b.png")
        	};
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

        // 1. Vẽ nền
        game.batch.draw(background, 0, 0, viewportWidth, viewportHeight);

        // 2. Tiêu đề
        game.font.setColor(Color.RED);
        GlyphLayout layout = new GlyphLayout();
        String title = "Paradise Seeker";
        layout.setText(game.font, title);
        float xTitle = Math.round((viewportWidth - layout.width) / 2f + 0.4f);
        float yTitle = Math.round(viewportHeight - 0.6f);
        game.font.draw(game.batch, layout, xTitle, yTitle);

        // 3. Vẽ icon nhân vật
        float iconWidth = 1.5f;
        float iconHeight = 1.5f;
        float xIcon = Math.round((viewportWidth - iconWidth) / 2f)+0.2f;
        float yIcon = Math.round(yTitle - iconHeight - 0.6f)-0.2f;
        game.batch.draw(characterIcon, xIcon, yIcon, iconWidth, iconHeight);
        
        // 4. Vẽ các nút menu
        float buttonWidth = 3.0f;            // giảm để không bị to/méo ảnh
        float buttonHeight = 0.9f;
        float xButton = (viewportWidth - buttonWidth) / 2f;

        // Bắt đầu từ dưới nhân vật 1 khoảng
        float yStart = yIcon - 1.5f; 

        for (int i = 0; i < buttonTextures.length; i++) {
            float yButton = yStart - i * (buttonHeight + 0.6f); // khoảng cách đều đẹp
            Texture buttonTex = (i == selectedIndex) ? selectedButtonTextures[i] : buttonTextures[i];
            game.batch.draw(buttonTex, xButton, yButton, buttonWidth, buttonHeight);

            // Vẽ dấu >
            if (i == selectedIndex) {
                game.font.setColor(Color.YELLOW);
                game.font.setColor(Color.BLACK); // hoặc Color.DARK_GRAY
                game.font.draw(game.batch, ">", xButton - 0.3f, yButton + buttonHeight * 0.7f);
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
            float yTitle = viewportHeight - 1f;
            float iconHeight = 1.5f;
            float yIcon = yTitle - 3f;

            for (int i = 0; i < buttonTextures.length; i++) {
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
            	if (game.currentGame == null) {
					game.currentGame = new GameScreen(game);
				}else {
					game.currentGame = null;
					game.currentGame = new GameScreen(game);
				}
            	game.setScreen(game.currentGame);
                break;
            case 1:
            	if (game.currentGame == null) {
            		game.setScreen(new MainMenuScreen(game));
            	}else {
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
       characterIcon.dispose();
       for (Texture t : buttonTextures) t.dispose();
       for (Texture t : selectedButtonTextures) t.dispose();
    }
}
