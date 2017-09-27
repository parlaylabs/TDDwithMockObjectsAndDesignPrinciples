package com.company;

public interface BloomBuffer {

    public void setBit(int bit);
    public boolean readBit(int bit);

    int getSize();
}
