package me.dylan.wands.commandhandler;

import me.dylan.wands.GUIs;
import me.dylan.wands.Wands;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("wands")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    GUIs.openGUI((Player) sender);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e ---- &6Wands&e ----"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Created by: &e_JustDylan_"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Current version:&e " + Wands.VERSION));
            } else if (args[0].equalsIgnoreCase("particle")) {
                if (args.length == 4)
                    try {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            Location loc = player.getLocation();
                            loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 0,
                                    Float.parseFloat(args[1])/225F,
                                    Float.parseFloat(args[2])/225F,
                                    Float.parseFloat(args[3])/225F,
                                    1, null, true);
                        }
                    } catch (Exception e) {
                        sender.sendMessage("could not parse arguments to numbers");
                        return true;
                    }
            }
            return true;
        }
        return true;
    }
}
