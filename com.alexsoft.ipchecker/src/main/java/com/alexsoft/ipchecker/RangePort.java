package com.alexsoft.ipchecker;


public class RangePort implements Range {

    private final int start;
    private final int end;

    public RangePort(int start, int end) {
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
        RangePort rangePort = (RangePort) range;
        int otherRangeStart = rangePort.getStart();
        int otherRangeEnd = rangePort.getEnd();
        if ((otherRangeStart >= start && otherRangeStart <= end)
          ||(start >= otherRangeStart && start <= otherRangeEnd)) {
            return true;
        }
        return false;
    }

}