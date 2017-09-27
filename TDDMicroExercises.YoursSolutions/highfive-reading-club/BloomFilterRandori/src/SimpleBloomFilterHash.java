/**
 * Created by ewei on 7/21/17.
 */
public class SimpleBloomFilterHash implements BloomFilterHash {
    @Override
    public int getHash(String word) {
        return Math.abs(word.hashCode());
    }
}
