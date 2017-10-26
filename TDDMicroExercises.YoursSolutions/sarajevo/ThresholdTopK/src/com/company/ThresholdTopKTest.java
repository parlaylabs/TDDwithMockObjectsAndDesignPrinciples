package com.company;


import org.easymock.EasyMock;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

public class ThresholdTopKTest {
    private final String BARBY = "Barby";
    private final String ELMO = "Elmo";
    private final String GOKU = "Goku";

    @Test
    public void testConstruction () {
        TopList topList1 = createTopList(new ItemWithFreq[] {});
        TopList topList2 = createTopList(new ItemWithFreq[] {});
        ThresholdTopK thresholdTopK = createThresholdTopK(new TopList[] {
            topList1,
            topList2
        });
        assertEquals(topList1, thresholdTopK.getTopLists()[0]);
        assertEquals(topList2, thresholdTopK.getTopLists()[1]);
    }

    @Test
    public void testCalculateThresholdForRank0() {
        ThresholdTopK thresholdTopK = createThresholdTopK(new TopList[] {
                createTopList(new ItemWithFreq[] {
                    new ItemWithFreq(BARBY, 50)
                }),
                createTopList(new ItemWithFreq[] {
                    new ItemWithFreq(ELMO, 10)
                })
        });
        assertEquals(60, thresholdTopK.getThresholdForRank(0));
    }

    @Test
    public void testSumAcrossListsByKey(){
        ThresholdTopK thresholdTopK = createThresholdTopK(new TopList[] {
                createTopList(new ItemWithFreq[] {
                        new ItemWithFreq(BARBY, 50),
                        new ItemWithFreq(ELMO, 10)
                }),
                createTopList(new ItemWithFreq[] {
                        new ItemWithFreq(ELMO, 40),
                        new ItemWithFreq(BARBY, 30)
                })
        });
        assertEquals (80, thresholdTopK.getCountForItem(BARBY));
        assertEquals (50, thresholdTopK.getCountForItem(ELMO));
    }

    @Test
    public void testSumAcrossTopListsWithItemsMissing(){
        ThresholdTopK thresholdTopK = createThresholdTopK(new TopList[] {
                createTopList(new ItemWithFreq[] {
                        new ItemWithFreq(BARBY, 50),
                        new ItemWithFreq(ELMO, 10)
                }),
                createTopList(new ItemWithFreq[] {
                        new ItemWithFreq(BARBY, 0),
                        new ItemWithFreq(ELMO, 0)
                })
        });
        assertEquals (50, thresholdTopK.getCountForItem(BARBY));
        assertEquals (10, thresholdTopK.getCountForItem(ELMO));
    }

    @Test
    public void testGetTopKForEmptyLists() {
        ThresholdTopK thresholdTopK = createThresholdTopK(new TopList[] {});
        int k = 5;
        List<ItemWithFreq> topK = thresholdTopK.getTopK(k);
        assertTrue(topK.isEmpty());
    }

    @Test
    public void testGetTopKForSingleList() {
        ItemWithFreq elmo = new ItemWithFreq(ELMO, 40);
        ItemWithFreq barby = new ItemWithFreq(BARBY, 30);
        ItemWithFreq goku = new ItemWithFreq(GOKU, 20);
        ThresholdTopK thresholdTopK = createThresholdTopK(new TopList[] {
                createTopList(new ItemWithFreq[]{
                        elmo, barby, goku
                })
        });
        int k = 2;
        List<ItemWithFreq> topK = thresholdTopK.getTopK(k);
        assertEquals(k, topK.size());
        assertEquals(elmo.getKey(), topK.get(0).getKey());
        assertEquals(barby.getKey(), topK.get(1).getKey());
    }

    @Test
    public void testGetTopKForMultipleLists() {
        ItemWithFreq elmo = new ItemWithFreq(ELMO, 40);
        ItemWithFreq barby = new ItemWithFreq(BARBY, 30);
        ItemWithFreq goku = new ItemWithFreq(GOKU, 20);
        ItemWithFreq barby2 = new ItemWithFreq(BARBY, 50);
        ItemWithFreq elmo2 = new ItemWithFreq(ELMO, 20);
        ItemWithFreq goku2 = new ItemWithFreq(GOKU, 10);
        ThresholdTopK thresholdTopK = createThresholdTopK(new TopList[] {
                createTopList(new ItemWithFreq[]{
                        elmo, barby, goku
                }),
                createTopList(new ItemWithFreq[]{
                        barby2, elmo2, goku2
                })
        });
        int k = 2;
        List<ItemWithFreq> topK = thresholdTopK.getTopK(k);
        assertEquals(k, topK.size());
        assertEquals(barby.getKey(), topK.get(0).getKey());
        assertEquals(elmo.getKey(), topK.get(1).getKey());
    }

    private void addItem(TopList topList, int rank, ItemWithFreq item){
        expect(topList.getByRank(rank)).andReturn(Optional.of(item)).anyTimes();
        expect(topList.getByKey(item.getKey())).andReturn(Optional.of(item)).anyTimes();
    }

    private TopList createTopList(ItemWithFreq[] items) {
        TopList topList = EasyMock.createMock(TopList.class);
        for (int rank = 0; rank < items.length; ++rank) {
            addItem(topList, rank, items[rank]);
        }
        expect(topList.size()).andReturn(items.length).anyTimes();
        replay(topList);
        return topList;
    }

    private ThresholdTopK createThresholdTopK(TopList[] topLists) {
        return new ThresholdTopK(topLists);
    }
}
