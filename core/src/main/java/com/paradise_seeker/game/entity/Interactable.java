package com.paradise_seeker.game.entity;

/**
 * Giao diện cho các đối tượng có thể tương tác trong game (ví dụ: NPC, rương, bảng chỉ dẫn).
 */
public interface Interactable {

    /**
     *
     * @param player đối tượng người chơi đang tương tác
     */
    void interact(Player player);
}
