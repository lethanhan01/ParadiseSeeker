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
    
    private int selectedSlotX = 9;
    private int selectedSlotY = 5;
    private boolean inDescriptionArea = false;

    public InventoryScreen(Main game, Player player) {
        this.game = game;
        this.player = player;
        this.layout = new GlyphLayout();
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Xử lý đóng inventory
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            game.setScreen(game.currentGame);
            return;
        }

        ScreenUtils.clear(Color.BLACK);
        game.camera.update();
        
        // Vẽ UI
        drawUI();
        
        // Xử lý input
        handleInput();
    }

    private void drawUI() {
        shapeRenderer.setProjectionMatrix(game.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        
        // Vẽ khung stats
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(2, 1, 6, 7);
        
        // Vẽ khung inventory
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(9, 5, 6, 3);
        
        // Vẽ khung mô tả
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(9, 1, 6, 3);
        
        // Vẽ khung chọn item
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(selectedSlotX, selectedSlotY, 1, 1);
        
        shapeRenderer.end();
        
        // Vẽ text và items
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        
        // Vẽ tiêu đề
        game.font.setColor(Color.RED);
        drawCenteredText("INVENTORY", game.viewport.getWorldHeight() - 0.5f);
        
        // Vẽ stats player
        game.font.setColor(Color.WHITE);
        drawText("HP: " + player.hp + "/" + Player.MAX_HP, 2.5f, 6.5f);
        drawText("MP: " + player.mp + "/" + Player.MAX_MP, 2.5f, 5.5f);
        drawText("ATK: " + player.atk, 2.5f, 4.5f);
        drawText("Skill 1: " + player.playerSkill1.getdamageMultiplier(), 2.5f, 3.5f);
        drawText("Skill 2: " + player.playerSkill2.getdamageMultiplier(), 2.5f, 2.5f);
        
        // Vẽ các item trong inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 6; col++) {
                int index = row * 6 + col;
                if (index < player.inventory.size()) {
                    Item item = player.inventory.get(index);
                    float x = 9 + col;
                    float y = 5 + (2 - row);
                    game.batch.draw(item.getTexture(), x, y, 1, 1);
                    
                    // Hiển thị số lượng nếu item có thể stack và count > 1
                    if (item.isStackable() && item.getCount() > 1) {
                        drawText(String.valueOf(item.getCount()), x + 0.7f, y + 0.3f);
                    }
                }
            }
        }
        
        // Vẽ mô tả item được chọn
        if (getSelectedItem() != null) {
            Item item = getSelectedItem();
            game.font.setColor(Color.WHITE);
            drawText("Name: " + item.getName(), 9.5f, 3.5f);
            drawText("Desc: " + item.getDescription(), 9.5f, 2.5f);
            drawText("Press E to use", 9.5f, 2.0f);
            drawText("Press Q to drop", 9.5f, 1.5f);
        }
        
        game.batch.end();
    }

    private void handleInput() {
        // Di chuyển giữa các ô inventory
        if (!inDescriptionArea) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)|| Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                selectedSlotY = Math.min(7, selectedSlotY + 1);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)|| Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                selectedSlotY = Math.max(5, selectedSlotY - 1);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)|| Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                selectedSlotX = Math.max(9, selectedSlotX - 1);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)|| Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                selectedSlotX = Math.min(14, selectedSlotX + 1);
            }
            
            // Chuyển sang vùng mô tả
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                inDescriptionArea = true;
            }
        } else {
            // Quay lại inventory
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                inDescriptionArea = false;
            }
        }
        
        // Sử dụng item
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            useSelectedItem();
        }
        
        // Vứt item
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            dropSelectedItem();
        }
    }

    private Item getSelectedItem() {
        int col = selectedSlotX - 9;
        int row = 7 - selectedSlotY; // Chuyển đổi từ tọa độ màn hình sang index hàng
        int index = row * 6 + col;
        
        if (index >= 0 && index < player.inventory.size()) {
            return player.inventory.get(index);
        }
        return null;
    }

    private void useSelectedItem() {
        Item item = getSelectedItem();
        if (item != null) {
            item.use(player);
            
            // Giảm count và xóa nếu count <= 0
            if (item.isStackable()) {
                item.setCount(item.getCount() - 1);
                if (item.getCount() <= 0) {
                    player.inventory.remove(item);
            	}
            }else{
				player.inventory.remove(item);
			}
        }
    }

    private void dropSelectedItem() {
        Item item = getSelectedItem();
        if (item != null) {
            // TODO: Thêm logic thả item xuống map
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
    }
}