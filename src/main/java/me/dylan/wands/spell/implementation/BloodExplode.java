package me.dylan.wands.spell.implementation;

import me.dylan.wands.spell.Spell;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.Spark;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class BloodExplode extends Spell {

    @Override
    public Behaviour getBehaviour() {
        return Spark.newBuilder()
                .setEffectRadius(4.5F)
                .setEntityDamage(7)
                .setEntityEffects(entity -> entity.setFireTicks(40))
                .setRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 20, 1, 1, 1, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 15, 1, 1, 1, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4.0F, 1.0F);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F))
                .setEffectDistance(30)
                .build();
    }
}
