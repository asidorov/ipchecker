package com.alexsoft.ipchecker;


public class RangeIPv4 implements Range {

    private final long start;
    private final long end;

    public RangeIPv4(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public boolean isOverlapWithRange(Range range) {
        RangeIPv4 rangeIPv4 = (RangeIPv4) range;
        long otherRangeStart = rangeIPv4.getStart();
        if (otherRangeStart >= start && otherRangeStart <= end) {
            return true;
        }
        return false;
    }

}