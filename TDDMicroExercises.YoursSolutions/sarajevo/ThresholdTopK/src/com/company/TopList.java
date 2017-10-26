package com.company;

import java.util.Optional;

public interface TopList {
    Optional<ItemWithFreq> getByRank(int i);
    Optional<ItemWithFreq> getByKey(String key);
    int size();
}
