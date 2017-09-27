package com.company;

import java.util.List;
import java.util.stream.Stream;

public class BloomFilter {
    private final BloomBuffer bitmap;
    private final List<HashFunction> hashFunctions;

    public BloomFilter(BloomBuffer bitmap, List<HashFunction> hashFunctions) {
        this.bitmap = bitmap;
        this.hashFunctions = hashFunctions;
    }

    public int getBitmapSize() {
        return bitmap.getSize();
    }

    public void addItem(String word) {
        Stream<Integer> hashCodes = getHashCodes(word);
        hashCodes.forEach(bitmap::setBit);
    }

    private Stream<Integer> getHashCodes(String word) {
        return hashFunctions.stream().map(hashFunction -> hashFunction.getHashCode(word));
    }

    public boolean matchItem(String word) {
        Stream<Integer> hashCodes = getHashCodes(word);
        return hashCodes.allMatch(bitmap::readBit);
    }
}
