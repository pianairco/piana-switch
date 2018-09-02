package ir.piana.fin.swtch.net.req;

import ir.piana.fin.swtch.net.util.BcdConverter;

import java.nio.ByteBuffer;

public enum RequestLengthCoding {
    ASCII_3_DIGIT(new AsciiLengthDecoder(3),
            new AsciiLengthEncoder(2), 3),
    ASCII_4_DIGIT(new AsciiLengthDecoder(4),
            new AsciiLengthEncoder(2), 4),
    BINARY_1_BYTE(new BinaryLengthDecoder(5),
            new BinaryLengthEncoder(1), 1),
    BINARY_2_BYTE(new BinaryLengthDecoder(4),
            new BinaryLengthEncoder(2), 2),
    BCD_1_HEX(new BcdLengthDecoder(1),
            new BcdLengthEncoder(1), 1),
    BCD_2_HEX(new BcdLengthDecoder(2),
            new BcdLengthEncoder(1), 1),
    BCD_3_HEX(new BcdLengthDecoder(3),
            new BcdLengthEncoder(2), 2),
    BCD_4_HEX(new BcdLengthDecoder(4),
            new BcdLengthEncoder(2), 2);

    private RequestLengthDecoder decoder;
    private RequestLengthEncoder encoder;
    private int byteLength;

    RequestLengthCoding(
            RequestLengthDecoder decoder,
            RequestLengthEncoder encoder,
            int byteLength) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.byteLength = byteLength;
    }

    public long decode(byte[] lengthBytes) {
        return decoder.decode(lengthBytes);
    }

    public byte[] encode(int length) {
        return encoder.encode(length);
    }

    public int getByteLength() {
        return byteLength;
    }

    /**
     * Must be completed
     */
    private static class AsciiLengthDecoder extends RequestLengthDecoder {
        public AsciiLengthDecoder(int digit) {
            super(digit);
        }

        @Override
        public long decode(byte[] lengthBytes) {
            return 0;
        }
    }

    /**
     * Must be completed
     */
    private static class BinaryLengthDecoder extends RequestLengthDecoder {
        public BinaryLengthDecoder(int digit) {
            super(digit);
        }

        @Override
        public long decode(byte[] lengthBytes) {
            ByteBuffer wrapped = ByteBuffer.wrap(lengthBytes); // big-endian by default
            return wrapped.getShort();

        }
    }

    private static class BcdLengthDecoder extends RequestLengthDecoder {
        public BcdLengthDecoder(int digit) {
            super(digit);
        }

        @Override
        public long decode(byte[] lengthBytes) {
            return BcdConverter.BcdToDecimal(lengthBytes);
        }
    }

    /**
     * Must be completed
     */
    private static class AsciiLengthEncoder extends RequestLengthEncoder {
        public AsciiLengthEncoder(int byteCount) {
            super(byteCount);
        }

        @Override
        public byte[] encode(int length) {
            return null;
        }
    }

    private static class BinaryLengthEncoder extends RequestLengthEncoder {
        public BinaryLengthEncoder(int byteCount) {
            super(byteCount);
        }

        @Override
        public byte[] encode(int length) {
            ByteBuffer lenBuff = ByteBuffer.allocate(byteCount);
            lenBuff.putShort((short) length);
            byte[] lBytes = lenBuff.array();
            return lBytes;
        }
    }

    private static class BcdLengthEncoder extends RequestLengthEncoder {
        public BcdLengthEncoder(int byteCount) {
            super(byteCount);
        }

        @Override
        public byte[] encode(int length) {
            return BcdConverter.DecimalToBcd(length);
        }
    }
}
