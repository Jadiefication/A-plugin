package src.net.jadiefication.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StaffModeCommand implements CommandExecutor, TabExecutor {

    private static final Map<Map<UUID, Boolean>, Inventory> playerInventory = new HashMap<>();
    private static final Map<UUID, Boolean> staffModeMap = new HashMap<>();

    public static Map<Map<UUID, Boolean>, Inventory> getPlayerInventory() {
        return playerInventory;
    }

    public static boolean isStaffMode(Player player) {
        return staffModeMap.getOrDefault(player.getUniqueId(), false);
    }

    public static void setStaffMode(Player player, boolean staffMode) {
        staffModeMap.put(player.getUniqueId(), staffMode);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player mainPlayer = null;
        boolean found = true;
        if (commandSender instanceof Player player) {
            if (player.hasPermission("gui2.staffmode")) {
                if (strings.length != 0) {
                    String playerName = strings[0];
                    for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                        if (onlinePlayer.getName().equalsIgnoreCase(playerName)) {
                            mainPlayer = onlinePlayer;
                        } else {
                            found = false;
                        }
                    }
                    if (!found) {
                        player.sendMessage("Player not found.");
                    }
                } else {
                    mainPlayer = player;
                }

                assert mainPlayer != null;
                setStaffMode(mainPlayer,true);

                Map<UUID, Boolean> playerMap = new HashMap<>();

                playerMap.put(mainPlayer.getUniqueId(), isStaffMode(mainPlayer));
                playerInventory.put(playerMap, mainPlayer.getInventory());

                setItems(mainPlayer);
                setPermissions(mainPlayer);
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
        } else {
            commandSender.sendMessage("Only players can run this command!");
        }

        return true;
    }


    private void setPermissions(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setInvulnerable(true);
    }

    private void setItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
        player.getInventory().setItem(2, new ItemStack(Material.BLAZE_ROD));
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
