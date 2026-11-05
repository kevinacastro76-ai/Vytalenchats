package studio.vytal.vytalenchants;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.*;
public final class ItemEnchantments {
    private final NamespacedKey key;
    public ItemEnchantments(VytalEnchants plugin){key=new NamespacedKey(plugin,"vytal_custom_enchants");}
    public Map<String,Integer> read(ItemStack item){
        Map<String,Integer> map=new LinkedHashMap<>();
        if(item==null || !item.hasItemMeta()) return map;
        String raw=item.getItemMeta().getPersistentDataContainer().get(key,PersistentDataType.STRING);
        if(raw!=null) for(String p:raw.split(";")){String[] a=p.split(":",2);if(a.length==2)try{map.put(a[0],Integer.parseInt(a[1]));}catch(Exception ignored){}}
        return map;
    }
    public void write(ItemStack item,Map<String,Integer> map){
        ItemMeta meta=item.getItemMeta(); if(meta==null)return;
        String raw=map.entrySet().stream().map(e->e.getKey()+":"+e.getValue()).reduce((a,b)->a+";"+b).orElse("");
        meta.getPersistentDataContainer().set(key,PersistentDataType.STRING,raw);
        List<String> lore=meta.hasLore()?new ArrayList<>(meta.getLore()):new ArrayList<>();
        lore.removeIf(s->ChatColor.stripColor(s).startsWith("✦ "));
        for(var e:map.entrySet()) EnchantRegistry.find(e.getKey()).ifPresent(d->lore.add(BookService.color("&d✦ &5"+d.name()+" &d"+e.getValue())));
        meta.setLore(lore); item.setItemMeta(meta);
    }
}
