package fr.clem28l.topluck;

import org.bukkit.plugin.java.JavaPlugin;

public class TopLuck extends JavaPlugin {

    @Override
    public void onEnable() {
        final BlockBreakListener blockBreakListener = new BlockBreakListener();
        this.getServer().getPluginManager().registerEvents(blockBreakListener, this);
        final TopLuckCommand topLuckCommand = new TopLuckCommand(blockBreakListener.getPlayersLuck());
        this.getCommand("topluck").setExecutor(topLuckCommand);
        this.getServer().getPluginManager().registerEvents(topLuckCommand, this);
        
        this.saveDefaultConfig();
        
        final ConfigUtil config = new ConfigUtil(this, "config.yml");
        config.getConfig().options().copyDefaults(true);
        config.save();
        
        System.out.println("Le plugin vient de s'activer");
    }

    @Override
    public void onDisable() {
        System.out.println("Le plugin vient de se désactiver");
    }
}
