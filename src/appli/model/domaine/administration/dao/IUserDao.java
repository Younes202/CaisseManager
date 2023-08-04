package appli.model.domaine.administration.dao;

import appli.model.domaine.administration.persistant.UserPersistant;
import framework.model.util.IGenericJpaDao;

public interface IUserDao extends IGenericJpaDao<UserPersistant, Long>{

	/**
	 * @param mail
	 * @param pw
	 * @return
	 */
	UserPersistant getUserByLoginAndPw(String mail, String pw);

	UserPersistant getUserByBadge(String badge);
}
