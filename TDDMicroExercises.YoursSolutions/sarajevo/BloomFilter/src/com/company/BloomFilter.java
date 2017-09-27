package com.company;

import java.util.List;

public class BloomFilter {
    private final BloomBufferA bitmap;

    public BloomFilter(BloomBufferA bitmap, List<HashFunction> hashFunctions) {
        this.bitmap = bitmap;
    }

    public int getBitmapSize() {
        return bitmap.getSize();
    }

    public void addItem(String word) {

    }

    public boolean matchItem(String word) {
        return false;
    }
}
