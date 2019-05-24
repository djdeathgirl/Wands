package me.dylan.wands.spell;

import me.dylan.wands.spell.model.CastableSpell;
import me.dylan.wands.spell.spelltemplates.SpellBehaviour;
import me.dylan.wands.spell.spelltemplates.WaveSpell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodStun extends CastableSpell {
    @Override
    public SpellBehaviour getSpellBehaviour() {
        return WaveSpell.newBuilder()
                .setEffectRadius(1F)
                .setEntityDamage(5)
                .setCastEffects(location -> location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 4, 1))
                .setEntityEffects(entity -> {
                    ((LivingEntity) entity).addPotionEffect(
                            new PotionEffect(PotionEffectType.SLOW, 180, 3, false), true);
                    Location location = entity.getLocation();
                    location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 4, 1);
                })
                .setVisualEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 2, 0.2, 0.2, 0.2, 0.04, null, true);
                    loc.getWorld().spawnParticle(Particle.DRIP_LAVA, loc, 2, 0.3, 0.3, 0.3, 0.04, null, true);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 1, 0.6, 0.7, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                }).setEffectDistance(30)
                .stopAtEntity(true)
                .build();
    }
}