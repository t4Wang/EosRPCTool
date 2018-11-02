package com.runze.eos.rpctool.util;

import com.runze.eos.rpctool.util.cypto.digest.Sha256;

public class EosUtils {

    public static byte[] getChainId(String chainId) {
        return new Sha256(getSha256FromHexStr(chainId)).getBytes();
    }

    private static byte[] getSha256FromHexStr(String chainId){
        int len = chainId.length();
        byte [] bytes = new byte[32];
        for(int i=0; i < len; i+=2){
            String hex = chainId.substring(i, i+2);
            Integer n = Integer.parseInt(hex, 16) & 0xFF;;
            bytes[i/2] = n.byteValue();
        }
        return bytes;
    }

}
