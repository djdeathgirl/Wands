package me.dylan.wands.spellbehaviour;

import me.dylan.wands.Wands;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("")
public abstract class SpellBehaviour {

    final static Wands plugin = Wands.getInstance();

    final int entityDamage;
    final float effectAreaRange;
    final float pushSpeed;
    final Consumer<Location> castEffects;
    final Consumer<Location> visualEffects;
    final Consumer<Entity> entityEffects;

    SpellBehaviour(int entityDamage, float effectAreaRange, float pushSpeed, Consumer<Location> castEffects, Consumer<Location> visualEffects, Consumer<Entity> entityEffects) {
        this.entityDamage = entityDamage;
        this.effectAreaRange = effectAreaRange;
        this.pushSpeed = pushSpeed;
        this.castEffects = castEffects;
        this.visualEffects = visualEffects;
        this.entityEffects = entityEffects;
    }

    public abstract void executeFrom(Player player);

    Iterable<Damageable> getNearbyDamageables(Player player, Location loc, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius).stream()
                .filter(Damageable.class::isInstance)
                .filter(entity -> !entity.equals(player))
                .map(Damageable.class::cast)
                .collect(Collectors.toList());
    }

    void damage(int damage, Entity source, Damageable victim) {
        victim.damage(damage, source);
        victim.setVelocity(new Vector(0, 0, 0));
    }

    void pushFrom(Location sLoc, Entity entity, float speed) {
        if (speed != 0) {
            entity.setVelocity(entity.getLocation().subtract(sLoc).toVector().normalize().multiply(speed));
        }
    }
}