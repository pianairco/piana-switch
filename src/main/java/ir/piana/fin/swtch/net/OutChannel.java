package ir.piana.fin.swtch.net;

import ir.piana.fin.swtch.net.req.RequestLengthCoding;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class OutChannel extends PianaChannel implements ISOSource {

    public OutChannel(Socket socket, int headerLength, RequestLengthCoding lengthCoding) {
        super(socket, headerLength, lengthCoding);
    }

    public void send(ISOMsg isoMsg)
            throws IOException, ISOException {
        if(isoMsg == null || socket == null)
            throw new ISOException("isoMsg or socket is null");
        byte[] mBytes = isoMsg.getPackager().pack(isoMsg);
        byte[] hBytes = null;
        if(isoMsg.getHeader() != null)
            hBytes = isoMsg.getHeader();
        short length = (short)(mBytes.length + (hBytes != null ? hBytes.length : 0));

        byte[] lBytes = lengthCoding.encode(length);
//        ByteBuffer lenBuff = ByteBuffer.allocate(lengthCoding.getByteLength());
//        lenBuff.putShort(length);
//        byte[] lBytes = lenBuff.array();

        byte[] bytes = new byte[length + lBytes.length];
//        System.arraycopy(lBytes, 0, bytes, 0, 2);
//        System.arraycopy(hBytes, 0, bytes, lBytes.length, hBytes.length);
//        System.arraycopy(mBytes, 0, bytes, lBytes.length + hBytes.length, mBytes.length);

        OutputStream outputStream = socket.getOutputStream();

        outputStream.write(lBytes);
        if(hBytes != null)
            outputStream.write(hBytes);
        outputStream.write(mBytes);
//        outputStream.write(bytes);
        outputStream.flush();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        outputStream.close();
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }
}
