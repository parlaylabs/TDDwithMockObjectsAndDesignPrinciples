package com.company;

import java.util.Objects;

public class ItemWithFreq implements Comparable<ItemWithFreq> {
    private final String key;
    private final int count;

    public ItemWithFreq(String key, int count) {
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ItemWithFreq other = (ItemWithFreq)obj;
        return Objects.equals(key, other.key) && count == other.count;
    }

    @Override
    public int compareTo(ItemWithFreq o) {
        return o.getCount() - getCount();
    }
}