package com.github.mrcybear.hamming;

public class HammingCode {

    public static int[] getCode(int[] dataBits64) {

        int[] u = dataBits64;

        int p0 = 0;
        p0 ^= p0 ^ u[0] ^ u[1] ^ u[3] ^ u[4] ^ u[6];
        p0 ^= u[8] ^ u[10] ^ u[11] ^ u[13] ^ u[15] ^ u[17];
        p0 ^= u[19] ^ u[21] ^ u[23] ^ u[25] ^ u[26] ^ u[28];
        p0 ^= u[30] ^ u[32] ^ u[34] ^ u[36] ^ u[38] ^ u[40];
        p0 ^= u[42] ^ u[44] ^ u[46] ^ u[48] ^ u[50] ^ u[52];
        p0 ^= u[54] ^ u[56] ^ u[57] ^ u[59] ^ u[61] ^ u[63];

        int p1 = 0;
        p1 ^= (p1 ^ u[0]) ^ (u[2] ^ u[3]) ^ (u[5] ^ u[6]);
        p1 ^= (u[9] ^ u[10]) ^ (u[12] ^ u[13]) ^ (u[16] ^ u[17]);
        p1 ^= (u[20] ^ u[21]) ^ (u[24] ^ u[25]) ^ (u[27] ^ u[28]);
        p1 ^= (u[31] ^ u[32]) ^ (u[35] ^ u[36]) ^ (u[39] ^ u[40]);
        p1 ^= (u[43] ^ u[44]) ^ (u[47] ^ u[48]) ^ (u[51] ^ u[52]);
        p1 ^= (u[55] ^ u[56]) ^ (u[58] ^ u[59]) ^ (u[62] ^ u[63]);

        int p2 = 0;
        p2 ^= (p2 ^ u[1] ^ u[2] ^ u[3]) ^ (u[7] ^ u[8] ^ u[9] ^ u[10]);
        p2 ^= (u[14] ^ u[15] ^ u[16] ^ u[17]) ^ (u[22] ^ u[23] ^ u[24] ^ u[25]);
        p2 ^= (u[29] ^ u[30] ^ u[31] ^ u[32]) ^ (u[37] ^ u[38] ^ u[39] ^ u[40]);
        p2 ^= (u[45] ^ u[46] ^ u[47] ^ u[48]) ^ (u[53] ^ u[54] ^ u[55] ^ u[56]);
        p2 ^= (u[60] ^ u[61] ^ u[62] ^ u[63]);

        int p3 = 0;
        p3 ^= (p3 ^ u[4] ^ u[5] ^ u[6] ^ u[7] ^ u[8] ^ u[9] ^ u[10]);
        p3 ^= (u[18] ^ u[19] ^ u[20] ^ u[21] ^ u[22] ^ u[23] ^ u[24] ^ u[25]);
        p3 ^= (u[33] ^ u[34] ^ u[35] ^ u[36] ^ u[37] ^ u[38] ^ u[39] ^ u[40]);
        p3 ^= (u[49] ^ u[50] ^ u[51] ^ u[52] ^ u[53] ^ u[54] ^ u[55] ^ u[56]);

        int p4 = 0;
        p4 ^= p4 ^ u[11] ^ u[12] ^ u[13] ^ u[14] ^ u[15] ^ u[16] ^ u[17];
        p4 ^= u[18] ^ u[19] ^ u[20] ^ u[21] ^ u[22] ^ u[23] ^ u[24] ^ u[25];
        p4 ^= u[41] ^ u[42] ^ u[43] ^ u[44] ^ u[45] ^ u[46] ^ u[47] ^ u[48];
        p4 ^= u[49] ^ u[50] ^ u[51] ^ u[52] ^ u[53] ^ u[54] ^ u[55] ^ u[56];

        int p5 = 0;
        p5 ^= p5 ^ u[26] ^ u[27] ^ u[28] ^ u[29] ^ u[30] ^ u[31] ^ u[32];
        p5 ^= u[33] ^ u[34] ^ u[35] ^ u[36] ^ u[37] ^ u[38] ^ u[39] ^ u[40];
        p5 ^= u[41] ^ u[42] ^ u[43] ^ u[44] ^ u[45] ^ u[46] ^ u[47] ^ u[48];
        p5 ^= u[49] ^ u[50] ^ u[51] ^ u[52] ^ u[53] ^ u[54] ^ u[55] ^ u[56];

        int p6 = 0;
        p6 ^= p6 ^ u[57] ^ u[58] ^ u[59] ^ u[60] ^ u[61] ^ u[62] ^ u[63];

        return new int[]{p0, p1, p2, p3, p4, p5, p6};
    }

    public static boolean tryCorrectData(int[] dataBits64, int[] hammingCode) {

        int newCheckBits = codeWordToIntegerWord(getCode(dataBits64));
        int receivedCheckBits = codeWordToIntegerWord(hammingCode);

        int syndrome = newCheckBits ^ receivedCheckBits;

        if (syndrome == 0) {
            return false;
        }

        if ((syndrome & (syndrome - 1)) == 0) {
            int checkBitIndex = 0;
            while (syndrome != 0) {
                syndrome >>= 1;
                checkBitIndex++;
            }
            hammingCode[checkBitIndex - 1] ^= 0x01;
            return true;
        } else {
            int biasedIndex = syndrome;

            int bitIndex;
            if (biasedIndex == 3) {
                bitIndex = 0;
            } else if (biasedIndex >= 5 && biasedIndex <= 7) {
                bitIndex = biasedIndex - 4;
            } else if (biasedIndex >= 9 && biasedIndex <= 15) {
                bitIndex = biasedIndex - 5;
            } else if (biasedIndex >= 17 && biasedIndex <= 31) {
                bitIndex = biasedIndex - 6;
            } else if (biasedIndex >= 33 && biasedIndex <= 63) {
                bitIndex = biasedIndex - 7;
            } else if (biasedIndex >= 65 && biasedIndex <= 71) {
                bitIndex = biasedIndex - 8;
            } else {
                return false;
            }

            dataBits64[bitIndex] ^= 0x01;

            return true;
        }
    }

    public static byte[] dataBits64ToDataBytes8(int[] dataBits64) {
        byte[] dataBytes8 = new byte[8];
        for (int byteIndex = 0, dataBits64Offset = 0; byteIndex < 8; byteIndex++) {
            int nextByte = 0;
            for (int bitIndex = 0; bitIndex < 8; bitIndex++) {
                nextByte |= (dataBits64[dataBits64Offset++] << bitIndex);
            }
            dataBytes8[byteIndex] = (byte) nextByte;
        }
        return dataBytes8;
    }

    public static int[] dataBytes8ToDataBits64(byte[] dataBytes8) {
        int[] dataBits64 = new int[64];
        for (int byteIndex = 0, dataBits64Offset = 0; byteIndex < 8; byteIndex++) {
            for (int bitIndex = 0, eightDataBits = dataBytes8[byteIndex]; bitIndex < 8; bitIndex++) {
                dataBits64[dataBits64Offset++] = eightDataBits & 0x01;
                eightDataBits >>= 1;
            }
        }
        return dataBits64;
    }

    private static int[] integerWordToCodeWord(int value) {
        int[] bits = new int[7];
        for (int bitIndex = 0; bitIndex < 7; bitIndex++) {
            bits[bitIndex] = value & 0x01;
            value >>= 1;
        }
        return bits;
    }

    private static int codeWordToIntegerWord(int[] code) {
        int checkBits = 0;
        int bitMask = 0x01;
        for (int bitIndex = 0; bitIndex < 7; bitIndex++, bitMask <<= 1) {
            if (code[bitIndex] != 0) {
                checkBits |= bitMask;
            }
        }
        return checkBits;
    }
}
