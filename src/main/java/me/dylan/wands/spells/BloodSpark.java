package me.dylan.wands.spells;

import me.dylan.wands.spellfoundation.CastableSpell;
import me.dylan.wands.spellfoundation.SpellBehaviour;
import me.dylan.wands.spellfoundation.SpellBehaviour.BaseProperties;
import me.dylan.wands.spellfoundation.SpellBehaviour.SparkSpell.Builder;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class BloodSpark extends CastableSpell {
    @Override
    public SpellBehaviour getSpellBehaviour() {
        BaseProperties baseProperties = SpellBehaviour.createEmptyBaseProperties()
                .setEffectRadius(2.2F)
                .setEntityDamage(10)
                .setVisualEffects(loc -> {
                    loc.add(0, 1, 0);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 20, 0.2, 0.2, 0.2, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 20, 0.6, 0.7, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);

                    runTaskLater(() ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F));

        return new Builder(baseProperties).setEffectDistance(30).build();
    }
}
