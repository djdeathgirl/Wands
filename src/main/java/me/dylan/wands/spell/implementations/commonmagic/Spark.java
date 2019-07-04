package me.dylan.wands.spell.implementations.commonmagic;

import me.dylan.wands.Main;
import me.dylan.wands.model.SoundPlayer;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.SparkSpell;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public enum Spark implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    Spark() {
        this.behaviour = SparkSpell.newBuilder()
                .setEffectRadius(2.2F)
                .setEntityDamage(10)
                .setRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.6, 0.7, 0.6, 0.2, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 0.2, 0.2, 0.2, 0.08, null, true);

                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastSound(SoundPlayer.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 4, 1)
                )
                .setEffectDistance(25)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
