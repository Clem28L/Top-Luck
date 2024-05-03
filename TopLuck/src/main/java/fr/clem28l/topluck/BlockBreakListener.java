package fr.clem28l.topluck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BlockBreakListener implements Listener {

    private final List<PlayerTopLuck> playersLuck = new ArrayList<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();
        final Material block = event.getBlock().getType();

        final List<String> blocksList = Bukkit.getPluginManager().getPlugin("TopLuck").getConfig().getStringList("BlockList.BlockListMinerais");

        final List<String> blockBase = Bukkit.getPluginManager().getPlugin("TopLuck").getConfig().getStringList("BlockList.BlockListBase");

        PlayerTopLuck playerTopLuck = getPlayerTopLuck(playerUUID);

        if (blockBase.contains(block.toString()) || blocksList.contains(block.toString())) {
            if (playerTopLuck != null) {

                playerTopLuck.incrementTotalBlocksBroken();


                if (block.toString().endsWith("_ORE")) {
                    playerTopLuck.incrementOreBlocksBroken(block.toString());
                }
            } else {

                playerTopLuck = new PlayerTopLuck(playerUUID, player.getName(), blocksList);

                playerTopLuck.incrementTotalBlocksBroken();

                if (block.toString().endsWith("_ORE")) {
                    playerTopLuck.incrementOreBlocksBroken(block.toString());
                }
                playersLuck.add(playerTopLuck);
            }

            final Map<String, Double> ratio = playerTopLuck.calculateOreRatios();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();


        final PlayerTopLuck playerTopLuck = getPlayerTopLuck(playerUUID);
        if (playerTopLuck != null) {
            playersLuck.remove(playerTopLuck);
        }
    }

    public List<PlayerTopLuck> getPlayersLuck() {
        return playersLuck;
    }

    private PlayerTopLuck getPlayerTopLuck(UUID playerUUID) {
        for (final PlayerTopLuck playerTopLuck : playersLuck) {
            if (playerTopLuck.getUUID().equals(playerUUID)) {
                return playerTopLuck;
            }
        }
        return null;
    }
}
