package com.gitlab.zachdeibert.GitBackupPlugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class GitBackupPlugin extends JavaPlugin {
    private static final byte       HAS_BEEN_INITED = 1;
    private static final byte       IS_ENABLED      = 2;
    private static final DateFormat DATE_FORMAT     = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private byte state;
    
    private void doBackup(CommandSender sender) {
        Server server = getServer();
        server.dispatchCommand(sender, "save-all");
        try {
            Thread.sleep(5000);
        } catch ( InterruptedException ex ) {
            getLogger().log(Level.SEVERE, "Unable to wait for save to complete", ex);
        }
        byte success = 2;
        try {
            for ( World world : server.getWorlds() ) {
                boolean fail = false;
                try {
                    GitRepo repo = new GitRepo(world.getWorldFolder());
                    fail = !repo.commitAll("[GitBackupPlugin] Backup at ".concat(DATE_FORMAT.format(new Date())));
                } catch ( IllegalArgumentException ex ) {
                    getLogger().log(Level.SEVERE, "Unable to backup world", ex);
                    fail = true;
                }
                if ( fail ) {
                    sender.sendMessage(String.format("Unable to backup world \"%s\"", world.getName()));
                    success = 1;
                }
            }
        } catch ( GitNotFoundException ex ) {
            getLogger().log(Level.SEVERE, "Unable to find Git on the system", ex);
            if ( !(sender instanceof ConsoleCommandSender) ) {
                sender.sendMessage("Unable to find Git on the system");
            }
            success = 0;
        } finally {
            switch ( success ) {
                case 2:
                    sender.sendMessage("Backup complete.");
                    break;
                case 1:
                    sender.sendMessage("Some worlds were not backed up.");
                    break;
                case 0:
                    sender.sendMessage("Backup failed.");
                    break;
            }
        }
    }
    
    @Override
    public boolean isInitialized() {
        return (state & HAS_BEEN_INITED) != 0;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if ( (state & IS_ENABLED) != 0 && command.getName().equalsIgnoreCase("backup") ) {
            doBackup(sender);
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        state ^= IS_ENABLED;
    }

    @Override
    public void onEnable() {
        state |= IS_ENABLED;
        doBackup(getServer().getConsoleSender());
    }

    @Override
    public void onLoad() {
        state |= HAS_BEEN_INITED;
    }
}
