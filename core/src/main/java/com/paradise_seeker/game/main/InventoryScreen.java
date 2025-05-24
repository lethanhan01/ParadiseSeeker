package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class InventoryScreen implements Screen {

    final Main game;
    GlyphLayout layout;
    String[] menuItems = {"INVENTORY","STATS", "ITEMS", "DECRIPTION"};
    ShapeRenderer shapeRenderer;
    private int selectedItemX = 9;
    private int selectedItemY = 7;
    private Texture[][] InventoryItems = new Texture[3][6];


    public InventoryScreen(Main game) {
		this.game = game;
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		InventoryItems[0][0] = new Texture(Gdx.files.internal("items/potion/potion9.png"));
		InventoryItems[0][1] = new Texture(Gdx.files.internal("items/potion/potion10.png"));
		InventoryItems[0][2] = new Texture(Gdx.files.internal("items/potion/potion11.png"));
		InventoryItems[0][3] = new Texture(Gdx.files.internal("items/potion/potion3.png"));
		InventoryItems[0][4] = new Texture(Gdx.files.internal("items/potion/potion4.png"));
		InventoryItems[0][5] = new Texture(Gdx.files.internal("items/potion/potion5.png"));
	}

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
    	if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			game.setScreen(game.currentGame);
		}
        ScreenUtils.clear(Color.BLACK);
        
        
        game.camera.update();
        
        
        shapeRenderer.setProjectionMatrix(game.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Line: chỉ viền. Dùng .Filled nếu muốn fill

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(2, 1, 6, 7);   // Rectangle 1

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(9, 5, 6, 3);   // Rectangle 2

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(9, 1, 6, 3);   // Rectangle 3
        shapeRenderer.setColor(Color.YELLOW);
        SelectionItems(shapeRenderer);
        

        shapeRenderer.end();
        
        game.batch.setProjectionMatrix(game.camera.combined);
        
        float viewportWidth = game.viewport.getWorldWidth();
        float viewportHeight = game.viewport.getWorldHeight();
        

        game.batch.begin();
        
        game.font.setColor(Color.RED);
        String text = menuItems[0];
        layout.setText(game.font, text);
        float x = (viewportWidth - layout.width) / 2f;
        float y = viewportHeight;
        game.font.draw(game.batch, layout, x, y);
        
        game.font.setColor(Color.RED);
        String text1 = menuItems[1];
        layout.setText(game.font, text1);
        float x1 = (10 - layout.width) / 2f;
        float y1 = 8.8f;
        game.font.draw(game.batch, layout, x1, y1);
        
        game.font.setColor(Color.GREEN);
        String text2 = menuItems[2];
        layout.setText(game.font, text2);
        float x2 = (24- layout.width) / 2f;
        float y2 = 8.8f;
        game.font.draw(game.batch, layout, x2, y2);
        
        game.font.setColor(Color.BLUE);
        String text3 = menuItems[3];
        layout.setText(game.font, text3);
        float x3 = (24 - layout.width) / 2f;
        float y3 = 4.6f;
        game.font.draw(game.batch, layout, x3, y3);
        for (int row = 2; row >= 0; row--) {
            for (int col = 5; col >= 0; col--) {
                Texture tex = InventoryItems[row][col];
                if (tex != null) {
                    float drawX = 9 + col;      // Hoặc col * cellSize
                    float drawY = 5 + row;      // Hoặc row * cellSize
                    game.batch.draw(tex, drawX, drawY, 1, 1);  // size = 1x1 unit
                }
            }
        }
        
       
        game.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            int gridX = selectedItemX - 9; // Giả sử grid bắt đầu từ x = 9
            int gridY = selectedItemY - 5; // Giả sử grid bắt đầu từ y = 5

            // Kiểm tra trong giới hạn mảng
            if (gridY >= 0 && gridY < InventoryItems.length && gridX >= 0 && gridX < InventoryItems[0].length) {
                Texture selectedItem = InventoryItems[gridY][gridX];
                if (selectedItem != null) {
                    System.out.println("Used item: " + selectedItem);
                    // TODO: gọi hàm useItem(selectedItem);
                    InventoryItems[gridY][gridX] = null; // Xoá item sau khi dùng (nếu cần)
                } else {
                    System.out.println("No item in this slot.");
                }
            }
        }
    }
    public void addItemToInventory(Texture texture) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 6; col++) {
                if (InventoryItems[row][col] == null) {
                    InventoryItems[row][col] = texture;
                    return;
                }
            }
        }
        System.out.println("Inventory is full!");
    }

    
    private void SelectionItems(ShapeRenderer shapeRenderer) {
		
    	 if ((Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP))&& selectedItemY < 7) 
    		 selectedItemY += 1;
         if ((Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN))&& selectedItemY > 5) 
        	 selectedItemY -= 1;
         if ((Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT))&& selectedItemX > 9) 
        	 selectedItemX -= 1;
         if ((Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))&& selectedItemX < 14) 
        	 selectedItemX += 1;
         shapeRenderer.rect(selectedItemX, selectedItemY, 1, 1);
	}

    @Override public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
    	shapeRenderer.dispose();
    }
}