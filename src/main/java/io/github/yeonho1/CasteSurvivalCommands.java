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
                if (!((Player) sender).isOp()) {
                    sender.sendMessage(ChatColor.DARK_RED + "관리자만 이 명령어를 사용할 수 있습니다!");
                    return false;
                }
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "인자가 너무 적습니다!");
                    sender.sendMessage(ChatColor.DARK_RED + "/csurv start 또는 /csurv stop 을 이용하세요.");
                    return false;
                } else if (args.length > 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "인자가 너무 많습니다!");
                    sender.sendMessage(ChatColor.DARK_RED + "/csurv start 또는 /csurv stop 을 이용하세요.");
                    return false;
                }
                if (args[0] == "start") {
                    if (this.plugin.isRunning) {
                        sender.sendMessage(ChatColor.DARK_RED + "게임이 이미 진행 중입니다!");
                        return false;
                    }
                    sender.sendMessage(ChatColor.GREEN + "게임을 시작합니다.");
                    this.plugin.stack.clear();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        this.plugin.stack.add(p);
                    }
                    Collections.shuffle(this.plugin.stack);
                    this.plugin.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
                            this.plugin,
                            new CasteSurvivalRunnable(this.plugin),
                            0L,
                            1L
                    );
                } else if (args[0] == "stop") {
                    if (!this.plugin.isRunning) {
                        sender.sendMessage(ChatColor.DARK_RED + "진행중인 게임이 없습니다!");
                        return false;
                    }
                    for (Player p: this.plugin.stack) {
                        p.setGravity(true);
                    }
                    this.plugin.stack.clear();
                    Bukkit.getScheduler().cancelTask(plugin.taskId);
                    sender.sendMessage(ChatColor.GREEN + "게임을 종료했습니다.");
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "올바르지 않은 명령어 입력입니다.");
                    sender.sendMessage(ChatColor.DARK_RED + "/csurv start 또는 /csurv stop 을 이용하세요.");
                }
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "플레이어만 이 명령어를 사용할 수 있습니다!");
        }
        return false;
    }
}
