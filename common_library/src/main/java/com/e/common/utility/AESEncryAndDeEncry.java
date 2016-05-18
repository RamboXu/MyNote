package com.e.common.utility;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryAndDeEncry {

    private static byte[] key;

    static {
        try {
            key = initkey();
        } catch (Exception e) {
        }
    }

    /**
     * 密钥算法
     * java6支持56位密钥，bouncycastle支持64位
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * 加密/解密算法/工作模式/填充方式
     * <p/>
     * JAVA6 支持PKCS5PADDING填充方式
     * Bouncy castle支持PKCS7Padding填充方式
     */
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

    /**
     * 生成密钥，java6只支持56位密钥，bouncycastle支持64位密钥
     *
     * @return byte[] 二进制密钥
     */
    public static byte[] initkey() throws Exception {
//          //实例化密钥生成器
//          Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//          KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM, "BC");
//          //初始化密钥生成器，AES要求密钥长度为128位、192位、256位
//            kg.init(256);
////          kg.init(128);
//          //生成密钥
//          SecretKey secretKey=kg.generateKey();
//          //获取二进制密钥编码形式
//          return secretKey.getEncoded();
        //为了便于测试，这里我把key写死了，如果大家需要自动生成，可用上面注释掉的代码
        return new byte[]{0x08, 0x08, 0x04, 0x0b, 0x02, 0x0f, 0x0b, 0x0c,
                0x01, 0x03, 0x09, 0x07, 0x0c, 0x03, 0x07, 0x0a, 0x04, 0x0f,
                0x06, 0x0f, 0x0e, 0x09, 0x05, 0x01, 0x0a, 0x0a, 0x01, 0x09,
                0x06, 0x07, 0x09, 0x0d};
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key 密钥
     */
    public static Key toKey(byte[] key) throws Exception {
        //实例化DES密钥
        //生成密钥
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    /**
     * 加密数据
     *
     * @param str 待加密数据
     * @return byte[] 加密后的数据
     */
    public static String encrypt(String str) throws Exception {
        if (str == null) return null;
        byte[] data = str.getBytes("UTF-8");
        System.out.println(key.length);
        //还原密钥
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
        //初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);
        //执行操作
        byte[] encryptArray = cipher.doFinal(data);
        encryptArray = Base64.encode(encryptArray, Base64.DEFAULT);

        return new String(encryptArray, "UTF-8");
    }

    /**
     * 解密数据
     *
     * @param str 待解密数据
     * @return byte[] 解密后的数据
     */
    public static String decrypt(String str) throws Exception {
        if (str == null) return null;
        byte[] data = str.getBytes("UTF-8");

        data = Base64.decode(data, Base64.DEFAULT);
        //欢迎密钥
        Key k = toKey(key);
        /**
         * 实例化
         * 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现
         * Cipher.getInstance(CIPHER_ALGORITHM,"BC")
         */
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        //执行操作
        return new String(cipher.doFinal(data), "UTF-8");
    }

    public static void main(String[] args) {
        String source = "{\"device_id\":\"99000457236076\",\"mobile\":\"13255125923\"}";
        try {
            String aa = encrypt(source);
            String bb = decrypt(aa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}