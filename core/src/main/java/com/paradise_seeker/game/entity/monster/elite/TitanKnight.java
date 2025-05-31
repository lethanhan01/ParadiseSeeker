package com.paradise_seeker.game.entity.monster.elite;

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

public class TitanKnight extends Monster {

    // --- Animation fields ---
    private boolean cleaveTurn = true;
    private Animation<TextureRegion> cleaveAnim, spellAnim;
    private Animation<TextureRegion> fireSpellAnim;
    private ArrayList<FireSpell> fireSpells = new ArrayList<>();

    private float spellDamage = 20f; // Damage khi spell trúng player
    private float fireSpellSpeed = 5f; // Tốc độ spell bay

    public TitanKnight(float x, float y) {
        super(x, y, 100, 2.4f, 20, 0f); // HP, speed, cleaveDamage, offset
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 2.5f;
        this.spriteHeight = 2.5f;
        updateBounds();

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 2.5f;
        updateBounds();
    }

    @Override
    protected float getScaleMultiplier() {
        return 2.4f;
    }

    @Override
    protected void loadAnimations() {
        // WALK (RUN)
        walkRight = loadAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/run/run", 8);
        walkLeft = walkRight;

        // IDLE
        idleRight = loadAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/idle/idle", 8);
        idleLeft = idleRight;

        // HIT (TAKEHIT) = HURT
        takeHitRight = loadAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/hurt/hurt", 5);
        takeHitLeft = takeHitRight;

        // CLEAVE = ảnh trong folder cleave (gồm 26 frame, atk1 & atk2)
        cleaveAnim = loadCleaveAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/cleave/");
        // SPELL = spell1.png ... spell17.png
        spellAnim = loadSpellAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/spell/spell", 17);

        cleaveRight = cleaveAnim;
        cleaveLeft = cleaveAnim;

        // DEATH
        deathRight = loadAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/death/death", 9);
        deathLeft = deathRight;

        // Spell projectile
        fireSpellAnim = loadFireSpellAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/fire_spell/firespell", 28);
    }

    // --- Logic: mỗi lần cast cleave gọi hàm này để chuyển xen kẽ cleave <-> spell ---
    public void switchCleaveTypeAndCastSpell(Player player) {
        cleaveTurn = !cleaveTurn;
        if (cleaveTurn) {
            cleaveRight = cleaveAnim;
            cleaveLeft = cleaveAnim;
        } else {
            cleaveRight = spellAnim;
            cleaveLeft = spellAnim;
            castFireSpell(player); // Khi sang spell, bắn spell về phía player
        }
    }

    // Tạo spell projectile bay đến vị trí player tại thời điểm cast
    private void castFireSpell(Player player) {
        Vector2 start = new Vector2(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
        Vector2 target = new Vector2(player.getBounds().x + player.getBounds().width / 2, player.getBounds().y + player.getBounds().height / 2);
        fireSpells.add(new FireSpell(start, target));
    }

    // Gọi từ GameMap hoặc AI mỗi frame để update spell (di chuyển, va chạm, gây damage)
    public void updateFireSpells(Player player, float delta) {
        Iterator<FireSpell> it = fireSpells.iterator();
        while (it.hasNext()) {
            FireSpell spell = it.next();
            spell.update(delta);

            // Nếu spell chạm player
            if (spell.getRect().overlaps(player.getBounds())) {
                player.takeDamage((int)spellDamage);
                it.remove();
            }
            // Nếu muốn: remove nếu spell bay ra khỏi vùng chơi, hoặc hết thời gian tồn tại
        }
    }

    // Render spell
    public void renderFireSpells(SpriteBatch batch, float stateTime) {
        for (FireSpell spell : fireSpells) {
            spell.render(batch, fireSpellAnim, stateTime);
        }
    }

    // --- Animation loader helper ---
    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = basePath + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i - 1] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    // Load cleave: gộp cả atk1 (1->13), atk2 (1->13) trong folder cleave
    private Animation<TextureRegion> loadCleaveAnimation(String folder) {
        int atkCount = 2;
        int framePerAtk = 13;
        TextureRegion[] frames = new TextureRegion[atkCount * framePerAtk];
        int idx = 0;
        for (int atk = 1; atk <= atkCount; atk++) {
            for (int frame = 1; frame <= framePerAtk; frame++) {
                String filename = folder + "atk" + atk + " (" + frame + ").png";
                Texture texture = new Texture(Gdx.files.internal(filename));
                frames[idx++] = new TextureRegion(texture);
            }
        }
        return new Animation<>(0.11f, frames);
    }

    // Load spell: spell1.png ... spell17.png
    private Animation<TextureRegion> loadSpellAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = basePath + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i - 1] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    // Load fire_spell: firespell0.png ... firespell27.png
    private Animation<TextureRegion> loadFireSpellAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.08f, frames);
    }

    // --- Nội bộ: class spell ---
    private class FireSpell {
        private Vector2 pos;
        private Vector2 velocity;
        private Rectangle rect;
        private float elapsed = 0f;

        public FireSpell(Vector2 start, Vector2 target) {
            this.pos = new Vector2(start);
            Vector2 dir = target.cpy().sub(start).nor();
            this.velocity = dir.scl(fireSpellSpeed);
            this.rect = new Rectangle(pos.x - 0.4f, pos.y - 0.4f, 0.8f, 0.8f); // Spell hitbox
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

    @Override
    public void render(SpriteBatch batch) {
        if (isDead) return;
        super.render(batch);
    }
}
