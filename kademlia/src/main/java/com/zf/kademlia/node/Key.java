package com.zf.kademlia.node;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.BitSet;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
@Data
@EqualsAndHashCode(of = "key")
public class Key {
    public final static int ID_LENGTH = 160;

    private BigInteger key;

    public Key(byte[] result) {
        if (result.length > ID_LENGTH / 8) {
            throw new RuntimeException("ID to long. Needs to be  " + ID_LENGTH + "bits long.");
        }
        this.key = new BigInteger(result);
    }

    public Key(String key) {
        this.key = new BigInteger(key, 16);
    }

    public Key(BigInteger key) {
        this.key = key;
    }

    public static Key random() {
        try {
            byte[] bytes = new byte[ID_LENGTH / 8];
            SecureRandom.getInstance("SHA1").nextBytes(bytes);
            return new Key(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("generate key error", e);
        }
    }

    /**
     * 计算this key和to key的异或值
     */
    public Key xor(Key nid) {
        return new Key(nid.getKey().xor(this.key));
    }

    /**
     * 生成一个距此key指定距离的key
     */
    public Key generateNodeIdByDistance(int distance) {
        byte[] result = new byte[ID_LENGTH / 8];

        /* Since distance = ID_LENGTH - prefixLength, we need to fill that amount with 0's */
        int numByteZeroes = (ID_LENGTH - distance) / 8;
        int numBitZeroes = 8 - (distance % 8);

        /* Filling byte zeroes */
        for (int i = 0; i < numByteZeroes; i++) {
            result[i] = 0;
        }

        /* Filling bit zeroes */
        BitSet bits = new BitSet(8);
        bits.set(0, 8);

        for (int i = 0; i < numBitZeroes; i++) {
            /* Shift 1 zero into the start of the value */
            bits.clear(i);
        }
        bits.flip(0, 8);        // Flip the bits since they're in reverse order
        result[numByteZeroes] = (byte) bits.toByteArray()[0];

        /* Set the remaining bytes to Maximum value */
        for (int i = numByteZeroes + 1; i < result.length; i++) {
            result[i] = Byte.MAX_VALUE;
        }

        return this.xor(new Key(result));
    }

    /**
     * Counts the number of leading 0's in this Key
     *
     * @return Integer The number of leading 0's
     */
    public int getFirstSetBitIndex() {
        int prefixLength = 0;

        for (byte b : this.key.toByteArray()) {
            if (b == 0) {
                prefixLength += 8;
            } else {
                /* If the byte is not 0, we need to count how many MSBs are 0 */
                int count = 0;
                for (int i = 7; i >= 0; i--) {
                    boolean a = (b & (1 << i)) == 0;
                    if (a) {
                        count++;
                    } else {
                        break;   // Reset the count if we encounter a non-zero number
                    }
                }

                /* Add the count of MSB 0s to the prefix length */
                prefixLength += count;

                /* Break here since we've now covered the MSB 0s */
                break;
            }
        }
        return prefixLength;
    }

    @Override
    public String toString() {
        return this.key.toString(16);
    }

    /**
     * 计算this key和to key的异或值
     * 然后获得异或返回键的第一个位的索引i
     * 计算距离为他们之间的距离是ID_LENGTH - i
     */
    public int getDistance(Key to) {
        return ID_LENGTH - this.xor(to).getFirstSetBitIndex();
    }
}
