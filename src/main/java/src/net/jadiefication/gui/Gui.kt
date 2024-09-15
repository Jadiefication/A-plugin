package src.net.jadiefication.gui

import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType
import org.bukkit.Bukkit
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.NotePlayEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import src.net.jadiefication.Commands.LobbyCommand
import src.net.jadiefication.Commands.SurvivalCommand
import java.util.*

class Gui: JavaPlugin(), Listener {

    private var api: UltimateAdvancementAPI = UltimateAdvancementAPI.getInstance(this)

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
        registerAdvancements()
        registerCommands()
    }

    private fun registerAdvancements() {
        val namespace = "gui2"
        val advancementTab = api.createAdvancementTab(namespace)

        val rootDisplay = AdvancementDisplay(
            Material.NOTE_BLOCK, "We are the future",
            AdvancementFrameType.CHALLENGE, true, true, 0f, 0f, "Join the server for the first time."
        )

        val texturePath = "textures/block/stone.png"
        val rootKey = "globe_root"
        val rootAdvancement = RootAdvancement(advancementTab, rootKey, rootDisplay, texturePath)

        try {
            advancementTab.registerAdvancements(rootAdvancement)
            logger.info("Root advancement registered successfully.")
        } catch (e: Exception) {
            logger.severe("Error registering RootAdvancement: " + e.message)
            e.printStackTrace()
            Bukkit.getPluginManager().disablePlugin(this)
        }
    }

    private fun createAdvancement(
        icon: Material, key: String, displayName: String, description: String,
        frameType: AdvancementFrameType, parent: Advancement
    ): BaseAdvancement {
        val display = AdvancementDisplay(
            icon, displayName, frameType, true, true, 0f, 0f,
            description
        )
        return BaseAdvancement(key, display, parent, 1)
    }

    private fun registerCommands() {
        if (getCommand("lobby") != null) {
            Objects.requireNonNull(getCommand("lobby"))?.setExecutor(LobbyCommand())
        } else {
            logger.warning("Lobby command not found in plugin.yml")
        }
        if (getCommand("survivallobby") != null) {
            Objects.requireNonNull(getCommand("survivallobby"))?.setExecutor(SurvivalCommand())
        } else {
            logger.warning("Survival command not found in plugin.yml")
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val tab = api.getAdvancementTab("gui2")
        if (tab != null) {
            if (!player.hasPlayedBefore()) {
                tab.rootAdvancement.grant(player)
                logger.info("Granted root advancement to " + player.name)
            }
        } else {
            logger.warning("AdvancementTab 'gui2' not found when player " + player.name + " joined.")
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock
        if (block != null && block.type == Material.NOTE_BLOCK) {
            val noteBlock = block.blockData as NoteBlock
            if (noteBlock.instrument == Instrument.PLING && noteBlock.note.id.toInt() == 7) {
                event.isCancelled = true
                val player = event.player
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "dm open signgui " + player.name)
            }
        }
    }

    @EventHandler
    fun onNotePlay(event: NotePlayEvent) {
        val block = event.block
        if (block.type == Material.NOTE_BLOCK) {
            val noteBlock = block.blockData as NoteBlock
            if (noteBlock.instrument == Instrument.PLING &&
                (noteBlock.note.id.toInt() == 7 || noteBlock.note.id.toInt() == 6)
            ) {
                event.isCancelled = true
                logger.info("Cancelled note change on PLING instrument")
            }
        }
    }

    override fun onDisable() {
        Bukkit.getPluginManager().disablePlugin(this)
    }
}