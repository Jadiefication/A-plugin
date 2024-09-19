package src.net.jadiefication.Placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import src.net.jadiefication.gui.Gui;

public class Suffix extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "gui2";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Jadiefication";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {

        if(player == null) {
            return "";
        }

        if (params.equals("suffix")) {
            return Gui.getSuffix(player);
        }

        return null;
    }
}
