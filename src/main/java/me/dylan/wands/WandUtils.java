package me.dylan.wands;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.stream.Collectors;

public class WandUtils {

    private WandUtils() {
    }

    public static Iterable<Damageable> getNearbyDamageables(Player player, Location loc, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius).stream()
                .filter(Damageable.class::isInstance)
                .filter(entity -> !entity.equals(player))
                .map(Damageable.class::cast)
                .collect(Collectors.toList());
    }

    public static void damage(int damage, Entity source, Damageable victim) {
        victim.damage(damage, source);
        victim.setVelocity(new Vector(0, 0, 0));
    }

}