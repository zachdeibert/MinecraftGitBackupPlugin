package com.gitlab.zachdeibert.GitBackupPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class GitBackupPlugin extends JavaPlugin {
    private static final byte HAS_BEEN_INITED = 1;
    private static final byte IS_ENABLED      = 2;
    private byte state;
    
    @Override
    public boolean isInitialized() {
        return (state & HAS_BEEN_INITED) != 0;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
                    final String label, final String[] args) {
        return false;
    }

    @Override
    public void onDisable() {
        state ^= IS_ENABLED;
    }

    @Override
    public void onEnable() {
        state |= IS_ENABLED;
    }

    @Override
    public void onLoad() {
        state |= HAS_BEEN_INITED;
    }
}
