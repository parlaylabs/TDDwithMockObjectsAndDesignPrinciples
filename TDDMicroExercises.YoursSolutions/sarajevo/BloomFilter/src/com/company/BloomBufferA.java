package com.company;

import java.util.BitSet;

public class BloomBufferA implements BloomBuffer {
    private BitSet bitSet;

    public BloomBufferA(int size) {
        this.bitSet = new BitSet(size);
    }

    @Override
    public void setBit(int bit) {
        if (bit < 0 || bit > this.bitSet.size()) {
            throw new IndexOutOfBoundsException("Out of range");
        }
        this.bitSet.set(bit);
    }

    @Override
    public boolean readBit(int bit) {
        if (bit < 0 || bit > this.bitSet.size()) {
            return false;
        }
        return this.bitSet.get(bit);
    }

    @Override
    public int getSize() {
        return this.bitSet.size();
    }
}
