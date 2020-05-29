package net.VytskaLT.BossBarAPI.bar;

import org.bukkit.entity.Player;

public interface Bossbar {

    /**
     * Returns the player that has this bossbar.
     * @return player that has this bossbar.
     */
    Player getPlayer();

    /**
     * Gets the current message of this bossbar.
     * @return current message of this bossbar.
     */
    String getMessage();

    /**
     * Sets the message of the bossbar.
     * @param message new bossbar message.
     */
    void setMessage(String message);

    /**
     * Gets the current percentage of this bossbar.
     * @return current percentage of this bossbar.
     */
    int getPercentage();

    /**
     * Sets the percentage of this bossbar.
     * @param percentage new bossbar percentage.
     */
    void setPercentage(int percentage);

    /**
     * Gets whether this bossbar is currently visible.
     * @return true if bossbar is visible, false if not.
     */
    boolean isVisible();

    /**
     * Sets the visibility of this bossbar.
     * @param visible whether the bossbar should be visible.
     */
    void setVisible(boolean visible);

    /**
     * Destroys this bossbar.
     */
    void destroy();

    /**
     * Returns whether this bossbar is destroyed.
     * @return true if this bossbar is destroyed, false if not.
     */
    boolean isDestroyed();
}
