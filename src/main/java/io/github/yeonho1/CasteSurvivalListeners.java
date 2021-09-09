package io.github.yeonho1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;

public class CasteSurvivalListeners implements Listener {
    private CasteSurvivalMain plugin;

    public CasteSurvivalListeners(CasteSurvivalMain pl) {
        this.plugin = pl;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (!this.plugin.stack.contains(player)) return;
        for (int i = 0; i < this.plugin.stack.size(); i++) {
            if (this.plugin.stack.get(i) == player) {
                int newIndex = i;
                for (int j = i + 1; j < this.plugin.stack.size(); j++) {
                    this.plugin.stack.set(newIndex, this.plugin.stack.get(j));
                    newIndex++;
                }
                this.plugin.stack.set(this.plugin.stack.size() - 1, player);
                this.plugin.teleportPlayers();
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (!this.plugin.stack.contains(player)) return;
        this.plugin.teleportPlayers();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getType() == EntityType.ENDER_DRAGON) {
            Player root = this.plugin.stack.get(0);
            String rootIs = "최하위에 있던 플레이어: ";
            String playername = "" + ChatColor.GOLD + ChatColor.ITALIC + root.getName();
            String ED = "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "엔더 드래곤" + ChatColor.RESET + "이 사망했습니다!";
            String rootAnnounce = rootIs + playername;
            for (int i = 0; i < this.plugin.stack.size(); i++) {
                this.plugin.stack.get(i).sendMessage(ED);
                this.plugin.stack.get(i).sendMessage(rootAnnounce);
            }
        }
    }
}
