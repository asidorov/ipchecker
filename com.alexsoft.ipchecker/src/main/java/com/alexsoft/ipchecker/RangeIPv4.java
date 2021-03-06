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
        long otherRangeEnd = rangeIPv4.getEnd();
        if ((otherRangeStart >= start && otherRangeStart <= end)
          ||(start >= otherRangeStart && start <= otherRangeEnd)) {
            return true;
        }
        return false;
    }

}