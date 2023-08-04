package appli.model.domaine.personnel.dao.impl;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import appli.model.domaine.personnel.dao.IClientDao;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.util.GenericJpaDao;


@Named
public class ClientDao extends GenericJpaDao<ClientPersistant, Long> implements IClientDao{
	@Override
	public ClientPersistant getClientByLoginAndPw(String login, String pw) {
		Query query = getQuery("from ClientPersistant client where client.login=:login and client.password=:pw")
				.setParameter("login", login).setParameter("pw", pw);
		
		return (ClientPersistant) getSingleResult(query);
	}

	@Override
	public ClientPersistant getClientByBadge(String badge) {
		List list = getQuery("from ClientPersistant client where client.badge=:badge")
				.setParameter("badge", badge)
				.getResultList();
		
		return (ClientPersistant) (list.isEmpty() ? null : list.get(0));
	}

}
