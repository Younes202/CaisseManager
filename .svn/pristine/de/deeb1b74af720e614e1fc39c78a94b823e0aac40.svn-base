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

import appli.controller.domaine.util_erp.ContextAppli;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.service.MessageService;
import framework.model.service.IGenericJpaService;
import framework.model.util.audit.ReplicationGenerationEventListener;

public class EncryptionEtsUtil {

	public static void main(String[] args) {
		//Format code abonnement --> "27/06/2018|MARKET_TIWISI_BLFA3|27/06/2020" : date action|code authentification|date echeance
		String txt = "30/01/2020|H24_RESTAU|30/02/2020";
		//String txt = "09/09/2018|LOCAL_TEST|ABON_UP";
		EncryptionEtsUtil enc = new EncryptionEtsUtil("Jcp1256!Ml?:");
		System.out.println(enc.encrypt("01"));
	}
	//	
	private Cipher ecipher;
	private Cipher dcipher;
	private String _KEY_CLIENT = null;
	
	public static String getDecrypKey() {
		String key = null;
		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE) {
			key = ContextAppli.getEtablissementBean().getDyc_key();
		} else {
			key = StrimUtil.GLOBAL_CONFIG_MAP.getProperty("client.key");
		}
		if(key == null && "localConf".equals(StrimUtil.getGlobalConfigPropertie("context.install"))) {
			if(MessageService.getGlobalMap().get("ETS_DYC_KEY") == null) {
				IGenericJpaService ps = (IGenericJpaService) ServiceUtil.getBusinessBean("EtablissementService");
				key = ReflectUtil.getStringPropertieValue(ps.findAll(EtablissementPersistant.class).get(0), "dyc_key");
				if(key != null) {
					MessageService.getGlobalMap().put("ETS_DYC_KEY", key);
				}
			} else {
				key = (String) MessageService.getGlobalMap().get("ETS_DYC_KEY");
			}
		}
		
		return (key==null ? "Jcp1256!Ml?:" : key); 
	}
	
	public EncryptionEtsUtil(String keyEncode){
		_KEY_CLIENT = (keyEncode == null ? "Jcp1256!Ml?:" : keyEncode);
		loadEncryption();
	}
	
    private final static int iterationCount = 3;
    
    private void loadEncryption() {
    	if(_KEY_CLIENT == null) {
    		return;
    	}
    	byte[] salt =
    	    { 
    	    	(byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, 
    	    	(byte) 0x9F, (byte) 0x5A, (byte) 0x75						
    	    };
    	
        try{
            KeySpec keySpec = new PBEKeySpec(_KEY_CLIENT.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            this.ecipher = Cipher.getInstance(key.getAlgorithm());
            this.dcipher = Cipher.getInstance(key.getAlgorithm());

            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            this.ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            this.dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (java.security.InvalidAlgorithmParameterException e){
        } catch (java.security.spec.InvalidKeySpecException e){
        } catch (javax.crypto.NoSuchPaddingException e){
        } catch (java.security.NoSuchAlgorithmException e){
        } catch (java.security.InvalidKeyException e){
        }
    }

    public String encrypt(String str){
    	if(StringUtil.isEmpty(str)){
    		return str;
    	}
        try{
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc  = this.ecipher.doFinal(utf8);

            //return new sun.misc.BASE64Encoder().encode(enc);
            return Base64.encodeBase64URLSafeString(enc);

        } catch (javax.crypto.BadPaddingException e){
        } catch (IllegalBlockSizeException e){
        } catch (UnsupportedEncodingException e){
        }

        return null;
    }

    public String decrypt(String str){
    	if(StringUtil.isEmpty(str)){
    		return str;
    	}
        try{
            byte[] dec = Base64.decodeBase64(str);//new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] utf8 = this.dcipher.doFinal(dec);

            return new String(utf8,"UTF8");
        } catch (javax.crypto.BadPaddingException e){
        } catch (IllegalBlockSizeException e){
        } catch (UnsupportedEncodingException e){
        }

        return null;
    }
}