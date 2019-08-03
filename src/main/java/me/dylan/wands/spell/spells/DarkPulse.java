package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Ray;
import me.dylan.wands.spell.types.Base;
import me.dylan.wands.spell.types.Base.Target;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum DarkPulse implements Castable {
    INSTANCE;
    private final Base baseType;

    private final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false);
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 40, 2, false);
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 40, 2, false);


    DarkPulse() {
        this.baseType = Ray.newBuilder(Target.MULTI)
                .setRayWidth(1)
                .setSpellEffectRadius(3.0F)
                .setMetersPerTick(2)
                .setKnockBack(0.6F, 0.5F)
                .setEntityEffects(entity -> {
                    entity.addPotionEffect(blind);
                    entity.addPotionEffect(wither);
                    entity.addPotionEffect(slow);
                })
                .setEntityDamage(8)
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 3, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 13, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                })
                .setEffectDistance(30)
                .setHitEffects((loc, world) -> {
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.createExplosion(0.0f);
                    loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_HURT, SoundCategory.MASTER, 4.0F, 1.0F);
                })
                .build();
    }

    @Override
    public Base getBaseType() {
        return baseType;
    }
}