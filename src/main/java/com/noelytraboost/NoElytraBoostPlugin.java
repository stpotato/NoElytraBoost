package com.noelytraboost;

import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class NoElytraBoostPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("NoElytraBoost has been enabled! Firework rocket boost is now disabled for elytra gliding.");
    }

    @Override
    public void onDisable() {
        getLogger().info("NoElytraBoost has been disabled!");
    }

    /**
     * Cancels firework rocket launch while player is gliding with elytra.
     * This is the most reliable method - we cancel the projectile launch event
     * directly, which prevents the rocket from being used and the boost from being
     * applied.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        // Only handle firework rockets
        if (!(event.getEntity() instanceof Firework firework)) {
            return;
        }

        // Check if shooter is a player
        if (!(firework.getShooter() instanceof Player player)) {
            return;
        }

        // Check if player is gliding with elytra
        if (!isGlidingWithElytra(player)) {
            return;
        }

        // Check if the firework was shot from a crossbow
        if (firework.isShotAtAngle()) {
            return;
        }

        // Cancel the projectile launch - this prevents the rocket from being used
        // and the boost from being applied
        event.setCancelled(true);
    }

    /**
     * Checks if a player is currently gliding with an elytra equipped
     *
     * @param player The player to check
     * @return true if player is gliding with elytra, false otherwise
     */
    private boolean isGlidingWithElytra(Player player) {
        // Check if player is gliding
        if (!player.isGliding()) {
            return false;
        }

        // Check if player has elytra in chest slot
        ItemStack chestplate = player.getInventory().getItem(EquipmentSlot.CHEST);
        if (chestplate == null || chestplate.getType() != Material.ELYTRA) {
            return false;
        }

        return true;
    }
}
