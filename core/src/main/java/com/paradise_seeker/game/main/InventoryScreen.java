package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.object.Item;

public class InventoryScreen implements Screen {
    private final Player player;
    private final Main game;
    private final GlyphLayout layout;
    private final ShapeRenderer shapeRenderer;
    private Texture backgroundTexture;
    
    // Changed to use grid coordinates (0-2, 0-2) instead of screen coordinates
    private int selectedCol = 0;
    private int selectedRow = 0;
    
    private boolean inDescriptionArea = false;
    private static final float BASE_HEIGHT = 720f;
    private float fontScale = 0.2f;
    
    // Grid constants
    private static final int GRID_ROWS = 3;
    private static final int GRID_COLS = 3;

    public InventoryScreen(Main game, Player player) {
        this.game = game;
        this.player = player;
        this.layout = new GlyphLayout();
        this.shapeRenderer = new ShapeRenderer();
        this.backgroundTexture = new Texture(Gdx.files.internal("menu/inventory_menu/inventoryscreen1.png"));
        updateFontScale();
    }

    private void updateFontScale() {
        float screenHeight = Gdx.graphics.getHeight();
        this.fontScale = (screenHeight / BASE_HEIGHT) * 0.02f;
    }

    @Override
    public void show() {
        updateFontScale();
    }

    @Override
    public void render(float delta) {
        // Save the original font scale
        float originalScaleX = game.font.getData().scaleX;
        float originalScaleY = game.font.getData().scaleY;

        // Set the dynamic font scale
        game.font.getData().setScale(fontScale);

        // Handle closing inventory
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            game.setScreen(game.currentGame);
            // Restore font scale before returning
            game.font.getData().setScale(originalScaleX, originalScaleY);
            return;
        }

        // Clear the screen
        ScreenUtils.clear(Color.BLACK);
        game.camera.update();

        // Draw background
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
        game.batch.end();

        // Draw UI
        drawUI();

        // Handle input
        handleInput();

        // Restore font scale after drawing everything
        game.font.getData().setScale(originalScaleX, originalScaleY);
    }

    private void drawUI() {
        // Draw text and items
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();

        // Draw player stats
        game.font.setColor(Color.WHITE);
        float startX = 3.2f;
        float startY = 7.5f;
        float lineSpacing = 0.8f;
        drawText("HP: " + player.hp + "/" + Player.MAX_HP, startX, startY);
        drawText("MP: " + player.mp + "/" + Player.MAX_MP, startX, startY - lineSpacing);
        drawText("ATK: " + player.atk, startX, startY - 2 * lineSpacing);
        drawText("Skill 1 DMG x" + player.playerSkill1.getdamageMultiplier(), startX, startY - 3 * lineSpacing);
        drawText("Skill 2 DMG x" + player.playerSkill2.getdamageMultiplier(), startX, startY - 4 * lineSpacing);

        // Grid start position
        float gridStartX = 8.74f;
        float gridStartY = 2.9f;
        float slotSize = 1f;
        float slotSpacing = 0.5f;

        // Draw inventory items as a 3x3 grid
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                int index = row * GRID_COLS + col;
                
                float x = gridStartX + col * (slotSize + slotSpacing);
                float y = gridStartY + (GRID_ROWS - 1 - row) * (slotSize + slotSpacing);

                if (index < player.inventory.size()) {
                    Item item = player.inventory.get(index);
                    game.batch.draw(item.getTexture(), x, y, slotSize, slotSize);

                    // Draw stack count if applicable
                    if (item.isStackable() && item.getCount() > 1) {
                        drawText(String.valueOf(item.getCount()), x + 0.55f, y + 0.1f);
                    }
                }

                // Highlight selected slot (draw on top of item)
                if (selectedCol == col && selectedRow == row) {
                    game.batch.end(); // End batch to draw shape
                    
                    shapeRenderer.setProjectionMatrix(game.camera.combined);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.rect(x, y, slotSize, slotSize);
                    shapeRenderer.end();
                    
                    game.batch.begin(); // Resume batch
                }
                
            }
        }

        // Draw selected item description
        Item item = getSelectedItem();
        if (item != null) {
            game.font.setColor(Color.WHITE);
            drawText("Name: " + item.getName(), 9f, 2.4f);
            drawText(item.getDescription(), 9f, 1.7f);
            drawText("[E] Use", 3f, 1.65f);
            drawText("[Q] Drop", 5f, 1.65f);
        }

        game.batch.end();
        game.batch.begin();
        game.font.setColor(Color.WHITE);
        drawCenteredText("[B] Exit Inventory", 0.4f); // Adjust Y-coordinate as needed
        game.batch.end();
    }

    private void handleInput() {
        // Move between inventory slots
        if (!inDescriptionArea) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                selectedRow = Math.max(0, selectedRow - 1); // Move up (decrease row)
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                selectedRow = Math.min(GRID_ROWS - 1, selectedRow + 1); // Move down (increase row)
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                selectedCol = Math.max(0, selectedCol - 1); // Move left (decrease column)
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                selectedCol = Math.min(GRID_COLS - 1, selectedCol + 1); // Move right (increase column)
            }
            // Switch to description area
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                inDescriptionArea = true;
            }
        } else {
            // Return from description area
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                inDescriptionArea = false;
            }
        }

        // Use item
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            useSelectedItem();
        }

        // Drop item
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            dropSelectedItem();
        }
    }

    private Item getSelectedItem() {
        int index = selectedRow * GRID_COLS + selectedCol;
        if (index >= 0 && index < player.inventory.size()) {
            return player.inventory.get(index);
        }
        return null;
    }

    private void useSelectedItem() {
        Item item = getSelectedItem();
        if (item != null) {
            item.use(player);
            if (item.isStackable()) {
                item.setCount(item.getCount() - 1);
                if (item.getCount() <= 0) {
                    player.inventory.remove(item);
                }
            } else {
                player.inventory.remove(item);
            }
        }
    }

    private void dropSelectedItem() {
        Item item = getSelectedItem();
        if (item != null) {
            // TODO: Add logic to drop item to the map
            player.inventory.remove(item);
        }
    }

    private void drawText(String text, float x, float y) {
        layout.setText(game.font, text);
        game.font.draw(game.batch, layout, x, y);
    }

    private void drawCenteredText(String text, float y) {
        layout.setText(game.font, text);
        float x = (game.viewport.getWorldWidth() - layout.width) / 2;
        game.font.draw(game.batch, layout, x, y);
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        updateFontScale();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        backgroundTexture.dispose();
    }
}