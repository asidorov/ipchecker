package com.alexsoft.ipchecker;


public class RangeIPv4 implements Range {

    private final int start;
    private final int end;

    public RangeIPv4(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isOverlapWithRange(Range range) {
        RangeIPv4 rangeIPv4 = (RangeIPv4) range;
        int otherRangeStart = rangeIPv4.getStart();
        if (otherRangeStart >= start && otherRangeStart <= end) {
            return true;
        }
        return false;
    }

}
