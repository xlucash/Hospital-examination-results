package me.lukaszpisarczyk.Hospital.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilsTest {
    @Test
    void testCompressionDecompressionCycle() throws Exception {
        byte[] originalData = "Test data".getBytes();
        byte[] compressedData = ImageUtils.compressImage(originalData);
        byte[] decompressedData = ImageUtils.decompressImage(compressedData);

        assertArrayEquals(originalData, decompressedData, "Original data and decompressed data should be equal");
    }
}