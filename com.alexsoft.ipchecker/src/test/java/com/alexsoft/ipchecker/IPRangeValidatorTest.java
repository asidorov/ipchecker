package com.alexsoft.ipchecker;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.alexsoft.ipchecker.IPRangeValidator.Rule;

public class IPRangeValidatorTest {

    @Test
    public void test() {
        //fail("Not yet implemented");
        List<String> ranges = new LinkedList<String>();
        ranges.add("0.0.0.0/0");
        ranges.add("192.168.0.2/31");
        assertTrue(IPRangeValidator.checkCIDRRangesOverlap(ranges));
        try {
            assertTrue(IPRangeValidator.checkTwoIPRangesOverlap("192.168.0.1", "192.168.0.1"));
            assertTrue(IPRangeValidator.checkTwoIPRangesOverlap("192.168.0.1", "192.168.0.1/32"));
            assertTrue(IPRangeValidator.checkTwoIPRangesOverlap("192.168.0.1", "192.168.0.1/2"));
            assertTrue(IPRangeValidator.checkTwoIPRangesOverlap("192.168.0.1/2", "192.168.0.1/3"));
            assertTrue(IPRangeValidator.checkTwoIPRangesOverlap("192.168.0.1", "0.0.0.0/0"));
            assertFalse(IPRangeValidator.checkTwoIPRangesOverlap("192.168.0.1", "192.168.0.2"));
            assertFalse(IPRangeValidator.checkTwoIPRangesOverlap("192.168.0.1", "192.168.1.1"));
            assertFalse(IPRangeValidator.checkTwoIPRangesOverlap("192.168.0.1/31", "192.168.1.1/31"));
            
            assertFalse(IPRangeValidator.checkTwoPortRangesOverlap("1", "65535"));
            assertFalse(IPRangeValidator.checkTwoPortRangesOverlap("1:65534", "65535"));
            assertFalse(IPRangeValidator.checkTwoPortRangesOverlap("1:65533", "65534:65535"));
            assertTrue(IPRangeValidator.checkTwoPortRangesOverlap("1", "1"));
            assertTrue(IPRangeValidator.checkTwoPortRangesOverlap("1:65535", "20"));
            assertTrue(IPRangeValidator.checkTwoPortRangesOverlap("1:100", "20:50"));
            assertTrue(IPRangeValidator.checkTwoPortRangesOverlap("1:100", "1:100"));
            assertTrue(IPRangeValidator.checkTwoPortRangesOverlap("1:100", "1:50"));
            
            assertTrue(IPRangeValidator.checkTwoProtocolTypesOverlap("TCP", "TCP"));
            assertTrue(IPRangeValidator.checkTwoProtocolTypesOverlap("UDP", "UDP"));
            assertTrue(IPRangeValidator.checkTwoProtocolTypesOverlap("ANY", "TCP"));
            assertTrue(IPRangeValidator.checkTwoProtocolTypesOverlap("ANY", "UDP"));
            assertTrue(IPRangeValidator.checkTwoProtocolTypesOverlap("TCP", "ANY"));
            assertTrue(IPRangeValidator.checkTwoProtocolTypesOverlap("UDP", "ANY"));
            assertFalse(IPRangeValidator.checkTwoProtocolTypesOverlap("TCP", "UDP"));
            assertFalse(IPRangeValidator.checkTwoProtocolTypesOverlap("UDP", "TCP"));

            IPRangeValidator.Rule ruleOne = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "ANY", "0.0.0.0", "IPv4");
            IPRangeValidator.Rule ruleTwo = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "ANY", "0.0.0.0", "IPv4");
            assertTrue(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));
            
            ruleOne = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "TCP", "0.0.0.0", "IPv4");
            ruleTwo = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "UDP", "0.0.0.0", "IPv4");
            assertFalse(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));

            ruleOne = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "ANY", "0.0.0.0", "IPv4");
            ruleTwo = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "UDP", "10.10.10.10", "IPv4");
            assertTrue(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));

            ruleOne = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "ANY", "10.10.10.20", "IPv4");
            ruleTwo = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "UDP", "10.10.10.10", "IPv4");
            assertFalse(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));
            
            ruleOne = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "ANY", "10.10.10.10", "IPv4");
            ruleTwo = new Rule("0.0.0.0/0", "1000", "192.168.0.1", "443", "UDP", "10.10.10.10", "IPv4");
            assertTrue(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));
            
            ruleOne = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "ANY", "10.10.10.10", "IPv4");
            ruleTwo = new Rule("0.0.0.0/0", "1000", "192.168.0.1", "444", "UDP", "10.10.10.10", "IPv4");
            assertFalse(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));
            
            ruleOne = new Rule("0.0.0.0/0", "1:65535", "192.168.0.1", "443", "ANY", "10.10.10.10", "IPv4");
            ruleTwo = new Rule("0.0.0.0/0", "1000", "192.168.0.1", "444", "UDP", "10.10.10.10", "IPv4");
            assertFalse(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));
            
            ruleOne = new Rule("192.168.0.1", "11111", "172.148.36.20", "443", "ANY", "10.10.10.10", "IPv4");
            ruleTwo = new Rule("192.168.0.2", "11111", "172.148.36.20", "443", "ANY", "10.10.10.10", "IPv4");
            assertFalse(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));
            
            ruleOne = new Rule("2a00:2381:757:50:216:3eff:fe00:16a", "11111", "2a00:2381:757:50:216:3eff:fe00:16b", "443", "ANY", "::", "IPv6");
            ruleTwo = new Rule("2a00:2381:757:50:216:3eff:fe00:16a", "11111", "2a00:2381:757:50:216:3eff:fe00:16b", "443", "ANY", "::", "IPv6");
            assertTrue(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));
            
            ruleOne = new Rule("2a00:2381:757:50:216:3eff:fe00:16a", "11111", "::/0", "443", "ANY", "::", "IPv6");
            ruleTwo = new Rule("2a00:2381:757:50:216:3eff:fe00:16a", "11111", "2a00:2381:757:50:216:3eff:fe00:16b", "443", "TCP", "::", "IPv6");
            assertTrue(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));
            
            ruleOne = new Rule("::/0", "1:65535", "::/0", "1:65535", "ANY", "::", "IPv6");
            ruleTwo = new Rule("2a00:2381:757:50:216:3eff:fe00:16a", "11111", "2a00:2381:757:50:216:3eff:fe00:16b", "443", "TCP", "::", "IPv6");
            assertTrue(IPRangeValidator.checkIfRuleOverlaps(ruleOne, ruleTwo));
            
            
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //assertFalse(IPRangeValidator.checkCIDRRangesOverlap(ranges));

    }

}
