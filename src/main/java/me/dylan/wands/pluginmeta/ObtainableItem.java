package me.dylan.wands.pluginmeta;

import me.dylan.wands.Main;
import me.dylan.wands.customitem.AssasinDagger;
import me.dylan.wands.customitem.CursedBow;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.util.ItemUtil;
import me.dylan.wands.util.WandUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

@SuppressWarnings("unused")
public enum ObtainableItem {
    ASSASSINS_DAGGER(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.MUSIC_DISC_MALL);
        ItemUtil.setName(itemStack, "&dAssassin's &8Dagger");
        ItemUtil.setPersistentData(itemStack, AssasinDagger.ID_TAG, PersistentDataType.BYTE, (byte) 1);
        return itemStack;
    })),

    CURSED_BOW(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.BOW);
        ItemUtil.setName(itemStack, "&cCursed Bow");
        ItemUtil.setItemMeta(itemStack, meta -> {
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.values());
        });
        ItemUtil.setPersistentData(itemStack, CursedBow.ID_TAG, PersistentDataType.BYTE, (byte) 1);
        return itemStack;
    })),

    GOD_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemUtil.setName(itemStack, "&8&lMagic Wand");
        WandUtil.setAsWand(itemStack);
        WandUtil.setSpells(itemStack, SpellType.COMET, SpellType.SPARK, SpellType.CONFUSE, SpellType.LAUNCH, SpellType.POISON_WAVE);
        return itemStack;
    })),

    BLOOD_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.NETHER_WART);
        ItemUtil.setName(itemStack, "&cBlood Magic");
        WandUtil.setAsWand(itemStack);
        WandUtil.setSpells(itemStack, SpellType.BLOOD_WAVE, SpellType.BLOOD_SPARK, SpellType.BLOOD_EXPLODE, SpellType.BLOOD_STUN, SpellType.BLOOD_BLOCK);
        return itemStack;
    }));

    private static final String[] names = Arrays.stream(values()).map(Enum::toString).toArray(String[]::new);

    private final ItemStack itemStack;

    ObtainableItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static String[] getNames() {
        return names;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, Main.PREFIX);
        for (ObtainableItem obtainableItem : ObtainableItem.values()) {
            inventory.addItem(obtainableItem.itemStack);
        }
        player.openInventory(inventory);
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}