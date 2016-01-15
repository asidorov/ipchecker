package com.alexsoft.ipchecker;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

import sun.net.util.IPAddressUtil;

public class IPRangeValidator {

    private static byte[] HIGH_32_BITS = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1};
    private static BigInteger HIGH_32_INT = new BigInteger(HIGH_32_BITS);
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

    public static boolean checkTwoRangesOverlap(String cidrOne, String cidrTwo) throws UnknownHostException {
        Range rangeOne = convertCIDRIPStringToRange(cidrOne);
        Range rangeTwo = convertCIDRIPStringToRange(cidrTwo);
        return rangeOne.isOverlapWithRange(rangeTwo);
    }

    private static Range convertCIDRIPStringToRange(String cidr) throws UnknownHostException {
        int ipIdx = cidr.lastIndexOf("/");
        if (ipIdx > 1) {
            String addr = cidr.substring(0, ipIdx);
            String sMask = cidr.substring(ipIdx + 1);
            int iMask = Integer.valueOf(sMask).intValue();
            try {
                byte[] b = InetAddress.getByName(addr).getAddress();
                if (b.length == 8) {
                    if (iMask <= 128 && iMask >= 0) {
                        BigInteger addrBigInt = new BigInteger(b);
                        BigInteger mask = HIGH_128_INT.shiftLeft(128 - iMask);
                        BigInteger lowerIp = addrBigInt.and(mask);
                        BigInteger higherIp = lowerIp.add(mask.not());
                        return new RangeIPv6(lowerIp, higherIp);
                    }
                } else if (b.length == 4){
                    if (iMask < 32 && iMask >= 0) {
                        SubnetUtils utils = new SubnetUtils(cidr);
                        SubnetInfo info = utils.getInfo();
                        long lowerIp = info.asInteger(info.getLowAddress()) & 0x00000000ffffffffL;
                        long higherIp = info.asInteger(info.getHighAddress()) & 0x00000000ffffffffL;
                        return new RangeIPv4(lowerIp, higherIp);
                    } else {
                        long addrInt = ((b[0] << 24) & 0x00000000FF000000L) 
                                     | ((b[1] << 16) & 0x00000000FF0000L) 
                                     | ((b[2] << 8) & 0x00000000FF00L) 
                                     | (b[3] & 0x00000000FFL);
                            return new RangeIPv4(addrInt, addrInt);
                    }
                }
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            byte[] b = InetAddress.getByName(cidr).getAddress();
            if (b.length == 8) {
                BigInteger addrBigInt = new BigInteger(b);
                return new RangeIPv6(addrBigInt, addrBigInt);
            } else {
                long addrInt = ((b[0] << 24) & 0x00000000FF000000L) 
                             | ((b[1] << 16) & 0x00000000FF0000L) 
                             | ((b[2] << 8) & 0x00000000FF00L) 
                             | (b[3] & 0x00000000FFL);
               return new RangeIPv4(addrInt, addrInt);
            }
        }
        return null;
    }
}
