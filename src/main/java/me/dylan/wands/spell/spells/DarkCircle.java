package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Circle;
import me.dylan.wands.spell.types.Circle.CirclePlacement;
import me.dylan.wands.spell.types.Base;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum DarkCircle implements Castable {
    INSTANCE;
    private final Base baseType;

    private final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false);

    DarkCircle() {
        this.baseType = Circle.newBuilder(CirclePlacement.RELATIVE)
                .setCircleRadius(4)
                .setEntityDamage(8)
                .setSpellEffectRadius(4.0F)
                .setEntityEffects(entity -> {
                    entity.setVelocity(entity.getVelocity().setY(0.5f));
                    entity.getLocation().createExplosion(0.0f);
                    entity.addPotionEffect(blind, true);
                })
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_ENDER_DRAGON_FLAP)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 8, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .setKnockBack(1.0F, 0.5F)
                .setCircleHeight(1)
                .build();
    }

    @Override
    public Base getBaseType() {
        return baseType;
    }
}