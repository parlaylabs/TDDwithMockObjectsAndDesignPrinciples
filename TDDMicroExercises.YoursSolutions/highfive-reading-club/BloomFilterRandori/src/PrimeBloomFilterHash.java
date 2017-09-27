///
// Copyright (c) 2015. Highfive Technologies, Inc.
///

public class PrimeBloomFilterHash implements BloomFilterHash {

  public private int hashPrime;

  public PrimeBloomFilterHash(int hashPrime) {
    this.hashPrime = hashPrime;
  }

  @Override
  public int getHash(String word) {
    char[] value = word.toCharArray();
    int h = 0;
    if (value.length > 0) {
      for (int i = 0; i < value.length; i++) {
        h = hashPrime * h + value[i];
      }
    }
    return h;
  }
}
