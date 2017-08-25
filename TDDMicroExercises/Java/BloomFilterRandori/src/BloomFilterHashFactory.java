/**
 * Created by ewei on 7/21/17.
 */
public class BloomFilterHashFactory {
    public BloomFilterHash getFilterHash() {
        return new SimpleBloomFilterHash();
    }
}
