package appli.controller.util_ctrl;


import framework.model.common.util.StrimUtil;

/**
 * Classe spéciale pour décoder les mots de passe base de données
 * @author T.K
 *
 */
public class CustomBasicDataSource extends org.apache.commons.dbcp2.BasicDataSource{

    public CustomBasicDataSource() { 
        super();
    }
    
    @Override
    public synchronized void setPassword(String encodedPassword){
//    	String contextInstall = StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.install");
    	
    	String p = StrimUtil.getGlobalConfigPropertie("db.pw");
//    	if("local".equals(contextInstall)){
//    		p = EncryptionUtil.decrypt(p);
//    	}
    	super.setPassword(p);
    }
    
    @Override
    public synchronized void setUsername(String encodedUserName){
//    	String contextInstall = StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.install");
    	
    	String u = StrimUtil.getGlobalConfigPropertie("db.user");
//    	if("local".equals(contextInstall)){
//    		u = EncryptionUtil.decrypt(u);
//    	}
    	super.setUsername(u);
    }
}