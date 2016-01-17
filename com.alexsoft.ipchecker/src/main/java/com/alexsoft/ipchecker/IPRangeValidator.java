package com.alexsoft.ipchecker;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.ws.handler.PortInfo;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

import sun.net.util.IPAddressUtil;

public class IPRangeValidator {
	
	public static class Rule {
		public final String clientIp;
		public final String clientPort;
		public final String destinationIp;
		public final String destinationPort;
		public final String protocolType;
		public final String interfaceIp;
		public final String ipType;
		
		public Rule(String clientIp,
				    String clientPort,
				    String destinationIp,
				    String destinationPort,
				    String protocolType,
				    String interfaceIp,
				    String ipType) {
			this.clientIp = clientIp;
			this.clientPort = clientPort;
			this.destinationIp = destinationIp;
			this.destinationPort = destinationPort;
			this.protocolType = protocolType;
			this.interfaceIp = interfaceIp;
			this.ipType = ipType;
		}
	}

    //IPv6
    private static byte[] HIGH_128_BITS = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    private static BigInteger HIGH_128_INT = new BigInteger(HIGH_128_BITS);

    private static List<Range> ranges = new LinkedList<Range>();
    
    
    public static boolean checkIfRuleOverlaps(Rule ruleOne, Rule ruleTwo) {
    	String interfaceIpOne = ruleOne.interfaceIp;
    	String interfaceIpTwo = ruleTwo.interfaceIp;
    	if (ruleOne.interfaceIp.equalsIgnoreCase("0.0.0.0")) {
    		interfaceIpOne = "0.0.0.0/0";
    	} else if (ruleOne.interfaceIp.equalsIgnoreCase("::")) {
    		interfaceIpOne = "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF/0";
    	}
    	if (ruleTwo.interfaceIp.equalsIgnoreCase("0.0.0.0")) {
    		interfaceIpTwo = "0.0.0.0/0";
    	} else if (ruleTwo.interfaceIp.equalsIgnoreCase("::")) {
    		interfaceIpTwo = "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF/0";
    	}

    	try {
			if (ruleOne.ipType.equalsIgnoreCase(ruleTwo.ipType) &&
				checkTwoIPRangesOverlap(ruleOne.clientIp, ruleTwo.clientIp) &&
			    checkTwoPortRangesOverlap(ruleOne.clientPort, ruleTwo.clientPort) &&
			    checkTwoIPRangesOverlap(ruleOne.destinationIp, ruleTwo.destinationIp) &&
			    checkTwoPortRangesOverlap(ruleOne.destinationPort, ruleTwo.destinationPort) &&
			    checkTwoProtocolTypesOverlap(ruleOne.protocolType, ruleTwo.protocolType) &&
			    checkTwoIPRangesOverlap(interfaceIpOne, interfaceIpTwo)) {
				return true;
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }

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

    public static boolean checkTwoIPRangesOverlap(String cidrOne, String cidrTwo) throws UnknownHostException {
        Range rangeOne = convertCIDRIPStringToRange(cidrOne);
        Range rangeTwo = convertCIDRIPStringToRange(cidrTwo);
        if (rangeOne != null && rangeTwo != null) {
            return rangeOne.isOverlapWithRange(rangeTwo);
        } else {
        	return false;
        }
    }
    
    public static boolean checkTwoPortRangesOverlap(String portOne, String portTwo) throws UnknownHostException {
        Range rangeOne = convertPortStringToRange(portOne);
        Range rangeTwo = convertPortStringToRange(portTwo);
        if (rangeOne != null && rangeTwo != null) {
            return rangeOne.isOverlapWithRange(rangeTwo);
        } else {
        	return false;
        }
    }
    
    public static boolean checkTwoProtocolTypesOverlap(String typeOne, String typeTwo) {
    	if (typeOne.equalsIgnoreCase("ANY") || typeTwo.equalsIgnoreCase("ANY")) {
    		return true;
    	}
    	return typeOne.equalsIgnoreCase(typeTwo);
    }

    private static Range convertCIDRIPStringToRange(String cidr) throws UnknownHostException {
        int ipIdx = cidr.lastIndexOf("/");
        if (ipIdx > 1) {
            String addr = cidr.substring(0, ipIdx);
            String sMask = cidr.substring(ipIdx + 1);
            int iMask = Integer.valueOf(sMask).intValue();
            try {
                byte[] b = InetAddress.getByName(addr).getAddress();
                if (b.length == 16) {
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
            if (b.length == 16) {
                BigInteger addrBigInt = new BigInteger(b);
                return new RangeIPv6(addrBigInt, addrBigInt);
            } else if (b.length == 4) {
                long addrInt = ((b[0] << 24) & 0x00000000FF000000L) 
                             | ((b[1] << 16) & 0x00000000FF0000L) 
                             | ((b[2] << 8) & 0x00000000FF00L) 
                             | (b[3] & 0x00000000FFL);
               return new RangeIPv4(addrInt, addrInt);
            }
        }
        return null;
    }
    
    private static Range convertPortStringToRange(String portString) {
    	int  colIdx = portString.lastIndexOf(":");
    	if (colIdx > 0) {
    		String portOne = portString.substring(0, colIdx);
            String portTwo = portString.substring(colIdx + 1);
            return new RangePort(Integer.valueOf(portOne), Integer.valueOf(portTwo));
    	} else {
    		int portNumber = Integer.parseInt(portString);
    		return new RangePort(portNumber, portNumber);
    	}
    }
}
