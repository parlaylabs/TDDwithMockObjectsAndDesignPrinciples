import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Bloom {
    private static final int DEFAULT_BITSET_SIZE = 10;
    private final List<BloomFilterHash> hashFunctions;
    private final int bitsetSize;
    private BitSet bitset;

    public Bloom() {
        this(DEFAULT_BITSET_SIZE, Collections.singletonList(new SimpleBloomFilterHash()));
    }

    public Bloom(int bitsetSize, List<BloomFilterHash> hashes) {
        this(new BitSet(bitsetSize), bitsetSize, hashes);
    }

    public Bloom(BitSet initialBitSet, int bitsetSize, List<BloomFilterHash> hashes) {
        this.bitset = initialBitSet;
        this.bitsetSize = bitsetSize;
        this.hashFunctions = hashes;
    }

    public boolean mayContain(String word) {
        List<Integer> result = getBitIndexForWord(word);
        for (Integer val : result) {
            if (!bitset.get(val)) {
                return false;
            }
        }

        return true;
    }

    public void add(String word) {
        List<Integer> result = getBitIndexForWord(word);
        for (Integer val : result) {
            bitset.set(val);
        }
    }

    private List<Integer> getBitIndexForWord(String word) {
        List<Integer> vals = new LinkedList<Integer>();
        for (BloomFilterHash hash : this.hashFunctions) {
            int hashedValue = hash.getHash(word);
            vals.add(hashedValue % bitsetSize); // keep returned hash value within bitmap index length
        }

        return vals;
    }

    public int getNumberOfHashesFunctions() {
        return this.hashFunctions.size();
    }

    public BitSet getBitSet() {
        return bitset;
    }
}
