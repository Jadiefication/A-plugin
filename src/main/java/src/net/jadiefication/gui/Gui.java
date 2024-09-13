package src.net.jadiefication.gui;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import src.net.jadiefication.Commands.LobbyCommand;
import src.net.jadiefication.Commands.SurvivalCommand;

import java.util.Objects;

public final class Gui extends JavaPlugin implements Listener {

    private UltimateAdvancementAPI api;

    @Override
    public void onEnable() {
        try {
            getServer().getPluginManager().registerEvents(this, this);
            api = UltimateAdvancementAPI.getInstance(this);
            if (api == null) {
                getLogger().severe("Failed to initialize UltimateAdvancementAPI. Make sure it's installed and loaded.");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            registerAdvancement();
            createCommands();
        } catch (Exception e) {
            getLogger().severe("Error during plugin initialization:");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerAdvancement() {
        String namespace = "gui2";
        AdvancementTab advancementTab = api.createAdvancementTab(namespace);

        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.NOTE_BLOCK, "We are the future",
                AdvancementFrameType.CHALLENGE, true, true, 0, 0, "Join the server for the first time.");

        String texturePath = "textures/block/stone.png";
        String rootKey = "globe_root";
        RootAdvancement rootAdvancement = new RootAdvancement(advancementTab, rootKey, rootDisplay, texturePath);

        try {
            advancementTab.registerAdvancements(rootAdvancement);
            getLogger().info("Root advancement registered successfully.");
        } catch (Exception e) {
            getLogger().severe("Error registering RootAdvancement: " + e.getMessage());
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private BaseAdvancement createAdvancement(Material icon, String key, String displayName, String description,
            AdvancementFrameType frameType, Advancement parent) {
        AdvancementDisplay display = new AdvancementDisplay(icon, displayName, frameType, true, true, 0, 0,
                description);
        return new BaseAdvancement(key, display, parent, 1);
    }

    private void createCommands() {
        if (getCommand("lobby") != null) {
            Objects.requireNonNull(getCommand("lobby")).setExecutor(new LobbyCommand());
        } else {
            getLogger().warning("Lobby command not found in plugin.yml");
        }
        if (getCommand("survival") != null) {
            Objects.requireNonNull(getCommand("survival")).setExecutor(new SurvivalCommand());
        } else {
            getLogger().warning("Survival command not found in plugin.yml");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AdvancementTab tab = api.getAdvancementTab("gui2");
        if (tab != null) {
            tab.getRootAdvancement().grant(player);
            getLogger().info("Granted root advancement to " + player.getName());
        } else {
            getLogger().warning("AdvancementTab 'gui2' not found when player " + player.getName() + " joined.");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.NOTE_BLOCK) {
            NoteBlock noteBlock = (NoteBlock) block.getBlockData();
            if (noteBlock.getInstrument() == Instrument.PLING && noteBlock.getNote().getId() == 7) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "dm open signgui " + player.getName());
            }
        }
    }

    @EventHandler
    public void onNotePlay(NotePlayEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock noteBlock = (NoteBlock) block.getBlockData();
            if (noteBlock.getInstrument() == Instrument.PLING &&
                    (noteBlock.getNote().getId() == 7 || noteBlock.getNote().getId() == 6)) {
                event.setCancelled(true);
                getLogger().info("Cancelled note change on PLING instrument");
            }
        }
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
