package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.Main;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.SpellManagementUtil.SpellCompoundUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Unbind extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String argument = args[0].toUpperCase();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (isWand(player, itemStack)) {
                    List<String> spells = SpellCompoundUtil.getSpells(itemStack);
                    String itemName = itemStack.getItemMeta().getDisplayName();
                    if (spells.contains(argument.toUpperCase())) {
                        if (SpellCompoundUtil.removeSpell(itemStack, argument, player, false)) {
                            sender.sendMessage(Main.PREFIX + "Successfully removed §7§l" + argument.toLowerCase() + "§r from " + itemName);
                        }
                    } else {
                        sender.sendMessage(Main.PREFIX + itemName + "§r doesn't contain spell §7§l" + argument.toLowerCase());
                    }
                }
            } else return false;
        }
        return true;
    }

}