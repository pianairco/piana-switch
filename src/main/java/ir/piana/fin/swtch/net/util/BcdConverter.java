package ir.piana.fin.swtch.net.util;

public class BcdConverter {
    public static byte[] DecimalToBcd(long num) {
        int digits = 0;

        long temp = num;
        while (temp != 0) {
            digits++;
            temp /= 10;
        }

        int byteLen = digits % 2 == 0 ? digits / 2 : (digits + 1) / 2;

        byte bcd[] = new byte[byteLen];

        for (int i = 0; i < digits; i++) {
            byte tmp = (byte) (num % 10);

            if (i % 2 == 0) {
                bcd[i / 2] = tmp;
            } else {
                bcd[i / 2] |= (byte) (tmp << 4);
            }

            num /= 10;
        }

        for (int i = 0; i < byteLen / 2; i++) {
            byte tmp = bcd[i];
            bcd[i] = bcd[byteLen - i - 1];
            bcd[byteLen - i - 1] = tmp;
        }

        return bcd;
    }

    public static long BcdToDecimal(byte[] bcd) {
        return Long.valueOf(BcdConverter.BcdToString(bcd));
    }

    public static String BcdToString(byte bcd) {
        StringBuffer sb = new StringBuffer();

        byte high = (byte) (bcd & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        byte low = (byte) (bcd & 0x0f);

        sb.append(high);
        sb.append(low);

        return sb.toString();
    }

    public static String BcdToString(byte[] bcd) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bcd.length; i++) {
            sb.append(BcdToString(bcd[i]));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        long l = BcdToDecimal(new byte[]{0x00, 0x76});
        System.out.println(l);

        byte[] bytes = DecimalToBcd(76);
        System.out.println(bytes[0] + " " + bytes[1]);
    }
}
