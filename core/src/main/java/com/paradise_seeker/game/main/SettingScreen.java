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
    String[] menuItems = {"Full Screen", "Music", "Control", "Back", "Return to Menu"};
    int selectedIndex = 0;

    public static float setVolume = 0.5f;
    Texture background;

    float musicVolume = setVolume;

    ShapeRenderer shapeRenderer;

    // Font scaling
    private static final float BASE_HEIGHT = 950f;
    private float fontScale = 0.025f;
    private float menuItemSpacing = 0.95f;
    private float menuStartY = 0;

    // Track fullscreen toggle state for display
    private boolean showFullscreenToggle = false;

    public SettingScreen(Main game) {
        this.game = game;
        this.layout = new GlyphLayout();
        background = new Texture("menu/settings_menu/setting_main/settings_scr2.png");
        shapeRenderer = new ShapeRenderer();
        updateFontScale();
    }

    private void updateFontScale() {
        float screenHeight = Gdx.graphics.getHeight();
        this.fontScale = (screenHeight / BASE_HEIGHT) * 0.045f;
        menuItemSpacing = 0.95f * fontScale * 48f;
        menuStartY = game.viewport.getWorldHeight() - 3f;
    }

    @Override
    public void show() {
        updateFontScale();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        float viewportWidth = game.viewport.getWorldWidth();
        float viewportHeight = game.viewport.getWorldHeight();

        float originalScaleX = game.font.getData().scaleX;
        float originalScaleY = game.font.getData().scaleY;
        game.font.getData().setScale(fontScale);

        game.batch.begin();
        game.batch.draw(background, 0, 0, viewportWidth, viewportHeight);

        float xMenu = 3f;
        float y = menuStartY;

        // We'll need to remember where the Music bar is, for the toggle label.
        float barX = 0, barY = 0, barWidth = 5.5f, barHeight = 0.2f;
        boolean musicBarFound = false;

        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i];
            boolean isSelected = (i == selectedIndex);

            if (isSelected) {
                game.font.setColor(Color.GOLD);
                drawText("> " + text, xMenu, y);
            } else {
                game.font.setColor(Color.WHITE);
                drawText("  " + text, xMenu, y);
            }

            // Only draw music volume bar for the Music option (index 1)
            if (i == 1) {
                float vol = musicVolume;
                barX = xMenu + 4.1f;
                barY = y - 0.25f;
                musicBarFound = true;

                game.batch.end();
                shapeRenderer.setProjectionMatrix(game.camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.GRAY);
                shapeRenderer.rect(barX, barY, barWidth, barHeight * 0.45f);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(barX, barY, barWidth * vol, barHeight * 0.45f);
                shapeRenderer.end();
                game.batch.begin();

                // Draw A/D and volume value
                game.font.setColor(Color.LIGHT_GRAY);
                game.font.draw(game.batch, "<A>", barX - 1.2f, barY + barHeight * 1.0f);
                game.font.draw(game.batch, "<D>", barX + barWidth + 0.3f, barY + barHeight * 1.0f);

                int volumeValue = Math.round(vol * 10);
                String volumeText = String.valueOf(volumeValue);
                GlyphLayout volumeLayout = new GlyphLayout(game.font, volumeText);
                float volumeTextX = barX + (barWidth - volumeLayout.width) / 2;
                float volumeTextY = barY + barHeight * -1.0f;
                game.font.setColor(Color.GOLD);
                game.font.draw(game.batch, volumeText, volumeTextX, volumeTextY);
            }

            y -= menuItemSpacing;
        }

        // Draw On/Off label **above the bar** when Full Screen is selected
        if (selectedIndex == 0 && musicBarFound) {
            boolean isFullScreen = Gdx.graphics.isFullscreen();
            String status = isFullScreen ? "On" : "Off";
            Color statusColor = isFullScreen ? Color.GREEN : Color.RED;

            float labelX = barX + barWidth / 2f;
            float labelY = barY + barHeight * 6.3f;

            game.font.setColor(statusColor);
            GlyphLayout statusLayout = new GlyphLayout(game.font, status);
            // Centered above the bar
            game.font.draw(game.batch, status, labelX - statusLayout.width / 2, labelY);
        }

        game.batch.end();
        game.font.getData().setScale(originalScaleX, originalScaleY);

        handleInput();
    }

    private void drawText(String text, float x, float y) {
        layout.setText(game.font, text);
        game.font.draw(game.batch, layout, x, y);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (selectedIndex == 1) {
                musicVolume = Math.max(0f, musicVolume - 0.1f);
                if (game.currentGame != null)
                    game.currentGame.music.setVolume(musicVolume);
                else
                    setVolume = Math.max(0f, musicVolume - 0.1f);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (selectedIndex == 1) {
                musicVolume = Math.min(1f, musicVolume + 0.1f);
                if (game.currentGame != null)
                    game.currentGame.music.setVolume(musicVolume);
                else
                    setVolume = Math.min(1f, musicVolume + 0.1f);
            }
        }

        // Toggle fullscreen when pressing enter/space on "Full Screen"
        if ((Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            switch (selectedIndex) {
                case 0: // Fullscreen
                    toggleFullscreen();
                    break;
                case 1: break; // Music
                case 2: // Control
                    if (game.controlScreen == null)
                        game.controlScreen = new ControlScreen(game);
                    game.setScreen(game.controlScreen);
                    break;
                case 3: // End Game (back to game or menu)
                    if (game.currentGame != null)
                        game.setScreen(game.currentGame);
                    else
                        game.setScreen(game.mainMenu);
                    break;
                case 4: game.setScreen(game.mainMenu); break; // Back to main menu
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

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        updateFontScale();
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        shapeRenderer.dispose();
    }
}
