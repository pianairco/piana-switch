package ir.piana.fin.swtch.net.req;

public abstract class RequestLengthDecoder {
    protected int digit;

    public RequestLengthDecoder(int digit) {
        this.digit = digit;
    }

    public abstract long decode(byte[] lengthBytes);
}
