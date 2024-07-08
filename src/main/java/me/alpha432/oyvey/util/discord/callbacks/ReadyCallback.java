package me.alpha432.oyvey.util.discord.callbacks;

import me.alpha432.oyvey.util.discord.DiscordUser;
import com.sun.jna.Callback;

public interface ReadyCallback extends Callback {
    void apply(final DiscordUser p0);
}
