package fr.clem28l.topluck;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import org.bukkit.Bukkit;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopLuckCommand implements CommandExecutor, Listener {

    private final List<PlayerTopLuck> playersLuck;

    public TopLuckCommand(List<PlayerTopLuck> playersLuck) {
        this.playersLuck = playersLuck;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        Inventory inventory = Bukkit.createInventory(null, 27, "Top Luck");

        
        Collections.sort(playersLuck, (p1, p2) -> Double.compare(p2.calculateOreRatios().getOrDefault("Global_Ore_Ratio", 0.0), p1.calculateOreRatios().getOrDefault("Global_Ore_Ratio", 0.0)));

        for (int i = 0; i < Math.min(9, playersLuck.size()); i++) {
            PlayerTopLuck playerTopLuck = playersLuck.get(i);
            Map<String, Double> oreRatios = playerTopLuck.calculateOreRatios();
            ItemStack skull = createSkull(playerTopLuck.getName(), oreRatios);
            inventory.setItem(i, skull);
        }

        
        player.openInventory(inventory);
        return true;
    }



    private ItemStack createSkull(String playerName, Map<String, Double> oreRatios) {
    	
        double limite = Bukkit.getPluginManager().getPlugin("TopLuck").getConfig().getDouble("WarningLimit");

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(playerName);
        meta.setDisplayName(playerName);

        List<String> lore = new ArrayList<>();
        
        
        double globalRatio = oreRatios.getOrDefault("Global_Ore_Ratio", 0.0);
        if (globalRatio > limite) {
            lore.add("§c Luck minerais : " + globalRatio);
        } else {
            lore.add("§aGlobal Ore Ratio: " + globalRatio);
        }
        lore.add(""); 

        List<Map.Entry<String, Double>> sortedRatios = new ArrayList<>(oreRatios.entrySet());
        
        sortedRatios.sort(Map.Entry.comparingByValue());
        
        Collections.reverse(sortedRatios);

        
        for (Map.Entry<String, Double> entry : sortedRatios) {
            String oreName = entry.getKey();
            double ratio = entry.getValue();
            if (!oreName.equals("Global_Ore_Ratio")) {
                if (ratio > limite) {
                    lore.add("§c" + oreName + ": " + ratio);
                } else {
                    lore.add(oreName + ": " + ratio);
                }
            } 
        }

        meta.setLore(lore);
        skull.setItemMeta(meta);
        return skull;
    }




    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Top Luck")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                SkullMeta skullMeta = (SkullMeta) event.getCurrentItem().getItemMeta();
                String playerName = skullMeta.getOwner();
                Player targetPlayer = Bukkit.getPlayerExact(playerName);
                Player clicker = (Player) event.getWhoClicked();
                if (targetPlayer != null && clicker != null) {
                    clicker.teleport(targetPlayer.getLocation()); 
                    clicker.sendMessage("Vous avez été téléporté à " + targetPlayer.getName());
                } else {
                    clicker.sendMessage("Le joueur n'est pas en ligne.");
                }
            }
        }
    }

}

