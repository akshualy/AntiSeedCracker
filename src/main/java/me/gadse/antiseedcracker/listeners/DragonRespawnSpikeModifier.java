package me.gadse.antiseedcracker.listeners;

import me.gadse.antiseedcracker.AntiSeedCracker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.persistence.PersistentDataType;

public class DragonRespawnSpikeModifier implements Listener {

    private final AntiSeedCracker plugin;
    private boolean taskScheduled = false;

    public DragonRespawnSpikeModifier(AntiSeedCracker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPlaceRespawnCrystals(EntityPlaceEvent event) {
        World world = event.getEntity().getWorld();
        if (event.getEntityType() != EntityType.END_CRYSTAL
                || world.getEnvironment() != World.Environment.THE_END
                || event.getBlock().getType() != Material.BEDROCK
                || isOutsidePortalRadius(event.getBlock().getLocation())
                || getAmountOfEnderCrystalsOnPortal(world) != 3
                || !plugin.getConfig().getStringList("modifiers.end_spikes.worlds").contains(world.getName())
                || taskScheduled) {
            return;
        }
        world.getPersistentDataContainer().set(plugin.getModifiedSpike(), PersistentDataType.BOOLEAN, false);

        taskScheduled = true;
        plugin.getServer().getScheduler().runTaskTimer(plugin, task -> {
            DragonBattle dragonBattle = world.getEnderDragonBattle();
            if (dragonBattle == null) {
                // Fall-back, should not be reachable.
                plugin.modifyEndSpikes(world);
                return;
            }

            if (dragonBattle.getRespawnPhase() == DragonBattle.RespawnPhase.START
                    || dragonBattle.getRespawnPhase() == DragonBattle.RespawnPhase.PREPARING_TO_SUMMON_PILLARS
                    || dragonBattle.getRespawnPhase() == DragonBattle.RespawnPhase.SUMMONING_PILLARS) {
                return;
            }

            plugin.modifyEndSpikes(world);
            taskScheduled = false;
            task.cancel();
        }, 300L, 20L);
    }

    private int getAmountOfEnderCrystalsOnPortal(World world) {
        Location endLocation = new Location(world, 0, 65, 0);
        return world.getNearbyEntities(
                endLocation, 7, 3, 7, entity -> entity instanceof EnderCrystal
                            && entity.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK
        ).size();
    }

    private boolean isOutsidePortalRadius(Location location) {
        return location.getX() < -3 || location.getX() > 3 || location.getZ() < -3 || location.getZ() > 3;
    }

    public void unregister() {
        EntityPlaceEvent.getHandlerList().unregister(this);
    }
}
