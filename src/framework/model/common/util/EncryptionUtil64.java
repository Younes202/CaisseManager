package framework.model.common.util;

public class EncryptionUtil64 {
	
    public static String encrypt64(String originalInput){
    	originalInput = "xx|?!**96"+originalInput+"XX956!?";
    	String encodedString = java.util.Base64.getEncoder().encodeToString(originalInput.getBytes());
    	
    	return encodedString;
    }

    public static String decrypt64(String encodedString){
    	byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedString);
    	String decodedString = new String(decodedBytes);
    	
    	if(StringUtil.isNotEmpty(decodedString)) {
    		decodedString = decodedString.substring(9, decodedString.length()-7);
    	}
    	
    	return decodedString;
    }
    
    public static void main(String[] args) {
		System.out.println(decrypt64("eHh8PyEqKjk2SmNwMTI1NiFNbD86WFg5NTYhPw=="));
	}
}