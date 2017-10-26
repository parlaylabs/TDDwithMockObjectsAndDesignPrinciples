package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

public class ThresholdTopK {
    private final TopList[] topLists;

    ThresholdTopK(TopList[] inputTopLists){
       this.topLists = inputTopLists;
    }

    TopList[] getTopLists() {
        return topLists;
    }

    int getThresholdForRank(int rank) {
        int sum = 0;
        for (TopList list: topLists) {
            Optional<ItemWithFreq> item = list.getByRank(rank);
            if (item.isPresent()) {
                sum += item.get().getCount();
            }
        }
        return sum;
    }

    int getCountForItem(String key) {
       int sum = 0;
       for (TopList list: topLists) {
           Optional<ItemWithFreq> item = list.getByKey(key);
           if (item.isPresent()) {
               sum += item.get().getCount();
           }
       }
       return sum;
    }

    List<ItemWithFreq> getTopK(int k) {
        List<ItemWithFreq> topK = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        PriorityQueue<ItemWithFreq> buffer = new PriorityQueue<>(k, Comparator.comparingInt(ItemWithFreq::getCount));
        for (int rank = 0, maxRank = getMaxRank(); rank < maxRank && topK.size() < k; ++rank) {
            Stream<ItemWithFreq> itemsWithRank = getItemsWithRank(rank);
            itemsWithRank.forEach(item -> {
                String key = item.getKey();
                if (seen.add(key)) {
                    buffer.add(new ItemWithFreq(key, getCountForItem(key)));
                }
            });
            moveBufferItemsToTopK(topK, buffer, getThresholdForRank(rank));
        }
        return topK;
    }

    private int getMaxRank() {
        return Arrays.stream(topLists).map(TopList::size).max(Integer::compareTo).orElse(0);
    }

    private Stream<ItemWithFreq> getItemsWithRank(final int rank) {
        return Arrays.stream(topLists).map(topList -> topList.getByRank(rank))
                .filter(Optional::isPresent).map(Optional::get);
    }

    private void moveBufferItemsToTopK(List<ItemWithFreq> topK, PriorityQueue<ItemWithFreq> buffer, int threshold) {
        boolean bufferItemIsTopK;
        do {
            Optional<ItemWithFreq> topItem = Optional.ofNullable(buffer.peek());
            bufferItemIsTopK = topItem.isPresent() && topItem.get().getCount() >= threshold;
            if (bufferItemIsTopK) {
                topK.add(topItem.get());
                buffer.remove(topItem.get());
            }
        } while (bufferItemIsTopK);
    }
}

