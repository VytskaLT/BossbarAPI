package net.VytskaLT.BossBarAPI;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.VytskaLT.BossBarAPI.bar.Bossbar;
import net.VytskaLT.BossBarAPI.bar.BossbarImpl;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class BossBarAPI extends JavaPlugin {

    private static BossBarAPI instance;

    public ProtocolManager manager;
    public Map<Player, BossbarImpl> playerBarMap = new HashMap<>();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        manager = ProtocolLibrary.getProtocolManager();
        getServer().getPluginManager().registerEvents(new BarListener(this), this);
    }

    @Override
    public void onDisable() {
        playerBarMap.forEach((p, bar) -> bar.destroy());
    }

    /**
     * Gets the bossbar of this player.
     * @param player player to get the bossbar from.
     * @return bossbar of this player.
     */
    public static Bossbar getBossbar(Player player) {
        if(instance.playerBarMap.containsKey(player))
            return instance.playerBarMap.get(player);
        BossbarImpl bar = new BossbarImpl(instance, player);
        instance.playerBarMap.put(player, bar);
        return bar;
    }
}
