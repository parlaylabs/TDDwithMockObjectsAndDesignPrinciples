///
// Copyright (c) 2015. Highfive Technologies, Inc.
///

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimeBloomFilterHashTest {

  public static final String dummyStr = "test";

  @Test
  public void shouldReturnSameValue() {
    PrimeBloomFilterHash hash = new PrimeBloomFilterHash(31);
    assertEquals(hash.getHash("Test"),hash.getHash("Test"));
  }

  @Test
  public void shouldReturnDistinctValue() {
    PrimeBloomFilterHash hash = new PrimeBloomFilterHash(31);
    int resultOne = hash.getHash(dummyStr);
    int resultTwo = hash.getHash("GianniString");
    assertNotEquals(resultOne, resultTwo);
  }

  @Test
  public void twoHashFunctionsReturnDifferentValues() {
    PrimeBloomFilterHash hash1 = new PrimeBloomFilterHash(31);
    PrimeBloomFilterHash hash2 = new PrimeBloomFilterHash(37);
    assertNotEquals(hash1.getHash(dummyStr), hash2.getHash(dummyStr));
  }

  @Test
  public void hashFunctionShouldRejectNonPrime() {
    int nonPrime = 20;
    try {
      PrimeBloomFilterHash hash = new PrimeBloomFilterHash(nonPrime);
      assertFalse(true);
    } catch(Throwable th) {
      assertTrue(true);
    }
  }
}
