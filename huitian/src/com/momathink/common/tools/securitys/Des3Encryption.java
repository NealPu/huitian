package com.momathink.common.tools.securitys;


/**
 * 
 * @ClassName com.yizhilu.os.core.util.Security.Des3Encryption
 * @description
 * @author : qinggang.liu voo@163.com
 * @Create Date : 2014-1-11 上午9:29:36
 */
public class Des3Encryption {
    public static final String CHAR_ENCODING = "UTF-8";

    public static byte[] encode(byte[] key, byte[] data) throws Exception {
        return MessageAuthenticationCode.des3Encryption(key, data);
    }

    public static byte[] decode(byte[] key, byte[] value) throws Exception {
        return MessageAuthenticationCode.des3Decryption(key, value);
    }

    public static String encode(String key, String data) {
        try {
            byte[] keyByte = key.getBytes(CHAR_ENCODING);
            byte[] dataByte = data.getBytes(CHAR_ENCODING);
            byte[] valueByte = MessageAuthenticationCode
                    .des3Encryption(keyByte, dataByte);
            String value = new String(Base64.encode(valueByte), CHAR_ENCODING);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decode(String key, String value) {
        try {
            byte[] keyByte = key.getBytes(CHAR_ENCODING);
            byte[] valueByte = Base64.decode(value.getBytes(CHAR_ENCODING));
            byte[] dataByte = MessageAuthenticationCode
                    .des3Decryption(keyByte, valueByte);
            String data = new String(dataByte, CHAR_ENCODING);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String decryptFromHex(String key, String value) {
        try {
            byte[] keyByte = key.getBytes(CHAR_ENCODING);
            byte[] valueByte = ConvertUtils.fromHex(value);
            byte[] dataByte = MessageAuthenticationCode
                    .des3Decryption(keyByte, valueByte);
            String data = new String(dataByte, CHAR_ENCODING);
            return data;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public static String encode(String value) {
        return encode("z9aa179L5c2g0253375qx67G", value);
    }

    public static String decode(String value) {
        return decode("z9aa179L5c2g0253375qx67G", value);
    }

}