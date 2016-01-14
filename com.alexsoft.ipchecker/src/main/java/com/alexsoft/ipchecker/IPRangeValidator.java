package com.alexsoft.ipchecker;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import sun.net.util.IPAddressUtil;

public class IPRangeValidator {

    //IPv6
    private static byte[] HIGH_128_BITS = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    private static BigInteger HIGH_128_INT = new BigInteger(HIGH_128_BITS);

    private static List<Range> ranges = new LinkedList<Range>();

    public static boolean checkCIDRRangesOverlap(List<String> cidrList) {
        for (String cidr : cidrList) {
            try {
                ranges.add(convertCIDRIPStringToRange(cidr));
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        for (int i = 0; i < ranges.size(); i++) {
            Range currentIterRange = ranges.get(i);
            for (int k = i + 1; k < ranges.size(); k++) {
                if (currentIterRange.isOverlapWithRange(ranges.get(k))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Range convertCIDRIPStringToRange(String cidr) throws UnknownHostException {
        int ipIdx = cidr.lastIndexOf("/");
        if (ipIdx > 1) {
            String addr = cidr.substring(0, ipIdx);
            String sMask = cidr.substring(ipIdx + 1);
            int iMask = Integer.valueOf(sMask).intValue();
            try {
                byte[] b = InetAddress.getByName(addr).getAddress();
                if (IPAddressUtil.isIPv6LiteralAddress(addr)) {
                    if (iMask <= 128 && iMask >= 0) {
                        BigInteger addrBigInt = new BigInteger(b);
                        BigInteger mask = HIGH_128_INT.shiftLeft(128 - iMask);
                        BigInteger lowerIp = addrBigInt.and(mask);
                        BigInteger higherIp = lowerIp.add(mask.not());
                        return new RangeIPv6(lowerIp, higherIp);
                    }
                } else {
                    if (iMask <= 32 && iMask >= 0) {
                        if (b.length == 4) {
                            int addrInt = ((b[0] << 24) & 0xFF000000) 
                                    | ((b[1] << 16) & 0xFF0000) 
                                    | ((b[2] << 8) & 0xFF00) 
                                    | (b[3] & 0xFF);
                            int mask = 0;
                            if (iMask != 0) {
                                mask = ((2147483647) << (32 - iMask));
                            }
                            int lowerIp = addrInt & mask;
                            int reversedMask = Integer.reverse(mask);
                            int higherIp = Integer.valueOf(lowerIp | (~mask)).intValue();
                            return new RangeIPv4(lowerIp, higherIp);
                        }
                    }
                }
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            byte[] b = InetAddress.getByName(cidr).getAddress();
            if (IPAddressUtil.isIPv6LiteralAddress(cidr)) {
                BigInteger addrBigInt = new BigInteger(b);
                return new RangeIPv6(addrBigInt, addrBigInt);
            } else {
                if (b.length == 4) {
                int addrInt = ((b[0] << 24) & 0xFF000000) 
                            | ((b[1] << 16) & 0xFF0000) 
                            | ((b[2] << 8) & 0xFF00) 
                            | (b[3] & 0xFF);
                        return new RangeIPv4(addrInt, addrInt);
                   
                }
            }
        }
        return null;
    }
}
