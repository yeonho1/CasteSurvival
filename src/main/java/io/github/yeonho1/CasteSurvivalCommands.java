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
            String cmdname = cmd.getName();
            if (cmdname.equalsIgnoreCase("csurv")) {
                Player player = (Player) sender;
                String name = player.getName();
                if (!player.isOp()) {
                    sender.sendMessage(ChatColor.DARK_RED + "관리자만 이 명령어를 사용할 수 있습니다!");
                    return true;
                }
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "인자가 너무 적습니다!");
                    sender.sendMessage(ChatColor.DARK_RED + "/csurv start 또는 /csurv stop 을 이용하세요.");
                    return true;
                } else if (args.length > 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "인자가 너무 많습니다!");
                    sender.sendMessage(ChatColor.DARK_RED + "/csurv start 또는 /csurv stop 을 이용하세요.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("start")) {
                    if (this.plugin.isRunning) {
                        sender.sendMessage(ChatColor.DARK_RED + "게임이 이미 진행 중입니다!");
                        return true;
                    }
                    this.plugin.stack.clear();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.GOLD + name + ChatColor.GREEN + "님이 게임을 시작했습니다.");
                        this.plugin.stack.add(p);
                    }
                    Collections.shuffle(this.plugin.stack);
                    this.plugin.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
                            this.plugin,
                            new CasteSurvivalRunnable(this.plugin),
                            0L,
                            1L
                    );
                    this.plugin.isRunning = true;
                } else if (args[0].equalsIgnoreCase("stop")) {
                    if (!this.plugin.isRunning) {
                        sender.sendMessage(ChatColor.DARK_RED + "진행중인 게임이 없습니다!");
                        return true;
                    }
                    for (Player p: this.plugin.stack) {
                        p.sendMessage(ChatColor.GOLD + name + ChatColor.GREEN + "님이 게임을 종료했습니다.");
                        p.setGravity(true);
                    }
                    this.plugin.stack.clear();
                    Bukkit.getScheduler().cancelTask(plugin.taskId);
                    this.plugin.isRunning = false;
                    sender.sendMessage(ChatColor.GREEN + "게임을 종료했습니다.");
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "올바르지 않은 명령어 입력입니다.");
                    sender.sendMessage(ChatColor.DARK_RED + "/csurv start 또는 /csurv stop 을 이용하세요.");
                }
            } else if (cmdname.equalsIgnoreCase("setgravity")) {
                Player player = (Player) sender;
                String name = player.getName();
                if (!player.isOp()) {
                    sender.sendMessage(ChatColor.DARK_RED + "관리자만 이 명령어를 사용할 수 있습니다!");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.DARK_RED + "인자가 너무 적습니다!");
                    sender.sendMessage(ChatColor.DARK_RED + "이용방법: /setgravity <플레이어 닉네임> <true/false>");
                    return true;
                } else if (args.length > 2) {
                    sender.sendMessage(ChatColor.DARK_RED + "인자가 너무 많습니다!");
                    sender.sendMessage(ChatColor.DARK_RED + "이용방법: /setgravity <플레이어 닉네임> <true/false>");
                    return true;
                }
                if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
                    sender.sendMessage(ChatColor.DARK_RED + "참/거짓 인자가 올바르지 않습니다.");
                    sender.sendMessage(ChatColor.DARK_RED + "이용방법: /setgravity <플레이어 닉네임> <true/false>");
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().equalsIgnoreCase(args[0])) {
                        String result = "플레이어 " + args[0] + "의 중력을 ";
                        if (args[1].equalsIgnoreCase("true")) {
                            p.setGravity(true);
                        } else {
                            p.setGravity(false);
                            result += "비";
                        }
                        sender.sendMessage(result + "활성화했습니다.");
                        return true;
                    }
                }
                sender.sendMessage(ChatColor.DARK_RED + "플레이어 " + args[0] + "이 존재하지 않습니다.");
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "플레이어만 이 명령어를 사용할 수 있습니다!");
        }
        return true;
    }
}
