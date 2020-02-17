package com.github.mrcybear.hamming;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class HammingCodeTest {

    final int[] EXPECTED_CODE = {0, 1, 0, 1, 0, 1, 0};
    int[] dataBits;

    @Before
    public void prepareDataBits() {
        dataBits = new int[64];
        for (int i = 0; i < dataBits.length; i++) {
            dataBits[i] = (i % 2 == 0) ? 0x00 : 0x01;
        }
    }

    @Test
    public void generatedHammingCodeIsAsExpected() {
        int[] code = HammingCode.getCode(dataBits);
        assertArrayEquals(code, EXPECTED_CODE);
    }

    @Test
    public void oneBitErrorInDataIsCorrected() {
        int[] code = HammingCode.getCode(dataBits);
        int[] originalDataBits = Arrays.copyOf(dataBits, dataBits.length);
        dataBits[47] ^= 0x01; // corrupt one data bit
        assertTrue(HammingCode.tryCorrectData(dataBits, code));
        assertArrayEquals(dataBits, originalDataBits);
    }

    @Test
    public void oneBitErrorInCodeIsCorrected() {
        int[] code = HammingCode.getCode(dataBits);
        int[] originalCode = Arrays.copyOf(code, code.length);
        code[0] = 1; // corrupt one code word
        assertTrue(HammingCode.tryCorrectData(dataBits, code));
        assertArrayEquals(code, originalCode);
    }
}
