package me.dylan.wands.artifacts;

import me.dylan.wands.ItemUtil;
import me.dylan.wands.Wands;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class TherosDagger implements Listener {

    private final Wands plugin = Wands.getInstance();
    private final String leapKey = "therosJump";
    private final String sneakKey = "therosInvisable";

    private boolean hasDagger(Player player) {
        if (!Wands.getStatus()) return false;
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool != null) {
            ItemUtil itemUtil = new ItemUtil(tool);
            return itemUtil.hasNbtTag("therosDagger");
        }
        return false;
    }

    @EventHandler
    public void onSpringToggle(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if (hasDagger(player)) {
            Bukkit.getScheduler().runTaskLater(Wands.getInstance(), () -> jumpParticles(player), 1);
        }
    }


    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            LivingEntity victim;
            if (event.getEntity() instanceof LivingEntity) {
                victim = (LivingEntity) event.getEntity();
                if (hasDagger(player)) {
                    victim.removePotionEffect(PotionEffectType.SPEED);
                    victim.removePotionEffect(PotionEffectType.BLINDNESS);
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false), true);
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5, true), true);
                    event.setDamage(4);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GUARDIAN_HURT, SoundCategory.MASTER, 3.0F, 1.0F);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GUARDIAN_HURT, SoundCategory.MASTER, 3.0F, 0.3F);
                }
            }
        }
    }

    @EventHandler
    public void leap(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (hasDagger(player)) {
            if (event.getHand() != null) {
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    Action a = event.getAction();
                    if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                        if (!player.hasMetadata(leapKey) && !player.hasMetadata(sneakKey)) {
                            player.setMetadata(leapKey, new FixedMetadataValue(plugin, true));
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SWAG, SoundCategory.MASTER, 3.0F, 1.0F);
                            Vector direction = player.getLocation().getDirection().setY(0).normalize().setY(1.2);
                            player.setVelocity(direction);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player.isOnGround()) {
                                        if (player.hasMetadata(leapKey)) {
                                            Bukkit.getScheduler().runTaskLater(plugin, () ->
                                                    player.removeMetadata(leapKey, plugin), 2);
                                            cancel();
                                        }
                                    }
                                }
                            }.runTaskTimer(Wands.getInstance(), 3, 3);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void fallDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (entity.hasMetadata(leapKey)) {
                    event.setCancelled(true);
                    entity.removeMetadata(leapKey, plugin);
                }
            }
        }
    }

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (hasDagger(player)) {
            if (event.isSneaking() && player.isOnGround()) {
                player.setMetadata(sneakKey, new FixedMetadataValue(plugin, true));
                Location location = player.getLocation();
                location.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 2f);
                location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 15, 0.5, 0.2, 0.5, 0.1, null, true);
                location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 20, 0.5, 0.5, 0.5, 0.1, null, true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 6000, 0, true), true);
                Wands.sendActionBar(player, "§6You are §aInvisible");
                return;
            }
        }
        if (player.hasMetadata(sneakKey)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            Wands.sendActionBar(player, "§6You are §cVisible");
            player.removeMetadata(sneakKey, plugin);
        }
    }

    private void jumpParticles(Player player) {
        if (player.isSprinting()) {
            Bukkit.getScheduler().runTaskLater(Wands.getInstance(), () -> {
                Location loc = player.getLocation();
                loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.1, 0.1, 0.1, 0.1, null, true);
                loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0.1, 0.1, 0.1, 0.1, null, true);
                jumpParticles(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 4, true), true);
            }, 1);
        } else {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }
}
