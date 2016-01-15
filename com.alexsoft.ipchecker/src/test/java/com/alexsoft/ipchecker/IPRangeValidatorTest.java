package com.alexsoft.ipchecker;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class IPRangeValidatorTest {

    @Test
    public void test() {
        //fail("Not yet implemented");
        List<String> ranges = new LinkedList<String>();
        ranges.add("0.0.0.0/0");
        ranges.add("192.168.0.2/31");
        assertTrue(IPRangeValidator.checkCIDRRangesOverlap(ranges));
        try {
            assertTrue(IPRangeValidator.checkTwoRangesOverlap("192.168.0.1", "192.168.0.1"));
            assertTrue(IPRangeValidator.checkTwoRangesOverlap("192.168.0.1", "192.168.0.1/32"));
            assertTrue(IPRangeValidator.checkTwoRangesOverlap("192.168.0.1", "192.168.0.1/2"));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //assertFalse(IPRangeValidator.checkCIDRRangesOverlap(ranges));

    }

}
