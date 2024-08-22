package io.acyloxy.cloverproxy.util;

import org.apache.commons.lang3.tuple.Pair;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.zip.CRC32;

public class CryptoUtils {
    private static final int VERIFY_LENGTH = 22;

    private static final byte[] MAGIC = new byte[]{0x4c, 0x54, 0x5a, 0x4a};

    public static void xorBytes(byte[] data) {
        int length = data.length;
        byte[] key = new byte[2];

        if (length > 255) {
            key[0] = (byte) (length & 0xff);
            length >>= 8;
            key[1] = (byte) (length & 0xff);
        } else {
            key[0] = (byte) length;
            key[1] = (byte) (255 - length);
        }

        for (int i = 0; i < data.length; i++) {
            data[i] ^= key[i % key.length];
        }
    }

    public static byte[] encrypt(byte[] data, int sequence) {
        ByteBuffer buffer = ByteBuffer.allocate(VERIFY_LENGTH + data.length);

        buffer.put(MAGIC);
        buffer.putInt(sequence);
        buffer.putLong(calculateCRC32(data));

        int minChunkSize = Math.max(1, data.length / 10);
        int maxChunkSize = Math.max(1, data.length / 3);
        int chunkSize = getRandomInt(minChunkSize, maxChunkSize);

        if (chunkSize > 0) {
            data = encryptData(data, chunkSize);
        }

        buffer.putShort((short) chunkSize);
        buffer.putInt(data.length);
        buffer.put(data);

        byte[] encryptedData = buffer.array();
        xorBytes(encryptedData);
        return encryptedData;
    }

    private static long calculateCRC32(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }

    private static int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static byte[] encryptData(byte[] data, int chunkSize) {
        int length = data.length;

        if (chunkSize <= 0 || chunkSize >= length) {
            return data;
        }

        byte[] result = new byte[length];
        int fullChunks = length / chunkSize;
        int remainder = length % chunkSize;
        int destPos = 0;

        if (remainder != 0) {
            System.arraycopy(data, length - remainder, result, destPos, remainder);
            destPos += remainder;
        }

        for (int i = fullChunks - 1; i >= 0; i--) {
            int srcPos = i * chunkSize;
            System.arraycopy(data, srcPos, result, destPos, chunkSize);
            destPos += chunkSize;
        }

        return result;
    }

    public static Pair<byte[], Integer> decrypt(byte[] encryptedData) throws Exception {
        if (encryptedData.length < VERIFY_LENGTH) {
            throw new Exception("Decrypt error, length must be at least " + VERIFY_LENGTH + " but got " + encryptedData.length);
        }

        xorBytes(encryptedData);
        ByteBuffer buffer = ByteBuffer.wrap(encryptedData);

        byte[] magic = new byte[MAGIC.length];
        buffer.get(magic);

        for (int i = 0; i < MAGIC.length; i++) {
            if (magic[i] != MAGIC[i]) {
                throw new Exception("Decrypt error, magic not equal.");
            }
        }

        int sequence = buffer.getInt();
        long crc32 = buffer.getLong();
        short chunkSize = buffer.getShort();
        int dataLength = buffer.getInt();

        byte[] decryptedData = decryptData(encryptedData, chunkSize, buffer.position(), dataLength);

        if (calculateCRC32(decryptedData) != crc32) {
            throw new Exception("Decrypt error, crc not equal.");
        }

        return Pair.of(decryptedData, sequence);
    }

    public static byte[] decryptData(byte[] data, int chunkSize, int offset, int length) {
        if (chunkSize <= 0 || chunkSize >= length) {
            byte[] result = new byte[length];
            System.arraycopy(data, offset, result, 0, length);
            return result;
        }

        byte[] result = new byte[length];
        int fullChunks = length / chunkSize;
        int remainder = length % chunkSize;
        int srcPos = offset;
        int destPos = length - remainder;

        if (remainder != 0) {
            System.arraycopy(data, srcPos, result, destPos, remainder);
            srcPos += remainder;
        }

        for (int i = fullChunks - 1; i >= 0; i--) {
            destPos -= chunkSize;
            System.arraycopy(data, srcPos, result, destPos, chunkSize);
            srcPos += chunkSize;
        }

        return result;
    }
}
