package ir.piana.fin.swtch.net;

import ir.piana.fin.swtch.net.req.RequestLengthCoding;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class PianaClient {

    protected ISOPackager packager;
    protected String host;
    protected int port;
//    protected byte[] header;
    protected int headerLength;
    protected RequestLengthCoding lengthCoding;

//    public PianaClient(String host, int port, ISOPackager packager,
//                       byte[] header,
//                       RequestLengthCoding lengthCoding) {
//        this.host = host;
//        this.port = port;
//        this.packager = packager;
//        this.header = header;
//        this.headerLength = (header == null ? 0 : header.length);
//        this.lengthCoding = lengthCoding;
//    }

    public PianaClient(String host, int port, ISOPackager packager,
                       int headerLength,
                       RequestLengthCoding lengthCoding) {
        this.host = host;
        this.port = port;
        this.packager = packager;
        this.headerLength = headerLength;
        this.lengthCoding = lengthCoding;
    }

    public ISOMsg request(ISOMsg msg) {
        ISOMsg receive = null;
        try {
            Socket socket = new Socket(host, port);

            OutputStream outputStream = socket.getOutputStream();
            msg.setPackager(packager);

            OutChannel outChannel = new OutChannel(socket, headerLength, lengthCoding);
            outChannel.send(msg);


            InChannel inChannel = new InChannel(socket, headerLength, lengthCoding, packager);
            receive = inChannel.receive();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ISOException e) {
            e.printStackTrace();
        }
        return receive;
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
