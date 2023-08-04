package appli.model.domaine.administration.dao.impl;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import appli.model.domaine.administration.dao.IUserDao;
import appli.model.domaine.administration.persistant.UserPersistant;
import framework.model.util.GenericJpaDao;

@Named
public class UserDao extends GenericJpaDao<UserPersistant, Long> implements IUserDao{
	@Override
	public UserPersistant getUserByLoginAndPw(String login, String pw) {
		Query query = getQuery("from UserPersistant user where user.login=:login and user.password=:pw")
				.setParameter("login", login).setParameter("pw", pw);
		
		return (UserPersistant) getSingleResult(query);
	}

	@Override
	public UserPersistant getUserByBadge(String badge) {
		List list = getQuery("from UserPersistant user where user.badge=:badge")
				.setParameter("badge", badge)
				.getResultList();
		
		return (UserPersistant) (list.isEmpty() ? null : list.get(0));
	}

}
