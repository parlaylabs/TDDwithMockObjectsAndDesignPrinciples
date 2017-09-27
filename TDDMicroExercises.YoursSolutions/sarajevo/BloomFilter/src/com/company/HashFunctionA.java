package com.company;

public class HashFunctionA implements HashFunction {
    @Override
    public int getHashCode(String str) {
        return str.hashCode();
    }
}
