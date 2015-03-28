package com.gitlab.zachdeibert.GitBackupPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PrintThread extends Thread {
    private static final int   BUFFER_SIZE = 256;
    private final InputStream  in;
    private final OutputStream out;
    
    @Override
    public void run() {
        int n = 0;
        byte buffer[] = new byte[BUFFER_SIZE];
        while ( n >= 0 ) {
            try {
                n = in.read(buffer, 0, BUFFER_SIZE);
                if ( n > 0 ) {
                    out.write(buffer, 0, n);
                }
            } catch ( IOException ex ) {
                n = -1;
            }
        }
    }
    
    public PrintThread(InputStream in, OutputStream out) {
        this.in  = in;
        this.out = out;
    }
}
