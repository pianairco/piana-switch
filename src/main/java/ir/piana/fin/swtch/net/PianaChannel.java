package ir.piana.fin.swtch.net;

import ir.piana.fin.swtch.net.req.RequestLengthCoding;

import java.net.Socket;

public class PianaChannel {
    protected Socket socket;
    protected int headerLength;
    protected RequestLengthCoding lengthCoding;

    PianaChannel(Socket socket, int headerLength, RequestLengthCoding lengthCoding) {
        this.socket = socket;
        this.headerLength = headerLength;
        this.lengthCoding = lengthCoding;
    }


}
