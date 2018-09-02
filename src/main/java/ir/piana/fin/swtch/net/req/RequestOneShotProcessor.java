package ir.piana.fin.swtch.net.req;

import org.jpos.iso.ISOPackager;

import java.net.Socket;

public class RequestOneShotProcessor extends RequestProcessor {
    public RequestOneShotProcessor(
            Socket socket, ISOPackager packager, int headerLength, RequestLengthCoding lengthCoding, String space) {
        super(socket, packager, headerLength, lengthCoding, space);
    }

    @Override
    public void read() {
        super.read();
    }
}
