package framework.model.common.util;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptionUtil {

	public static void main(String[] args) {
		//Format code abonnement --> "27/06/2018|MARKET_TIWISI_BLFA3|27/06/2020" : date action|code authentification|date echeance
		String txt = "30/01/2020|H24_RESTAU|30/02/2020";
		//String txt = "09/09/2018|LOCAL_TEST|ABON_UP";
		System.out.println(decrypt("rl-I_est120"));
	}
//	
	private static Cipher ecipher;
	private static Cipher dcipher;
	private static String _KEY_CLIENT = "KNVL12365?!P";//"Jcp1256!Ml?:"
	
	static{
		loadEncryption();
	}
	
    private final static int iterationCount = 3;
    
    private static void loadEncryption() {
    	// 128 bit key
    	byte[] salt =
    	    { 
    	    	(byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, 
    	    	(byte) 0x9F, (byte) 0x5A, (byte) 0x75						
    	    };
    	
        try{
            KeySpec keySpec = new PBEKeySpec(_KEY_CLIENT.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (java.security.InvalidAlgorithmParameterException e){
        } catch (java.security.spec.InvalidKeySpecException e){
        } catch (javax.crypto.NoSuchPaddingException e){
        } catch (java.security.NoSuchAlgorithmException e){
        } catch (java.security.InvalidKeyException e){
        }
    }

    public static String encrypt(String str){
    	if(StringUtil.isEmpty(str)){
    		return str;
    	}
        try{
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc  = ecipher.doFinal(utf8);

            //return new sun.misc.BASE64Encoder().encode(enc);
            return Base64.encodeBase64URLSafeString(enc);

        } catch (javax.crypto.BadPaddingException e){
        } catch (IllegalBlockSizeException e){
        } catch (UnsupportedEncodingException e){
        }

        return null;
    }

    public static String decrypt(String str){
    	if(StringUtil.isEmpty(str)){
    		return str;
    	}
        try{
            byte[] dec = Base64.decodeBase64(str);//new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8,"UTF8");
        } catch (javax.crypto.BadPaddingException e){
        } catch (IllegalBlockSizeException e){
        } catch (UnsupportedEncodingException e){
        }

        return null;
    }
}