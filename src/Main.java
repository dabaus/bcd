
public class Main {
    public static void main(String[] args) {
        {
            var encoded = encodeDCD(1337);
            var decoded = decodeBCD(encoded);
            System.out.println("Encoded and decoded: %d".formatted(decoded));
        }
        {
            var encoded = encodeDCD(-42);
            var decoded = decodeBCD(encoded);
            System.out.println("Encoded and decoded: %d".formatted(decoded));
        }
    }

    public static byte[] encodeDCD(int number) {
        var numberAsString =  Integer.toString(number).replaceAll("-", "") ;
        var noDigits = numberAsString.length();

        // Add zero at the end for sign
        numberAsString += "0";
        if((noDigits-1) % 2 != 0) {
            // Pad with zero
            numberAsString = "0" + numberAsString;
        }

        var bcdByte = new byte[(noDigits /= 2) + 1];
        var n = 0;
        var chars =  numberAsString.toCharArray();
        for(int i=0; i<bcdByte.length;i++) {
            bcdByte[i] = (byte)((char2Byte(chars[n++]) << 4 | char2Byte(chars[n++])));
        }
        if(number < 0) {
            // Set last nibble to indicate negation
            bcdByte[bcdByte.length-1] |= 0xD;
        } else {
            bcdByte[bcdByte.length-1] |= 0xC;
        }
        return bcdByte;
    }

    private static byte char2Byte(char c) {
       return (byte)((c - '0') & 0x0F);
    }
    public static int decodeBCD(byte[] bcdByte) {
        int result = 0;
        int pos = 1;

        // Handle last byte
        var lastIndex = bcdByte.length-1;
        int msbLast = (bcdByte[lastIndex] & 0xF0) >> 4;
        int lsbLast = bcdByte[lastIndex] & 0x0F;

        result += msbLast * pos;
        pos *= 10;

        for (int i=bcdByte.length-2; i >= 0; i--) {
            int msb = (bcdByte[i] & 0xF0) >> 4;
            int lsb = bcdByte[i] & 0x0F;
            
            result += lsb * pos;
            pos *= 10;
            result += msb * pos;
            pos *= 10;
        }

        // Check if the last nibble indicates negation
        if (lsbLast == 0xD) {
            result *= -1;
        }
        return result;
    }
}