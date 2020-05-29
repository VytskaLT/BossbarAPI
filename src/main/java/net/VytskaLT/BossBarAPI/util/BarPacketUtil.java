package net.VytskaLT.BossBarAPI.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lombok.experimental.UtilityClass;
import net.VytskaLT.BossBarAPI.bar.BossbarImpl;
import org.bukkit.Location;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

@UtilityClass
public class BarPacketUtil {

    public void spawn(BossbarImpl bar) {
        PacketContainer spawnPacket = bar.plugin.manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        spawnPacket.getIntegers().write(0, bar.entityId); // Entity id
        spawnPacket.getIntegers().write(1, 64); // Wither id
        writeLocation(spawnPacket, 2, bar.location);
        spawnPacket.getDataWatcherModifier().write(0, createWatcher(bar)); // Metadata

        try {
            bar.plugin.manager.sendServerPacket(bar.player, spawnPacket);
        } catch (InvocationTargetException e) {
            bar.plugin.getLogger().log(Level.SEVERE, "Couldn't send spawn packet", e);
        }
    }

    public void remove(BossbarImpl bar) {
        PacketContainer removePacket = bar.plugin.manager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        removePacket.getIntegerArrays().write(0, new int[] {bar.entityId});

        try {
            bar.plugin.manager.sendServerPacket(bar.player, removePacket);
        } catch (InvocationTargetException e) {
            bar.plugin.getLogger().log(Level.SEVERE, "Couldn't send remove packet", e);
        }
    }

    public void updateMeta(BossbarImpl bar) {
        PacketContainer metadataPacket = bar.plugin.manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metadataPacket.getIntegers().write(0, bar.entityId);
        metadataPacket.getWatchableCollectionModifier().write(0, createWatcher(bar).getWatchableObjects());

        try {
            bar.plugin.manager.sendServerPacket(bar.player, metadataPacket);
        } catch (InvocationTargetException e) {
            bar.plugin.getLogger().log(Level.SEVERE, "Couldn't send metadata packet", e);
        }
    }

    public void updateLocation(BossbarImpl bar) {
        PacketContainer teleportPacket = bar.plugin.manager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        teleportPacket.getIntegers().write(0, bar.entityId);
        writeLocation(teleportPacket, 1, bar.location);

        try {
            bar.plugin.manager.sendServerPacket(bar.player, teleportPacket);
        } catch (InvocationTargetException e) {
            bar.plugin.getLogger().log(Level.SEVERE, "Couldn't send teleport packet", e);
        }
    }

    private WrappedDataWatcher createWatcher(BossbarImpl bar) {
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, (byte) 32); // Make invisible
        watcher.setObject(2, bar.message == null ? "" : bar.message); // Set name
        watcher.setObject(4, (byte) 1); // Make silent
        watcher.setObject(6, bar.percentage*3f); // Set health
        return watcher;
    }

    private void writeLocation(PacketContainer packet, int start, Location loc) {
        packet.getIntegers().write(start, getLocation(loc.getX())); // X
        packet.getIntegers().write(++start, getLocation(loc.getY())); // Y
        packet.getIntegers().write(++start, getLocation(loc.getZ())); // Z
    }

    private int getLocation(double loc) {
        return (int) Math.floor(loc*32);
    }
}
