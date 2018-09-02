package ir.piana.fin.swtch.net.req;

public abstract class RequestLengthEncoder {
    protected int byteCount;

    public RequestLengthEncoder(int byteCount) {
        this.byteCount = byteCount;
    }

    public abstract byte[] encode(int length);
}
