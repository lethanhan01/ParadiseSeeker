	package com.paradise_seeker.game.entity;
	
	import com.badlogic.gdx.Gdx;
	import com.badlogic.gdx.Input;
	import com.badlogic.gdx.graphics.g2d.SpriteBatch;
	import com.badlogic.gdx.graphics.Texture;
	import com.badlogic.gdx.graphics.g2d.Animation;
	import com.badlogic.gdx.graphics.g2d.TextureRegion;
	import com.badlogic.gdx.math.Rectangle;
	import com.paradise_seeker.game.entity.skill.*;
	
	public class Player extends Character {
	    public PlayerSkill playerSkill1;
	    public PlayerSkill playerSkill2;
	    public Weapon weapon;
	
	    private Animation<TextureRegion> runUp, runDown, runLeft, runRight;
	    private Animation<TextureRegion> attackUp, attackDown, attackLeft, attackRight;
	    private Animation<TextureRegion> idleUp, idleDown, idleLeft, idleRight;
	    private TextureRegion currentFrame;
	    private float stateTime = 0f;
	    private String direction = "down";
	    private boolean isMoving = false;
	    private boolean isAttacking = false;
	
	    private boolean isDashing = false;
	    private float dashCooldown = 1.0f;
	    private float dashTimer = 0f;
	    private float dashDistance = 50f;
	
	    private boolean isShielding = false;
	    private boolean menuOpen = false;
	    private boolean isPaused = false;
	
	    private Texture shieldDown, shieldUp, shieldLeft, shieldRight;
	
	    private boolean isClimbing = false;
	    private Animation<TextureRegion> climbUp, climbDown, climbUpAfter, climbDownBefore;
	
	    private boolean isDead = false;
	    private Animation<TextureRegion> deathAnimation;
	
	    private boolean isPushing = false;
	    private Animation<TextureRegion> pushUp, pushDown, pushLeft, pushRight;
	
	    private boolean isShieldedHit = true;
	    private Animation<TextureRegion> shieldedHitUp, shieldedHitDown, shieldedHitLeft, shieldedHitRight;
	
	    private boolean isHit = false;
	    private Animation<TextureRegion> hitUp, hitDown, hitLeft, hitRight;
	
	    public Player(Rectangle bounds) {
	        super(bounds, 100, 50, 10, 5f);
	        loadAnimations();
	        this.playerSkill1 = new PlayerSkill();
	        this.playerSkill2 = new PlayerSkill();
	    }
	
	    private void loadAnimations() {
	        runDown = loadAnimation("images/Entity/characters/player/char_run_down_anim_strip_6.png");
	        runUp = loadAnimation("images/Entity/characters/player/char_run_up_anim_strip_6.png");
	        runLeft = loadAnimation("images/Entity/characters/player/char_run_left_anim_strip_6.png");
	        runRight = loadAnimation("images/Entity/characters/player/char_run_right_anim_strip_6.png");
	
	        idleDown = loadAnimation("images/Entity/characters/player/char_idle_down_anim_strip_6.png");
	        idleUp = loadAnimation("images/Entity/characters/player/char_idle_up_anim_strip_6.png");
	        idleLeft = loadAnimation("images/Entity/characters/player/char_idle_left_anim_strip_6.png");
	        idleRight = loadAnimation("images/Entity/characters/player/char_idle_right_anim_strip_6.png");
	
	        attackDown = loadAnimation("images/Entity/characters/player/char_attack_down_anim_strip_6.png");
	        attackUp = loadAnimation("images/Entity/characters/player/char_attack_up_anim_strip_6.png");
	        attackLeft = loadAnimation("images/Entity/characters/player/char_attack_left_anim_strip_6.png");
	        attackRight = loadAnimation("images/Entity/characters/player/char_attack_right_anim_strip_6.png");
	
	        shieldDown = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_down.png"));
	        shieldUp = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_up.png"));
	        shieldLeft = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_left.png"));
	        shieldRight = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_right.png"));
	
	        climbDown = loadAnimation("images/Entity/characters/player/char_climbing_down_anim_strip_6.png");
	        climbDownBefore = loadAnimation("images/Entity/characters/player/char_climbing_down_before_anim_strip_3.png", 3);
	        climbUp = loadAnimation("images/Entity/characters/player/char_climbing_up_anim_strip_6.png");
	        climbUpAfter = loadAnimation("images/Entity/characters/player/char_climbing_up_after_anim_strip_3.png", 3);
	
	        pushUp = loadAnimation("images/Entity/characters/player/char_pushing_up_anim_strip_6.png");
	        pushDown = loadAnimation("images/Entity/characters/player/char_pushing_down_anim_strip_6.png");
	        pushLeft = loadAnimation("images/Entity/characters/player/char_pushing_left_anim_strip_6.png");
	        pushRight = loadAnimation("images/Entity/characters/player/char_pushing_right_anim_strip_6.png");
	
	        shieldedHitUp = loadAnimation("images/Entity/characters/player/char_shielded_hit_up_anim_strip_5.png", 5);
	        shieldedHitDown = loadAnimation("images/Entity/characters/player/char_shielded_hit_down_anim_strip_5.png", 5);
	        shieldedHitLeft = loadAnimation("images/Entity/characters/player/char_shielded_hit_left_anim_strip_5.png", 5);
	        shieldedHitRight = loadAnimation("images/Entity/characters/player/char_shielded_hit_right_anim_strip_5.png", 5);
	
	        hitUp = loadAnimation("images/Entity/characters/player/char_hit_up_anim_strip_3.png", 3);
	        hitDown = loadAnimation("images/Entity/characters/player/char_hit_down_anim_strip_3.png", 3);
	        hitLeft = loadAnimation("images/Entity/characters/player/char_hit_left_anim_strip_3.png", 3);
	        hitRight = loadAnimation("images/Entity/characters/player/char_hit_right_anim_strip_3.png", 3);
	
	        deathAnimation = loadAnimation("images/Entity/characters/player/char_death_all_dir_anim_strip_10.png", 10);
	        currentFrame = runDown.getKeyFrame(0);
	    }
	
	    private Animation<TextureRegion> loadAnimation(String filePath) {
	        Texture sheet = new Texture(Gdx.files.internal(filePath));
	        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 6, sheet.getHeight());
	        return new Animation<>(0.1f, tmp[0]);
	    }
	
	    private Animation<TextureRegion> loadAnimation(String filePath, int frameCount) {
	        Texture sheet = new Texture(Gdx.files.internal(filePath));
	        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / frameCount, sheet.getHeight());
	        return new Animation<>(0.1f, tmp[0]);
	    }
	
	    public void regenMana(float deltaTime) {
	        if (mp < 100) {
	            mp += 5 * deltaTime;
	        }
	    }
	
	    public void update(float deltaTime) {
	        handleInput(deltaTime);
	        regenMana(deltaTime);
	        dashTimer -= deltaTime;
	
	        if (isMoving || isAttacking) {
	            stateTime += deltaTime;
	        } else {
	            stateTime = 0;
	        }
	
	        if (isAttacking) {
	            Animation<TextureRegion> currentAttack = getAttackAnimationByDirection();
	            if (currentAttack.isAnimationFinished(stateTime)) {
	                isAttacking = false;
	                stateTime = 0;
	            }
	        }
	    }
	
	    private void handleInput(float deltaTime) {
	        if (isPaused || isAttacking) return;
	
	        handleMovement(deltaTime);
	        handleDash();
	        handleAttack();
	        handleShield();
	        handleSkillCast(); // Gọi kỹ năng bằng phím U
	    }
	
	    private void handleSkillCast() {
	        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
	            if (playerSkill1.canUse(System.currentTimeMillis()) && mp >= playerSkill1.getManaCost()) {
	                mp -= playerSkill1.getManaCost();

	                int skillX = (int) (bounds.x + bounds.width / 2);
	                int skillY = (int) (bounds.y + bounds.height / 2);
	                int offset = 32;

	                switch (direction) {
	                    case "up":    skillY += offset; break;
	                    case "down":  skillY -= offset; break;
	                    case "left":  skillX -= offset; break;
	                    case "right": skillX += offset; break;
	                }

	                playerSkill1.castSkill(atk, skillX, skillY, direction);
	            }
	        }
	    }

	
	    private void handleMovement(float deltaTime) {
	        float dx = 0, dy = 0;
	        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
	        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
	        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
	        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;
	
	        float len = (float) Math.sqrt(dx * dx + dy * dy);
	        isMoving = len > 0;
	
	        if (isMoving) {
	            bounds.x += (dx / len) * speed * deltaTime;
	            bounds.y += (dy / len) * speed * deltaTime;
	
	            if (Math.abs(dx) > Math.abs(dy)) {
	                direction = dx > 0 ? "right" : "left";
	            } else {
	                direction = dy > 0 ? "up" : "down";
	            }
	        }
	    }
	
	    private void handleDash() {
	        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashTimer <= 0 && isMoving) {
	            float dx = 0, dy = 0;
	            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
	            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
	            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
	            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;
	
	            float len = (float) Math.sqrt(dx * dx + dy * dy);
	            if (len > 0) {
	                bounds.x += (dx / len) * dashDistance;
	                bounds.y += (dy / len) * dashDistance;
	                dashTimer = dashCooldown;
	            }
	        }
	    }
	
	    private void handleAttack() {
	        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
	            isAttacking = true;
	            stateTime = 0;
	        }
	    }
	
	    private void handleShield() {
	        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
	            if (!isShielding) {
	                isShielding = true;
	                hp = Math.min(hp + 10, 100);
	            }
	        } else {
	            isShielding = false;
	            isShieldedHit = false;
	        }
	    }
	
	    private Animation<TextureRegion> getAttackAnimationByDirection() {
	        switch (direction) {
	            case "up": return attackUp;
	            case "down": return attackDown;
	            case "left": return attackLeft;
	            case "right": return attackRight;
	        }
	        return attackDown;
	    }
	
	    private Animation<TextureRegion> getIdleAnimationByDirection() {
	        switch (direction) {
	            case "up": return idleUp;
	            case "down": return idleDown;
	            case "left": return idleLeft;
	            case "right": return idleRight;
	        }
	        return idleDown;
	    }
	
	    @Override
	    public void render(SpriteBatch batch) {
	        if (isDead) {
	            renderDeath(batch); return;
	        }
	        if (isShielding && isShieldedHit) {
	            renderShieldedHit(batch); return;
	        }
	        if (isHit) {
	            renderHit(batch); return;
	        }
	        if (isShielding) {
	            renderShield(batch); return;
	        }
	        if (isAttacking) {
	            renderAttack(batch);
	        } else if (isClimbing) {
	            renderClimbing(batch);
	        } else if (isPushing) {
	            renderPushing(batch);
	        } else if (isMoving) {
	            renderMovement(batch);
	        } else {
	            renderIdle(batch);
	        }
	    }
	
	    // Các hàm render: renderShield, renderAttack, renderMovement, renderIdle, renderClimbing, renderDeath, renderPushing, renderHit, renderShieldedHit
	    // (Giữ nguyên như bạn đã có trước đó – không đổi)
	
	    @Override public void move() {}
	    @Override public void onDeath() {
	        isDead = true;
	        stateTime = 0;
	    }
	
	    @Override public void onCollision(Collidable other) {
	        if (isShielding) {
	            isShieldedHit = true;
	        } else {
	            isHit = true;
	            stateTime = 0;
	        }
	    }
	
	    public boolean isPaused() { return isPaused; }
	    public boolean isMenuOpen() { return menuOpen; }
	
	
	//=== Các hàm render ===
	
	private void renderShield(SpriteBatch batch) {
	 Texture shieldTexture = shieldDown;
	 switch (direction) {
	     case "up": shieldTexture = shieldUp; break;
	     case "down": shieldTexture = shieldDown; break;
	     case "left": shieldTexture = shieldLeft; break;
	     case "right": shieldTexture = shieldRight; break;
	 }
	 batch.draw(shieldTexture, bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	private void renderHit(SpriteBatch batch) {
	 stateTime += Gdx.graphics.getDeltaTime();
	 switch (direction) {
	     case "up": currentFrame = hitUp.getKeyFrame(stateTime, false); break;
	     case "down": currentFrame = hitDown.getKeyFrame(stateTime, false); break;
	     case "left": currentFrame = hitLeft.getKeyFrame(stateTime, false); break;
	     case "right": currentFrame = hitRight.getKeyFrame(stateTime, false); break;
	 }
	 float scaledWidth = bounds.width * 2.5f;
	 float scaledHeight = bounds.height * 2.5f;
	 float drawX = bounds.x - (scaledWidth - bounds.width) / 2f;
	 float drawY = bounds.y - (scaledHeight - bounds.height) / 2f;
	 batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
	
	 if (hitUp.isAnimationFinished(stateTime) || hitDown.isAnimationFinished(stateTime) ||
	     hitLeft.isAnimationFinished(stateTime) || hitRight.isAnimationFinished(stateTime)) {
	     isHit = false;
	     stateTime = 0;
	 }
	}
	
	private void renderShieldedHit(SpriteBatch batch) {
	 stateTime += Gdx.graphics.getDeltaTime();
	 switch (direction) {
	     case "up": currentFrame = shieldedHitUp.getKeyFrame(stateTime, false); break;
	     case "down": currentFrame = shieldedHitDown.getKeyFrame(stateTime, false); break;
	     case "left": currentFrame = shieldedHitLeft.getKeyFrame(stateTime, false); break;
	     case "right": currentFrame = shieldedHitRight.getKeyFrame(stateTime, false); break;
	 }
	 float scaledWidth = bounds.width * 2.5f;
	 float scaledHeight = bounds.height * 2.5f;
	 float drawX = bounds.x - (scaledWidth - bounds.width) / 2f;
	 float drawY = bounds.y - (scaledHeight - bounds.height) / 2f;
	 batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
	
	 if (shieldedHitUp.isAnimationFinished(stateTime) || shieldedHitDown.isAnimationFinished(stateTime) ||
	     shieldedHitLeft.isAnimationFinished(stateTime) || shieldedHitRight.isAnimationFinished(stateTime)) {
	     isShieldedHit = false;
	     stateTime = 0;
	 }
	}
	
	private void renderDeath(SpriteBatch batch) {
	 currentFrame = deathAnimation.getKeyFrame(stateTime, false);
	 batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	private void renderAttack(SpriteBatch batch) {
	 currentFrame = getAttackAnimationByDirection().getKeyFrame(stateTime, false);
	 float scaledWidth = bounds.width * 2.5f;
	 float scaledHeight = bounds.height * 2.5f;
	 float drawX = bounds.x - (scaledWidth - bounds.width) / 2f;
	 float drawY = bounds.y - (scaledHeight - bounds.height) / 2f;
	 batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
	}
	
	private void renderMovement(SpriteBatch batch) {
	 switch (direction) {
	     case "up": currentFrame = runUp.getKeyFrame(stateTime, true); break;
	     case "down": currentFrame = runDown.getKeyFrame(stateTime, true); break;
	     case "left": currentFrame = runLeft.getKeyFrame(stateTime, true); break;
	     case "right": currentFrame = runRight.getKeyFrame(stateTime, true); break;
	 }
	 batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	private void renderIdle(SpriteBatch batch) {
	 stateTime += Gdx.graphics.getDeltaTime();
	 currentFrame = getIdleAnimationByDirection().getKeyFrame(stateTime, true);
	 float scaledWidth = bounds.width * 2.5f;
	 float scaledHeight = bounds.height * 2.5f;
	 float drawX = bounds.x - (scaledWidth - bounds.width) / 2f;
	 float drawY = bounds.y - (scaledHeight - bounds.height) / 2f;
	 batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
	}
	
	private void renderClimbing(SpriteBatch batch) {
	 if (direction.equals("up")) {
	     currentFrame = climbUp.getKeyFrame(stateTime, true);
	 } else if (direction.equals("down")) {
	     currentFrame = climbDown.getKeyFrame(stateTime, true);
	 } else {
	     currentFrame = climbDown.getKeyFrame(stateTime, true);
	 }
	 batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	private void renderPushing(SpriteBatch batch) {
	 switch (direction) {
	     case "up": currentFrame = pushUp.getKeyFrame(stateTime, true); break;
	     case "down": currentFrame = pushDown.getKeyFrame(stateTime, true); break;
	     case "left": currentFrame = pushLeft.getKeyFrame(stateTime, true); break;
	     case "right": currentFrame = pushRight.getKeyFrame(stateTime, true); break;
	 }
	 batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
	}
	}
