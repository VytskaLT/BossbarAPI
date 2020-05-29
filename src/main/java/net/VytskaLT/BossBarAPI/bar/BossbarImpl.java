package net.VytskaLT.BossBarAPI.bar;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.VytskaLT.BossBarAPI.BossBarAPI;
import net.VytskaLT.BossBarAPI.util.BarPacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BossbarImpl implements Bossbar {

    public final BossBarAPI plugin;
    @Getter
    public final Player player;
    public final int entityId = (int) (Math.random() * Integer.MAX_VALUE);
    public Location location;
    @Getter
    public boolean visible, destroyed;
    @Getter
    public String message;
    @Getter
    public int percentage = 100;
    public float yaw, pitch;

    @Override
    public void setMessage(String message) {
        Preconditions.checkNotNull(message);
        Preconditions.checkState(message.length() <= 64, "Message too long");
        checkDestroyed();
        if(this.message == null || !this.message.equals(message)) {
            this.message = message;
            update();
        }
    }

    @Override
    public void setPercentage(int percentage) {
        checkDestroyed();
        percentage = Math.max(percentage, 0);
        percentage = Math.min(percentage, 100);
        if(this.percentage != percentage) {
            this.percentage = percentage;
            update();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        checkDestroyed();
        if(this.visible != visible) {
            if(visible) {
                setLocation(player.getLocation());
                BarPacketUtil.spawn(this);
            } else {
                BarPacketUtil.remove(this);
                this.location = null;
            }
            this.visible = visible;
        }
    }

    @Override
    public void destroy() {
        setVisible(false);
        plugin.playerBarMap.remove(player);
        this.destroyed = true;
    }

    public void setLocation(Location loc) {
        yaw = loc.getYaw();
        pitch = loc.getPitch();
        location = loc.clone().add(loc.getDirection().multiply(64));
    }

    private void checkDestroyed() {
        if(destroyed) throw new IllegalStateException("Bar is destroyed");
    }

    private void update() {
        if(visible) BarPacketUtil.updateMeta(this);
    }
}
