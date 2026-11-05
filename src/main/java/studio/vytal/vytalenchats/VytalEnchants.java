package studio.vytal.vytalenchants;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;
public final class VytalEnchants extends JavaPlugin implements CommandExecutor,TabCompleter {
    private BookService books;private ItemEnchantments itemEnchantments;private EnchantMenu menu;
    @Override public void onEnable(){
        saveDefaultConfig();books=new BookService(this);itemEnchantments=new ItemEnchantments(this);menu=new EnchantMenu(this);
        getServer().getPluginManager().registerEvents(menu,this);getServer().getPluginManager().registerEvents(new AnvilListener(this),this);getServer().getPluginManager().registerEvents(new EffectListener(this),this);
        PluginCommand cmd=getCommand("ve");if(cmd!=null){cmd.setExecutor(this);cmd.setTabCompleter(this);}
        getLogger().info("Loaded "+EnchantRegistry.all().size()+" VytalEnchants definitions.");
    }
    public BookService books(){return books;}public ItemEnchantments itemEnchants(){return itemEnchantments;}
    @Override public boolean onCommand(CommandSender s,Command c,String label,String[] a){
        if(a.length==0||a[0].equalsIgnoreCase("menu")){
            if(!(s instanceof Player p)){s.sendMessage("Use this command in-game.");return true;}
            if(!p.hasPermission("vytalenchants.use")){p.sendMessage(BookService.color("&cNo permission."));return true;}menu.openMain(p);return true;
        }
        if(a[0].equalsIgnoreCase("list")){s.sendMessage(BookService.color("&5VytalEnchants &7- &d"+EnchantRegistry.all().size()+" encantamientos"));for(Category cat:Category.values())s.sendMessage(BookService.color("&d"+cat.name()+"&7: "+EnchantRegistry.category(cat).size()));return true;}
        if(a[0].equalsIgnoreCase("book")){
            if(!s.hasPermission("vytalenchants.admin")){s.sendMessage("No permission.");return true;}
            if(a.length<3){s.sendMessage("/ve book <id> <nivel> [jugador]");return true;}
            EnchantDef def=EnchantRegistry.find(a[1]).orElse(null);if(def==null){s.sendMessage("Unknown enchantment ID. Use /ve list.");return true;}
            int level;try{level=Integer.parseInt(a[2]);}catch(NumberFormatException ex){s.sendMessage("Invalid level.");return true;}
            if(level<1||level>def.maxLevel()){s.sendMessage("Level must be 1-"+def.maxLevel());return true;}
            Player target=a.length>=4?getServer().getPlayerExact(a[3]):(s instanceof Player p?p:null);
            if(target==null){s.sendMessage("Specify an online player or run in-game.");return true;}
            target.getInventory().addItem(books.create(def,level));s.sendMessage("Book delivered to "+target.getName());return true;
        }
        if(a[0].equalsIgnoreCase("reload")){if(!s.hasPermission("vytalenchants.admin"))return true;reloadConfig();s.sendMessage("VytalEnchants config reloaded.");return true;}
        s.sendMessage("/ve [menu|list|book|reload]");return true;
    }
    @Override public List<String> onTabComplete(CommandSender s,Command c,String alias,String[] a){
        if(a.length==1)return List.of("menu","list","book","reload");
        if(a.length==2&&a[0].equalsIgnoreCase("book"))return EnchantRegistry.all().stream().map(EnchantDef::id).filter(x->x.toLowerCase(Locale.ROOT).startsWith(a[1].toLowerCase(Locale.ROOT))).toList();
        return Collections.emptyList();
    }
}
