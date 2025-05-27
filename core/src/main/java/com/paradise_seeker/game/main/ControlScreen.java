package com.paradise_seeker.game.main;

import com.badlogic.gdx.Screen;

public class ControlScreen implements Screen {

	final Main game;

	public ControlScreen(Main game) {
		this.game = game;
	}

	@Override
	public void show() {
		// Initialize resources or settings if needed
	}

	@Override
	public void render(float delta) {
		// Render the control screen
		game.batch.begin();
		// Draw control instructions here
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// Handle resizing if necessary
	}

	@Override
	public void pause() {
		// Handle pause state if necessary
	}

	@Override
	public void resume() {
		// Handle resume state if necessary
	}

	@Override
	public void hide() {
		// Clean up resources if necessary
	}

	@Override
	public void dispose() {
		// Dispose of resources if necessary
	}

}
