package me.dylan.wands.spell.behaviourhandler;

import me.dylan.wands.Main;
import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.util.DataUtil;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public class ProjectileSpell<T extends Projectile> extends BaseBehaviour implements Listener {
    private static int idCount;
    private final Class<T> projectile;
    private final Consumer<T> projectileProps;
    private final Consumer<Location> hitEffects;
    private final float speed;
    private final int lifeTime;
    private final float pushSpeed;
    private final String metadataTag;

    //can be accessed via builder
    private ProjectileSpell(AbstractBuilder.BaseMeta baseMeta, Class<T> projectile, Consumer<T> projectileProps, Consumer<Location> hitEffects, float speed, int lifeTime, int pushSpeed) {
        super(baseMeta);
        ListenerRegistry.addListener(this);
        this.projectile = projectile;
        this.projectileProps = projectileProps;
        this.hitEffects = hitEffects;
        this.speed = speed;
        this.lifeTime = lifeTime;
        this.pushSpeed = pushSpeed;
        this.metadataTag = "ProjectileSpell_" + ++idCount;
    }

    @Override
    public void cast(Player player) {
        Vector velocity = player.getLocation().getDirection().multiply(speed);
        T projectile = player.launchProjectile(this.projectile, velocity);
        trail(projectile);
        projectileProps.accept(projectile);
        projectile.setMetadata(metadataTag, new FixedMetadataValue(plugin, true));
        activateLifeTimer(projectile);
        castEffects.accept(player.getLocation());
    }

    private void hit(Player player, Projectile projectile) {
        projectile.remove();
        Location loc = projectile.getLocation();
        EffectUtil.getNearbyDamageables(player, loc, effectAreaRange).forEach(entity -> {
            entityEffects.accept(entity);
            EffectUtil.damage(entityDamage, player, entity);
            entity.setVelocity(new Vector(0, 0, 0));
            pushFrom(loc, entity, pushSpeed);
        });
        hitEffects.accept(loc);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.hasMetadata(metadataTag)) {
            hit((Player) projectile.getShooter(), projectile);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager().hasMetadata(metadataTag)) {
            event.setCancelled(true);
        }
    }

    private void activateLifeTimer(Projectile projectile) {
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            if (projectile.isValid()) {
                hit((Player) projectile.getShooter(), projectile);
            }
        }, lifeTime);
    }

    private void trail(Projectile projectile) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (projectile.isValid()) {
                    visualEffects.accept(projectile.getLocation());

                } else cancel();
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().hasMetadata(metadataTag)) {
            event.setCancelled(true);
        }
    }

    private void pushFrom(Location origin, Entity entity, float speed) {
        if (speed != 0) {
            Location location = entity.getLocation().subtract(origin);
            Vector vector = location.toVector().normalize().multiply(speed);
            if (!Double.isFinite(vector.getX()) || !Double.isFinite(vector.getY()) || !Double.isFinite(vector.getZ())) {
                vector = new Vector(0, 0.2, 0);
            }
            entity.setVelocity(vector);
        }
    }

    public static <T extends Projectile> Builder<T> newBuilder(Class<T> projectileClass, float speed) {
        return new Builder<>(projectileClass, speed);
    }

    public static class Builder<T extends Projectile> extends AbstractBuilder<Builder<T>> {

        private final Class<T> projectile;
        private final float speed;

        private Consumer<T> projectileProps = DataUtil.emptyConsumer();
        private Consumer<Location> hitEffects = DataUtil.emptyConsumer();
        private int lifeTime;
        private int pushSpeed;

        private Builder(Class<T> projectileClass, float speed) throws NullPointerException {
            this.projectile = projectileClass;
            this.speed = speed;
        }

        @Override
        Builder<T> self() {
            return this;
        }

        public Builder<T> setProjectileProps(Consumer<T> projectileProps) {
            this.projectileProps = projectileProps;
            return self();
        }

        public Builder<T> setHitEffects(Consumer<Location> hitEffects) {
            this.hitEffects = hitEffects;
            return self();
        }

        public Builder<T> setLifeTime(int lifeTime) {
            this.lifeTime = lifeTime;
            return self();
        }

        public Builder<T> setPushSpeed(int pushSpeed) {
            this.pushSpeed = pushSpeed;
            return self();
        }

        public ProjectileSpell<T> build() {
            return new ProjectileSpell<>(getMeta(), projectile, projectileProps, hitEffects, speed, lifeTime, pushSpeed);
        }
    }
}