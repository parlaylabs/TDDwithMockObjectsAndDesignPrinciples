package com.company;

import java.util.*;

public class ThresholdTopK {
    private final TopList[] topLists;

    public ThresholdTopK (TopList[] inputTopLists){
       this.topLists = inputTopLists;
    }

    public TopList[] getTopLists() {
        return topLists;
    }

    public int getThresholdForRank(int rank) {
        int sum = 0;
        for (TopList list: topLists) {
            sum += list.getByRank(rank).get().getCount();
        }
        return sum;
    }

    public int getCountForItem(String key) {
       int sum = 0;
       for (TopList list: topLists){
           sum += list.getByKey(key).get().getCount();
       }
       return sum;
    }

    public List<ItemWithFreq> getTopK(int k) {
        List<ItemWithFreq> topK = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        List<ItemWithFreq> buffer = new ArrayList<>();
        int rank=0;
        while (k > 0) {
            int threshold = getThresholdForRank(rank);
            for (TopList topList: topLists) {
                if (topList.size() > rank) {
                    ItemWithFreq item = topList.getByRank(rank).get();
                    String key = item.getKey();
                    if (seen.add(key)) {
                        buffer.add(new ItemWithFreq(key, getCountForItem(key)));
                    }
                }
            }
            if (threshold == 0) {
                break;
            }
            buffer.sort((a,b) -> b.getCount() - a.getCount());
            List<ItemWithFreq> tmpBuffer = new ArrayList<>(buffer);
            for (ItemWithFreq item: buffer) {
                if (item.getCount() >= threshold && k > 0) {
                    topK.add(item);
                    tmpBuffer.remove(item);
                    k--;
                } else {
                    break;
                }
            }
            buffer = tmpBuffer;
            rank++;
        }
        return topK;
    }
}

