package studio.vytal.vytalenchants;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import java.util.*;
public final class EffectListener implements Listener {
    private final VytalEnchants plugin; private final Random random=new Random();
    public EffectListener(VytalEnchants plugin){this.plugin=plugin;}
    @EventHandler(ignoreCancelled=true) public void mine(BlockBreakEvent e){
        Player p=e.getPlayer();Map<String,Integer> ench=plugin.itemEnchants().read(p.getInventory().getItemInMainHand());
        if(ench.getOrDefault("Minero3x3",0)<=0||!plugin.getConfig().getBoolean("settings.effects.minero3x3",true))return;
        // Extra blocks are limited to the same block type and natural drops.
        int[][] offsets={{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1},{1,1,0},{-1,-1,0}};
        for(int[] o:offsets){var b=e.getBlock().getRelative(o[0],o[1],o[2]);if(b.getType()==e.getBlock().getType()&&b.getType()!=Material.BEDROCK)b.breakNaturally(p.getInventory().getItemInMainHand());}
    }
    @EventHandler(ignoreCancelled=true) public void damage(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player p))return;
        Map<String,Integer> ench=plugin.itemEnchants().read(p.getInventory().getItemInMainHand());
        int sharp=ench.getOrDefault("FiloMortal",0);if(sharp>0)e.setDamage(e.getDamage()+sharp*1.5);
        int life=ench.getOrDefault("RoboDeVida",0);
        if(life>0&&random.nextDouble()<Math.min(.75,.10*life))p.setHealth(Math.min(p.getMaxHealth(),p.getHealth()+life*.5));
    }
}
