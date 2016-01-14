package com.alexsoft.ipchecker;

import java.math.BigInteger;

public class RangeIPv6 implements Range {

    private final BigInteger start;
    private final BigInteger end;

    public RangeIPv6(BigInteger start, BigInteger end) {
        this.start = start;
        this.end = end;
    }

    public BigInteger getStart() {
        return start;
    }

    public BigInteger getEnd() {
        return end;
    }

    public boolean isOverlapWithRange(Range range) {
        RangeIPv6 rangeIPv6 = (RangeIPv6) range;
        BigInteger otherRangeStart = rangeIPv6.getStart();
        if (otherRangeStart.compareTo(start) >= 0 && otherRangeStart.compareTo(end) <= 0) {
            return true;
        }
        return false;
    }

}
