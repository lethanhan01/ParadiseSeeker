package com.paradise_seeker.game.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Import các class từ LibGDX để xử lý input, hình ảnh, animation, và hình chữ nhật
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.paradise_seeker.game.entity.npc.NPC1;
import com.paradise_seeker.game.entity.object.Item;
// Import các kỹ năng người chơi
import com.paradise_seeker.game.entity.skill.*;
import com.paradise_seeker.game.map.GameMap;
//At the top of Player class

// Lớp Player đại diện cho nhân vật điều khiển được, kế thừa từ lớp Character
public class Player extends Character {
	// Added smoke animation
	// Thêm lớp Smoke để quản lý hiệu ứng khói khi dash
	// Lớp này lưu trữ vị trí và thời gian của khói
	private class Smoke {
        float x, y, stateTime;
        Smoke(float x, float y) { this.x = x; this.y = y; this.stateTime = 0f; }
    }
    private List<Smoke> smokes = new ArrayList<>();
    private Animation<TextureRegion> smokeAnim;
    public boolean showInteractMessage = false;
    
	private float speedMultiplier = 1f;         // Hệ số tốc độ khi đi qua object
	private Vector2 lastPosition = new Vector2(); // Ghi nhớ vị trí trước khi di chuyển

	public static final int MAX_HP = 1000; // Máu tối đa
	public static final int MAX_MP = 100;  // Mana tối đa
    public PlayerSkill playerSkill1; // Kỹ năng 1 của người chơi
    public PlayerSkill playerSkill2; // Kỹ năng 2 của người chơi
    public Weapon weapon;            // Vũ khí đang dùng
 // Thêm biến để theo dõi NPC gần nhất
    private NPC1 nearestNPC = null;

    
    public ArrayList<Item> inventory = new ArrayList<>(); // Kho đồ của người chơi
    public int inventorySize = 18; // Kích thước kho đồ

    // Các animation cho nhân vật di chuyển theo các hướng
    private Animation<TextureRegion> runUp, runDown, runLeft, runRight;
    // Các animation cho hành động tấn công theo hướng
    private Animation<TextureRegion> attackUp, attackDown, attackLeft, attackRight;
    // Các animation cho trạng thái đứng yên
    private Animation<TextureRegion> idleUp, idleDown, idleLeft, idleRight;

    private TextureRegion currentFrame; // Frame hiện tại đang được vẽ
    public float stateTime = 0f;       // Thời gian trôi qua để cập nhật animation
    public String direction = "down";  // Hướng hiện tại của nhân vật
    public  boolean isMoving = false;   // Trạng thái đang di chuyển
    public boolean isAttacking = false; // Trạng thái đang tấn công

    private boolean isDashing = false;  // Trạng thái đang dash
    private float dashCooldown = 0f;  // Thời gian hồi chiêu dash
    private float dashTimer = 0f;       // Thời gian còn lại cho dash
    private float dashDistance = 2f;   // Khoảng cách dash

    boolean isShielding = false; // Trạng thái đang giơ khiên
    private boolean menuOpen = false;    // Menu đang mở
    private boolean isPaused = false;    // Trò chơi đang bị tạm dừng

    // Các hình ảnh cho nhân vật khi đang giơ khiên ở các hướng
    private Texture shieldDown, shieldUp, shieldLeft, shieldRight;

    // Thêm biến climbing
    private boolean isClimbing = false; // Trạng thái đang leo trèo
    private Animation<TextureRegion> climbUp, climbDown, climbUpAfter, climbDownBefore;

    // Thêm biến death
    public boolean isDead = false;
    private Animation<TextureRegion> deathAnimation;

    // Thêm biến pushing
    private boolean isPushing = false;
    private Animation<TextureRegion> pushUp, pushDown, pushLeft, pushRight;

    // Thêm biến shielded hit
    public boolean isShieldedHit = true;
    private Animation<TextureRegion> shieldedHitUp, shieldedHitDown, shieldedHitLeft, shieldedHitRight;

    // Thêm biến hit
    public boolean isHit = false;
    private Animation<TextureRegion> hitUp, hitDown, hitLeft, hitRight;

    private GameMap gameMap;

    public void setGameMap(GameMap map) {
        this.gameMap = map;
    }

    // Hàm khởi tạo nhân vật với tọa độ khởi đầu
    private BitmapFont font;
    public Player(Rectangle bounds, BitmapFont font) {
        super(bounds, 1000, 100, 10, 5f); // existing code
        loadAnimations();
        this.playerSkill1 = new PlayerSkill(true);
        this.playerSkill2 = new PlayerSkill(false);
        this.font = font; // Save the font reference
    }
    // Load tất cả animation và texture của nhân vật
    private void loadAnimations() {
        // Load animation di chuyển
        runDown = loadAnimation("images/Entity/characters/player/char_run_down_anim_strip_6.png");
        runUp = loadAnimation("images/Entity/characters/player/char_run_up_anim_strip_6.png");
        runLeft = loadAnimation("images/Entity/characters/player/char_run_left_anim_strip_6.png");
        runRight = loadAnimation("images/Entity/characters/player/char_run_right_anim_strip_6.png");

        // Load animation đứng yên
        idleDown = loadAnimation("images/Entity/characters/player/char_idle_down_anim_strip_6.png");
        idleUp = loadAnimation("images/Entity/characters/player/char_idle_up_anim_strip_6.png");
        idleLeft = loadAnimation("images/Entity/characters/player/char_idle_left_anim_strip_6.png");
        idleRight = loadAnimation("images/Entity/characters/player/char_idle_right_anim_strip_6.png");

        // Load animation tấn công
        attackDown = loadAnimation("images/Entity/characters/player/attack_down_new.png");
        attackUp = loadAnimation("images/Entity/characters/player/attack_up_new.png");
        attackLeft = loadAnimation("images/Entity/characters/player/attack_left_new.png");
        attackRight = loadAnimation("images/Entity/characters/player/attack_right_new.png");

        // Load texture khi giơ khiên theo từng hướng
        shieldDown = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_down.png"));
        shieldUp = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_up.png"));
        shieldLeft = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_left.png"));
        shieldRight = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_right.png"));

        // Load climbing animations
        climbDown = loadAnimation("images/Entity/characters/player/char_climbing_down_anim_strip_6.png");
        climbDownBefore = loadAnimation("images/Entity/characters/player/char_climbing_down_before_anim_strip_3.png", 3);
        climbUp = loadAnimation("images/Entity/characters/player/char_climbing_up_anim_strip_6.png");
        climbUpAfter = loadAnimation("images/Entity/characters/player/char_climbing_up_after_anim_strip_3.png", 3);

        // Load pushing animations
        pushUp = loadAnimation("images/Entity/characters/player/char_pushing_up_anim_strip_6.png");
        pushDown = loadAnimation("images/Entity/characters/player/char_pushing_down_anim_strip_6.png");
        pushLeft = loadAnimation("images/Entity/characters/player/char_pushing_left_anim_strip_6.png");
        pushRight = loadAnimation("images/Entity/characters/player/char_pushing_right_anim_strip_6.png");

        // Load shielded hit animations
        shieldedHitUp = loadAnimation("images/Entity/characters/player/char_shielded_hit_up_anim_strip_5.png", 5);
        shieldedHitDown = loadAnimation("images/Entity/characters/player/char_shielded_hit_down_anim_strip_5.png", 5);
        shieldedHitLeft = loadAnimation("images/Entity/characters/player/char_shielded_hit_left_anim_strip_5.png", 5);
        shieldedHitRight = loadAnimation("images/Entity/characters/player/char_shielded_hit_right_anim_strip_5.png", 5);

        // Load hit animations
        hitUp = loadAnimation("images/Entity/characters/player/char_hit_up_anim_strip_3.png", 3);
        hitDown = loadAnimation("images/Entity/characters/player/char_hit_down_anim_strip_3.png", 3);
        hitLeft = loadAnimation("images/Entity/characters/player/char_hit_left_anim_strip_3.png", 3);
        hitRight = loadAnimation("images/Entity/characters/player/char_hit_right_anim_strip_3.png", 3);

        // Load death animation
        deathAnimation = loadAnimation("images/Entity/characters/player/char_death_all_dir_anim_strip_10.png", 10);
        // Load smoke animation
        Texture smokeSheet = new Texture(Gdx.files.internal("images/spritesheet_smoke.png"));
        TextureRegion[] smokeFrames = TextureRegion.split(smokeSheet, smokeSheet.getWidth() / 6, smokeSheet.getHeight())[0];
        smokeAnim = new Animation<>(0.08f, smokeFrames);

        currentFrame = runDown.getKeyFrame(0); // Khung hình đầu tiên khi game khởi động
    }

    // Tạo một animation từ file ảnh (gồm 6 frame ngang)
    private Animation<TextureRegion> loadAnimation(String filePath) {
        Texture sheet = new Texture(Gdx.files.internal(filePath)); // Load sheet từ file
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 6, sheet.getHeight()); // Chia thành 6 frame
        return new Animation<>(0.1f, tmp[0]); // Animation 0.1s cho mỗi frame
    }

    // Overload loadAnimation cho climbing strip 3
    private Animation<TextureRegion> loadAnimation(String filePath, int frameCount) {
        Texture sheet = new Texture(Gdx.files.internal(filePath));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / frameCount, sheet.getHeight());
        return new Animation<>(0.1f, tmp[0]);
    }

    public void regenMana(float deltaTime) {
        if (mp < MAX_MP) {
            mp += 45 * deltaTime; // Regenerate 45 mana per deltaTime
        }
        if (mp > MAX_MP) {
            mp = MAX_MP; // Cap mana at MAX_MP
        }
        //System.out.println("Mana: " + mp + ", DeltaTime: " + deltaTime); // Debug log
    }


    // Cập nhật logic nhân vật mỗi frame
 // Trong update()
    public void update(float deltaTime) {
        if (isDead) return; // ✅ Không update nếu đã chết

        lastPosition.set(bounds.x, bounds.y);
        handleInput(deltaTime);
        regenMana(deltaTime);
        dashTimer -= deltaTime;
        speedMultiplier = 1f;

        if (isHit || isShieldedHit || isMoving || isAttacking) {
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

        isClimbing = false;
        Iterator<Smoke> iter = smokes.iterator();
        while (iter.hasNext()) {
            Smoke s = iter.next();
            s.stateTime += deltaTime;
            if (smokeAnim.isAnimationFinished(s.stateTime)) iter.remove();
        }
        if (gameMap != null) {
            nearestNPC = null; // Reset NPC near the player
            showInteractMessage = false; // Reset the message flag
            for (NPC1 npc : gameMap.getNPCs()) {
                float distance = Vector2.dst(
                    bounds.x + bounds.width / 2, bounds.y + bounds.height / 2,
                    npc.getBounds().x + npc.getBounds().width / 2, npc.getBounds().y + npc.getBounds().height / 2
                );
                if (distance <= 2.5f) { // If within interaction range
                    nearestNPC = npc;
                    npc.setTalking(true);
                    showInteractMessage = true; // Show the interaction message
                } else {
                    npc.setTalking(false);
                }
            }
        }

    }



    // Xử lý tất cả hành vi người chơi dựa trên phím bấm
    private void handleInput(float deltaTime) {
        if (isPaused || isAttacking|| isDead) return; // Nếu đang pause hoặc tấn công thì bỏ qua

        handleMovement(deltaTime); // Xử lý di chuyển
        handleDash();              // Xử lý dash
        handleAttack();           // Xử lý tấn công
        handleShield();           // Xử lý giơ khiên
        handleSkills();           // Xử lý kỹ năng
     // ✅ Hồi đầy máu và mana khi nhấn phím R
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            this.hp = MAX_HP;
            this.mp = MAX_MP;
        }
    }

    // Xử lý di chuyển nhân vật bằng phím WASD hoặc phím mũi tên
    private void handleMovement(float deltaTime) {
        float dx = 0, dy = 0; // Hướng di chuyển
        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && nearestNPC != null) {
            nearestNPC.openChest();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        isMoving = len > 0;

        // --- Always update direction if input is held ---
        if (len > 0) {
            if (Math.abs(dx) > Math.abs(dy)) {
                direction = dx > 0 ? "right" : "left";
            } else if (Math.abs(dy) > 0) {
                direction = dy > 0 ? "up" : "down";
            }
        }

        // --- Only move if not blocked ---
        if (isMoving) {
            float moveX = (dx / len) * speed * speedMultiplier * deltaTime;
            float moveY = (dy / len) * speed * speedMultiplier * deltaTime;

            float nextX = bounds.x + moveX;
            float nextY = bounds.y + moveY;
            Rectangle nextBounds = new Rectangle(nextX, nextY, bounds.width, bounds.height);

            if (gameMap == null || !gameMap.isBlocked(nextBounds)) {
                bounds.x = nextX;
                bounds.y = nextY;
            }
            clampToMapBounds();

        }
    }


    private void handleDash() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashTimer <= 0 && isMoving) {
            float dx = 0, dy = 0;

            // Xác định hướng dash
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

            float len = (float) Math.sqrt(dx * dx + dy * dy);

            if (len > 0) {
                float stepSize = 0.1f; // Step size per mini-move (smaller = more precise, larger = faster)
                float totalDash = 0f;
                float maxDash = dashDistance;
                float prevX = bounds.x;
                float prevY = bounds.y;

                // Try to move in increments until hit something or finished full dash
                while (totalDash < maxDash) {
                    float nextX = bounds.x + (dx / len) * stepSize;
                    float nextY = bounds.y + (dy / len) * stepSize;
                    Rectangle nextBounds = new Rectangle(nextX, nextY, bounds.width, bounds.height);

                    if (gameMap == null || !gameMap.isBlocked(nextBounds)) {
                        bounds.x = nextX;
                        bounds.y = nextY;
                        totalDash += stepSize;
                    } else {
                        // Hit solid, stop dash here
                        break;
                    }
                }
                dashTimer = dashCooldown;
                smokes.add(new Smoke(prevX, prevY));
            }
            clampToMapBounds();

        }
    }

    private void clampToMapBounds() {
        // Ensure the player's rectangle stays inside the map boundaries
        if (gameMap == null) return;
        float minX = 0;
        float minY = 0;
        float maxX = gameMap.getMapWidth() - bounds.width;
        float maxY = gameMap.getMapHeight() - bounds.height;

        bounds.x = Math.max(minX, Math.min(bounds.x, maxX));
        bounds.y = Math.max(minY, Math.min(bounds.y, maxY));
    }


    // Bắt đầu tấn công nếu nhấn phím Space
    private void handleAttack() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            isAttacking = true;
            stateTime = 0;

            if (gameMap != null) {
                float centerX = bounds.x + bounds.width / 2;
                float centerY = bounds.y + bounds.height / 2;
                gameMap.damageMonstersInRange(centerX, centerY, 5f, atk); // ✅ Tấn công quái trong phạm vi 5
            }
        }
    }


    // Giơ khiên khi nhấn phím K, đồng thời hồi máu
    private void handleShield() {
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            if (!isShielding) {
                isShielding = true;
            }
        } else {
            isShielding = false; // Ngưng giơ khiên nếu thả phím
            isShieldedHit = false; // Reset trạng thái shielded hit khi thả phím K
        }
    }

    // Lấy animation tấn công theo hướng hiện tại
    private Animation<TextureRegion> getAttackAnimationByDirection() {
        switch (direction) {
            case "up": return attackUp;
            case "down": return attackDown;
            case "left": return attackLeft;
            case "right": return attackRight;
        }
        return attackDown;
    }

    // Lấy animation đứng yên theo hướng hiện tại
    private Animation<TextureRegion> getIdleAnimationByDirection() {
        switch (direction) {
            case "up": return idleUp;
            case "down": return idleDown;
            case "left": return idleLeft;
            case "right": return idleRight;
        }
        return idleDown;
    }

    // Render nhân vật mỗi frame
    @Override
    public void render(SpriteBatch batch) {
        if (isDead) {
            renderDeath(batch);
            return;
        }
        if (isShielding && isShieldedHit) {
            renderShieldedHit(batch);
            return;
        }
        if (isHit) {
            renderHit(batch);
            return;
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
        for (Smoke s : smokes) {
            TextureRegion frame = smokeAnim.getKeyFrame(s.stateTime, false);
            batch.draw(frame, s.x, s.y, bounds.width, bounds.height);
        }
        


    }

    // Vẽ nhân vật đang giơ khiên
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

    // Vẽ animation tấn công với hiệu ứng phóng to
    private void renderAttack(SpriteBatch batch) {
        currentFrame = getAttackAnimationByDirection().getKeyFrame(stateTime, false);
        float scaledWidth = bounds.width * 3.f;
        float scaledHeight = bounds.height * 3.f;
        float drawX = bounds.x - (scaledWidth - bounds.width) / 2f;
        float drawY = bounds.y - (scaledHeight - bounds.height) / 2f;
        batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
    }

    // Vẽ nhân vật khi đang di chuyển
    private void renderMovement(SpriteBatch batch) {
        switch (direction) {
            case "up": currentFrame = runUp.getKeyFrame(stateTime, true); break;
            case "down": currentFrame = runDown.getKeyFrame(stateTime, true); break;
            case "left": currentFrame = runLeft.getKeyFrame(stateTime, true); break;
            case "right": currentFrame = runRight.getKeyFrame(stateTime, true); break;
        }
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    // Vẽ nhân vật khi đứng yên (idle) với hiệu ứng phóng to giống attack
    private void renderIdle(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = getIdleAnimationByDirection().getKeyFrame(stateTime, true);
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    // Thêm hàm renderClimbing
    private void renderClimbing(SpriteBatch batch) {
        // Chọn animation climbing phù hợp
        if (direction.equals("up")) {
            currentFrame = climbUp.getKeyFrame(stateTime, true);
        } else if (direction.equals("down")) {
            currentFrame = climbDown.getKeyFrame(stateTime, true);
        } else {
            // Nếu cần, có thể dùng climbUpAfter hoặc climbDownBefore cho các trạng thái đặc biệt
            currentFrame = climbDown.getKeyFrame(stateTime, true);
        }
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    // Thêm hàm renderDeath
    private void renderDeath(SpriteBatch batch) {
        currentFrame = deathAnimation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    // Thêm hàm renderPushing
    private void renderPushing(SpriteBatch batch) {
        switch (direction) {
            case "up": currentFrame = pushUp.getKeyFrame(stateTime, true); break;
            case "down": currentFrame = pushDown.getKeyFrame(stateTime, true); break;
            case "left": currentFrame = pushLeft.getKeyFrame(stateTime, true); break;
            case "right": currentFrame = pushRight.getKeyFrame(stateTime, true); break;
        }
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    // Thêm hàm renderHit với hiệu ứng phóng to giống attack
    private void renderHit(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        switch (direction) {
            case "up": currentFrame = hitUp.getKeyFrame(stateTime, false); break;
            case "down": currentFrame = hitDown.getKeyFrame(stateTime, false); break;
            case "left": currentFrame = hitLeft.getKeyFrame(stateTime, false); break;
            case "right": currentFrame = hitRight.getKeyFrame(stateTime, false); break;
        }
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        // Đặt lại isHit khi animation kết thúc
        if (hitUp.isAnimationFinished(stateTime) || hitDown.isAnimationFinished(stateTime) ||
            hitLeft.isAnimationFinished(stateTime) || hitRight.isAnimationFinished(stateTime)) {
            isHit = false;
            stateTime = 0;
        }
    }

    // Thêm hàm renderShieldedHit với hiệu ứng phóng to giống attack
    private void renderShieldedHit(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        switch (direction) {
            case "up": currentFrame = shieldedHitUp.getKeyFrame(stateTime, false); break;
            case "down": currentFrame = shieldedHitDown.getKeyFrame(stateTime, false); break;
            case "left": currentFrame = shieldedHitLeft.getKeyFrame(stateTime, false); break;
            case "right": currentFrame = shieldedHitRight.getKeyFrame(stateTime, false); break;
        }
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        // Đặt lại isShieldedHit khi animation kết thúc
        if (shieldedHitUp.isAnimationFinished(stateTime) || shieldedHitDown.isAnimationFinished(stateTime) ||
            shieldedHitLeft.isAnimationFinished(stateTime) || shieldedHitRight.isAnimationFinished(stateTime)) {
            isShieldedHit = false;
            stateTime = 0;
        }
    }

    // Override nhưng chưa dùng
    @Override public void move() {}
    @Override public void onDeath() {
      isDead = true;
       stateTime = 0;
    //	hp=MAX_HP;
   // 	mp=MAX_MP;
    }
    @Override public void onCollision(Collidable other) {
        // Khi nhân vật bị đánh, set isShieldedHit=true nếu đang giơ khiên, ngược lại set isHit=true
        if (isShielding) {
            isShieldedHit = true;
        } else {
            isHit = true;
            stateTime = 0;
        }
    }

    // Thi triển kỹ năng 1 nếu đủ mana
    public void castSkill1(int x, int y) {
        if (mp >= playerSkill1.getManaCost()) {
            mp -= playerSkill1.getManaCost();
            playerSkill1.castSkill(atk, x, y);
        }
    }

    // Thi triển kỹ năng 2 nếu đủ mana
    public void castSkill2(Character target) {
        if (mp >= playerSkill2.getManaCost()) {
            mp -= playerSkill2.getManaCost();
            playerSkill2.castSkill(atk, target);
        }
    }

    // Trả về trạng thái pause và menu
    public boolean isPaused() { return isPaused; }
    public boolean isMenuOpen() { return menuOpen; }

    public void takeDamage(int damage) {
        if (isShielding) {
            damage /= 2;
        }

        hp = Math.max(0, hp - damage);

        if (hp == 0 && !isDead) {
            onDeath();
        } else {
            // ✅ Tự động bật hiệu ứng bị đánh
            isHit = true;
            stateTime = 0;
        }
    }



    public void setSpeedMultiplier(float multiplier) {
        this.speedMultiplier = multiplier;
    }

    public void blockMovement() {
        bounds.x = lastPosition.x;
        bounds.y = lastPosition.y;
    }
    public void setClimbing(boolean climbing) {
        this.isClimbing = climbing;
    }

    // Xử lý kỹ năng khi nhấn phím U và I
    private void handleSkills() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            if (mp >= 2) {
                mp -= 2;
                playerSkill1.castSkill(atk, bounds, direction);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (mp >= 2) {
                mp -= 2;
                playerSkill2.castSkill(atk, bounds, direction);
            }
        }
    }
    public void pushBackFrom(Rectangle source) {
        float dx = bounds.x - source.x;
        float dy = bounds.y - source.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) return;

        // Đẩy nhẹ ra xa khỏi quái
        float pushAmount = 0.1f;
        bounds.x += (dx / distance) * pushAmount;
        bounds.y += (dy / distance) * pushAmount;
    }
    public void addItemToInventory(Item newItem) {
        if (newItem == null || !newItem.isActive()) return;

        // Nếu item có thể stack, kiểm tra xem đã có trong inventory chưa
        if (newItem.isStackable()) {
            for (Item existingItem : inventory) {
                if (existingItem.canStackWith(newItem)) {
                    int total = existingItem.getCount() + newItem.getCount();
                    if (total <= existingItem.getMaxStackSize()) {
                        existingItem.setCount(total);
                        newItem.setActive(false);
                        return;
                    } else {
                        int remaining = total - existingItem.getMaxStackSize();
                        existingItem.setCount(existingItem.getMaxStackSize());
                        newItem.setCount(remaining);
                    }
                }
            }
        }

        // Nếu không stack được hoặc inventory còn chỗ
        if (inventory.size() < inventorySize) {
            inventory.add(newItem);
            newItem.setActive(false);
        } else {
            // Inventory đầy, có thể thông báo cho người chơi
            System.out.println("Inventory is full!");
        }
    }
}
