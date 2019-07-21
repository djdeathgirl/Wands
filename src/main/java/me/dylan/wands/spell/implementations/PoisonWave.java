package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.WaveSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum PoisonWave implements Castable {
    INSTANCE;
    private final Behaviour behaviour;
    private final PotionEffect poison = new PotionEffect(PotionEffectType.POISON, 60, 4, false);

    PoisonWave() {
        this.behaviour = WaveSpell.newBuilder()
                .setSpellEffectRadius(2F)
                .setEntityDamage(2)
                .setEffectDistance(20)
                .setCastSound(Sound.ENTITY_EVOKER_CAST_SPELL)
                .setEntityEffects(entity -> entity.addPotionEffect(poison, true))
                .setSpellRelativeEffects((loc, world) -> {
                    SpellEffectUtil.spawnColoredParticle(Particle.SPELL_MOB, loc, 18, 1.2, 1.2, 1.2, 75, 140, 50, false);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}