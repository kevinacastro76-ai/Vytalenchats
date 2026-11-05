package studio.vytal.vytalenchants;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import java.util.*;
public final class AnvilListener implements Listener {
    private final VytalEnchants plugin;
    public AnvilListener(VytalEnchants plugin){this.plugin=plugin;}
    @EventHandler public void prepare(PrepareAnvilEvent e){
        if(!plugin.getConfig().getBoolean("settings.anvil.enabled",true))return;
        ItemStack target=e.getInventory().getItem(0), book=e.getInventory().getItem(1);
        String id=plugin.books().id(book);if(target==null||id==null)return;
        EnchantDef def=EnchantRegistry.find(id).orElse(null);if(def==null||!allowed(def.category(),target.getType()))return;
        int level=plugin.books().level(book);ItemStack result=target.clone();
        Map<String,Integer> map=plugin.itemEnchants().read(result);int old=map.getOrDefault(id,0);
        int applied=Math.min(def.maxLevel(),old==level?old+1:Math.max(old,level));map.put(id,applied);
        plugin.itemEnchants().write(result,map);e.setResult(result);
        e.getInventory().setRepairCost(Math.min(plugin.getConfig().getInt("settings.anvil.max-cost",39),Math.max(1,level)));
    }
    private boolean allowed(Category c,Material m){
        String n=m.name();
        return switch(c){
            case MINERIA -> n.endsWith("_PICKAXE")||n.endsWith("_AXE")||n.endsWith("_SHOVEL");
            case COMBATE,ARMAS -> n.endsWith("_SWORD")||n.endsWith("_AXE")||m==Material.TRIDENT;
            case ARMADURAS -> n.endsWith("_HELMET")||n.endsWith("_CHESTPLATE")||n.endsWith("_LEGGINGS")||n.endsWith("_BOOTS");
            case BOTAS -> n.endsWith("_BOOTS");
            case ARCO -> m==Material.BOW||m==Material.CROSSBOW;
            case CULTIVOS -> n.endsWith("_HOE");
            case PESCA_Y_ESPECIALES -> m==Material.FISHING_ROD||n.endsWith("_PICKAXE")||n.endsWith("_SWORD");
        };
    }
          }
                                                
