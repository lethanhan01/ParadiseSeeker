package com.paradise_seeker.game.entity;

// Import các class từ LibGDX để xử lý input, hình ảnh, animation, và hình chữ nhật
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
// Import các kỹ năng người chơi
import com.paradise_seeker.game.entity.skill.*;

// Lớp Player đại diện cho nhân vật điều khiển được, kế thừa từ lớp Character
public class Player extends Character {
    public PlayerSkill playerSkill1; // Kỹ năng 1 của người chơi
    public PlayerSkill playerSkill2; // Kỹ năng 2 của người chơi
    public Weapon weapon;            // Vũ khí đang dùng

    // Các animation cho nhân vật di chuyển theo các hướng
    private Animation<TextureRegion> runUp, runDown, runLeft, runRight;
    // Các animation cho hành động tấn công theo hướng
    private Animation<TextureRegion> attackUp, attackDown, attackLeft, attackRight;
    // Các animation cho trạng thái đứng yên
    private Animation<TextureRegion> idleUp, idleDown, idleLeft, idleRight;

    private TextureRegion currentFrame; // Frame hiện tại đang được vẽ
    private float stateTime = 0f;       // Thời gian trôi qua để cập nhật animation
    private String direction = "down";  // Hướng hiện tại của nhân vật
    private boolean isMoving = false;   // Trạng thái đang di chuyển
    private boolean isAttacking = false; // Trạng thái đang tấn công

    private boolean isDashing = false;  // Trạng thái đang dash
    private float dashCooldown = 1.0f;  // Thời gian hồi chiêu dash
    private float dashTimer = 0f;       // Thời gian còn lại cho dash
    private float dashDistance = 50f;   // Khoảng cách dash

    private boolean isShielding = false; // Trạng thái đang giơ khiên
    private boolean menuOpen = false;    // Menu đang mở
    private boolean isPaused = false;    // Trò chơi đang bị tạm dừng

    // Các hình ảnh cho nhân vật khi đang giơ khiên ở các hướng
    private Texture shieldDown, shieldUp, shieldLeft, shieldRight;

    // Thêm biến climbing
    private boolean isClimbing = false; // Trạng thái đang leo trèo
    private Animation<TextureRegion> climbUp, climbDown, climbUpAfter, climbDownBefore;

    // Thêm biến death
    private boolean isDead = false;
    private Animation<TextureRegion> deathAnimation;

    // Thêm biến pushing
    private boolean isPushing = false;
    private Animation<TextureRegion> pushUp, pushDown, pushLeft, pushRight;

    // Thêm biến shielded hit
    private boolean isShieldedHit = true;
    private Animation<TextureRegion> shieldedHitUp, shieldedHitDown, shieldedHitLeft, shieldedHitRight;

    // Thêm biến hit
    private boolean isHit = false;
    private Animation<TextureRegion> hitUp, hitDown, hitLeft, hitRight;

    // Hàm khởi tạo nhân vật với tọa độ khởi đầu
    public Player(Rectangle bounds) {
        super(bounds, 100, 50, 10, 5f); // Gọi constructor của Character: (bounds, hp, mp, atk, speed)
        loadAnimations();               // Load các animation cho nhân vật
        this.playerSkill1 = new PlayerSkill(); // Khởi tạo kỹ năng 1
        this.playerSkill2 = new PlayerSkill(); // Khởi tạo kỹ năng 2
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
        attackDown = loadAnimation("images/Entity/characters/player/char_attack_down_anim_strip_6.png");
        attackUp = loadAnimation("images/Entity/characters/player/char_attack_up_anim_strip_6.png");
        attackLeft = loadAnimation("images/Entity/characters/player/char_attack_left_anim_strip_6.png");
        attackRight = loadAnimation("images/Entity/characters/player/char_attack_right_anim_strip_6.png");

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

    // Hồi phục mana mỗi giây
    public void regenMana(float deltaTime) {
        if (mp < 100) {
            mp += 5 * deltaTime; // Hồi 5 mana mỗi giây
        }
    }

    // Cập nhật logic nhân vật mỗi frame
    public void update(float deltaTime) {
        handleInput(deltaTime);  // Xử lý input
        regenMana(deltaTime);    // Hồi mana
        dashTimer -= deltaTime;  // Giảm thời gian hồi dash

        // Cập nhật thời gian animation nếu đang di chuyển hoặc tấn công
        if (isMoving || isAttacking) {
            stateTime += deltaTime;
        } else {
            stateTime = 0;
        }

        // Kiểm tra nếu animation tấn công kết thúc
        if (isAttacking) {
            Animation<TextureRegion> currentAttack = getAttackAnimationByDirection();
            if (currentAttack.isAnimationFinished(stateTime)) {
                isAttacking = false;
                stateTime = 0;
            }
        }
    }

    // Xử lý tất cả hành vi người chơi dựa trên phím bấm
    private void handleInput(float deltaTime) {
        if (isPaused || isAttacking) return; // Nếu đang pause hoặc tấn công thì bỏ qua

        handleMovement(deltaTime); // Xử lý di chuyển
        handleDash();              // Xử lý dash
        handleAttack();           // Xử lý tấn công
        handleShield();           // Xử lý giơ khiên
    }

    // Xử lý di chuyển nhân vật bằng phím WASD hoặc phím mũi tên
    private void handleMovement(float deltaTime) {
        float dx = 0, dy = 0; // Hướng di chuyển

        // Xác định hướng dựa trên phím bấm
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        float len = (float) Math.sqrt(dx * dx + dy * dy); // Chuẩn hóa hướng di chuyển
        isMoving = len > 0;

        if (isMoving) {
            // Cập nhật tọa độ
            bounds.x += (dx / len) * speed * deltaTime;
            bounds.y += (dy / len) * speed * deltaTime;

            // Cập nhật hướng nhân vật
            if (Math.abs(dx) > Math.abs(dy)) {
                direction = dx > 0 ? "right" : "left";
            } else {
                direction = dy > 0 ? "up" : "down";
            }
        }
    }

    // Xử lý dash khi nhấn Shift và hết cooldown
    private void handleDash() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashTimer <= 0 && isMoving) {
            float dx = 0, dy = 0;

            // Xác định hướng
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

            float len = (float) Math.sqrt(dx * dx + dy * dy);
            if (len > 0) {
                bounds.x += (dx / len) * dashDistance;
                bounds.y += (dy / len) * dashDistance;
                dashTimer = dashCooldown; // Reset thời gian dash
            }
        }
    }

    // Bắt đầu tấn công nếu nhấn phím Space
    private void handleAttack() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            isAttacking = true;
            stateTime = 0;
        }
    }

    // Giơ khiên khi nhấn phím K, đồng thời hồi máu
    private void handleShield() {
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            if (!isShielding) {
                isShielding = true;
                hp = Math.min(hp + 10, 100); // Hồi máu khi giơ khiên
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
        float scaledWidth = bounds.width * 2.5f;
        float scaledHeight = bounds.height * 2.5f;
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
        float scaledWidth = bounds.width * 1.0f;
        float scaledHeight = bounds.height * 1.0f;
        float drawX = bounds.x - (scaledWidth - bounds.width) / 2f;
        float drawY = bounds.y - (scaledHeight - bounds.height) / 2f;
        batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
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
        float scaledWidth = bounds.width * 2.5f;
        float scaledHeight = bounds.height * 2.5f;
        float drawX = bounds.x - (scaledWidth - bounds.width) / 2f;
        float drawY = bounds.y - (scaledHeight - bounds.height) / 2f;
        batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
        // Reset isHit khi animation kết thúc
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
        float scaledWidth = bounds.width * 2.5f;
        float scaledHeight = bounds.height * 2.5f;
        float drawX = bounds.x - (scaledWidth - bounds.width) / 2f;
        float drawY = bounds.y - (scaledHeight - bounds.height) / 2f;
        batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
        // Reset isShieldedHit khi animation kết thúc
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
    
    public float getCenterX() { return bounds.x + bounds.width / 2; }
    public float getCenterY() { return bounds.y + bounds.height / 2; }

    // Trả về trạng thái pause và menu
    public boolean isPaused() { return isPaused; }
    public boolean isMenuOpen() { return menuOpen; }
    
}
