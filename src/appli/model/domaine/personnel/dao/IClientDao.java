package appli.model.domaine.personnel.dao;

import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.util.IGenericJpaDao;

public interface IClientDao extends IGenericJpaDao<ClientPersistant, Long>{

	ClientPersistant getClientByLoginAndPw(String login, String pw);

	ClientPersistant getClientByBadge(String badge);
	
}
