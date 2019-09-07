package me.dylan.wands;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ListenerRegistry {
    private static final WandsPlugin plugin = JavaPlugin.getPlugin(WandsPlugin.class);
    private final Set<Listener> toggleableListeners = new HashSet<>();

    public static void addListener(@NotNull Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    void addToggleableListener(Listener... listeners) {
        toggleableListeners.addAll(Arrays.asList(listeners));
        if (plugin.getConfigurableData().isMagicUseAllowed()) {
            addListener(listeners);
        }
    }

    public void disableListeners() {
        toggleableListeners.forEach(HandlerList::unregisterAll);
    }

    public void enableListeners() {
        toggleableListeners.forEach(HandlerList::unregisterAll);
        toggleableListeners.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, plugin));
    }
}
