package com.gitlab.zachdeibert.GitBackupPlugin;

import java.io.File;
import java.io.IOException;

public class GitRepo {
    private static Boolean gitFound = null;
    private final File repo;
    
    private static void print(Process proc) {
        new PrintThread(proc.getInputStream(), System.out).start();
        new PrintThread(proc.getErrorStream(), System.err).start();
    }
    
    private static int run(String cmd[], File dir) {
        try {
            Runtime rt   = Runtime.getRuntime();
            Process proc = rt.exec(cmd, null, dir);
            print(proc);
            return proc.waitFor();
        } catch ( IOException|InterruptedException ex ) {
            return -1;
        }
    }
    
    public static boolean checkForGit() {
        if ( gitFound == null ) {
            gitFound = run(new String[] {
                "git", "--version"
            }, new File(".")) == 0;
        }
        return gitFound;
    }
    
    public boolean commitAll(String message) {
        if ( run(new String[] {
                "git", "add", "--all"
            }, repo) == 0 ) {
            if ( run(new String[] {
                    "git", "commit", "-m", message
                }, repo) == 0 ) {
                return true;
            }
        }
        return false;
    }
    
    public GitRepo(File dir) throws GitNotFoundException, IllegalArgumentException {
        if ( dir.isFile() ) {
            throw new IllegalArgumentException("dir must be a directory!");
        }
        if ( !dir.exists() ) {
            dir.mkdirs();
        }
        if ( !checkForGit() ) {
            throw new GitNotFoundException("Could not find Git on the system.");
        }
        if ( !new File(String.format("%s%c.git", dir.getAbsolutePath(), File.separatorChar)).exists() ) {
            if ( run(new String[] {
                    "git", "init"
                }, dir) != 0 ) {
                throw new GitNotFoundException("Git failed to init");
            }
        }
        repo = dir;
    }
}
