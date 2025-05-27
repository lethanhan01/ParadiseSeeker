package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class SettingScreen implements Screen {

    final Main game;
    GlyphLayout layout;
    String[] menuItems = {"SETTINGS", "Full Screen", "Music", "SE", "Control", "End Game", "Back"};
    int selectedIndex = 1;

    float setVolume = 0.5f; // Volume từ 0 đến 1
    Texture background;
    Texture[] buttonTextures;
    Texture[] selectedButtonTextures;

    float musicVolume = setVolume; 
    float seVolume = setVolume; // Âm lượng SE cũng từ 0 đến 1

    ShapeRenderer shapeRenderer;

    public SettingScreen(Main game) {
        this.game = game;
        this.layout = new GlyphLayout();
        background = new Texture("menu/settings_menu/setting_main/menu_setting_c.png");

        buttonTextures = new Texture[]{
                new Texture("menu/settings_menu/setting_main/full_screen (1).png"),
                new Texture("menu/settings_menu/setting_main/music.png"),
                new Texture("menu/settings_menu/setting_main/SE.png"),
                new Texture("menu/settings_menu/setting_main/control.png"),
                new Texture("menu/settings_menu/setting_main/end_game.png"),
                new Texture("menu/settings_menu/setting_main/Back.png")
        };

        selectedButtonTextures = new Texture[]{
                new Texture("menu/settings_menu/setting_main/full_screen_b (1).png"),
                new Texture("menu/settings_menu/setting_main/music_b.png"),
                new Texture("menu/settings_menu/setting_main/SE_b.png"),
                new Texture("menu/settings_menu/setting_main/control_b.png"),
                new Texture("menu/settings_menu/setting_main/end_game_b.png"),
                new Texture("menu/settings_menu/setting_main/Back_b.png")
        };

        shapeRenderer = new ShapeRenderer();
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

        float buttonWidth = 3.2f;
        float buttonHeight = 0.85f;
        float xButton = 1.2f;

        float yStart = viewportHeight - 2.5f;
        float buttonSpacing = 0.55f;

        float volumeBarWidth = 7.5f;
        float volumeBarHeight = 0.2f;
        float xBar = xButton + buttonWidth + 3.0f;

        for (int i = 0; i < buttonTextures.length; i++) {
            float y = yStart - i * (buttonHeight + buttonSpacing);
            Texture tex = (i + 1 == selectedIndex) ? selectedButtonTextures[i] : buttonTextures[i];
            game.batch.draw(tex, xButton, y, buttonWidth, buttonHeight);

            if (i + 1 == selectedIndex) {
                game.font.setColor(Color.WHITE);
                game.font.draw(game.batch, ">", xButton - 0.5f, y + buttonHeight * 0.7f);
            }

            // Vẽ thanh âm lượng cho Music và SE
            if (i == 1 || i == 2) {
                float vol = (i == 1) ? musicVolume : seVolume;
                float yBar = y + buttonHeight / 2 - volumeBarHeight / 2;

                game.batch.end(); // Vẽ shape
                shapeRenderer.setProjectionMatrix(game.camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.GRAY);
                shapeRenderer.rect(xBar, yBar, volumeBarWidth, volumeBarHeight);
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rect(xBar, yBar, volumeBarWidth * vol, volumeBarHeight);
                shapeRenderer.end();
                game.batch.begin();

                // Vẽ chữ A và D
                game.font.setColor(Color.WHITE);
                game.font.draw(game.batch, "A", xBar - 0.4f, yBar + volumeBarHeight);
                game.font.draw(game.batch, "D", xBar + volumeBarWidth + 0.1f, yBar + volumeBarHeight);

                // Vẽ số giữa thanh
                int volumeValue = Math.round(vol * 10);
                String volumeText = String.valueOf(volumeValue);
                GlyphLayout volumeLayout = new GlyphLayout(game.font, volumeText);
                float volumeTextX = xBar + (volumeBarWidth - volumeLayout.width) / 2;
                float volumeTextY = yBar + volumeBarHeight * 5.5f;
                game.font.draw(game.batch, volumeText, volumeTextX, volumeTextY);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)|| Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (selectedIndex == 2) {
                musicVolume = Math.max(0f, musicVolume - 0.1f);
                if (game.currentGame != null)
                	game.currentGame.music.setVolume(musicVolume); 
                else
                	setVolume = Math.max(0f, musicVolume - 0.1f);
            } else if (selectedIndex == 3) {
                seVolume = Math.max(0f, seVolume - 0.1f);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)|| Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (selectedIndex == 2) {
                musicVolume = Math.min(1f, musicVolume + 0.1f);
                if (game.currentGame != null)
					game.currentGame.music.setVolume(musicVolume); 
				else
					setVolume = Math.min(1f, musicVolume + 0.1f);
            } else if (selectedIndex == 3) {
                seVolume = Math.min(1f, seVolume + 0.1f);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            switch (selectedIndex) {
                case 1: toggleFullscreen(); break; 								//fullscreen
                case 2: break; 													// Music
                case 3: break; 													// SE
                case 4: 														// Control
                    if (game.controlScreen == null)
                        game.controlScreen = new ControlScreen(game);
                    game.setScreen(game.controlScreen);
                    break;
                case 5:															//Back to the game
                    if (game.currentGame != null)
                        game.setScreen(game.currentGame);
                    else
                        game.setScreen(game.mainMenu);
                    break;
                case 6: game.setScreen(game.mainMenu); break;					// Back to main menu
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
        shapeRenderer.dispose();
    }
}
