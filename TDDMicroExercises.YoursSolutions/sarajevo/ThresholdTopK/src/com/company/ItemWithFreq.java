package com.company;

public class ItemWithFreq {
    private final String key;
    private final int count;

    public String getKey() {
        return key;
    }

    public ItemWithFreq(String key, int count) {
        this.key = key;
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}