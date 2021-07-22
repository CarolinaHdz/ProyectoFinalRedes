/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Raid5 extends OutputStream {

    public final static int PARITY_MARKER = 1;
    public final static int DATA_MARKER = 0;
    private File disks[];
    private FileOutputStream streams[];
    private int mode = 0;
    private final static int MAX_MODE = 2;
    private int[] buffer = new int[2];

    public Raid5(File disks[]) throws IOException {
        super();
        if (disks.length != 3) {
            throw new RuntimeException("we need a disk count of x times 3");
        }
        int i = 0;
        this.disks = new File[disks.length];
        this.streams = new FileOutputStream[disks.length];
        for (File f : disks) {
            if (!f.exists() && !f.createNewFile()) {
                throw new RuntimeException(f.getAbsolutePath() + " does not exists and cannot be created!");
            } else {
                System.out.println("using " + f.getAbsolutePath() + "\n");
                this.disks[i] = f;
                this.streams[i] = new FileOutputStream(f);
            }
            i++;
        }
        writerMarker();
    }

    private void writerMarker() throws IOException {
        for (int i = 0; i < this.streams.length; i++) {
            if (i % 3 == 0) {
                this.streams[i].write(PARITY_MARKER);
            } else {
                this.streams[i].write(DATA_MARKER);
            }
        }
    }

    @Override
    public void write(int i) throws IOException {
        switch (mode) {
            case MAX_MODE:
                this.streams[mode].write(buffer[0] ^ buffer[1]);
                mode = 0;
            default:
                this.buffer[mode] = i;
                this.streams[mode].write(i);
                mode++;
        }
    }
}
