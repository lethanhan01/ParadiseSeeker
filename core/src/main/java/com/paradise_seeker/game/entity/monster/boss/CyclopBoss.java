package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;
import com.paradise_seeker.game.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;

public class CyclopBoss extends Monster {
    private boolean cleaveTurn = true; // true: cleave, false: skill
    private Animation<TextureRegion> cleaveRightAnim, cleaveLeftAnim;
    private Animation<TextureRegion> skillRightAnim, skillLeftAnim;
    private Animation<TextureRegion> skillProjectileRightAnim, skillProjectileLeftAnim;
    private ArrayList<SkillProjectile> skillProjectiles = new ArrayList<>();
    private float skillDamage = 24f;
    private float skillProjectileSpeed = 5.2f;

    public CyclopBoss(float x, float y) {
        super(x, y, 220, 1.6f, 30, 0f);
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 6f;
        this.spriteHeight = 6f;
        updateBounds();

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 4.0f;
        updateBounds();
    }

    @Override
    protected float getScaleMultiplier() {
        return 8f;
    }

    @Override
    protected void loadAnimations() {
        // WALK
        walkRight = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/walk/phai/cyclop", 15, 26);
        walkLeft = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/walk/trai/cyclop", 165, 176);

        // IDLE
        idleRight = loadIdleAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/idle/phai/");
        idleLeft = loadIdleAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/idle/trai/");

        // TAKE HIT
        takeHitRight = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/takehit/phai/cyclop", 75, 79);
        takeHitLeft = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/takehit/trai/cyclop", 225, 229);

        // DEATH
        deathRight = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/death/phai/cyclop", 90, 98);
        deathLeft = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/death/trai/cyclop", 240, 248);

        // CLEAVE
        cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/cleave/phai/cyclop", 45, 57);
        cleaveLeftAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/cleave/trai/cyclop", 195, 207);

        // SKILL
        skillRightAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/skill/phai/cyclop", 120, 125);
        skillLeftAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/skill/trai/cyclop", 270, 275);

        // SKILL PROJECTILE
        skillProjectileRightAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/skill/phai/projectile/cyclop", 135, 142);
        skillProjectileLeftAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/skill/trai/projectile/cyclop", 285, 292);
    }

    // Hàm chuyển cleave xen kẽ cleave và skill. Nếu skill thì bắn projectile về phía player.
    public void switchCleaveTypeAndCastSkill(Player player) {
        cleaveTurn = !cleaveTurn;
        if (cleaveTurn) {
            cleaveRight = cleaveRightAnim;
            cleaveLeft = cleaveLeftAnim;
        } else {
            cleaveRight = skillRightAnim;
            cleaveLeft = skillLeftAnim;
            castSkillProjectile(player);
        }
    }

    // Bắn skill projectile về phía player
    private void castSkillProjectile(Player player) {
        boolean facingRight = this.facingRight;
        Vector2 start = new Vector2(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
        Vector2 target = new Vector2(player.getBounds().x + player.getBounds().width / 2, player.getBounds().y + player.getBounds().height / 2);
        skillProjectiles.add(new SkillProjectile(start, target, facingRight));
    }

    // Cập nhật các projectile
    public void updateSkillProjectiles(Player player, float delta) {
        Iterator<SkillProjectile> it = skillProjectiles.iterator();
        while (it.hasNext()) {
            SkillProjectile spell = it.next();
            spell.update(delta);

            if (spell.getRect().overlaps(player.getBounds())) {
                player.takeDamage((int)skillDamage);
                it.remove();
            }
        }
    }

    // Vẽ các projectile
    public void renderSkillProjectiles(SpriteBatch batch, float stateTime) {
        for (SkillProjectile spell : skillProjectiles) {
            if (spell.facingRight) {
                spell.render(batch, skillProjectileRightAnim, stateTime);
            } else {
                spell.render(batch, skillProjectileLeftAnim, stateTime);
            }
        }
    }

    // --- Animation loader helpers ---
    private Animation<TextureRegion> loadAnimation(String prefix, int from, int to) {
        int frameCount = to - from + 1;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = prefix + (from + i) + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    // Idle của cyclop có các số lẻ: 0-14, nhưng bị lộn xộn nên phải hard-code list (hoặc sort lại nếu thích)
    private Animation<TextureRegion> loadIdleAnimation(String folder) {
        // phai: cyclop0, 1, 2, ..., 14; trai: cyclop150, 151,...,164
        String[] rightFrames = {
            "cyclop0.png", "cyclop1.png", "cyclop2.png", "cyclop3.png", "cyclop4.png",
            "cyclop5.png", "cyclop6.png", "cyclop7.png", "cyclop8.png", "cyclop9.png",
            "cyclop10.png", "cyclop11.png", "cyclop12.png", "cyclop13.png", "cyclop14.png"
        };
        String[] leftFrames = {
            "cyclop150.png", "cyclop151.png", "cyclop152.png", "cyclop153.png", "cyclop154.png",
            "cyclop155.png", "cyclop156.png", "cyclop157.png", "cyclop158.png", "cyclop159.png",
            "cyclop160.png", "cyclop161.png", "cyclop162.png", "cyclop163.png", "cyclop164.png"
        };
        String[] useFrames = folder.contains("phai") ? rightFrames : leftFrames;
        TextureRegion[] frames = new TextureRegion[useFrames.length];
        for (int i = 0; i < useFrames.length; i++) {
            String filename = folder + useFrames[i];
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.14f, frames);
    }

    // --- Nội bộ: class skill projectile ---
    private class SkillProjectile {
        private Vector2 pos;
        private Vector2 velocity;
        private Rectangle rect;
        private float elapsed = 0f;
        public boolean facingRight;

        public SkillProjectile(Vector2 start, Vector2 target, boolean facingRight) {
            this.pos = new Vector2(start);
            Vector2 dir = target.cpy().sub(start).nor();
            this.velocity = dir.scl(skillProjectileSpeed);
            this.facingRight = facingRight;
            this.rect = new Rectangle(pos.x - 1.0f, pos.y - 1.0f, 2.0f, 2.0f);
        }

        public void update(float delta) {
            pos.mulAdd(velocity, delta);
            rect.setPosition(pos.x - rect.width / 2, pos.y - rect.height / 2);
            elapsed += delta;
        }

        public Rectangle getRect() {
            return rect;
        }

        public void render(SpriteBatch batch, Animation<TextureRegion> anim, float stateTime) {
            TextureRegion frame = anim.getKeyFrame(elapsed, true);
            batch.draw(frame, pos.x - rect.width/2, pos.y - rect.height/2, rect.width, rect.height);
        }
    }

    // --- Render boss ---
    @Override
    public void render(SpriteBatch batch) {
        if (isDead) return;
        super.render(batch);
    }
}
