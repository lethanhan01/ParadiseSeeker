package com.paradise_seeker.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class ControlScreen implements Screen {

	final Main game;
	BitmapFont font;
	GlyphLayout layout;

	String[][] controls = {
			{"Moving", "WASD / ↑↓←→"},
			{"Dashing", "Move + Left Shift"},
			{"Attack", "SPACE"},
			{"Skill 1", "U"},
			{"Skill 2", "I"},
			{"Pause Game", "ESC"},
			{"Inventory", "B"},
			{"   - Use Item", "E"},
			{"   - Drop Item", "Q"}
	};

	public ControlScreen(Main game) {
		this.game = game;
		this.font = game.font; // dùng font chung
		this.layout = new GlyphLayout();
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		float viewportWidth = game.viewport.getWorldWidth();
        float viewportHeight = game.viewport.getWorldHeight();
		ScreenUtils.clear(Color.DARK_GRAY);

		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.begin();

		float xLeft = 2f;
		float xRight = 10f;
		float yStart = game.viewport.getWorldHeight() - 2f;
		float lineHeight = 0.75f;

		font.setColor(Color.WHITE);

		for (int i = 0; i < controls.length; i++) {
			String left = controls[i][0];
			String right = controls[i][1];

			float y = yStart - i * lineHeight;

			font.draw(game.batch, left, xLeft, y);
			font.draw(game.batch, right, xRight, y);
		}
		font.setColor(Color.RED);
		layout.setText(font, "- Controls -");
        float x = (viewportWidth - layout.width) / 2f;
		font.draw(game.batch, layout, x, viewportHeight);
		// Hint để quay lại
		font.setColor(Color.YELLOW);
		font.draw(game.batch, "[ESC] Return", xLeft, 0.5f);

		game.batch.end();

		// Quay lại SettingScreen
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.setScreen(game.settingMenu);
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

