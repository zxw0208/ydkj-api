package cn.yidukeji.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-1
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public class HMACUtils {

    public static String sha265(String key, String text){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            //get the bytes of the hmac key and data string
            byte[] secretByte = key.getBytes("UTF-8");
            byte[] dataBytes = text.getBytes("UTF-8");
            SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");

            mac.init(secret);
            byte[] doFinal = mac.doFinal(dataBytes);
            byte[] hexB = new Hex().encode(doFinal);
            return new String(hexB);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
