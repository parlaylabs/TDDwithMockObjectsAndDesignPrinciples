package com.company;

import java.util.BitSet;

public class BloomBufferA implements BloomBuffer {
    private BitSet bitSet;
    private int size;

    public BloomBufferA(int size) {
        this.bitSet = new BitSet(size);
        this.size = size;
    }

    private int getIndexWithinRange(int position) {
        int mult = Math.abs(position / size) + 1;
        return (size * mult + position) % size;
    }

    @Override
    public void setBit(int bit) {
        this.bitSet.set(getIndexWithinRange(bit));
    }

    @Override
    public boolean readBit(int bit) {
        return this.bitSet.get(getIndexWithinRange(bit));
    }

    @Override
    public int getSize() {
        return size;
    }
}
