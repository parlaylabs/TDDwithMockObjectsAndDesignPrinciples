import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class BloomTest {

    public static final String TEST_STRING = "testString";
    public static final String OTHER_TEST_STRING = "testString2";
    
    @Test
    public void testConstruction() {
        Bloom bloomFilter = new Bloom(100, Collections.singletonList(new SimpleBloomFilterHash()));
        assertEquals(1, bloomFilter.getNumberOfHashesFunctions());
    }

    @Test
    public void testMayContainShouldReturnFalse() {
        Bloom bloomFilter = new Bloom();
        assertFalse(bloomFilter.mayContain(TEST_STRING));
    }

    // Hash Function Tests
    @Test
    public void hashFunctionShouldReturnInteger() {
        SimpleBloomFilterHash filterHash = new SimpleBloomFilterHash();
        int hashCode = filterHash.getHash(TEST_STRING);
        assertTrue(filterHash.getHash(TEST_STRING) == hashCode);
    }

    @Test
    public void testBitMapInsertion() {
        BloomFilterHash hashFunction = getMockHashFunction(0);
        Bloom bloomFilter = new Bloom(10, Collections.singletonList(hashFunction));
        bloomFilter.add(TEST_STRING);
        BitSet bitset = bloomFilter.getBitSet();
        Assert.assertTrue(bitset.get(0));
    }

    @Test
    public void testMinimumBitsetSize() {
        BitSet bitSet = new BitSet(1);
        Assert.assertEquals(64,bitSet.size());
    }

    @Test
    public void testBitsetSize64() {
        BitSet bitSet = new BitSet(64);
        Assert.assertEquals(64,bitSet.size());
    }

    @Test
    public void testBitsetSize65() {
        BitSet bitSet = new BitSet(65);
        Assert.assertEquals(128,bitSet.size());
    }

    @Test
    public void testBitMapInsertionOutOfRange() {
        BloomFilterHash hashFunction = getMockHashFunction(10);
        Bloom bloomFilter = new Bloom(10, Collections.singletonList(hashFunction));
        bloomFilter.add(TEST_STRING);
        BitSet bitset = bloomFilter.getBitSet();
        Assert.assertTrue(bitset.get(0));
    }

    @Test
    public void testMayContainShouldReturnTrue() {
        BloomFilterHash mockHashFunction = getMockHashFunction(0);
        Bloom bloomFilter = new Bloom(10, Collections.singletonList(mockHashFunction));
        bloomFilter.add(TEST_STRING);
        Assert.assertTrue(bloomFilter.mayContain(TEST_STRING));
    }

    @Test
    public void testMayContainsReturnFalse() {
        BloomFilterHash mockHashFunction = getMockHashFunction(0);
        Bloom bloomFilter = new Bloom(10, Collections.singletonList(mockHashFunction));
        bloomFilter.add(TEST_STRING);
        Assert.assertFalse(bloomFilter.mayContain(OTHER_TEST_STRING));
    }

    @Test
    public void testMultipleHashesExists() {
        BloomFilterHash mockHashFunctionOne = getMockHashFunction(0);
        BloomFilterHash mockHashFunctionTwo = getMockHashFunction(0);
        List<BloomFilterHash> hashes = new ArrayList<BloomFilterHash>();
        Collections.addAll(hashes, mockHashFunctionOne, mockHashFunctionTwo);
        Bloom bloom = new Bloom(10, hashes);
        Assert.assertEquals(2, bloom.getNumberOfHashesFunctions());
    }

    @Test
    public void testMultipleHashesReturnTrue() {
        BloomFilterHash mockHashFunctionOne = getMockHashFunction(0);
        BloomFilterHash mockHashFunctionTwo = getMockHashFunction(1);
        List<BloomFilterHash> hashes = new ArrayList<BloomFilterHash>();
        Collections.addAll(hashes, mockHashFunctionOne, mockHashFunctionTwo);
        Bloom bloom = new Bloom(10, hashes);
        bloom.add(TEST_STRING);
        Assert.assertTrue(bloom.mayContain(TEST_STRING));
    }

    @Test
    public void testMockHashFunctions() {
        Map<String, List<Integer>> testHashMappings = new HashMap<String, List<Integer>>() {{
            put("alice", Arrays.asList(0, 1));
        }};
        List<BloomFilterHash> hashes = getMockHashFunctions(2, testHashMappings);
        Assert.assertEquals(0, hashes.get(0).getHash("alice"));
        Assert.assertEquals(1, hashes.get(1).getHash("alice"));
    }

    @Test
    public void testAddWithMultipleHashFunctions() {
        String alice = "alice";
        Map<String, List<Integer>> testHashMappings = new HashMap<String, List<Integer>>() {{
            put(alice, Arrays.asList(0, 1));
        }};
        List<BloomFilterHash> hashes = getMockHashFunctions(2, testHashMappings);
        Bloom bloom = new Bloom(4, hashes);
        bloom.add(alice);
        Assert.assertTrue(bloom.getBitSet().get(0));
        Assert.assertTrue(bloom.getBitSet().get(1));
        Assert.assertFalse(bloom.getBitSet().get(2));
        Assert.assertFalse(bloom.getBitSet().get(3));
    }

    @Test
    public void testMayContainWithMultipleHashFunctions() {
        String alice = "alice";
        Map<String, List<Integer>> testHashMappings = new HashMap<String, List<Integer>>() {{
            put(alice, Arrays.asList(0, 1));
        }};
        List<BloomFilterHash> hashes = getMockHashFunctions(2, testHashMappings);
        Bloom bloom = new Bloom(4, hashes);
        bloom.add(alice);
        Assert.assertTrue(bloom.mayContain(alice));
    }

    @Test
    public void testDoesNotContainWithMultipleHashFunctions() {
        String alice = "alice";
        String bob = "bob";
        Map<String, List<Integer>> testHashMappings = new HashMap<String, List<Integer>>() {{
            put(alice, Arrays.asList(0, 1));
            put(bob, Arrays.asList(2,3));
        }};
        List<BloomFilterHash> hashes = getMockHashFunctions(2, testHashMappings);
        Bloom bloom = new Bloom(4, hashes);
        bloom.add(alice);
        Assert.assertTrue(bloom.mayContain(alice));
        Assert.assertFalse(bloom.mayContain(bob));
    }
    @Test
    public void testAllBitsMatchExceptOne() {
        String alice = "alice";
        Map<String, List<Integer>> testHashMappings = new HashMap<String, List<Integer>>() {{
            put(alice, Arrays.asList(0, 1));
        }};
        List<BloomFilterHash> hashes = getMockHashFunctions(2, testHashMappings);
        BitSet initialBitSet = new BitSet();
        initialBitSet.set(0);
        Bloom bloom = new Bloom(initialBitSet,4, hashes);
        Assert.assertFalse(bloom.mayContain(alice));
    }

    @Test
    public void testAllBitsMatch() {
        String alice = "alice";
        Map<String, List<Integer>> testHashMappings = new HashMap<String, List<Integer>>() {{
            put(alice, Arrays.asList(0, 1));
        }};
        List<BloomFilterHash> hashes = getMockHashFunctions(2, testHashMappings);
        BitSet initialBitSet = new BitSet();
        initialBitSet.set(0);
        initialBitSet.set(1);
        Bloom bloom = new Bloom(initialBitSet,4, hashes);
        Assert.assertTrue(bloom.mayContain(alice));
    }



    private List<BloomFilterHash> getMockHashFunctions(int numHashFunctions, Map<String, List<Integer>> expectedHashCodes) {
      List<BloomFilterHash> hashes = new ArrayList<>();
      for (int i = 0; i < numHashFunctions; ++i) {
          hashes.add(mock(BloomFilterHash.class));
      }
      for (Map.Entry<String, List<Integer>> entry : expectedHashCodes.entrySet()) {
          for (int i = 0; i < numHashFunctions; ++i) {
              BloomFilterHash bloomFilterHash = hashes.get(i);
              expect(bloomFilterHash.getHash(entry.getKey())).andReturn(entry.getValue().get(i)).anyTimes();
          }

      }
      for(BloomFilterHash hash: hashes) {
          replay(hash);
      }
      return hashes;
    }

    private BloomFilterHash getMockHashFunction(int expectedHashCode) {
        BloomFilterHash hashFunction = mock(BloomFilterHash.class);
        expect(hashFunction.getHash(TEST_STRING)).andReturn(expectedHashCode).anyTimes();
        expect(hashFunction.getHash(OTHER_TEST_STRING)).andReturn(expectedHashCode + 1).anyTimes();
        replay(hashFunction);
        return hashFunction;
    }

}
