package studio.vytal.vytalenchants;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.*;
public final class BookService {
    private final VytalEnchants plugin;
    private final NamespacedKey idKey, levelKey;
    public BookService(VytalEnchants plugin) {
        this.plugin=plugin; idKey=new NamespacedKey(plugin,"vytal_enchant_id"); levelKey=new NamespacedKey(plugin,"vytal_enchant_level");
    }
    public ItemStack create(EnchantDef def,int level) {
        int lv=Math.max(1,Math.min(level,def.maxLevel()));
        ItemStack item=new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta=item.getItemMeta();
        meta.setDisplayName(color("&5&l"+def.name()+" &d"+lv));
        meta.setLore(List.of(color("&8VytalEnchants"),color("&7"+def.description()),
            color("&7Categoria: &d"+def.category().name().replace('_',' ')),
            color("&7Nivel: &d"+lv+"&7/&d"+def.maxLevel()),color("&eCombina con un objeto en un yunque.")));
        meta.getPersistentDataContainer().set(idKey,PersistentDataType.STRING,def.id());
        meta.getPersistentDataContainer().set(levelKey,PersistentDataType.INTEGER,lv);
        item.setItemMeta(meta); return item;
    }
    public String id(ItemStack item) {
        if(item==null || item.getType()!=Material.ENCHANTED_BOOK || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(idKey,PersistentDataType.STRING);
    }
    public int level(ItemStack item) {
        if(item==null || !item.hasItemMeta()) return 0;
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(levelKey,PersistentDataType.INTEGER,0);
    }
    public static String color(String s){return ChatColor.translateAlternateColorCodes('&',s);}
}
