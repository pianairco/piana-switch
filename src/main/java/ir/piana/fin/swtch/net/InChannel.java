package ir.piana.fin.swtch.net;

import ir.piana.fin.swtch.net.PianaChannel;
import ir.piana.fin.swtch.net.req.RequestLengthCoding;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class InChannel extends PianaChannel {
    protected ISOPackager packager;

    public InChannel(Socket socket, int headerLength, RequestLengthCoding lengthCoding, ISOPackager packager) {
        super(socket, headerLength, lengthCoding);
        this.packager = packager;
    }

    public ISOMsg receive()
            throws IOException, ISOException {
        if(socket == null)
            throw new ISOException("isoMsg or socket is null");
        InputStream is = socket.getInputStream();
        ISOMsg isoMsg = packager.createISOMsg();
        isoMsg.setPackager(packager);

        byte[] lByte = new byte[lengthCoding.getByteLength()];
        int count = is.read(lByte, 0, lengthCoding.getByteLength());
        long length = lengthCoding.decode(lByte);

        byte[] hBytes = null;
        if(headerLength > 0) {
            hBytes = new byte[headerLength];
            count += is.read(hBytes, 0, headerLength);
        }

        byte[] mBytes = new byte[(int)length - headerLength];
        count += is.read(mBytes, 0, mBytes.length);

        if(hBytes != null)
            isoMsg.setHeader(hBytes);
        isoMsg.unpack(mBytes);
        return isoMsg;
    }
}
