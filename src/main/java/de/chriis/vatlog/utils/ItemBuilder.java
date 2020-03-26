package de.chriis.vatlog.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {

    private Material material;
    private int amount;
    private int subId;

    /*
        ItemMeta
     */
    private String name;
    private List<ItemFlag> itemFlags = new ArrayList<>();
    private List<String> lores = new ArrayList<>();
    private HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private Short durability;
    private Potion potion;
    private Color color;
    private String skullOwner;


    public ItemBuilder(Material material){
        this.material = material;
        this.amount = 1;
        this.subId = 0;
    }

    public ItemBuilder(Material material, int amount){
        this.material = material;
        this.amount = amount;
        this.subId = 0;
    }

    public ItemBuilder(Material material, int amount, int subId){
        this.material = material;
        this.amount = amount;
        this.subId = subId;
    }

    public ItemBuilder withName(String name){
        this.name = name;
        return this;
    }

    public ItemBuilder withAmount(int amount){
        this.amount = amount;
        return this;
    }

    public ItemBuilder withItemFlag(ItemFlag itemFlag){
        this.itemFlags.add(itemFlag);
        return this;
    }

    public ItemBuilder withItemFlags(ItemFlag... itemFlags){
        this.itemFlags.addAll(Arrays.asList(itemFlags));
        return this;
    }

    public ItemBuilder withLore(String lore){
        this.lores.add(lore);
        return this;
    }

    public ItemBuilder withLores(String... lores){
        this.lores.addAll(Arrays.asList(lores));
        return this;
    }

    public ItemBuilder withLores(List<String> lores){
        this.lores.addAll(lores);
        return this;
    }

    public ItemBuilder withDurability(short durability){
        this.durability = durability;
        return this;
    }

    public ItemBuilder withPotion(Potion potion){
        this.potion = potion;
        return this;
    }

    public ItemBuilder withColor(Color color){
        this.color = color;
        return this;
    }

    public ItemBuilder withEnchantment(Enchantment enchantment, int level){
        this.enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder toPlayerHeadBuilder(String skullOwner){
        this.skullOwner = skullOwner;
        this.material = Material.SKULL_ITEM;
        this.subId = 3;
        return this;
    }


    public ItemStack toPlayerHead(String skullOwner){
        this.subId = 3;
        this.material = Material.SKULL_ITEM;
        this.skullOwner = skullOwner;
        return this.toItemStack();
    }


    public ItemStack toItemStack(){
        ItemStack itemStack = new ItemStack(this.material, this.amount, (byte)this.subId);
        if(this.material == Material.AIR){
            return itemStack;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if(this.name != null){
            itemMeta.setDisplayName(this.name);
        }
        if(this.durability != null){
            itemStack.setDurability(this.durability);
        }
        if(this.potion != null && this.material == Material.POTION){
            potion.apply(itemStack);
        }
        if(this.color != null && itemMeta instanceof LeatherArmorMeta){
            ((LeatherArmorMeta) itemMeta).setColor(this.color);
        }
        if(material == Material.SKULL_ITEM && skullOwner != null){
            ((SkullMeta)itemMeta).setOwner(this.skullOwner);

        }

        if(itemFlags.size() > 0){
            itemMeta.addItemFlags(this.itemFlags.toArray(new ItemFlag[this.itemFlags.size()]));
        }
        if(enchantments.size() > 0){
            for(Enchantment enchantment : this.enchantments.keySet()){
                itemMeta.addEnchant(enchantment, this.enchantments.get(enchantment), true);
            }
        }
        itemMeta.setLore(this.lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
