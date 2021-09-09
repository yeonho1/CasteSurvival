package io.github.yeonho1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;

public class CasteSurvivalCommands implements CommandExecutor {
    CasteSurvivalMain plugin;

    public CasteSurvivalCommands(CasteSurvivalMain pl) {
        this.plugin = pl;
    }

    private class CasteSurvivalRunnable implements Runnable {
        CasteSurvivalMain plugin;
        public CasteSurvivalRunnable(CasteSurvivalMain pl) {
            this.plugin = pl;
        }
        public void run() {
            this.plugin.teleportPlayers();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("csurv")) {
                sender.sendMessage(ChatColor.GREEN + "게임을 시작합니다.");
                this.plugin.stack.clear();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    this.plugin.stack.add(p);
                }
                Collections.shuffle(this.plugin.stack);
                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
                        this.plugin,
                        new CasteSurvivalRunnable(this.plugin),
                        0L,
                        1L
                );
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "플레이어만 이 명령어를 사용할 수 있습니다!");
        }
        return false;
    }
}
