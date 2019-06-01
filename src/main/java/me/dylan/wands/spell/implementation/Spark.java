package me.dylan.wands.spell.implementation;

import me.dylan.wands.spell.BaseSpell;
import me.dylan.wands.spell.behaviourhandler.BaseBehaviour;
import me.dylan.wands.spell.behaviourhandler.SparkSpell;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class Spark extends BaseSpell {

    @Override
    public BaseBehaviour getBaseBehaviour() {
        return SparkSpell.newBuilder()
                .setEffectRadius(2.2F)
                .setEntityDamage(10)
                .setRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.6, 0.7, 0.6, 0.2, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 0.2, 0.2, 0.2, 0.08, null, true);

                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F))
                .setEffectDistance(25)
                .build();
    }
}