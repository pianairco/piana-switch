package ir.piana.fin.swtch;

import ir.piana.fin.swtch.net.PianaServer;
import ir.piana.fin.swtch.packager.ShatootPackager;

import java.util.concurrent.Executors;

public class PianaApplication {
    public static void main(String[] args) {
        PianaServer pianaServer = new PianaServer(6708, new ShatootPackager(), 10);

        Executors.newSingleThreadExecutor().execute(pianaServer);
    }
}
