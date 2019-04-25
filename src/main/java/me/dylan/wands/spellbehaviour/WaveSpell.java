package me.dylan.wands.spellbehaviour;

import me.dylan.wands.WandUtils;
import me.dylan.wands.Wands;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class WaveSpell extends SpellBehaviour {
    private final int effectDistance;
    private final boolean stopAtEntity;

    //can be accessed via builder
    private WaveSpell(SpellBehaviour.Builder.BuilderWrapper builderWrapper, int effectDistance, boolean stopAtEntity) {
        super(builderWrapper);
        this.effectDistance = effectDistance;
        this.stopAtEntity = stopAtEntity;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void cast(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        //id should prevent the entity from being affected twice by the wave
        String randomID = RandomStringUtils.random(5, true, false);
        castEffects.accept(direction.clone().multiply(10).toLocation(player.getWorld()).add(player.getEyeLocation()));
        Location currentLoc = player.getEyeLocation();

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count++ >= effectDistance) cancel();
                Location loc = currentLoc.add(direction).clone();
                visualEffects.accept(loc);
                for (Damageable entity : WandUtils.getNearbyDamageables(player, loc, effectAreaRange)) {
                    if (!entity.hasMetadata(randomID)) {
                        player.setMetadata(randomID, new FixedMetadataValue(plugin, true));
                        WandUtils.damage(entityDamage, player, entity);
                        entityEffects.accept(entity);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (entity.isValid()) {
                                entity.removeMetadata(randomID, plugin);
                            }
                        }, effectDistance - count);
                        if (stopAtEntity) {
                            cancel();
                            break;
                        }
                    }
                }
            }
        }.runTaskTimer(Wands.getPlugin(), 1, 1);
    }

    public static class Builder extends SpellBehaviour.Builder<Builder> {
        private int effectDistance;
        private boolean stopAtEntity = false;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return self();
        }

        public Builder stopAtEntity(boolean stopAtEntity) {
            this.stopAtEntity = stopAtEntity;
            return self();
        }

        public WaveSpell build() {
            return new WaveSpell(createBuilderWrapper(), effectDistance, stopAtEntity);
        }
    }
}
