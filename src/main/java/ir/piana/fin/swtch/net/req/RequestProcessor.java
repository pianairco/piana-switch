package ir.piana.fin.swtch.net.req;

import ir.piana.fin.swtch.net.OutChannel;
import ir.piana.fin.swtch.net.PianaISOSpace;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public abstract class RequestProcessor implements Runnable {
    protected Socket socket;
    protected ISOPackager packager;
    protected int headerLength;
    protected RequestLengthCoding lengthCoding;
    protected String space;

    public RequestProcessor(
            Socket socket, ISOPackager packager, int headerLength, RequestLengthCoding lengthCoding, String space) {
        this.socket = socket;
        this.packager = packager;
        this.headerLength = headerLength;
        this.lengthCoding = lengthCoding;
        this.space = space;
    }

    @Override
    public void run() {
        read();
    }

    public void read() {
        int read = 0;
        byte[] msg = null;
        byte[] header = null;
        byte[] lengthBytes = null;
        InputStream is = null;
        try {
            is = socket.getInputStream();
            int count = 0;
            long messageLength = 0;
            if(lengthCoding != null) {
                lengthBytes = new byte[lengthCoding.getByteLength()];
                read = is.read(lengthBytes, 0, lengthCoding.getByteLength());
//                read = is.read(b, count, lengthDigit);
                count += read;
                messageLength = lengthCoding.decode(lengthBytes);
            }

            if(headerLength > 0) {
                header = new byte[headerLength];
                read = is.read(header, 0, headerLength);
                count += read;
            }

            msg = new byte[(int)(messageLength - headerLength)];
            read = is.read(msg, 0, (int)(messageLength - headerLength));
            count += read;

            ISOMsg isoMsg = packager.createISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.unpack(msg);
            isoMsg.setHeader(header);
            isoMsg.setSource(new OutChannel(socket, headerLength, lengthCoding));
            isoMsg.setDirection(0);

            PianaISOSpace.getInstance().put(space, isoMsg);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }

    public void printRawBytes(byte[] b, int len) {
        for(int i = 0; i < len;  i++) {
            System.out.print(String.format("%02X", b[i]));
            if (i > 0 && (i + 1) % 2 == 0)
                System.out.print(", ");
            if (i > 0 && (i + 1) % 16 == 0)
                System.out.print("\n");
        }
    }
}
