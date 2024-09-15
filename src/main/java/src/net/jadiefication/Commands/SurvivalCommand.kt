package src.net.jadiefication.Commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class SurvivalCommand: CommandExecutor, TabExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p0 is Player) {
            val player = p0.player

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp ${player?.name} world")
        } else {
            Bukkit.getLogger().info("This command can only be executed by a player.")
        }

        return true
    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>
    ): MutableList<String>? {
        TODO("Not yet implemented")
    }
}