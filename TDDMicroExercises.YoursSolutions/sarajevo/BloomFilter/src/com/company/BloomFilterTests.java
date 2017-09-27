package com.company;


import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.testng.Assert.assertTrue;

public class BloomFilterTests {

    @Test
    public void testConstructBloomFilter() {
        int bitmapSize = 1024;
        List<HashFunction> hashFunctions = Collections.<HashFunction>singletonList(new HashFunctionA());
        BloomFilter bloomFilter = new BloomFilter(bitmapSize, hashFunctions);
        Assertions.assertEquals(bitmapSize, bloomFilter.getBitmapSize());
    }

    @Test
    public void testCreateBuffer() {
        int bitCount = 1024;
        BloomBuffer buffer = new BloomBufferA(bitCount);
        Assertions.assertEquals(bitCount, buffer.getSize());
    }

    @Test
    public void testAddBitToBuffer(){
        int bitCount = 1024;
        BloomBuffer buffer = new BloomBufferA(bitCount);
        buffer.setBit(1);
        Assertions.assertTrue(buffer.readBit(1));
    }

    @Test
    public void testAddEdgeBitToBuffer() {
        final int bitCount = 1024;
        final BloomBuffer buffer = new BloomBufferA(bitCount);
        Assertions.assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                buffer.setBit(bitCount + 1);
            }
        });
    }

    @Test
    public void testWriteTestHash() {
       HashFunction hashFunction = createMock(HashFunction.class);
       final String CAT = "cat";
        expect(hashFunction.getHashCode( CAT)).andReturn(1).anyTimes();
       replay(hashFunction);
        BloomBufferA bitmap = new BloomBufferA(10);
        BloomFilter filter = new BloomFilter(bitmap, Collections.singletonList(hashFunction));
        filter.addItem(CAT);
        assertTrue(bitmap.readBit(1));
    }
}
