package ir.piana.fin.swtch.net.req;

import ir.piana.fin.swtch.net.PianaISOSpace;
import org.jpos.iso.ISOPackager;

import java.net.Socket;

public class RequestProcessorBuilder {
    private Socket socket;
    private ISOPackager packager;
    private int headerLength;
    private RequestLengthCoding lengthCoding;
    private String space;
    private boolean isPermanent;

    public RequestProcessorBuilder(Socket socket, ISOPackager packager) {
        this.socket = socket;
        this.packager = packager;
        this.space = PianaISOSpace.DEFAULT_SPACE;
    }

    public RequestProcessorBuilder setLength(
            RequestLengthCoding lengthCoding) {
        this.lengthCoding = lengthCoding;
        return this;
    }

    public RequestProcessorBuilder setHeaderLength(
            int headerLength) {
        this.headerLength = headerLength;
        return this;
    }

    public RequestProcessorBuilder setSpace(String space) {
        this.space = space;
        return this;
    }

    public RequestProcessorBuilder setPermanent(boolean isPermanent) {
        this.isPermanent = isPermanent;
        return this;
    }

    public RequestProcessor build() {
        if(isPermanent)
            return new RequestOneShotProcessor(socket, packager, headerLength, lengthCoding, space);
        else
            return new RequestPermanentReadProcessor(socket, packager, headerLength, lengthCoding, space);
    }
}
