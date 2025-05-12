package com.paradise_seeker.game.main;

import com.paradise_seeker.game.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
	final Main game;

	Texture backgroundTexture;
	Texture CharTexture;
	

	Sprite CharSprite;
	Vector2 touchPos;

	Rectangle CharRectangle;



	private Music music;

	public GameScreen(final Main game) {
		this.game = game;

		// load the images for the background, bucket and droplet
		backgroundTexture = new Texture("background.png");
		CharTexture = new Texture(Gdx.files.internal("images/Entity/characters/s2.png"));


		// load the drop sound effect and background music
	//	dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
	    music = Gdx.audio.newMusic(Gdx.files.internal("music/music.mp3"));
		music.setLooping(true);
		music.setVolume(0.5F); 

		CharSprite = new Sprite(CharTexture);
		CharSprite.setSize(1, 1);
		
		touchPos = new Vector2();
		
		CharRectangle = new Rectangle();
		
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		music.play();
	}

	@Override
	public void render(float delta) {
		input();
		logic();
		draw();
	}

	private void input() {
		float speed = 4f;
		float delta = Gdx.graphics.getDeltaTime();
        
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			CharSprite.translateX(speed * delta);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			CharSprite.translateX(-speed * delta);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			CharSprite.translateY(speed * delta);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			CharSprite.translateY(-speed * delta);
		}
	
		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY());
			game.viewport.unproject(touchPos);
			CharSprite.setCenter(touchPos.x, touchPos.y);
		}
	}

	private void logic() {
		float worldWidth = game.viewport.getWorldWidth();
		float worldHeight = game.viewport.getWorldHeight();
		float CharWidth = CharSprite.getWidth();
		float CharHeight = CharSprite.getHeight();
		float delta = Gdx.graphics.getDeltaTime();
	
		CharSprite.setX(MathUtils.clamp(CharSprite.getX(), 0, worldWidth - CharWidth));
		CharSprite.setY(MathUtils.clamp(CharSprite.getY(), 0, worldHeight - CharHeight));
		CharRectangle.set(CharSprite.getX(), CharSprite.getY(), CharWidth, CharHeight);
	
	}

	private void draw() {
		ScreenUtils.clear(Color.BLACK);
		game.viewport.apply();
		game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
		game.batch.begin();
		
		float worldWidth = game.viewport.getWorldWidth();
		float worldHeight = game.viewport.getWorldHeight();
        
		game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
		CharSprite.draw(game.batch);
	
	

		game.batch.end();
	}



	@Override
	public void resize(int width, int height) {
		game.viewport.update(width, height, true);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		backgroundTexture.dispose();

		music.dispose();

		CharTexture.dispose();
	}
}
