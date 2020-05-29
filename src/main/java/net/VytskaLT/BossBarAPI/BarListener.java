package net.VytskaLT.BossBarAPI;

import lombok.RequiredArgsConstructor;
import net.VytskaLT.BossBarAPI.bar.BossbarImpl;
import net.VytskaLT.BossBarAPI.util.BarPacketUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@RequiredArgsConstructor
public class BarListener implements Listener {

    private final BossBarAPI plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    public void quit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if(plugin.playerBarMap.containsKey(player)) {
            plugin.playerBarMap.get(player).destroy();
            plugin.playerBarMap.remove(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void move(PlayerMoveEvent e) {
        handleMove(e);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void move(PlayerTeleportEvent e) {
        handleMove(e);
    }

    private void handleMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        BossbarImpl bar = plugin.playerBarMap.get(player);
        if(bar == null || !bar.visible) return;
        if(e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockY() != e.getFrom().getBlockY() || e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
            if(e.getFrom().getWorld() != e.getTo().getWorld() || bar.location.distance(e.getTo()) >= 100) {
                bar.setLocation(e.getTo());
                BarPacketUtil.remove(bar);
                BarPacketUtil.spawn(bar);
                return;
            }
            double dist = e.getTo().distance(bar.location);
            if(dist <= 50 || dist >= 75 || e.getFrom().distance(e.getTo()) >= 100) {
                bar.setLocation(e.getTo());
                BarPacketUtil.updateLocation(bar);
                return;
            }
        }
        if(e.getFrom().getYaw() != e.getTo().getPitch() || e.getFrom().getPitch() != e.getTo().getPitch()) {
            float yaw = getDifference(e.getTo().getYaw(), bar.yaw);
            float pitch = getDifference(e.getTo().getPitch(), bar.pitch);
            if(yaw >= 40 || pitch >= 40) {
                bar.setLocation(e.getTo());
                BarPacketUtil.updateLocation(bar);
            }
        }
    }

    private static float getDifference(float a, float b) {
        float diff = a - b;
        return diff < 0 ? -diff : diff;
    }
}
