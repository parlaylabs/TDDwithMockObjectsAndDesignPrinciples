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
import static org.testng.Assert.*;

public class BloomFilterTests {

    @Test
    public void testConstructBloomFilter() {
        int bitmapSize = 1024;
        List<HashFunction> hashFunctions = Collections.<HashFunction>singletonList(new HashFunctionA());
        BloomFilter bloomFilter = new BloomFilter(new BloomBufferA(bitmapSize), hashFunctions);
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
    public void testAddOverflowBitToBuffer() {
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
    public void testAddItemShouldSetBit() {
        final String CAT = "cat";
        HashFunction hashFunction = getHashFunction(new String[] { CAT }, new int[] { 1 });
        BloomBufferA bitmap = new BloomBufferA(10);
        BloomFilter filter = new BloomFilter(bitmap, Collections.singletonList(hashFunction));
        filter.addItem(CAT);
        assertTrue(bitmap.readBit(1));
    }

    @Test
    public void testAddItemShouldSetLastBit() {
        final String CAT = "cat";
        HashFunction hashFunction = getHashFunction(new String[] { CAT }, new int[] { 9 });
        BloomBufferA bitmap = new BloomBufferA(10);
        BloomFilter filter = new BloomFilter(bitmap, Collections.singletonList(hashFunction));
        filter.addItem(CAT);
        assertFalse(bitmap.readBit(1));
        assertTrue(bitmap.readBit(9));
    }

    @Test
    public void testAddItemShouldThrow() {
        final String CAT = "cat";
        HashFunction hashFunction = getHashFunction(new String[] { CAT }, new int[] { 10 });
        BloomBufferA bitmap = new BloomBufferA(10);
        BloomFilter filter = new BloomFilter(bitmap, Collections.singletonList(hashFunction));

        Assertions.assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filter.addItem(CAT);
            }
        });
        assertFalse(bitmap.readBit(1));
        assertFalse(bitmap.readBit(10));
    }

    @Test
    public void testBloomFilterMatch() {
        final String CAT = "cat";
        HashFunction hashFunction = getHashFunction(new String[] { CAT }, new int[] { 9 });
        BloomBufferA bitmap = new BloomBufferA(10);
        BloomFilter filter = new BloomFilter(bitmap, Collections.singletonList(hashFunction));
        filter.addItem(CAT);
        assertTrue(filter.matchItem(CAT));
    }

    @Test
    public void testMultipleHashFunctions() {
        final String CAT = "cat";
        HashFunction hashFunction1 = getHashFunction(new String[] { CAT }, new int[] { 9 });
        HashFunction hashFunction2 = getHashFunction(new String[] { CAT }, new int[] { 1 });
        BloomBufferA bitmap = new BloomBufferA(10);
        BloomFilter filter = new BloomFilter(bitmap, Arrays.asList(hashFunction1, hashFunction2));
        filter.addItem(CAT);
        assertTrue(filter.matchItem(CAT));
    }

    @Test
    public void testMultipleHashFunctionsMismatch() {
        final String CAT = "cat";
        final String CAT_SALTED = "cat_salted";
        HashFunction hashFunction1 = getHashFunction(new String[] { CAT, CAT_SALTED }, new int[] { 9, 7 });
        HashFunction hashFunction2 = getHashFunction(new String[] { CAT, CAT_SALTED }, new int[] { 1, 1 });
        BloomBufferA bitmap = new BloomBufferA(10);
        BloomFilter filter = new BloomFilter(bitmap, Arrays.asList(hashFunction1, hashFunction2));
        filter.addItem(CAT);
        assertFalse(filter.matchItem(CAT_SALTED));
    }

    private HashFunction getHashFunction(String[] word, int[] bitPosition) {
        HashFunction hashFunction = createMock(HashFunction.class);
        for (int i = 0; i < word.length; ++i) {
            expect(hashFunction.getHashCode(word[i])).andReturn(bitPosition[i]).anyTimes();
        }
        replay(hashFunction);
        return hashFunction;
    }
}
