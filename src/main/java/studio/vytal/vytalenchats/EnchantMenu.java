package studio.vytal.vytalenchants;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;
public final class EnchantMenu implements Listener {
    private final VytalEnchants plugin;
    private final Map<UUID,Category> categories=new HashMap<>();
    private final Map<UUID,Integer> pages=new HashMap<>();
    private static final int[] CATEGORY_SLOTS={10,11,12,14,15,16,20,22};
    private static final Material[] ICONS={Material.DIAMOND_PICKAXE,Material.NETHERITE_SWORD,Material.DIAMOND_SWORD,Material.NETHERITE_CHESTPLATE,Material.NETHERITE_BOOTS,Material.BOW,Material.GOLDEN_HOE,Material.ENCHANTED_BOOK};
    public EnchantMenu(VytalEnchants plugin){this.plugin=plugin;}
    public void openMain(Player p){
        Inventory inv=Bukkit.createInventory(null,27,BookService.color("&5&lVytalEnchants"));
        Category[] values=Category.values();
        for(int i=0;i<values.length;i++){
            ItemStack item=new ItemStack(ICONS[i]); ItemMeta m=item.getItemMeta();
            m.setDisplayName(BookService.color("&d&l"+values[i].name().replace('_',' ')));
            m.setLore(List.of(BookService.color("&7"+EnchantRegistry.category(values[i]).size()+" encantamientos"),BookService.color("&eClick para abrir")));
            item.setItemMeta(m);inv.setItem(CATEGORY_SLOTS[i],item);
        }
        p.openInventory(inv);
    }
    public void openCategory(Player p,Category c,int page){
        List<EnchantDef> list=EnchantRegistry.category(c); int max=Math.max(0,(list.size()-1)/27); page=Math.max(0,Math.min(page,max));
        categories.put(p.getUniqueId(),c);pages.put(p.getUniqueId(),page);
        Inventory inv=Bukkit.createInventory(null,36,BookService.color("&5&l"+c.name().replace('_',' ')+" &8- &d"+(page+1)));
        for(int i=0;i<27;i++){int ix=page*27+i;if(ix>=list.size())break;EnchantDef d=list.get(ix);
            ItemStack book=new ItemStack(Material.ENCHANTED_BOOK);ItemMeta m=book.getItemMeta();
            m.setDisplayName(BookService.color("&d&l"+d.name()));
            m.setLore(List.of(BookService.color("&7"+d.description()),BookService.color("&7Nivel maximo: &d"+d.maxLevel()),BookService.color("&eClick para recibir libro nivel I"),BookService.color("&8ID: "+d.id())));
            book.setItemMeta(m);inv.setItem(i,book);
        }
        inv.setItem(31,button(Material.BARRIER,"&cVolver"));
        if(page>0)inv.setItem(28,button(Material.ARROW,"&ePagina anterior"));
        if(page<max)inv.setItem(34,button(Material.SPECTRAL_ARROW,"&ePagina siguiente"));
        p.openInventory(inv);
    }
    private ItemStack button(Material mat,String name){ItemStack i=new ItemStack(mat);ItemMeta m=i.getItemMeta();m.setDisplayName(BookService.color(name));i.setItemMeta(m);return i;}
    @EventHandler public void click(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player p))return;
        String title=e.getView().getTitle();
        boolean main=title.equals(BookService.color("&5&lVytalEnchants"));
        boolean category=Arrays.stream(Category.values()).anyMatch(c->title.startsWith(BookService.color("&5&l"+c.name().replace('_',' '))));
        if(!main&&!category)return;
        e.setCancelled(true);int slot=e.getRawSlot();if(slot<0||slot>=e.getView().getTopInventory().getSize())return;
        if(main){for(int i=0;i<CATEGORY_SLOTS.length;i++)if(slot==CATEGORY_SLOTS[i]){openCategory(p,Category.values()[i],0);return;}return;}
        Category c=categories.get(p.getUniqueId());if(c==null)return;int page=pages.getOrDefault(p.getUniqueId(),0);
        if(slot==31){openMain(p);return;}if(slot==28){openCategory(p,c,page-1);return;}if(slot==34){openCategory(p,c,page+1);return;}
        if(slot>=27)return;List<EnchantDef> list=EnchantRegistry.category(c);int ix=page*27+slot;if(ix>=list.size())return;
        EnchantDef d=list.get(ix);p.getInventory().addItem(plugin.books().create(d,1));
        p.sendMessage(BookService.color("&5VytalEnchants &8» &aRecibiste &d"+d.name()+" I&a."));
    }
}
