package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NPC1 implements Collidable {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> talkAnimation;
    private Animation<TextureRegion> openChestAnimation;
    private Animation<TextureRegion> chestOpenedAnimation;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion currentFrame;
    

    private float stateTime;

    private Rectangle bounds;
    private float spriteWidth = 3f;
    private float spriteHeight = 3f;

    private boolean isChestOpened = false;
    private boolean isOpeningChest = false;
    private boolean isTalking = false;
    private boolean hasTalked = false;

    public boolean hasTalked() {
        return hasTalked;
    }

    public void setHasTalked(boolean value) {
        this.hasTalked = value;
    }
    public NPC1(float x, float y) {
        loadIdleAnimation();
        loadTalkAnimation();
        loadOpenChestAnimation();
        loadChestOpenedAnimation();

        currentAnimation = idleAnimation;
        currentFrame = currentAnimation.getKeyFrame(0f);
        stateTime = 0f;
        bounds = new Rectangle(x + 0.2f, y + 0.2f, 2.6f, 2.6f);
    }

    private void loadIdleAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 120; i <= 130; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act3/npc" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            frames.add(new TextureRegion(texture));
        }
        idleAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void loadTalkAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 10; i <= 19; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act1/npc" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            frames.add(new TextureRegion(texture));
        }
        talkAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        talkAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }
    public boolean isChestOpened() {
        return isChestOpened;
    }

    private void loadOpenChestAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 131; i <= 137; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act4/npc" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            frames.add(new TextureRegion(texture));
        }
        openChestAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        openChestAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    private void loadChestOpenedAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 140; i <= 145; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act5/npc" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            frames.add(new TextureRegion(texture));
        }
        chestOpenedAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        chestOpenedAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float deltaTime) {
        if (isChestOpened) {
            stateTime += deltaTime;
            currentFrame = currentAnimation.getKeyFrame(stateTime);
            return;
        }

        stateTime += deltaTime;
        currentFrame = currentAnimation.getKeyFrame(stateTime);

        if (isOpeningChest && openChestAnimation.isAnimationFinished(stateTime)) {
            currentAnimation = chestOpenedAnimation;
            stateTime = 0f;
            isChestOpened = true;
        }
    }


    // Hàm để Player gọi khi va chạm
    public void setTalking(boolean talking) {
        if (isChestOpened || isOpeningChest) return; // Đang mở rương hoặc đã mở rương thì không đổi animation nữa
        if (isTalking != talking) {
            isTalking = talking;
            currentAnimation = isTalking ? talkAnimation : idleAnimation;
            stateTime = 0f;
        }
    }
    
 // Thêm vào NPC1
    public void openChest() {
        if (isChestOpened || isOpeningChest) return;
        currentAnimation = openChestAnimation;
        stateTime = 0f;
        isOpeningChest = true;
        isChestOpened = true; // ✅ thêm dòng này
    }


    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, bounds.x, bounds.y, spriteWidth, spriteHeight);
    }

    public Rectangle getBounds() {
        return bounds;
    }
    public boolean shouldShowOptions() {
        return getCurrentLineIndex() == 2; // Câu số 3
    }

	@Override
	public void onCollision(Player player) {
		// TODO Auto-generated method stub
		
	}
	// ======================= HỘP THOẠI =========================
	private List<String> dialogueLines = new ArrayList<>();
	private int currentLineIndex = 0;

	public void setDialogue(List<String> lines) {
	    this.dialogueLines = lines;
	    this.currentLineIndex = 0;
	}

	public String getCurrentLine() {
	    if (dialogueLines.isEmpty()) return "";
	    return dialogueLines.get(currentLineIndex);
	}

	public boolean hasNextLine() {
	    return currentLineIndex < dialogueLines.size() - 1;
	}

	public void nextLine() {
	    if (hasNextLine()) currentLineIndex++;
	}

	public void resetDialogue() {
	    currentLineIndex = 0;
	}
	public int getCurrentLineIndex() {
	    return currentLineIndex;
	}
}
