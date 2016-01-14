package com.alexsoft.ipchecker;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class IPRangeValidatorTest {

    @Test
    public void test() {
        //fail("Not yet implemented");
        List<String> ranges = new LinkedList<String>();
        ranges.add("0.0.0.0/0");
        ranges.add("192.168.0.2/32");
        //assertTrue(IPRangeValidator.checkCIDRRangesOverlap(ranges));
        assertFalse(IPRangeValidator.checkCIDRRangesOverlap(ranges));
    }

}
