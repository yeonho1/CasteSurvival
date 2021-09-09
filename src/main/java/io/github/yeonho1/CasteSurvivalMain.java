package io.github.yeonho1;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class CasteSurvivalMain extends JavaPlugin {
    ArrayList<Player> stack;
    CasteSurvivalListeners lis;
    CasteSurvivalCommands comm;
    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().info("CasteSurvival by yeonho1 enabled");
        stack = new ArrayList<>();
        this.lis = new CasteSurvivalListeners(this);
        this.comm = new CasteSurvivalCommands(this);
        getCommand("csurv").setExecutor(this.comm);
        Bukkit.getPluginManager().registerEvents(this.lis, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info("CasteSurvival by yeonho1 disabled");
        for (Player p: this.stack) {
            p.setGravity(true);
        }
    }

    public boolean playerWillBeStuck(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        int xceil = (int) Math.ceil(x);
        int yceil = (int) Math.ceil(y);
        int zceil = (int) Math.ceil(z);
        int[] xs = { xceil, xceil+1, xceil-1 };
        int[] ys = { yceil, yceil+1 };
        int[] zs = { zceil, zceil+1, zceil-1 };
        World w = location.getWorld();
        for (int tx : xs) {
            for (int ty : ys) {
                for (int tz : zs) {
                    if (w.getBlockAt(tx, ty, tz).getType() != Material.AIR) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void teleportPlayers() {
        Player p1 = this.stack.get(0);
        Location p1loc = p1.getLocation();
        World w = p1.getWorld();
        double x = p1loc.getX();
        double y = p1loc.getY();
        double z = p1loc.getZ();
        p1.setGravity(true);
        for (int i = 1; i < this.stack.size(); i++) {
            Player pi = this.stack.get(i);
            Location ol = pi.getLocation();
            Location l = pi.getLocation();
            l.setWorld(w);
            l.setX(x);
            l.setY(y+2.5f*i);
            l.setZ(z);
            if (pi.getLocation().distance(l) > 3.0f) {
                pi.teleport(l);
            } else if (pi.getLocation().distance(l) > 0){
                if (playerWillBeStuck(l) || playerWillBeStuck(ol)) {
                    pi.teleport(l);
                } else {
                    Location difference = l.subtract(pi.getLocation());
                    pi.setVelocity(difference.toVector());
                }
            } else {
                pi.setVelocity(new Vector(0,0,0));
            }
            pi.setGravity(false);
            pi.setFallDistance(0);
        }
    }
}
