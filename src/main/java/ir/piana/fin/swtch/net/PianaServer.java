package ir.piana.fin.swtch.net;

import ir.piana.fin.swtch.net.req.RequestLengthCoding;
import ir.piana.fin.swtch.net.req.RequestProcessor;
import ir.piana.fin.swtch.net.req.RequestProcessorBuilder;
import ir.piana.fin.swtch.packager.ShatootPackager;
import org.jpos.iso.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PianaServer implements Runnable {

    protected ExecutorService executorService;
    protected ISOPackager packager;
    protected int port;
    protected boolean isPermanent;

    public PianaServer(int port, ISOPackager packager, int poolSize) {
        this(port, packager, poolSize, false);
    }

    public PianaServer(int port, ISOPackager packager, int poolSize, boolean isPermanent) {
        this.port = port;
        this.packager = packager;
        executorService = Executors.newFixedThreadPool(poolSize);
        this.isPermanent = isPermanent;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("PianaServer started...");

            for(;;) {
                Socket socket = serverSocket.accept();
                System.out.println("PianaServer accepted new connection from " +
                        socket.getInetAddress().getHostAddress() + " : " +
                        socket.getPort());
                executorService.execute(new RequestProcessorBuilder(socket, packager)
                        .setLength(RequestLengthCoding.BINARY_2_BYTE)
                        .setHeaderLength(5)
                        .setPermanent(isPermanent)
                        .build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void read(Socket socket)
//            throws IOException {
//        int read;
//        byte[] b = new byte[1024];
//        InputStream is = socket.getInputStream();
//        OutputStream os = socket.getOutputStream();
//        for(;;) {
//            read = is.read(b);
//
//            System.out.println("byte received = " + read);
//            for(int i = 0; i < read;  i++) {
//                System.out.print(String.format("%02X", b[i]));
//                if(i > 0 && (i + 1) % 2 == 0)
//                    System.out.print(", ");
//                if(i > 0 && (i + 1) % 16 == 0)
//                    System.out.print("\n");
//            }
//
////            byte[] bytes = new byte[msg810.length * 2];
////            ByteBuffer.wrap(bytes).asCharBuffer().put(msg810);
//
//            ISOPackager packager = new ShatootPackager();
//            ISOMsg isoMsg = packager.createISOMsg();
//            isoMsg.setPackager(packager);
//            try {
//                byte[] bytes = new byte[b.length - 7];
//                System.arraycopy(b, 7, bytes, 0, b.length - 7);
//                isoMsg.unpack(bytes);
//            } catch (ISOException e) {
//                e.printStackTrace();
//            }
//
//            isoMsg.unset(4);
//            isoMsg.unset(42);
//            isoMsg.unset(55);
//
//            try {
//                byte[] pack = isoMsg.pack();
//                System.out.println(pack.length);
//
//                isoMsg.setHeader(new byte[] {0x60, 0x00, 0x00, 0x00, 0x00});
//                byte[] pack1 = isoMsg.pack();
//                System.out.println(pack1.length);
//            } catch (ISOException e) {
//                e.printStackTrace();
//            }
//
//            os.write(b);
//            os.flush();
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            os.close();
//        }
//    }
}
